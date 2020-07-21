package com.caircb.rcbtracegadere.dialogs;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TableInformacionTransportista {

    private TableLayout tableLayout;
    private Context context;
    private String[]header;
    private ArrayList<String[]>data;
    private TableRow tableRow;
    private TextView txtCell;
    private  int indexC;
    private int indexR;
    private int size;

    public TableInformacionTransportista(TableLayout tableLayout, Context context,Integer size) {
        this.tableLayout = tableLayout;
        this.context = context;
        this.size=size;
    }
    public void addHeader(String[]header){
        this.header=header;
        createHeader();
    }
    public void addData(ArrayList<String[]>data){
        this.data=data;
        CreateDataTable();
    }
    private void newRow(){
        tableRow=new TableRow(context);
    }
    private  void newCell(){
        txtCell=new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(10);
    }
    private void createHeader(){
        indexC=0;
        newRow();
        while (indexC<header.length){
            newCell();
            txtCell.setText(header[indexC++]);
            tableRow.addView(txtCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }
    private void CreateDataTable(){
        String info;
        for (indexR=1;indexR<=size;indexR++){
            newRow();
            for (indexC=0;indexC<header.length;indexC++){
                newCell();
                String [] columns=data.get(indexR-1);
                info = (indexC<columns.length)?columns[indexC]:"";
                txtCell.setText(info);
                tableRow.addView(txtCell,newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(15,15,15,15);
        params.weight=0.80f;
        return params;
    }



}
