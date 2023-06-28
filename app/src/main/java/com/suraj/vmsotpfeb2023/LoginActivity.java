package com.suraj.vmsotpfeb2023;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    CardView cardView;
    EditText Phone;
    ProgressBar pb;
    RequestQueue requestQueue;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String name = "";
    String phone;
    String count="";
    String UserDealerID = "";
    int id=0;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar a = getSupportActionBar();
        a.hide();
        preferences=getSharedPreferences(Util.AcuPrefs,MODE_PRIVATE);
        editor=preferences.edit();

        Phone = findViewById(R.id.Phone);
        cardView= findViewById(R.id.cardView);
        pb = findViewById(R.id.progressbar);
        requestQueue= Volley.newRequestQueue(this);
        Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ser = Phone.getText().toString();
                int ll = ser.length();
                if (ll > 9) {
                    cardView.setVisibility(View.VISIBLE);
                }

                if(ll != 10)
                {

                    cardView.setVisibility(View.GONE);
                }





            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = Phone.getText().toString().trim();
                if(phone.isEmpty())
                {
                    Phone.setError("Phone is required");
                    Phone.requestFocus();
                    return;
                }
                if (ConnectionCheck.isConnected(connectivityManager,networkInfo,LoginActivity.this)) {
                    loginUser();

                }else {
                    Toast.makeText(LoginActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    void loginUser(){

        pb.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(Request.Method.POST, Util.login1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.w("ass",""+response);

                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("students");
                    String message=object.getString("message");
                    if (message.contains("Login Sucessful")){
                        pb.setVisibility(View.GONE);

                        for (int i=0;i<array.length();i++)
                        {
                            JSONObject object1=array.getJSONObject(i);
                            id = object1.getInt("User_ID");
                            Log.w("jkjkjkjkjk",""+id);
                            name = object1.getString("User_name");
                            count = object1.getString("Count");
                            UserDealerID = object1.getString("DealerID");

                        }

                        editor.putString(Util.Name,name);
                        editor.putString(Util.Phone,Phone.getText().toString());
                        editor.putString(Util.count,count);
                        editor.putString(Util.UserDealerID, UserDealerID);
                        editor.putInt(Util.id,id);
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();







                    }else{
                        pb.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,"User Already Login. Please try with another Phone number", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Login Fail 75" +error.getMessage(), Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("Phone",Phone.getText().toString());
                //  map.put("Flag","off");
                return map;
            }
        };
        requestQueue.add(request);
    }

}
