package com.example.covidtrackerapp;

import android.content.Context;
import android.icu.number.NumberFormatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    int mType=1;
    Context context;
    List <CovidDetailsDAO> mCountryList;

    public RecyclerAdapter(Context context, List<CovidDetailsDAO> mCountryList) {
        this.context = context;
        this.mCountryList = mCountryList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        if (mType==1)
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(mCountryList.get(position).cases)));
        if (mType==2)
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(mCountryList.get(position).recovered)));
        if (mType==3)
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(mCountryList.get(position).deaths)));
        if (mType==4)
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(mCountryList.get(position).active)));

        holder.countryName.setText(mCountryList.get(position).country);
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        TextView cases, countryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cases = itemView.findViewById(R.id.cntryCasesCount);
            countryName = itemView.findViewById(R.id.countryName);
        }
    }

    public void filter(String itemType)
    {
        if (itemType.equalsIgnoreCase(Constants.CASES))
        {
            mType=1;
        }
        if (itemType.equalsIgnoreCase(Constants.DEATHS))
        {
            mType=2;
        }
        if (itemType.equalsIgnoreCase(Constants.RECOVERED))
        {
            mType=3;
        }
        if (itemType.equalsIgnoreCase(Constants.ACTIVE))
        {
            mType=4;
        }
    }
}
