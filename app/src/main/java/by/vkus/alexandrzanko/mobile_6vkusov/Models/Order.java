package by.vkus.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 1/25/17.
 */

public class Order {

    private Product product;
    private int count;
    private Variant variant;

    public Order(Product product, Variant variant, int count){
        this.product = product;
        this.count = count;
        this.variant = variant;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void addCount(int count){
        this.count += count;
    }

    public int getCount(){
        return this.count;
    }

    public Product getProduct() {
        return product;
    }

    public Variant getVariant() {
        return variant;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Order){
            Order order = (Order)obj;
            return variant.equals(order.getVariant()) && product.equals(order.getProduct());
        }else{
            return false;
        }
    }

}
