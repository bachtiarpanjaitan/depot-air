package com.bataxdev.waterdepot.data.model;

import com.bataxdev.waterdepot.data.Enumerable.EnumOrderStatus;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OrderModel implements Serializable {
    private String user_email;
    private int order_value;
    private String product_id;
    private String datetime;
    private String status;
    private String key;

    public  OrderModel(
        String user_email,
        int order_value,
        String product_id,
        String datetime,
        String status,
        String key
    ){
        this.user_email = user_email;
        this.order_value = order_value;
        this.product_id = product_id;
        this.datetime = datetime;
        this.status = status;
        this.key = key;
    }

    public OrderModel() {}

    public Map<String, Object> toMap(DataSnapshot child){
        HashMap<String,Object> result = new HashMap<>();
        result.put("user_email", user_email);
        result.put("order_value",order_value);
        result.put("prodct_id",product_id);
        result.put("datetime",datetime);
        result.put("status",status);
        result.put("key",key);
        return result;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getOrder_value() {
        return order_value;
    }

    public void setOrder_value(int order_value) {
        this.order_value = order_value;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
