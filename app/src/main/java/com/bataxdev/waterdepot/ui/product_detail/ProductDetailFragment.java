package com.bataxdev.waterdepot.ui.product_detail;

import android.app.Activity;
import android.app.NotificationManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bataxdev.waterdepot.MainActivity;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.Enumerable.EnumOrderStatus;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.ui.order.OrderFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class ProductDetailFragment extends Fragment {

    private ProductDetailViewModel mViewModel;

    public static ProductDetailFragment newInstance() {
        return new ProductDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.product_detail_fragment, container, false);

        final String product_id = getArguments().getString("PRODUCT_ID");
        final int value_order = getArguments().getInt("VALUE_ORDER");
        final boolean has_edit = getArguments().getBoolean("HAS_EDIT");
        final String order_id = getArguments().getString("ORDER_ID");

        final ImageView image = view.findViewById(R.id.image);
        final TextView name = view.findViewById(R.id.name);
        final TextView price = view.findViewById(R.id.price);
        final TextView unit = view.findViewById(R.id.unit);
        final TextView discount = view.findViewById(R.id.discount);
        final TextView description = view.findViewById(R.id.description);
        final EditText order_number = view.findViewById(R.id.order_number);
        final Button order_now = view.findViewById(R.id.btn_order_now);

        FirebaseDatabase.getInstance().getReference("products").child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ProductModel product = snapshot.getValue(ProductModel.class);
                if(product.getImage() != "" && !product.getImage().isEmpty()){
                    Picasso.get().load(product.getImage()).into(image);
                }
                unit.setText("/ "+ product.getUnit());
                name.setText(product.getName());
                price.setText("Harga : Rp."+ product.getPrice());
                discount.setText("Diskon : Rp."+ product.getDiscount());
                description.setText(product.getDescription());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        if(value_order != 0)
        {
            order_number.setText(String.valueOf(value_order));
        }

        order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = order_number.getText().toString();
                if(value == "" || value == null)
                {
                    Toast.makeText(getContext(), "Jumlah Pesanan Tidak Boleh Kosong",0).show();
                }else{
                    int value_order = Integer.parseInt(order_number.getText().toString());
                    if(has_edit == false) insert_order(value_order,product_id,v);
                    else update_order(value_order,order_id);
                }
            }
        });

        return view;
    }

    private void update_order(int value_order, String order_id) {
        FirebaseDatabase.getInstance().getReference().child("orders").child(order_id).child("order_value").setValue(value_order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                if(error != null){
                    Toast.makeText(getContext(), "Pesanan gagal diubah",0).show();
                }else{
                    Toast.makeText(getContext(), "Pesanan berhasil Diubah",0).show();
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    private void insert_order(int value_order, String product_id, final View v) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("orders");

        String ctime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date());

        OrderModel order = new OrderModel();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            order.setUser_email(currentUser.getEmail());
            order.setOrder_value(value_order);
            order.setProduct_id(product_id);
            order.setDatetime(ctime);
            order.setStatus(EnumOrderStatus.OPEN.getName());
            database.push().setValue(order, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                    if(error != null){
                        Toast.makeText(getContext(), "Pesanan gagal disimpan",0).show();
                    }else{
                        Toast.makeText(getContext(), "Pesanan berhasil disimpan",0).show();
                        ((MainActivity)getActivity()).sendNotification("Pesanan Baru",currentUser.getDisplayName()+" melakukan pemesanan sebanyak "+ order.getOrder_value(),getString(R.string.default_notification_channel_id));
                        getFragmentManager().popBackStack();
                    }
                }
            });
        }else{
            Toast.makeText(getContext(), "Tidak dapat membuat pesanan",0).show();
        }

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductDetailViewModel.class);
        // TODO: Use the ViewModel
    }

}