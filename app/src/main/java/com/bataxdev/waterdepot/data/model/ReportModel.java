package com.bataxdev.waterdepot.data.model;

import java.io.Serializable;
import com.google.gson.annotations.*;

public class ReportModel implements Serializable {
   private int order_value;
   private String product_name;
   private long price;
   private long discount;
   private long total;

   public int getOrder_value() {
      return order_value;
   }

   public void setOrder_value(int order_value) {
      this.order_value = order_value;
   }

   public String getProduct_name() {
      return product_name;
   }

   public void setProduct_name(String product_name) {
      this.product_name = product_name;
   }

   public long getPrice() {
      return price;
   }

   public void setPrice(long price) {
      this.price = price;
   }

   public long getDiscount() {
      return discount;
   }

   public void setDiscount(long discount) {
      this.discount = discount;
   }

   public long getTotal() {
      return total;
   }

   public void setTotal(long total) {
      this.total = total;
   }
}
