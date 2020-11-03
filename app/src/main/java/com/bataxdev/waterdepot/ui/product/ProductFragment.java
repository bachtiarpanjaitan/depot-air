package com.bataxdev.waterdepot.ui.product;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.ui.home.HomeFragment;
import com.bataxdev.waterdepot.ui.order.OrderFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class ProductFragment extends Fragment {

    private ProductViewModel mViewModel;

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.product_fragment, container, false);

        final EditText t_name = view.findViewById(R.id.name);
        final EditText t_price = view.findViewById(R.id.price);
        final EditText t_discount = view.findViewById(R.id.discount);
        final EditText t_unit = view.findViewById(R.id.unit);
        final EditText t_description = view.findViewById(R.id.description);
        final EditText t_image = view.findViewById(R.id.image);
        final Button b_save_product = view.findViewById(R.id.btn_save_product);

        b_save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long price = 0;
                long discount = 0;
                if(!t_price.getText().toString().isEmpty()) price = Long.parseLong(t_price.getText().toString());
                if(!t_discount.getText().toString().isEmpty()) discount = Long.parseLong(t_discount.getText().toString());

                String ctime = new SimpleDateFormat("ddMMyyyyHHmmss").format(new java.util.Date());

                ProductModel product = new ProductModel();
                product.setUid(ctime);
                product.setName(t_name.getText().toString());
                product.setPrice(price);
                product.setDiscount(discount);
                product.setUnit(t_unit.getText().toString());
                product.setDescription(t_description.getText().toString());
                product.setImage(t_image.getText().toString());

                insert_product(product);
            }
        });

        return view;
    }

    private void insert_product(ProductModel product) {
        DatabaseReference products = FirebaseDatabase.getInstance().getReference().child("products").child(product.getUid());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            if(product.getName().isEmpty()){
                Toast.makeText(getContext(), "Nama produk tidak boleh kosong",0).show();
                return;
            }
            if(product.getUnit().isEmpty()){
                Toast.makeText(getContext(), "Satuan tidak boleh kosong",0).show();
                return;
            }

            products.setValue(product, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                    if(error != null){
                        Toast.makeText(getContext(), "Produk gagal disimpan",0).show();
                    }else{
                        Toast.makeText(getContext(), "Produk berhasil disimpan",0).show();
                        Fragment homeFragment = new HomeFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.frame_product, homeFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
            });

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        // TODO: Use the ViewModel
    }

}