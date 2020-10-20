package com.bataxdev.waterdepot.ui.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.adapter.OrderAdapter;
import com.bataxdev.waterdepot.adapter.ProductAdapter;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import okhttp3.internal.cache.DiskLruCache;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    private OrderViewModel mViewModel;
    private View view;
    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    @Override
    public void onAttachFragment(@NonNull @NotNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        getDataList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataList();
    }

    private void getDataList() {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("orders").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                ArrayList<OrderModel> orders = new ArrayList<>();
                for(DataSnapshot child : snapshot.getChildren()){
                    OrderModel order;
                    order = child.getValue(OrderModel.class);
                    order.setKey(child.getKey());
                    if(order.getUser_email().equals(currentUser.getEmail())){
                        orders.add(order);
                    }
                }

                TextView no_data = view.findViewById(R.id.no_data);
                no_data.setText("Tidak ada Pesanan");
                if(orders.size() <= 0) no_data.setVisibility(View.VISIBLE);

                RecyclerView rv_order = view.findViewById(R.id.rv_order);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                OrderAdapter adapter = new OrderAdapter(orders,getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv_order.setLayoutManager(llm);
                rv_order.setHasFixedSize(true);
                rv_order.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

}