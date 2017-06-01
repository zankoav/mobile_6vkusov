package by.vkus.alexandrzanko.mobile_6vkusov.Models;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexandrzanko on 30/11/16.
 */

public class Product {

    private String _name;
    private String _icon;
    private String _description;
    private int _points;
    private HashMap<String,String> _category;
    private ArrayList<Variant> _variants;

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

    public ArrayList<Variant> get_variants() {
        return _variants;
    }

    public void set_variants(ArrayList<Variant> _variants) {
        this._variants = _variants;
    }

    private int _id;

    public Product(int _id, String _name, String _icon, String _description, int _points, HashMap<String, String> _category, ArrayList<Variant> _variants) {
        this._id = _id;
        this._name = _name;
        this._icon = _icon;
        this._description = _description;
        this._points = _points;
        this._category = _category;
        this._variants = _variants;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product){
            Product product = (Product)obj;
            return  this.get_id() == product.get_id();
        }else{
            return false;
        }
    }
}