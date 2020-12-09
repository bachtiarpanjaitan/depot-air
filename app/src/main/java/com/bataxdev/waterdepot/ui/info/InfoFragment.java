package com.bataxdev.waterdepot.ui.info;

import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
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
import com.bataxdev.waterdepot.adapter.InfoAdapter;
import com.bataxdev.waterdepot.adapter.OrderAdapter;
import com.bataxdev.waterdepot.data.model.InfoModel;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.ui.product_detail.ProductDetailFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private InfoViewModel mViewModel;
    private View view;
    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.info_fragment, container, false);

        Button add_info = view.findViewById(R.id.add_info);
        RelativeLayout info_list = view.findViewById(R.id.c_info_list);

        add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment info_form = new InfoFormFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, info_form).addToBackStack(null).commit();
            }
        });

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    add_info.setVisibility(View.INVISIBLE);

                    FrameLayout.LayoutParams rp = (FrameLayout.LayoutParams)info_list.getLayoutParams();
                    rp.topMargin =0;
                    info_list.setLayoutParams(rp);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onAttachFragment(@NonNull @NotNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseDatabase.getInstance().getReference("infos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                final ArrayList<InfoModel> infos = new ArrayList<>();

                if(snapshot.exists()){
                    for(DataSnapshot child : snapshot.getChildren()){
                        Log.i("INFO", child.toString());
                        InfoModel info = child.getValue(InfoModel.class);
                        info.setKey(child.getKey());
                        infos.add(info);
                    }

                    RecyclerView rv_info = view.findViewById(R.id.rv_info);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    InfoAdapter adapter = new InfoAdapter(infos,view.getContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    rv_info.setLayoutManager(llm);
                    rv_info.setHasFixedSize(true);
                    rv_info.setAdapter(adapter);
                }else{
                    TextView no_data = getActivity().findViewById(R.id.no_data);
                    no_data.setText("Informasi tidak tersedia");
                    no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}