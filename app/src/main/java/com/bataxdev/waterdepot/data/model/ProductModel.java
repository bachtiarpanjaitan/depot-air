package com.bataxdev.waterdepot.data.model;

import android.net.Uri;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProductModel implements Serializable {
    private String name;
    private long price;
    private String description;
    private long discount;
    private String image;
    private String unit;
    private String uid;

    public ProductModel(){}

    public ProductModel(
            String name,
            long harga,
            String keterangan,
            long potongan,
            String gambar,
            String satuan
        ){
        this.name = name;
        this.price = harga;
        this.description = keterangan;
        this.discount = potongan;
        this.image = gambar;
        this.unit = satuan;
    }

    public Map<String, Object> toMap(DataSnapshot child){
        HashMap<String,Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name",name);
        result.put("price",price);
        result.put("description",description);
        result.put("diskon",discount);
        result.put("image",image);
        result.put("unit",unit);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
