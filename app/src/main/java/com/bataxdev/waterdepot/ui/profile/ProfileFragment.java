package com.bataxdev.waterdepot.ui.profile;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FirebaseUser user;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);
        EditText address = view.findViewById(R.id.address);
        EditText phone = view.findViewById(R.id.no_telp);
        Button btn_save = view.findViewById(R.id.btn_save_profile);

        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String c_email = user.getEmail();
                for(DataSnapshot child : snapshot.getChildren()){
                    UserModel item = child.getValue(UserModel.class);
                   if(c_email.equals(item.getEmail()))
                   {
                       name.setText(item.getName());
                       email.setText(item.getEmail());
                       address.setText(item.getAddress());
                       phone.setText(item.getAddress());
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan_profile();
            }
        });


        return view;
    }

    private void simpan_profile() {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}