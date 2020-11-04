package com.bataxdev.waterdepot.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.bataxdev.waterdepot.data.model.ReportModel;
import de.codecrafters.tableview.TableDataAdapter;

import java.util.List;

public class ReportTableAdapter extends TableDataAdapter<ReportModel> {
    public ReportTableAdapter(Context context, List<ReportModel> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
       ReportModel report = getRowData(rowIndex);
       View renderview = null;
       return renderview;
    }
}
