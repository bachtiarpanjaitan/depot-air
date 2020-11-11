package com.bataxdev.waterdepot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.data.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private List<UserModel> users;
    private Context context;
    private  final String TAG = "USER_MODEL";
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user,parent,false);
        return new UserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.display_name.setText(users.get(position).getName());
        holder.email.setText(users.get(position).getEmail());
        holder.phone_number.setText(users.get(position).getPhone());
        holder.address.setText(users.get(position).getAddress());
        if(users.get(position).getImage() != "" && !users.get(position).getImage().isEmpty()){
            Picasso.get().load(users.get(position).getImage())
                    .centerCrop()
                    .resize(100,100)
                    .into(holder.user_image);
        }
    }

    @Override
    public int getItemCount() {return users.size();}

    public UserAdapter(List<UserModel> users, Context context)
    {
        this.users = users;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView email;
        TextView display_name;
        TextView phone_number;
        ImageView user_image;
        TextView address;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.product_list);
            email = itemView.findViewById(R.id.email);
            display_name = itemView.findViewById(R.id.display_name);
            phone_number = itemView.findViewById(R.id.phone_number);
            user_image = itemView.findViewById(R.id.user_image);
            address = itemView.findViewById(R.id.address);
        }
    }
}
