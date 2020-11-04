package com.bataxdev.waterdepot.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.adapter.ProductAdapter;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseDatabase.getInstance().getReference("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<ProductModel> products = new ArrayList<>();
                for(DataSnapshot child : snapshot.getChildren()){
                    ProductModel product;
                    product = child.getValue(ProductModel.class);
                    product.setKey(child.getKey());
                    products.add(product);
                }

                try {
                    RecyclerView rv_product = getActivity().findViewById(R.id.rv_product);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    ProductAdapter adapter = new ProductAdapter(products,getActivity());
                    rv_product.setLayoutManager(llm);
                    rv_product.setHasFixedSize(true);
                    rv_product.setAdapter(adapter);
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }

        });


    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {

    }
}