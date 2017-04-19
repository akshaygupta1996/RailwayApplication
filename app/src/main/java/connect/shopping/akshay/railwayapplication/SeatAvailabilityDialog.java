package connect.shopping.akshay.railwayapplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 18-04-2017.
 */

public class SeatAvailabilityDialog extends DialogFragment implements AdapterView.OnItemSelectedListener{


    private Spinner spinner;
    private Button btnCheck, btnBook, btnCancel;
    private TextView txtName, txtNumber, txtFrom, txtTo, txtAvailabe;
    private String coach, train, from, to, date, av, name;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflator=getActivity().getLayoutInflater();
        View view=inflator.inflate(R.layout.seat_availability_dialog,null);
        builder.setView(view);


        Bundle bundle = getArguments();
        train = bundle.getString("number");
        from = bundle.getString("from");
        to = bundle.getString("to");
        date = bundle.getString("date");
        name = bundle.getString("name");



        requestQueue = Volley.newRequestQueue(getContext());
        spinner = (Spinner)view.findViewById(R.id.classSpinner);
        btnCheck = (Button)view.findViewById(R.id.btnAvailable);
        btnBook = (Button)view.findViewById(R.id.btnBook);
        btnCancel = (Button)view.findViewById(R.id.btnCancel);


        spinner.setOnItemSelectedListener(this);
        List<String> coaches = new ArrayList<String>();
        coaches.add("1A");
        coaches.add("2A");
        coaches.add("3A");
        coaches.add("SL");

        ArrayAdapter<String> dataAdapter  = new ArrayAdapter<String>(getContext(),  android.R.layout.simple_spinner_item, coaches);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        txtName = (TextView)view.findViewById(R.id.textName);
        txtNumber = (TextView)view.findViewById(R.id.textNumber);
        txtFrom = (TextView)view.findViewById(R.id.textFrom);
        txtTo = (TextView)view.findViewById(R.id.textTo);
        txtAvailabe = (TextView)view.findViewById(R.id.textAvailable);


        btnBook.setClickable(false);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Checking Availability...");
                pDialog.show();

                String url = "http://api.railwayapi.com/check_seat/train/"+train+"/source/"+from+"/dest/"+to+"/date/"+date+"/class/"+coach+"/quota/GN/apikey/edbcmt2c";


                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("RESPONSE",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray avail = jsonObject.getJSONArray("availability");
                            if(avail.length()>0) {
                                JSONObject json = (JSONObject) avail.get(0);
                                av = json.getString("status");
                                btnBook.setClickable(false);
                            }else{
                                av = "Not Available";
                            }

                            txtName.setText(name);
                            txtNumber.setText(train);
                            txtFrom.setText(from);
                            txtTo.setText(to);
                            txtAvailabe.setText(av);

                            btnBook.setClickable(true);
                            pDialog.dismiss();



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
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(), IRCTCWebVIew.class);
                startActivity(intent);
                
            }
        });






        Dialog dialog=builder.create();


        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        coach = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
