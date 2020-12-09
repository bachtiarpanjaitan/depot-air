package com.bataxdev.waterdepot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.InfoModel;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<InfoModel> infos;
    private Context context;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

    @NonNull
    @NotNull
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_info,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InfoAdapter.ViewHolder holder, int position) {
        holder.title.setText(infos.get(position).getTitle());
        holder.content.setText(infos.get(position).getContent());
        holder.datetime.setText(infos.get(position).getDatetime());
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                        {
                            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.option);
                            popupMenu.inflate(R.menu.info_menu);
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()){
                                        case R.id.delete_info:
                                            FirebaseDatabase.getInstance().getReference("infos").child(infos.get(position).getKey()).removeValue();
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

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public InfoAdapter(List<InfoModel> infos, Context context)
    {
        this.infos = infos;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView datetime;
        TextView option;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.info_title);
            content = itemView.findViewById(R.id.content);
            datetime = itemView.findViewById(R.id.datetime);
            option = itemView.findViewById(R.id.option_info);
        }
    }
}
