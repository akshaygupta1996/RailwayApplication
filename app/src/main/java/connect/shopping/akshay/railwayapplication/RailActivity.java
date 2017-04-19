package connect.shopping.akshay.railwayapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RailActivity extends AppCompatActivity {

    EditText editFromCity, edtToCity;

    private int way, btn;
    private String fromcity, tocity,fromscode,toscode;

    private int dyear, ryear, year;
    private int dmonth, rmonth, month;
    private int dday, rday, day;
    private int[] s = {0,0};

    static final int DATE_PICKER_ID = 1111;

    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog pDialog;



    Button btnDepDate, btnToDate, btnSearchTrains, btnOneWay, btnTwoWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail);

        sharedPreferences = getSharedPreferences("PREFS",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString("fromscode","");
        editor.putString("toscode","");


        ryear=rmonth=rday=0;



        s[0]=0;
        s[1] = 0;



        btn = 0;

        way = 1;

        //1 one way
        // 2 two way


        editFromCity = (EditText)findViewById(R.id.editFromCity);
        edtToCity = (EditText)findViewById(R.id.editToCity);

        btnOneWay  = (Button)findViewById(R.id.btnOneWay);
        btnTwoWay = (Button)findViewById(R.id.btnTwoWay);


        btnDepDate = (Button)findViewById(R.id.btnDepDate);
        btnToDate = (Button)findViewById(R.id.btnRetDate);

        btnSearchTrains = (Button)findViewById(R.id.btnSearchTrains);


        btnToDate.setVisibility(View.INVISIBLE);
        btnSearchTrains.setClickable(false);


        requestQueue = Volley.newRequestQueue(this);
        btnDepDate.setText("Select Departure Date");
        btnToDate.setText("Select Return Date");



        btnOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDepDate.setText("Select Departure Date");
                btnToDate.setText("Select Return Date");
                s[0]= s[1]=0;
                btnSearchTrains.setClickable(false);

                way = 1;
                btnOneWay.setAlpha((float) 0.8);
                btnTwoWay.setAlpha((float) 0.3);

                btnToDate.setVisibility(View.INVISIBLE);

            }
        });

        btnTwoWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSearchTrains.setClickable(false);

                btnDepDate.setText("Select Departure Date");
                btnToDate.setText("Select Return Date");
                s[0]= s[1]=0;

                btnToDate.setVisibility(View.VISIBLE);

                way = 2;

                btnOneWay.setAlpha((float) 0.3);
                btnTwoWay.setAlpha((float) 0.8);

            }
        });

        btnDepDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn = 1;

                showDialog(DATE_PICKER_ID);

                btnDepDate.setText(dday+"-"+dmonth+"-"+dyear);

                if(way == 1){
                    btnSearchTrains.setClickable(true);
                }else if(way == 2){
                    s[0] = 1;
                    if(s[1] == 1){
                        btnSearchTrains.setClickable(true);
                    }
                }

            }
        });


        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn =2;

                showDialog(DATE_PICKER_ID);

                btnToDate.setText(rday+"-"+rmonth+"-"+dyear);
                if(way == 2){
                    s[1] = 1;
                    if(s[0]==1){
                        btnSearchTrains.setClickable(true);
                    }
                }



            }
        });

        btnSearchTrains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSearchTrains.setClickable(false);

                fromcity = editFromCity.getText().toString();
                tocity = edtToCity.getText().toString();



                saveStationCodes1();






            }
        });






    }

    public void nextActivity()
    {


        pDialog.dismiss();
        Log.d("CODES", fromcity+"  "+fromscode+"  "+tocity+"   "+toscode);
        if(fromscode!=null&&toscode!=null){

            editor.putString("fromsname", fromcity);
            editor.putString("fromscode", fromscode);

            editor.putString("tosname", tocity);
            editor.putString("toscode", toscode);

            editor.putInt("way",way);

            editor.putInt("dyear",dyear);
            editor.putInt("dmonth",dmonth);
            editor.putInt("dday",dday);

            editor.putInt("ryear",ryear);
            editor.putInt("rmonth",rmonth);
            editor.putInt("rday",rday);

            editor.commit();


            Intent intent = new Intent(RailActivity.this, ListTrainsActivity.class);
            startActivity(intent);

        }


    }

    private void saveStationCodes2() {

        String url = "http://api.railwayapi.com/name_to_code/station/"+tocity.toUpperCase()+"/apikey/edbcmt2c";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONObject jsonObject;
                JSONArray jsonArray;
                Log.d("RESPONSE",response.toString());
                try {
                    jsonObject = new JSONObject(response);

                    jsonArray = jsonObject.getJSONArray("stations");
                    String code = null,name;


                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject1= (JSONObject) jsonArray.get(i);

                        code = jsonObject1.getString("code");
                        name = jsonObject1.getString("fullname");

                        Log.d("Code Fullname", code+"  "+name);
                        if(name.contains(tocity.toUpperCase())){
                            toscode = code;
                            i = jsonArray.length()+1;
                        }
                    }
                    nextActivity();




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);



    }

    private void saveStationCodes1() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = "http://api.railwayapi.com/name_to_code/station/"+fromcity.toUpperCase()+"/apikey/edbcmt2c";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONObject jsonObject;
                JSONArray jsonArray;
                Log.d("RESPONSE",response.toString());
                try {
                    jsonObject = new JSONObject(response);

                    if(jsonObject.has("stations")) {
                        jsonArray = jsonObject.getJSONArray("stations");
                        String code = null, name;


                        Log.d("RES", jsonArray.length() + "");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                            code = jsonObject1.getString("code");
                            name = jsonObject1.getString("fullname");

                            Log.d("Code Fullname", code + "  " + name);


                            if (name.contains(fromcity.toUpperCase())) {

                                fromscode = code;
                                i = jsonArray.length() + 1;
                            }
                        }

                        saveStationCodes2();

                    }else{
                        Toast.makeText(RailActivity.this, "Enter City Name correctly", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }

//                    editor.putString("fromsname", fromcity);
//                    editor.putString("fromscode", code);
//                    editor.commit();




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(stringRequest);


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(this, pickerListener, mYear, mMonth,mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {


        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            if(way == 1) {

                dyear = selectedYear;
                dmonth = selectedMonth;
                dday = selectedDay;
                Toast.makeText(RailActivity.this, dday + "-" +dmonth+"- "+dyear, Toast.LENGTH_SHORT).show();
            }else if (way ==2 ){

                if(btn == 1){
                    dyear = selectedYear;
                    dmonth = selectedMonth;
                    dday = selectedDay;


                    Toast.makeText(RailActivity.this, dday + "-" +dmonth+"- "+dyear, Toast.LENGTH_SHORT).show();



                }else if(btn == 2){

                    ryear = selectedYear;
                    rmonth = selectedMonth;
                    rday = selectedDay;
                    Toast.makeText(RailActivity.this, rday + "-" +rmonth+"- "+ryear, Toast.LENGTH_SHORT).show();

                }
            }

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

//            Toast.makeText(RailActivity.this, day + "-" +month+"- "+year, Toast.LENGTH_SHORT).show();
            // Show selected date
//            Output.setText(new StringBuilder().append(month + 1)
//                    .append("-").append(day).append("-").append(year)
//                    .append(" "));

        }
    };
}
