package com.bataxdev.waterdepot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

public class DetailProductActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        String product_id = getIntent().getStringExtra("PRODUCT_ID");

        final ImageView image = findViewById(R.id.image);
        final TextView price = findViewById(R.id.price);
        final TextView unit = findViewById(R.id.unit);
        final TextView discount = findViewById(R.id.discount);
        final TextView description = findViewById(R.id.description);
        EditText order_number = findViewById(R.id.order_number);
        Button order_now = findViewById(R.id.btn_order_now);

        FirebaseDatabase.getInstance().getReference("products").child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ProductModel product = snapshot.getValue(ProductModel.class);
                if(product.getImage() != ""){
                    Picasso.get().load(product.getImage()).into(image);
                }
                unit.setText("/ "+ product.getUnit());
                price.setText("Harga : Rp."+ product.getPrice());
                discount.setText("Diskon : Rp."+ product.getDiscount());
                description.setText(product.getDescription());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}