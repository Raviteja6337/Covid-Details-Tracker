package com.example.covidtrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    CountryCodePicker countryCodePicker;
    String mcountrySlctd;
    TextView mFilter;
    String[] types = {"cases", "deaths", "recovered", "active"};
    PieChart pieChart;
    Spinner spinnerItem;
    TextView mtotalCases, mtodayCases, mactive, mdeaths, mtodayDeaths, mrecovered, mtodayRecovered;

    private List<CovidDetailsDAO> arrCovidDetails;
    private List<CovidDetailsDAO> mRecyclerItems;

    RecyclerView mrecyclerView;
    RecyclerAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        countryCodePicker = findViewById(R.id.cntryCodePicker);
        mtodayCases = findViewById(R.id.todayTotalCases);
        mtotalCases = findViewById(R.id.totalCases);
        mactive = findViewById(R.id.activeCases);
        mrecovered = findViewById(R.id.recoveredCases);
        mtodayRecovered = findViewById(R.id.tdyRecovered);
        mtodayDeaths = findViewById(R.id.todayDeaths);
        mdeaths = findViewById(R.id.totalDeaths);


        mrecyclerView = findViewById(R.id.recyclerView);

        pieChart = findViewById(R.id.piechart);
        spinnerItem = findViewById(R.id.spinner);
        mFilter = findViewById(R.id.filter);
        arrCovidDetails = new ArrayList<>();

        mRecyclerItems = new ArrayList<>();


        spinnerItem.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerItem.setAdapter(arrayAdapter);


        CallServer.getApiInterface().getCountiresData().enqueue(new Callback<List<CovidDetailsDAO>>() {
            @Override
            public void onResponse(Call<List<CovidDetailsDAO>> call, Response<List<CovidDetailsDAO>> response) {
                if (response.isSuccessful()){
                    mRecyclerItems.addAll(response.body());
                    //Notify Adapter
                    madapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),R.string.fetch_error,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CovidDetailsDAO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),R.string.fetch_error,Toast.LENGTH_LONG).show();
            }
        });

        madapter = new RecyclerAdapter(getApplicationContext(), mRecyclerItems);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setAdapter(madapter);




        countryCodePicker.setAutoDetectedCountry(true);
        mcountrySlctd = countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                mcountrySlctd = countryCodePicker.getSelectedCountryName();
                fetchData();
            }
        });

        fetchData();
    }

    private void fetchData() {

        CallServer.getApiInterface().getCountiresData().enqueue(new Callback<List<CovidDetailsDAO>>() {
            @Override
            public void onResponse(Call<List<CovidDetailsDAO>> call, Response<List<CovidDetailsDAO>> response) {

                if (response.isSuccessful()){
                    arrCovidDetails.addAll(response.body());
                    for (CovidDetailsDAO item : arrCovidDetails)
                    {
                        if (item.getCountry().equalsIgnoreCase(mcountrySlctd))
                        {
                            mactive.setText((item.active));
                            mtodayDeaths.setText((item.todayDeaths));
                            mdeaths.setText((item.deaths));
                            mrecovered.setText((item.recovered));
                            mdeaths.setText((item.deaths));
                            mtodayDeaths.setText((item.todayDeaths));
                            mtotalCases.setText((item.cases));
                            mtodayCases.setText((item.todayCases));

                            updateGraph(item.cases, item.active, item.recovered, item.deaths);
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),R.string.fetch_error,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CovidDetailsDAO>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),R.string.fetch_error,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateGraph(String cases, String active, String recovered, String deaths) {

        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel("confirm", Integer.parseInt(cases), getResources().getColor(R.color.yellow)));
        pieChart.addPieSlice(new PieModel("active", Integer.parseInt(active), getResources().getColor(R.color.green)));
        pieChart.addPieSlice(new PieModel("recovered", Integer.parseInt(recovered), getResources().getColor(R.color.blue)));
        pieChart.addPieSlice(new PieModel("deaths", Integer.parseInt(deaths), getResources().getColor(R.color.red)));

        pieChart.startAnimation();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        try {
            String itemType = types[position];
            mFilter.setText(itemType);
            madapter.filter(itemType);
            madapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}