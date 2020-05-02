package com.example.restimplementation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.restimplementation.volley.VolleySinglton;

import java.util.HashMap;
import java.util.Map;

public class InsertBarcodeData {
    String barcode_data;
    String barcodeFormat;
    String barcodeValueFormat;
    String barcodeScanDate;
    String barcodeScanTime;
    String userId;
    public String InsertBarcode(String uid,String data, String barcode_Format, String barcodeValue_Format, final String barcodeScan_Date, final String barcodeScan_Time)
    {
        this.userId=uid;
        this.barcode_data=data;
        this.barcodeFormat=barcode_Format;
        this.barcodeValueFormat=barcodeValue_Format;
        this.barcodeScanDate=barcodeScan_Date;
        this.barcodeScanTime=barcodeScan_Time;

        final String[] result = new String[1];
        String appURL="http://scanner-app.sarangtech.co.in/api/insertscanitems";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, appURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("1")) {
                    result[0] ="Y";
                } else {
                    result[0] ="N";
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
                            break;
                        case 404:
                            break;
                        case 403:
                            break;
                    }
                } else {
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
                param.put("barcode_data", barcode_data);
                param.put("user_id",userId);
                param.put("barcode_format",barcodeFormat);
                param.put("barcode_value_format",barcodeValueFormat);
                param.put("barcode_scan_date",barcodeScanDate);
                param.put("barcode_scan_time",barcodeScanTime);
                return param;
            }
        };
        VolleySinglton.getInstance().addRequestQueue(stringRequest);
        return result[0];
    }
}
