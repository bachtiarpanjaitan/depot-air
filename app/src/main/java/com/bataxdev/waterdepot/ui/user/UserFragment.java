package com.bataxdev.waterdepot.ui.user;

import android.util.Log;
import android.widget.TextView;
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
import com.bataxdev.waterdepot.adapter.UserAdapter;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.data.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    private UserViewModel mViewModel;
    private View view;
    public static UserFragment newInstance() {
        return new UserFragment();
    }
    private final String TAG = "USERS";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.user_fragment, container, false);
        return view;
    }

    @Override
    public void onAttachFragment(@NonNull @NotNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        getDataList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataList();
    }

    private void getDataList() {
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                final ArrayList<UserModel> users = new ArrayList<>();
                for(DataSnapshot child : snapshot.getChildren()){
                    UserModel user;
                    user = child.getValue(UserModel.class);
                    users.add(user);
                }

                Log.i(TAG, String.valueOf(users.size()));

                TextView no_data = view.findViewById(R.id.no_data);
                no_data.setText("Tidak ada Pengguna");
                if(users.size() <= 0) no_data.setVisibility(View.VISIBLE);

                RecyclerView rv_user = view.findViewById(R.id.rv_user);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                UserAdapter adapter = new UserAdapter(users,getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv_user.setLayoutManager(llm);
                rv_user.setHasFixedSize(true);
                rv_user.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}