package com.example.restimplementation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.restimplementation.volley.VolleySinglton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
Button createNewAccountButton,logInButton;
private String appURL;
private String user_email,user_password;
TextInputEditText userEmail;
TextInputEditText userPassword;
TextInputLayout passwordToggle;
Activity mContext=this;
TextView forgotPassword;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String UserId = "UserIDKey";
    public static final String Email = "emailKey";
    public static final String LoginStatus="login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNewAccountButton=findViewById(R.id.createNewAccountButtonId);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, this.MODE_PRIVATE);
        userEmail=findViewById(R.id.emailEditTextId);
        userPassword=findViewById(R.id.loginPagePasswordId);
        logInButton=findViewById(R.id.signInButtonId);
        passwordToggle=findViewById(R.id.password_toggle);
        forgotPassword=findViewById(R.id.forgot_password_Id);
        appURL="scanner-app.sarangtech.co.in/api/submit";
        String isUserActive=sharedpreferences.getString("login","");
        if(isUserActive.equals("Active"))
        {
          Intent dashboard=new Intent(getApplicationContext(),Dashboard_Nav.class);
          startActivity(dashboard);
        }
        else
        {
            // 1.  On Sign In Button Click
            logInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logIn();
                }
            });
            //2.  On Create New Button Click
            createNewAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performOperationOnCreateNewAccountButton();
                }
            });
            //on forgot password operations
            forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    //Method Called when user want to Create New Account
    private void performOperationOnCreateNewAccountButton() {
        Intent registrationPageIntent=new Intent(getApplicationContext(), SignUp.class);
        startActivity(registrationPageIntent);
    }
    //Method call when user want to log in
    private void logIn()
    {
        user_email=userEmail.getText().toString();
        user_password=userPassword.getText().toString();
        if(user_email.isEmpty())
        {
            userEmail.setError("Email can not be empty");
        }
        else if(user_password.isEmpty())
        {
            passwordToggle.setPasswordVisibilityToggleEnabled(false);
            userPassword.setError("Please enter valid password");
        }
        else if(user_password.length()<6)
        {
            passwordToggle.setPasswordVisibilityToggleEnabled(false);
            userPassword.setError("Please should be more than 5 characters");
           // passwordToggle.(false);

        }
        else
        {
            RequestQueue requestQueue= Volley.newRequestQueue(this);

            StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://scanner-app.sarangtech.co.in/api/login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String ID,NAME,EMAIL;
                    ID="";
                    NAME="";
                    EMAIL="";
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if(status.equals("Valid credential")) {
                            // fetch JSONArray named data
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            // implement for loop for getting users list data
                            for (int i = 0; i < dataArray.length(); i++) {
                                // create a JSONObject for fetching single user data
                                JSONObject userDetail = dataArray.getJSONObject(i);
                                // fetch email and name and store it in arraylist
                                ID = (userDetail.getString("id"));
                                NAME=(userDetail).getString("name");
                                EMAIL=(userDetail).getString("email");

                            }
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(UserId, ID);
                            editor.putString(Name, NAME);
                            editor.putString(Email, EMAIL);
                            editor.putString(LoginStatus,"Active");
                            editor.apply();
                            Intent dashboard=new Intent(getApplicationContext(),Dashboard_Nav.class);
                            startActivity(dashboard);
                        }
                        else
                        {
                            AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
                            alert=new AlertDialog.Builder(mContext);
                            alert.setTitle("Error");
                            alert.setMessage(status);
                            alert.setCancelable(false);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                        }
                    }
                    catch (Exception e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder alert;
                    NetworkResponse response=error.networkResponse;
                    if(response!=null && response.data !=null)
                    {
                        //Switch
                    }
                    else
                    {
                        alert=new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
                        alert.setMessage(error.toString());
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();

                    }
                }
            }){
                public Map<String,String> getHeaders()throws AuthFailureError
                {
                    HashMap<String,String>params=new HashMap<>();
                    params.put("Accept","Application/json;charset-UTF-8");
                    return params;
                }
                protected Map<String, String> getParams() throws AuthFailureError{
                    HashMap<String,String>param=new HashMap<>();
                    param.put("user_email", user_email);
                    param.put("user_password", user_password);
                    return param;
                }
            };
            //VolleySinglton.getInstance().addRequestQueue(stringRequest);
            requestQueue.add(stringRequest);
        }
    }
}
