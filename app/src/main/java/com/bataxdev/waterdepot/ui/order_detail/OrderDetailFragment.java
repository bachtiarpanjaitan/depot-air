package com.bataxdev.waterdepot.ui.order_detail;

import android.text.style.ClickableSpan;
import android.util.Log;
import android.widget.*;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.data.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class OrderDetailFragment extends Fragment {

    private OrderDetailViewModel mViewModel;
    private DatabaseReference user;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public static OrderDetailFragment newInstance() {
        return new OrderDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.order_detail_fragment, container, false);

        TextView product_name = view.findViewById(R.id.product_name);
        TextView total_price = view.findViewById(R.id.total_price);
        TextView discount = view.findViewById(R.id.discount);
        TextView username = view.findViewById(R.id.username);
        TextView phone = view.findViewById(R.id.phone);
        TextView address = view.findViewById(R.id.address);
        Button btn_rating = view.findViewById(R.id.btn_rating);
        RatingBar rating = view.findViewById(R.id.ratingBar);
        LinearLayout c_rating = view.findViewById(R.id.c_rating);

        final String order_id = getArguments().getString("ORDER_ID");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    c_rating.setVisibility(1);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("orders").child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                OrderModel order = snapshot.getValue(OrderModel.class);

                if(snapshot.exists())
                {
                    FirebaseDatabase.getInstance().getReference("products").child(order.getProduct_id()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            ProductModel product = snapshot.getValue(ProductModel.class);
                            if(snapshot.exists())
                            {
                                product_name.setText(product.getName().toUpperCase());
                                Long total = order.getOrder_value() * (product.getPrice() - (int) product.getDiscount());
                                total_price.setText(total.toString());
                                Long diskon = (order.getOrder_value() * product.getPrice()) - total;
                                discount.setText(diskon.toString());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for(DataSnapshot child : snapshot.getChildren()){
                               if(child.exists())
                               {
                                   UserModel user = child.getValue(UserModel.class);
                                   if(user.getEmail().equals(order.getUser_email())){
                                       username.setText(user.getName());
                                       if(!user.getPhone().isEmpty()){
                                           phone.setText(user.getPhone());
                                       }
                                       address.setText(user.getAddress());
                                   }
                               }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference("orders").child(order_id).child("ratings").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                rating.setRating(((long) snapshot.child("value").getValue()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating_value = rating.getRating();
                FirebaseDatabase.getInstance().getReference("orders").child(order_id).child("ratings").child(currentUser.getUid()).child("value").setValue(rating_value);

                Toast.makeText(getContext(), "Feedback berhasil di simpan.",0).show();

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderDetailViewModel.class);
        // TODO: Use the ViewModel
    }

}