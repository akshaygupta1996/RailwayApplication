package connect.shopping.akshay.railwayapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://akki7272.000webhostapp.com/tutorials/login.php";
    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    SharedPreferences prefs;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String firstname, lastname;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsername = (EditText) findViewById(R.id.input_username);
        edtPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

        requestQueue = Volley.newRequestQueue(this);

        prefs = getSharedPreferences("PREFS",MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String username = edtUsername.getText().toString();
                final String password = edtPassword.getText().toString();


                if (username.isEmpty() || password.isEmpty()) {

                    if (username.isEmpty()) {

                        edtUsername.setError("Field cannot be empty");
                    } else {

                        edtPassword.setError("Field cannot be empty");
                    }
                } else {

                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                    stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);


                                if (jsonObject.getString("result").equals("success")) {


                                    firstname = jsonObject.getString("firstname").toString();
                                    lastname = jsonObject.getString("lastname").toString();
                                    user_id = jsonObject.getInt("user_id");
                                    editor.putInt("user_id", user_id);
                                    editor.putString("fname",firstname);
                                    editor.putString("lname",lastname);
                                    editor.commit();

                                    Intent intent = new Intent(getApplicationContext(), RailActivity.class);
                                    progressDialog.dismiss();
                                    finish();

                                    startActivity(intent);


                                } else {

                                    Toast.makeText(getApplicationContext(), "Username or Password Incorrect..Please Try Again", Toast.LENGTH_SHORT).show();
                                    edtUsername.setText("");
                                    edtPassword.setText("");
                                    progressDialog.dismiss();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Log.d("Method", "GetParams Called");
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("username", username);
                            hashMap.put("password", password);
                            return hashMap;
                        }

                    };

                    requestQueue.add(stringRequest);
                }

            }


        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

}
