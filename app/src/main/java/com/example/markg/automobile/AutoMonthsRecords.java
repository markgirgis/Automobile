package com.example.markg.automobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AutoMonthsRecords extends Activity {


    ArrayList<String> details;
    ArrayList<String> names;
    ListView lv;
    ProgressBar progressBar;
    ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_months_records);



        lv = (ListView) findViewById(R.id.monthRecordsListView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        details = new ArrayList<>();
        names = new ArrayList<>();
        listAdapter = new ListAdapter(this);
        lv.setAdapter(listAdapter);

        new Calculate().execute();

        progressBar.setVisibility(View.VISIBLE);


    }


    private class Calculate extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(1000);
                publishProgress(75);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i=0; i< getIntent().getStringArrayListExtra("details").size();i++){
                details.add(getIntent().getStringArrayListExtra("details").get(i));
                names.add(getIntent().getStringArrayListExtra("names").get(i));
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            listAdapter.notifyDataSetChanged();

        }
    }


    private class ListAdapter extends ArrayAdapter<String>
    {
        private ListAdapter(Context ctx)
        {
            super(ctx, 0);
        }

        public int getCount()
        {
            return details.size();
        }

        public String getMonthName(int position)
        {
            return names.get(position);
        }

        public String getMonthDetails(int position){
            return details.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = AutoMonthsRecords.this.getLayoutInflater();
            View result;
            result = inflater.inflate(R.layout.in_month_listview, null);
            TextView monthName = (TextView) result.findViewById(R.id.monthName);
            monthName.setText(getMonthName(position));
            TextView monthdetails = (TextView) result.findViewById(R.id.monthRecords);
            monthdetails.setText(getMonthDetails(position));
            return result;
        }
    }


}
