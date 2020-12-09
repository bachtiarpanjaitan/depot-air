package com.bataxdev.waterdepot.ui.info;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.InfoModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class InfoFormFragment extends Fragment {

    private InfoFormViewModel mViewModel;
    private View view;

    public static InfoFormFragment newInstance() {
        return new InfoFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.info_form_fragment, container, false);

        EditText title = view.findViewById(R.id.title);
        EditText content = view.findViewById(R.id.content);
        Button btn_save = view.findViewById(R.id.btn_save_info);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("infos");


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ctime = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
                InfoModel info = new InfoModel();
                info.setTitle(title.getText().toString());
                info.setContent(content.getText().toString());
                info.setDatetime(ctime);

                database.push().setValue(info, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                        if(error == null)  Toast.makeText(getContext(), "Informasi berhasil disimpan",0).show();
                        else  Toast.makeText(getContext(), "Informasi gagal disimpan",0).show();

                        getFragmentManager().popBackStack();
                    }
                });
            }
        });

         return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InfoFormViewModel.class);
        // TODO: Use the ViewModel
    }

}