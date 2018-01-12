package com.example.markg.automobile;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AutoActivity extends AppCompatActivity {

   /* private static String combineNumbers (double x, double y, double z){
        String xs = Double.toString(x)+" $ ";
        String ys = Double.toString(y)+" liters ";
        String zs = Double.toString(z)+" Km. ";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String total = (xs + ys + zs +"\n"+dateFormat.format(cal.getTime()));
        return total;
    }*/
    protected static final int REQUEST_MSG_DETAILS = 1;
    protected static final int REQUEST_MSG_DELETE = 2;
    protected static final int REQUEST_MSG_EDIT = 3;

    protected static final String ACTIVITY_NAME = "AutoActivity";

    SQLiteDatabase db;
    AutoDatabaseHelper dbHelper;
    ArrayList<Long> autoIDs;


    //ArrayList<Double> priceArray;
    //ArrayList<Double> litersArray;
    //rrayList<Double> kilometersArray;
    //ArrayList<String> allArray;
    ArrayList<AutoData> autoData;
    ArrayList<String> monthsNames;
    ArrayList<String> yearsNames;
    //String monthName;



    TextView welcome;
    ListView listView;
    EditText price;
    EditText liters;
    EditText kilometers;
    FloatingActionButton addBtn;
    FloatingActionButton monthBtn;
    FloatingActionButton yearBtn;
    View dialgoview;
    boolean isTablet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] allColumns = {AutoDatabaseHelper.KEY_ID, AutoDatabaseHelper.KEY_MESSAGE};
        dbHelper = new AutoDatabaseHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        listView = (ListView) findViewById(R.id.list);

        /*priceArray = new ArrayList<>();
        litersArray = new ArrayList<>();
        kilometersArray = new ArrayList<>();*/
        //allArray = new ArrayList<>();
        autoData= new ArrayList<>();
        autoIDs = new ArrayList<>();
        monthsNames = new ArrayList<>();
        yearsNames = new ArrayList<>();


        final ListAdapter listAdapter = new ListAdapter(this);
        listView.setAdapter(listAdapter);
        isTablet = (findViewById(R.id.messageDetailFrame) != null);

        Cursor cursor = db.query(AutoDatabaseHelper.TABLE_MESSAGES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(AutoDatabaseHelper.KEY_MESSAGE)));
            autoData.add(new AutoData(cursor.getString(cursor.getColumnIndex(AutoDatabaseHelper.KEY_MESSAGE))));
            autoIDs.add(cursor.getLong(cursor.getColumnIndex(AutoDatabaseHelper.KEY_ID)));
            cursor.moveToNext();
        }
        cursor.close();

        addBtn = (FloatingActionButton) findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AutoActivity.this);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                dialgoview =  inflater.inflate(R.layout.add_dialog_layout, null) ;
                builder.setView (dialgoview)
                        // Add action buttons
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    price = (EditText) dialgoview.findViewById(R.id.price);
                                    liters = (EditText) dialgoview.findViewById(R.id.liters);
                                    kilometers = (EditText) dialgoview.findViewById(R.id.kilos);
                                    AutoData ad = new AutoData(Double.parseDouble(liters.getText().toString()),
                                            Double.parseDouble(kilometers.getText().toString()),
                                            Double.parseDouble(price.getText().toString()));
                                    autoData.add(ad);
                                    //allArray.add(ad.toString());
                                    ContentValues insertValues = new ContentValues();
                                    insertValues.put(AutoDatabaseHelper.KEY_MESSAGE,
                                            ad.toString());

                                    autoIDs.add(db.insert(AutoDatabaseHelper.TABLE_MESSAGES, null, insertValues));
                                    /*priceArray.add(Double.parseDouble(price.getText().toString()));
                                    litersArray.add(Double.parseDouble(liters.getText().toString()));
                                    kilometersArray.add(Double.parseDouble(kilometers.getText().toString()));
                                    String c = combineNumbers(Double.parseDouble(price.getText().toString()),
                                            Double.parseDouble(liters.getText().toString()),
                                            Double.parseDouble(kilometers.getText().toString()));
                                    allArray.add(c);*/
                                    listAdapter.notifyDataSetChanged();

                                    dialog.dismiss();
                                    Snackbar.make(findViewById(R.id.appBarLayout), "Items Added", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();

                                }catch (Exception e){
                                    Toast.makeText(AutoActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog dialog2 = builder.create();
                dialog2.show();



            }
        });

        monthBtn = (FloatingActionButton) findViewById(R.id.month);
        monthBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //AutoMonthsRecords amr = new AutoMonthsRecords(autoData);
                Intent intent = new Intent(AutoActivity.this,AutoMonthsRecords.class);
                intent.putStringArrayListExtra("names", monthsNames);
                intent.putStringArrayListExtra("details",getMonthsRecords(autoData));

                startActivity(intent);
            }
        });

        yearBtn = (FloatingActionButton) findViewById(R.id.year);
        yearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutoActivity.this,AutoMonthsRecords.class);
                intent.putStringArrayListExtra("names", yearsNames);
                intent.putStringArrayListExtra("details",getYearsRecords(autoData));

                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Bundle newBundle = new Bundle();
                newBundle.putString("message", listAdapter.getItem(position));
                newBundle.putLong("id", listAdapter.getItemID(position));

                // Action if tablet
                if (isTablet)
                {
                    AutoFragment messageFragment = new AutoFragment(AutoActivity.this);
                    messageFragment.setArguments(newBundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.messageDetailFrame, messageFragment);
                    ft.commit();
                }
                else
                {
                    Intent msgDetailsIntent = new Intent(getApplicationContext(), AutoDetails.class);
                    msgDetailsIntent.putExtras(newBundle);
                    startActivityForResult(msgDetailsIntent, REQUEST_MSG_DETAILS);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("position", "position is: "+i);
                editItem(autoData.get(i).toString(),i);

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_about:
                Toast.makeText(this, "Automobile by Mark Girgis", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(AutoActivity.this);
                builder.setMessage("1- click the middle button to add a new information\n" +
                        "2- click the right button to show your month records\n" +
                        "3- click the left button to get the year records\n" +
                        "4- click and hold on an instance to edit it\n"+
                        "5- choose one of the toolbar buttons to switch between activities")
                        .setPositiveButton("I know", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                            }
                        });
                AlertDialog dialog3 =  builder.create();
                dialog3.show();
                break;
        }

        return true;
    }

    public double getTotalLitersPerMonth(int month) {
        double sum = 0.0;
        for(int i=0;i<autoData.size();i++){
            if(autoData.get(i).getMonth()==month) {
                sum += autoData.get(i).getLiters();
            }
        }
        return sum;
    }

    public double getAvgPricePerMonth(int month) {
        double avg = 0.0, sum=0.0,  count=0.0;
        for(int i=0;i<autoData.size();i++){
            if(autoData.get(i).getMonth()==month) {
                sum+= autoData.get(i).getPrice();
                count++;
            }
        }
        avg = sum/count;
        return avg;
    }

    public double getAvgLitersPerMonth(int year){
        double avg = 0.0, sum =0.0, count = 1.0;
        for(int i = 0; i<autoData.size(); i++){
            if(autoData.get(i).getYear()==year){
                sum+= autoData.get(i).getLiters();
            }

            if(i!=0&&autoData.get(i).getMonth()!=autoData.get(i-1).getMonth()){
                count++;
            }
        }
        avg = sum/count;
        return avg;
    }

    public String getYearDetails(int year){
        return ("Your average gasoline per month for this year is:"+getAvgLitersPerMonth(year));
    }

    public String getMonthDetails(int month){
        return ("Your Total Liters This Month is: \n" + getTotalLitersPerMonth(month)
        +"\n Your Average Gasoline Price for this Month is \n"+getAvgPricePerMonth(month));
    }
    public String getMonthName(int monthNumber){
        String monthString = "";
        switch (monthNumber){
            case 1:  monthString = "January";
                break;
            case 2:  monthString = "February";
                break;
            case 3:  monthString = "March";
                break;
            case 4:  monthString = "April";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "June";
                break;
            case 7:  monthString = "July";
                break;
            case 8:  monthString = "August";
                break;
            case 9:  monthString = "September";
                break;
            case 10: monthString = "October";
                break;
            case 11: monthString = "November";
                break;
            case 12: monthString = "December";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;
    }

    public ArrayList<String> getMonthsRecords(ArrayList<AutoData> x){
        ArrayList<String> monthsRecords;
        monthsRecords = new ArrayList<>();
        int month = 0;
        for(int i = 0;i<x.size();i++){
            if(x.get(i).getMonth()!= month){
                month=x.get(i).getMonth();
                monthsNames.add(getMonthName(month));
                monthsRecords.add(getMonthDetails(month));
            }
        }
        return  monthsRecords;
    }

    public ArrayList<String> getYearsRecords(ArrayList<AutoData> x){
        ArrayList<String> yearsRecords;
        yearsRecords = new ArrayList<>();
        int year = 0;
        for(int i = 0;i<x.size();i++){
            if(x.get(i).getYear()!= year){
                year=x.get(i).getYear();
                yearsNames.add(Integer.toString(year));
                yearsRecords.add(getYearDetails(year));
            }
        }
        return  yearsRecords;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if (resultCode == REQUEST_MSG_DELETE)
        {
            Long msgID = data.getLongExtra("msgID",-1);
            Log.i(ACTIVITY_NAME, "Request Deletion of message with id: " + Long.toString(msgID));
            deleteItem(msgID);
        }

    }

    public void deleteItem(long id)
    {
        db.delete(AutoDatabaseHelper.TABLE_MESSAGES, AutoDatabaseHelper.KEY_ID + "=" + id, null);

        int position = autoIDs.indexOf(id);
        autoData.remove(position);
        autoIDs.remove(position);

        final ListAdapter adapter = (ListAdapter) listView.getAdapter();
        adapter.notifyDataSetChanged();
    }

    public void editItem(final String x , final int i){
        Log.i("string selected","string is"+x);
        AlertDialog.Builder builder = new AlertDialog.Builder(AutoActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        dialgoview =  inflater.inflate(R.layout.add_dialog_layout, null) ;
        builder.setView (dialgoview)
                // Add action buttons
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        price = (EditText) dialgoview.findViewById(R.id.price);
                        liters = (EditText) dialgoview.findViewById(R.id.liters);
                        kilometers = (EditText) dialgoview.findViewById(R.id.kilos);

                        AutoData ad = new AutoData(Double.parseDouble(liters.getText().toString()),
                                Double.parseDouble(kilometers.getText().toString()),
                                Double.parseDouble(price.getText().toString()));

                        autoData.set(i,ad);

                        ContentValues cv = new ContentValues();
                        cv.put(AutoDatabaseHelper.KEY_MESSAGE,
                                ad.toString());
                        db.update(AutoDatabaseHelper.TABLE_MESSAGES,cv,AutoDatabaseHelper.KEY_ID + "=" + (i+1),null);



                        Log.i("updated ?", "kilos is: "+autoData.get(i).getKilos());


                        final ListAdapter adapter = (ListAdapter) listView.getAdapter();
                        adapter.notifyDataSetChanged();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog2 = builder.create();
        dialog2.show();
    }

    private class ListAdapter extends ArrayAdapter<String>
    {
        private ListAdapter(Context ctx)
        {
            super(ctx, 0);
        }

        public int getCount()
        {
            return autoData.size();
        }

        public String getItem(int position)
        {
            return autoData.get(position).toString();
        }

        public Long getItemID(int position)
        {
            return autoIDs.get(position);
        }


        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = AutoActivity.this.getLayoutInflater();
            View result;
            result = inflater.inflate(R.layout.in_the_listview, null);
            TextView message = (TextView) result.findViewById(R.id.text);
            message.setText(getItem(position));
            return result;
        }
    }
}
