package connect.shopping.akshay.railwayapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {


    private EditText edtFname,edtLname,edtRUsername,edtRPassword,edtCPassword,edtREmail,edtRPhone,edtRCollege;
    private Button btnRegister;
    private TextView txtLoginButton;
    private RequestQueue requestQueue;
    private static final String URL="https://akki7272.000webhostapp.com/tutorials/register.php";
    private StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtFname=(EditText)findViewById(R.id.input_fname);
        edtLname=(EditText)findViewById(R.id.input_lname);
        edtREmail=(EditText)findViewById(R.id.input_email);
        edtRUsername=(EditText)findViewById(R.id.input_username);
        edtRPassword=(EditText)findViewById(R.id.input_password);
        edtCPassword=(EditText)findViewById(R.id.input_repassword);
        edtRPhone = (EditText)findViewById(R.id.input_phone);
        edtRCollege = (EditText)findViewById(R.id.input_college);
        btnRegister=(Button)findViewById(R.id.btn_signup);
        txtLoginButton=(TextView)findViewById(R.id.link_login);

        edtRCollege.setVisibility(View.INVISIBLE);

        requestQueue= Volley.newRequestQueue(this);

        txtLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fname=edtFname.getText().toString();
                final String lname=edtLname.getText().toString();
                final String username=edtRUsername.getText().toString();
                final String password=edtRPassword.getText().toString();
                final String cPassword=edtCPassword.getText().toString();
                final String email=edtREmail.getText().toString();
                final String college = "Railway App Entry";
                final String phoneno = edtRPhone.getText().toString();


                if (!validate()) {
                    onSignupFailed();
                    return;
                }else {

                    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();



                    stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("Status", "onResponse method Invoked");


                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                progressDialog.dismiss();


                                if (jsonObject.getString("status").equals("1")) {



                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(getApplicationContext(), "Registration Complete", Toast.LENGTH_SHORT).show();
                                } else if (jsonObject.getString("status").equals("0")) {

                                    Toast.makeText(getApplicationContext(), "Registration Incomplete", Toast.LENGTH_SHORT).show();
                                } else if (jsonObject.getString("status").equals("2")) {
                                    Toast.makeText(RegisterActivity.this, "Username Already Exists", Toast.LENGTH_SHORT).show();
                                    edtRUsername.setText("");
                                    edtRUsername.setError("Username Already Exists");
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                                    edtRPassword.setText("");
                                    edtCPassword.setText("");
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
                            hashMap.put("firstname", fname);
                            hashMap.put("lastname", lname);
                            hashMap.put("email",email);
                            hashMap.put("username", username);
                            hashMap.put("password", password);
                            hashMap.put("repassword", cPassword);
                            hashMap.put("college",college);
                            hashMap.put("phone",phoneno);
                            return hashMap;
                        }

                    };

                    requestQueue.add(stringRequest);


                }


            }
        });


    }

    private void onSignupFailed() {

        Toast.makeText(RegisterActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
    }

    private boolean validate() {

        boolean valid = true;

        String v_fname = edtFname.getText().toString();
        String v_lname=edtLname.getText().toString();
        String v_email = edtREmail.getText().toString();
        String v_password = edtRPassword.getText().toString();
        String v_repassword=edtCPassword.getText().toString();
        String v_username=edtRUsername.getText().toString();
        String v_phone = edtRPhone.getText().toString();
        String v_college = "Railway App Entry";


        if (v_fname.isEmpty() || v_fname.length() < 3) {
            edtFname.setError("at least 3 characters");
            valid = false;
        } else {
            edtFname.setError(null);
        }
        if (v_lname.isEmpty() || v_lname.length() < 3) {
            edtLname.setError("at least 3 characters");
            valid = false;
        } else {
            edtLname.setError(null);
        }

        if (v_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(v_email).matches()) {
            edtREmail.setError("enter a valid email address");
            valid = false;
        } else {
            edtREmail.setError(null);
        }
        if (v_password.isEmpty() || v_password.length() < 4 || v_password.length() > 10) {
            edtRPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            edtRPassword.setError(null);
        }
        if (!v_repassword.equals(v_password)) {
            edtCPassword.setError("both password should match");
            valid = false;
        } else {
            edtCPassword.setError(null);
        }
        if (v_username.isEmpty() || v_username.length() < 8) {
            edtRUsername.setError("at least 8 characters");
            valid = false;
        } else {
            edtRUsername.setError(null);
        }
        if(v_college.isEmpty()){
            edtRCollege.setError("College cannot be empty");
            valid = false;
        }
        if(v_phone.isEmpty() || v_phone.length() !=10){
            edtRPhone.setError("Enter valid Phone Number");
            valid = false;

        }


        return valid;
    }

}
