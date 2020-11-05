package com.bataxdev.waterdepot.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.Enumerable.EnumOrderStatus;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.ui.product_detail.ProductDetailFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private List<OrderModel> orders;
    private Context context;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_order,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {

        int order_value = orders.get(position).getOrder_value();
        String product_id = orders.get(position).getProduct_id();
        FirebaseDatabase.getInstance().getReference("products").child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                ProductModel product = snapshot.getValue(ProductModel.class);

                if(product != null)
                {
                    Long discount = product.getDiscount();
                    Long price = product.getPrice();
                    Long total =  (order_value * price) - discount;

                    NumberFormat nf = NumberFormat.getInstance(new Locale("us","US"));
                    String string_total = nf.format(total);

                    holder.product_order_name.setText(product.getName());
                    holder.order_number.setText("Jumlah : "+Double.toString(order_value));
                    holder.total_price.setText("Total : Rp."+ string_total);
                    holder.status.setText(orders.get(position).getStatus());
                    holder.datetime.setText(orders.get(position).getDatetime());
                    if(product.getImage() != "" && !product.getImage().isEmpty()){
                        Picasso.get().load(product.getImage()).into(holder.product_order_image);
                    }
                    holder.options.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            user.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.options);
                                        popupMenu.inflate(R.menu.order_menu);
                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                switch (item.getItemId()){
                                                    case R.id.delete_order:
                                                        if(orders.get(position).getStatus().equals(EnumOrderStatus.OPEN.getName())) {
                                                            FirebaseDatabase.getInstance().getReference("orders").child(orders.get(position).getKey()).removeValue();
                                                        }else Toast.makeText(v.getContext(), "Pesanan sudah tidak bisa dihapus",0).show();
                                                        break;
                                                    case R.id.edit_order:
                                                        if(orders.get(position).getStatus().equals(EnumOrderStatus.OPEN.getName())) {
                                                            Bundle data = new Bundle();
                                                            data.putString("PRODUCT_ID", orders.get(position).getProduct_id());
                                                            data.putInt("VALUE_ORDER", orders.get(position).getOrder_value());
                                                            data.putBoolean("HAS_EDIT", true);
                                                            data.putString("ORDER_ID", orders.get(position).getKey());
                                                            Fragment pdf = new ProductDetailFragment();
                                                            pdf.setArguments(data);
                                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, pdf).addToBackStack(null).commit();
                                                        }else Toast.makeText(v.getContext(), "Pesanan sudah tidak bisa diubah lagi",0).show();
                                                        break;
                                                }

                                                return false;
                                            }
                                        });
                                        popupMenu.show();
                                    }else{
                                        PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.options);
                                        popupMenu.inflate(R.menu.admin_order_menu);
                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                switch (item.getItemId()){
                                                    case R.id.delete_order:
                                                        if(orders.get(position).getStatus().equals(EnumOrderStatus.OPEN.getName())) {
                                                            FirebaseDatabase.getInstance().getReference("orders").child(orders.get(position).getKey()).removeValue();
                                                        }else Toast.makeText(v.getContext(), "Pesanan sudah tidak bisa dihapus",0).show();
                                                        break;
                                                    case R.id.admin_sending_order:
                                                        FirebaseDatabase.getInstance().getReference("orders")
                                                                .child(orders.get(position).getKey())
                                                                .child("status").setValue(EnumOrderStatus.SENDING.getName());
                                                        break;
                                                    case R.id.admin_close_order:
                                                        FirebaseDatabase.getInstance().getReference("orders")
                                                                .child(orders.get(position).getKey())
                                                                .child("status").setValue(EnumOrderStatus.CLOSED.getName());
                                                        break;
                                                }

                                                return false;
                                            }
                                        });
                                        popupMenu.show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });




                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        final CardView orderView = holder.itemView.findViewById(R.id.order_list);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public OrderAdapter(List<OrderModel> orders, Context context)
    {
        this.orders = orders;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView product_order_name;
        TextView order_number;
        TextView total_price;
        ImageView product_order_image;
        TextView status;
        TextView datetime;
        TextView options;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.order_list);
            product_order_name = itemView.findViewById(R.id.product_order_name);
            order_number = itemView.findViewById(R.id.order_number);
            total_price = itemView.findViewById(R.id.total_price);
            product_order_image = itemView.findViewById(R.id.product_order_image);
            status = itemView.findViewById(R.id.order_status);
            datetime = itemView.findViewById(R.id.order_datetime);
            options = itemView.findViewById(R.id.option_order);
        }
    }
}
