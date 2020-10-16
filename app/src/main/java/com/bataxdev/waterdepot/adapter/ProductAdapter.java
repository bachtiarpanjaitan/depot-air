package com.bataxdev.waterdepot.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bataxdev.waterdepot.DetailProductActivity;
import com.bataxdev.waterdepot.MainActivity;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.helper.DownloadImageTask;
import com.bataxdev.waterdepot.ui.LoginActivity;
import com.bataxdev.waterdepot.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductModel> products;
    private Context context;
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product,parent,false);
       return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        holder.product_name.setText(products.get(position).getName());
        holder.product_price.setText("Rp."+Double.toString(products.get(position).getPrice()));
        holder.product_price.setTypeface(Typeface.MONOSPACE,1);
        holder.product_description.setText(products.get(position).getDescription());
        if(products.get(position).getImage() != ""){
            Picasso.get().load(products.get(position).getImage()).into(holder.product_image);
        }

        final CardView productView = holder.itemView.findViewById(R.id.product_list);
        productView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("PRODUCT_ID",products.get(position).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public ProductAdapter(List<ProductModel> products, Context context)
    {
        this.products = products;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView product_name;
        TextView product_price;
        TextView product_description;
        ImageView product_image;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.product_list);
            product_name = (TextView)itemView.findViewById(R.id.product_name);
            product_price = (TextView)itemView.findViewById(R.id.product_price);
            product_description = (TextView)itemView.findViewById(R.id.product_description);
            product_image = (ImageView)itemView.findViewById(R.id.product_image);
        }
    }
}
