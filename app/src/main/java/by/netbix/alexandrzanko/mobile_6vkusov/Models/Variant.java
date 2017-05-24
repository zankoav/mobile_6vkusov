package by.netbix.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 1/24/17.
 */

public class Variant {

    private int _id;
    private double _price;
    private String _size;
    private String _weight;
    private int _count;

    public Variant(int _id, double _price, String _size, String _weight, int _count) {
        this._id = _id;
        this._price = _price;
        this._size = _size;
        this._weight = _weight;
        this._count = _count;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double get_price() {
        return _price;
    }

    public void set_price(double _price) {
        this._price = _price;
    }

    public String get_size() {
        return _size;
    }

    public void set_size(String _size) {
        this._size = _size;
    }

    public String get_weigth() {
        return _weight;
    }

    public void set_weigth(String _weight) {
        this._weight = _weight;
    }

    public int get_count() {
        return _count;
    }

    public void set_count(int _count) {
        this._count = _count;
    }

    public void addCount(){
        _count += 1;
    }

    public void minusCount(){
        if (_count > 0){
            _count -= 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variant){
            Variant variant = (Variant)obj;
            return  _id == variant.get_id();
        }else{
            return false;
        }
    }
}

