package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexandrzanko on 3/9/17.
 */

public class Restaurant {

    private String _slug;
    private String _name;
    private String _working_time;
    private double _minimal_price;
    private String _delivery_time;
    private String _kitchens;
    private InfoRestaurant _info;
    private String _iconURL;
    private HashMap<String,Integer> _comments;
    private ArrayList<String> _categoriesSlugs;

    public String get_slug() {
        return _slug;
    }

    public void set_slug(String _slug) {
        this._slug = _slug;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_working_time() {
        return _working_time;
    }

    public void set_working_time(String _working_time) {
        this._working_time = _working_time;
    }

    public double get_minimal_price() {
        return _minimal_price;
    }

    public void set_minimal_price(double _minimal_price) {
        this._minimal_price = _minimal_price;
    }

    public String get_delivery_time() {
        return _delivery_time;
    }

    public void set_delivery_time(String _delivery_time) {
        this._delivery_time = _delivery_time;
    }

    public String get_kitchens() {
        return _kitchens;
    }

    public void set_kitchens(String _kitchens) {
        this._kitchens = _kitchens;
    }

    public InfoRestaurant get_info() {
        return _info;
    }

    public void set_info(InfoRestaurant _info) {
        this._info = _info;
    }

    public String get_iconURL() {
        return _iconURL;
    }

    public void set_iconURL(String _iconURL) {
        this._iconURL = _iconURL;
    }

    public HashMap<String, Integer> get_comments() {
        return _comments;
    }

    public void set_comments(HashMap<String, Integer> _comments) {
        this._comments = _comments;
    }

    public ArrayList<String> get_categoriesSlugs() {
        return _categoriesSlugs;
    }

    public void set_categoriesSlugs(ArrayList<String> _categoriesSlugs) {
        this._categoriesSlugs = _categoriesSlugs;
    }

    public boolean isNew(){
        return true;
    }

    public boolean isFreeFood(){
        return true;
    }

    public boolean isFlash(){
        return true;
    }

    public boolean isSale(){
        return true;
    }

    public Restaurant(String _slug, String _name, String _working_time, double _minimal_price, String _delivery_time, String _kitchens, InfoRestaurant _info, String _iconURL, HashMap<String, Integer> _comments, ArrayList<String> _categoriesSlugs) {
        this._slug = _slug;
        this._name = _name;
        this._working_time = _working_time;
        this._minimal_price = _minimal_price;
        this._delivery_time = _delivery_time;
        this._kitchens = _kitchens;
        this._info = _info;
        this._iconURL = _iconURL;
        this._comments = _comments;
        this._categoriesSlugs = _categoriesSlugs;
    }

}
