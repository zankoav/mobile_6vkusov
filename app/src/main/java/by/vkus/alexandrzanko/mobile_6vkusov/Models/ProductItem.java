package by.vkus.alexandrzanko.mobile_6vkusov.Models;

import java.util.HashMap;

/**
 * Created by alexandrzanko on 4/26/17.
 */

public class ProductItem {

    private int _id;
    private String _name;
    private String _icon;
    private String _description;
    private int _points;
    private HashMap<String,String> _category;
    private Variant _variant;
    private int _count;


    public void addCount(){
        _count += 1;
    }

    public void minusCount(){
        _count -= 1;
    }


    public void addCountTo(int count){
        _count += count;
    }

    public ProductItem(int _id, String _name, String _icon, String _description, int _points, HashMap<String, String> _category, Variant _variant) {
        this._id = _id;
        this._name = _name;
        this._icon = _icon;
        this._description = _description;
        this._points = _points;
        this._category = _category;
        this._variant = _variant;
        this._count = _variant.get_count();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_icon() {
        return _icon;
    }

    public void set_icon(String _icon) {
        this._icon = _icon;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public int get_points() {
        return _points;
    }

    public void set_points(int _points) {
        this._points = _points;
    }

    public HashMap<String, String> get_category() {
        return _category;
    }

    public void set_category(HashMap<String, String> _category) {
        this._category = _category;
    }

    public Variant get_variant() {
        return _variant;
    }

    public void set_variant(Variant _variant) {
        this._variant = _variant;
    }

    public int get_count() {
        return _count;
    }

    public void set_count(int _count) {
        this._count = _count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductItem){
            ProductItem productItem = (ProductItem)obj;
            return  this.get_variant().get_id() == productItem.get_variant().get_id();
        }else{
            return false;
        }
    }
}
