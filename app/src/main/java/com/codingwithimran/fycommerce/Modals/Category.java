package com.codingwithimran.fycommerce.Modals;

public class Category {
    String category_name, category_icon, category_type;

    public Category(String category_name, String category_icon, String category_type) {
        this.category_name = category_name;
        this.category_icon = category_icon;
        this.category_type = category_type;
    }

    public Category() {
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }
}
