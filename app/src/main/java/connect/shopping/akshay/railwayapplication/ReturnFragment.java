package connect.shopping.akshay.railwayapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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


public class ReturnFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private String fromcity, tocity, fromcode, tocode;
    private int month,day,year, way;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ListView listView;

    private List<TrainsBetween> list ;
    private TrainsBetweenAdaptor adaptor;




    public ReturnFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        way = sharedPreferences.getInt("way",0);
        fromcity = sharedPreferences.getString("fromsname","");
        tocity = sharedPreferences.getString("tosname","");
        fromcode = sharedPreferences.getString("fromscode","");
        tocode = sharedPreferences.getString("toscode","");
        month = sharedPreferences.getInt("rmonth",0);
        year = sharedPreferences.getInt("ryear",0);
        day = sharedPreferences.getInt("rday",0);

        requestQueue = Volley.newRequestQueue(getContext());

        list = new ArrayList<>();






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_return, container, false);



            listView = (ListView) view.findViewById(R.id.returnListView);
            adaptor = new TrainsBetweenAdaptor(getActivity(), list);
            listView.setAdapter(adaptor);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //train number
                // source
                //destination
                //date
                //class


                String train_name = list.get(i).getTrain_name().toString();
                String train_number = list.get(i).getTrain_no()+"";
                //fromcode
                //tocode
                String date = day+"-"+month+"-"+year;

                Bundle bundle = new Bundle();
                bundle.putString("number",train_number);
                bundle.putString("name",train_name);
                bundle.putString("from",tocode);
                bundle.putString("to",fromcode);
                bundle.putString("date",date);

                SeatAvailabilityDialog seatAvailabilityDialog = new SeatAvailabilityDialog();
                seatAvailabilityDialog.setArguments(bundle);
                seatAvailabilityDialog.show(getActivity().getFragmentManager(),"SEAT AVAIL");
            }
        });

        if(way == 1) {
        }else {
            getData();
        }

        return  view;
    }


    public void getData(){

        String url = "http://api.railwayapi.com/between/source/"+tocode+"/dest/"+fromcode+"/date/"+day+"-"+month+"/apikey/edbcmt2c";
        //String url = "http://api.railwayapi.com/between/source/gkp/dest/ngp/date/27-08/apikey/edbcmt2c";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("train");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = jsonArray.getJSONObject(i);

                        int no = json.getInt("no");
                        String name = json.getString("name");
                        int number = json.getInt("number");
                        String depTime = json.getString("src_departure_time");
                        String arrivalTime = json.getString("dest_arrival_time");
                        String travelTime = json.getString("travel_time");

                        list.add(new TrainsBetween(name, arrivalTime, depTime, travelTime, number, no));
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                adaptor.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);

    }




}
