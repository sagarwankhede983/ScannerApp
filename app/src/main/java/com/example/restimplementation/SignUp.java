package com.example.restimplementation;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.restimplementation.volley.VolleySinglton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    Button signupButton;
    String appURL;
    CheckBox cterms;
    EditText userEmail;
    TextInputEditText userName,userPassword,userConfirmPassword;
    EditText userPhone;
    String user_name,user_email,user_phone,user_password,user_confirm_password,business;
    Activity mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        spinner=findViewById(R.id.spinner);
        signupButton=findViewById(R.id.signUpButtonId);
        userName=findViewById(R.id.nameEditTextId);
        userEmail=findViewById(R.id.emailEditTextId);
        userPassword=findViewById(R.id.passwordloginEditTextId);
        userConfirmPassword=findViewById(R.id.confirmPassEditTextId);
        userPhone=findViewById(R.id.phoneEditTextId);
        cterms=findViewById(R.id.checkBox2);
        appURL="http://scanner-app.sarangtech.co.in/api/signup";
        setSpinnerValues();
        //On Sign Up Click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignupOperation();
            }
        });
    }
    public void setSpinnerValues() {
        spinner.setOnItemSelectedListener(this);
        String []spinner_items={"Automotive","Business Support & Supplies","Computers & Electronics","Construction & Contractors","Education","Entertainment","Food & Dining","Health & Medicine","Home & Garden","Legal & Financial","Manufacturing/Wholesale/Distribution","Merchants (Retail)","Miscellaneous","Personal Care & Services","Real Estate","Travel & Transportation"};
        ArrayAdapter spinnerArrayadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinner_items);
        spinnerArrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayadapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        business = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void onSignupOperation() {
        String checked_term_clicked = "";
        boolean sign_up_validation_result;
        boolean term_condition_validation=true,business_validation=true;
        user_name = userName.getText().toString().trim();
        user_email = userEmail.getText().toString().trim();
        user_password = userPassword.getText().toString().trim();
        user_confirm_password = userConfirmPassword.getText().toString().trim();
        user_phone = userPhone.getText().toString().trim();
        if (!cterms.isChecked()) {
            checked_term_clicked = "notChecked";
            final AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
            alert.setTitle("Error");
            alert.setMessage("Please accept term & conditions");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            term_condition_validation=false;
        }
        if(business.isEmpty())
        {final AlertDialog.Builder alert=new AlertDialog.Builder(mContext);
            alert.setTitle("Error");
            alert.setMessage("Please select business");
            alert.setCancelable(false);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
            business_validation=false;
        }
      //  SignUpValidation signUpValidation = new SignUpValidation();
        //sign_up_validation_result = signUpValidation.onSignupBasicValidation(user_name, user_email, user_phone, user_password, user_confirm_password, business, checked_term_clicked,userName,userPhone,userEmail,userPassword,userConfirmPassword);
        sign_up_validation_result=true;
        if (sign_up_validation_result && term_condition_validation && business_validation) {

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, appURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("true")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setView(R.layout.activity_registration_successfull_dialog_layout);
                        alert.setCancelable(false);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent returnToLogInIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(returnToLogInIntent);
                            }
                        });
                        alert.show();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
                        alert.setMessage(response);
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
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder alert;
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 400:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage(response.data.toString());
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 404:
                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage(response.data.toString());
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                            case 403:

                                alert = new AlertDialog.Builder(mContext);
                                alert.setTitle("Error");
                                alert.setMessage(response.data.toString());
                                alert.setCancelable(false);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                break;
                        }
                    } else {
                        alert = new AlertDialog.Builder(mContext);
                        alert.setTitle("Error");
                        alert.setMessage("please try again");
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Accept", "Application/html;charset-UTF-8");
                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("user_name", user_name);
                    param.put("user_email", user_email);
                    param.put("user_phone", user_phone);
                    param.put("business", business);
                    param.put("user_password", user_password);
                    param.put("user_confirm_password", user_confirm_password);
                    return param;
                }
            };
            VolleySinglton.getInstance().addRequestQueue(stringRequest);
        }
         else {
        }
    }
}
