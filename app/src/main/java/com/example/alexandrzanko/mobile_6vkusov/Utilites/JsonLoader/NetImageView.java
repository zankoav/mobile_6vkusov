package com.example.alexandrzanko.mobile_6vkusov.Utilites.JsonLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetImageView extends ImageView {

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;
    private static final String DISK_CACHE_PATH = "/netimage_cache/";
    private static final boolean USE_DISK_CASHE = true;
    private static final boolean USE_MEMORY_CASHE = true;
    private static final int MEMORY_SIZE_LIMIT = 50;
    private static final int REQUEST_POOL_SIZE_LIMIT = 10;

    private String mDiskCachePath;
    private double mCurrPercent = 0.0D;
    private boolean mLoaded = false;
    private boolean mNeedShowProgress = true;
    private Handler mHandler = new Handler();
    private static final String TAG = "netimage";
    private static final ConcurrentHashMap<String, Bitmap> memoryCache = new ConcurrentHashMap<String, Bitmap>();
    private static final ConcurrentLinkedQueue<String> requestPool = new ConcurrentLinkedQueue<String>();
    private static final Object monitor = new Object();
    private Bitmap rezbmp;

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDiskCachePath = context.getCacheDir().getAbsolutePath() + DISK_CACHE_PATH;
        File outFile = new File(mDiskCachePath);
        outFile.mkdirs();
    }

    public void loadImage(final String url, int staticLoaderImageResource, final int failLoadImageResource, boolean needShowProgress) {
        this.mNeedShowProgress = needShowProgress;
        setImageResource(staticLoaderImageResource);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mLoaded = false;
                final Bitmap bmp;
                try {
                    bmp = cashedLoad(url);
                    mLoaded = true;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (bmp==null) setImageResource(failLoadImageResource);
                            else setImageBitmap(bmp);
                        }
                    });
                } catch (AlreadyLoadingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mLoaded && mNeedShowProgress) {
            int delimiter = new BigDecimal((getMeasuredWidth() - 30) * mCurrPercent).intValue();
            if (delimiter<30) delimiter = 30;
            canvas.drawColor(Color.BLACK);
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            canvas.drawText(new BigDecimal(mCurrPercent * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() + "%", 30, getMeasuredHeight() / 2 - 10, p);
            canvas.drawRect(30, getMeasuredHeight() / 2 - 4, delimiter, getMeasuredHeight() / 2 + 4, p);
            p.setColor(Color.GRAY);
            canvas.drawRect(delimiter, getMeasuredHeight() / 2 - 4, getMeasuredWidth() - 30, getMeasuredHeight() / 2 + 4, p);
        }
    }

    private Bitmap cashedLoad(String url) throws AlreadyLoadingException {
        rezbmp = null;
        String key;
        try {
            key = new String(Base64.encode(url.getBytes("UTF-8"), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        final File dcashef = new File(mDiskCachePath + key);

        if (USE_MEMORY_CASHE && (rezbmp = memoryCache.get(key))!=null) {
            Log.v(TAG, "restored from memory cashe key: " + key);
            if (USE_DISK_CASHE && !dcashef.exists()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rezbmp.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dcashef));
                            Log.v(TAG, "updated disk cashe file: " + dcashef.getAbsolutePath() + " successfully");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            return rezbmp;
        }

        if (USE_DISK_CASHE && dcashef.exists() && (rezbmp = BitmapFactory.decodeFile(dcashef.getAbsolutePath()))!=null) {
            Log.v(TAG, "restored from cashe file: " + dcashef.getAbsolutePath());
            if (USE_MEMORY_CASHE) {
                if (memoryCache.size()>=MEMORY_SIZE_LIMIT) memoryCache.remove(memoryCache.keySet().iterator().next());
                memoryCache.put(key, rezbmp);
                Log.v(TAG, "updated to memory cashe successfully");
            }
            return rezbmp;
        }

        if (requestPool.contains(url)) throw new AlreadyLoadingException();
        while (requestPool.size()>=REQUEST_POOL_SIZE_LIMIT) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        requestPool.add(url);

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            FileOutputStream out = new FileOutputStream(dcashef);
            InputStream in = conn.getInputStream();
            byte[] buf = new byte[128];
            int readed;
            int allread = 0;
            while ((readed = in.read(buf)) > 0) {
                mCurrPercent = (double) allread / conn.getContentLength();
                out.write(buf, 0, readed);
                allread += readed;
                postInvalidate();
            }
            out.flush();
            out.close();
            in.close();
            requestPool.remove(url);
            synchronized (monitor) {
                monitor.notifyAll();
            }
            Log.v(TAG, "stored to file: " + dcashef.getAbsolutePath() + " successfully");
            rezbmp = BitmapFactory.decodeFile(dcashef.getAbsolutePath());
            if (!USE_DISK_CASHE) dcashef.delete();
        } catch (Exception e) {
            e.printStackTrace();
            dcashef.delete();
        }

        if (USE_MEMORY_CASHE && rezbmp!=null) {
            if (memoryCache.size()>=MEMORY_SIZE_LIMIT) memoryCache.remove(memoryCache.keySet().iterator().next());
            memoryCache.put(key, rezbmp);
            Log.v(TAG, "stored to memory cashe successfully");
        }
        return rezbmp;
    }

    public static void clearCashe() {
        memoryCache.clear();
    }

    private class AlreadyLoadingException extends Exception {}
}