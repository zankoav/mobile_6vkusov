package by.vkus.alexandrzanko.mobile_6vkusov.Models.Discounts;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MOurDiscountCategory {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("discounts")
    @Expose
    private List<Discount> discounts = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

}