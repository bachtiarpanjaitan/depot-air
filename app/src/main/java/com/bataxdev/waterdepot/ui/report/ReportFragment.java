package com.bataxdev.waterdepot.ui.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bataxdev.waterdepot.MainActivity;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.OrderModel;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.data.model.ReportModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private View view;
    public static ReportFragment newInstance() {
        return new ReportFragment();
    }
    DatePickerDialog picker;
    DatePickerDialog picker2;
    EditText todate;
    EditText fromdate;
    Button search;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.report_fragment, container, false);
        final String[] TABLE_HEADER = {"Nama Produk","Harga","Diskon","Jumlah","Total"};
        ArrayList reports = new ArrayList<>();

        TableView<String[]> tableView = view.findViewById(R.id.tableview);
        int colorEventRows = getResources().getColor(R.color.white);
        int colorOddRows = getResources().getColor(R.color.gray);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),TABLE_HEADER));
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEventRows,colorOddRows));

        fromdate = view.findViewById(R.id.fromdate);
        todate = view.findViewById(R.id.todate);
        search = view.findViewById(R.id.btn_search);
        fromdate.setInputType(InputType.TYPE_NULL);
        todate.setInputType(InputType.TYPE_NULL);
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fromdate.setText(dayOfMonth + "-" + (month + 1) +"-"+ year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                picker2 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        todate.setText(dayOfMonth + "-" + (month + 1) +"-"+ year);
                    }
                },year,month,day);
                picker2.show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), fromdate.getText() +" "+todate.getText(),0).show();
            }
        });




        //set width columns
        TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(getActivity(),5,100);
        columnModel.setColumnWidth(3, 200);
        columnModel.setColumnWidth(0, 200);
        tableView.setColumnModel(columnModel);


        FirebaseDatabase.getInstance().getReference("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot child : snapshot.getChildren()){
                    OrderModel order = child.getValue(OrderModel.class);
                    FirebaseDatabase.getInstance().getReference("products").child(order.getProduct_id()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot sn) {
                            ProductModel product = sn.getValue(ProductModel.class);

                            String[] row = {
                                    product.getName(),
                                    String.valueOf(product.getPrice()),
                                    String.valueOf(product.getDiscount()),
                                    String.valueOf(order.getOrder_value()),
                                    String.valueOf((product.getPrice() * order.getOrder_value()) - product.getDiscount())
                            };
                            reports.add(row);
                            tableView.setDataAdapter(new SimpleTableDataAdapter(getContext(), reports));
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}