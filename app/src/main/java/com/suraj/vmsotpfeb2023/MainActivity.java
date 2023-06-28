package com.suraj.vmsotpfeb2023;

// This is the Dealer/User App


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;


public class MainActivity extends AppCompatActivity {

    ImageView img, imglogo;
    TextView txtresult, nameser, wnames, Or, Or1, OR2, TxtVersion;
    Button scan, ServiceScan, ChooseCode, OpenCameraScanner;
    Uri uri, imageUri;
    RequestQueue requestQueue;
    public Bitmap mbitmap;
    public static final int PICK_IMAGE = 1;
    CardView cardViewButton = null;
    CardView cardViewButton1 = null;
    CardView gcardViewButton = null;
    CardView gcardViewButton1 = null;
    EditText editText = null;
    String sex = " ";
    String sexd = " ";
    EditText geditText = null;
    String newString = "";
    String value1 = "";
    int updatedPaperCount = 0;
    String unchangedvalue0;
    String vmskey = "";
    String QrCodeDealerId = "";
    int PaperCount = 0;
    String mcid = "";
    String value0 = "";
    String LMG = "", MG = "", RMG = "";
    String PLMG = "", PMG = "", PRMG = "";
    int c = 0;
    String name = "";
    Button Entrprnt, CameraButton2;
    String DealerName = "";
    String zone = "";
    String qrcode ="";
    String OTPCODE = "";
    int printCount = 0;
    int BalanceCount = 0;
    String RequestedFilmSize = "";
    int RequestedRechargeCount = 0;
    String gname = "";
    String gzone = "";
    String LogedInPhoneNo = "";
    String OtpUserTableDealerId;
    String ctd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


    private static final int CAMERA_PERMISSION_CODE = 112;


    String gOTPCODE = "";
    TextView textViewZone, RemainingCount, textViewVmsKey, textViewMcid, txtQrCode, txtBalanceCount;
    EditText textViewName, ServiceEdit, gtextViewName, PrintID,gtextViewZone, textViewDealerName, gtextViewDealerName;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    int count = 0, id = 0;
    String userName = "";
    String wname;
    Dialog dialog, pdialog;

    ProgressDialog pd;
    Spinner spinner;
    String SheetTpe; // This is FlimSize
    String hi; // this is the main requestid that we are inserting into table

    private CodeScanner codeScanner;
    CodeScannerView codeScannerView;

    // second camera
    SurfaceView cameraPreview;
    TextView cresult;
    BarcodeDetector detector;
    CameraSource cameraSource;

    String loginph = "";
    List<String> otpList;
    String updatedresult;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RemainingCount = findViewById(R.id.remainpc);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        preferences = getSharedPreferences(Util.AcuPrefs, MODE_PRIVATE);
        editor = preferences.edit();
        count = Integer.parseInt(preferences.getString(Util.count, ""));
        OtpUserTableDealerId = preferences.getString(Util.UserDealerID,"");
        Log.w("iddivalue",OtpUserTableDealerId);
        wname = preferences.getString(Util.Name, "");
        LogedInPhoneNo = preferences.getString(Util.Phone,"");

        id = preferences.getInt(Util.id, 0);
        userName = preferences.getString(Util.Name, "");
        requestQueue = Volley.newRequestQueue(this);
        codeScannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, codeScannerView);
        scan = (findViewById(R.id.scan));
        img = (findViewById(R.id.imgview));
        OR2 = findViewById(R.id.tvssss);
        ServiceEdit = findViewById(R.id.editser);
        Or = findViewById(R.id.tvs);
        Or1 = findViewById(R.id.tvs1);
        wnames = findViewById(R.id.wname);
        nameser = findViewById(R.id.namenn);
        imglogo = findViewById(R.id.logoimg);
        ServiceScan = findViewById(R.id.scanservice);
        ChooseCode = findViewById(R.id.slectimg);
        txtresult = (findViewById(R.id.txtResult));
        Entrprnt = findViewById(R.id.gntprnt);
        TxtVersion = findViewById(R.id.versiontxt);
        CameraButton2 = findViewById(R.id.chooseImage2);
        scan.setVisibility(View.GONE);
        pd = new ProgressDialog(this);
        OpenCameraScanner = findViewById(R.id.chooseImage);
        pd.setMessage("Generating OTP..");
        pd.setCancelable(false);

        ServiceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ser = ServiceEdit.getText().toString();
                int ll = ser.length();
                if (ll > 11) {
                    ServiceScan.setVisibility(View.VISIBLE);
                    Or.setVisibility(View.GONE);
                    Or1.setVisibility(View.GONE);
                    ChooseCode.setVisibility(View.GONE);


                }
                if (ll < 12) {
                    ServiceScan.setVisibility(View.GONE);
                    Or.setVisibility(View.VISIBLE);
                    Or1.setVisibility(View.VISIBLE);
                    ChooseCode.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.w("MobileNoDiValue",LogedInPhoneNo);
        getLogoutdata();
    }



    void ScanNshare()
    {

        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        Frame frame = new Frame.Builder()
                .setBitmap(mbitmap)
                .build();

        SparseArray<Barcode> barcodeSparseArray = detector.detect(frame);

        if (barcodeSparseArray.size() > 0) {
            Barcode result = barcodeSparseArray.valueAt(0);
            String serviceOTP = scanTxt(result.rawValue);
            if (Character.isAlphabetic(serviceOTP.charAt(0)) & Character.isDigit(serviceOTP.charAt(1)) & Character.isDigit(serviceOTP.charAt(2)) & Character.isDigit(serviceOTP.charAt(3))& Character.isDigit(serviceOTP.charAt(4))& Character.isDigit(serviceOTP.charAt(5))& Character.isDigit(serviceOTP.charAt(6))& Character.isDigit(serviceOTP.charAt(7))& Character.isAlphabetic(serviceOTP.charAt(8))& Character.isDigit(serviceOTP.charAt(9))& Character.isDigit(serviceOTP.charAt(10))& Character.isDigit(serviceOTP.charAt(11))) {
                try {

                    String[] otp = serviceOTP.split(";");
                    OTPCODE = otp[0];

                    qrcode = OTPCODE;


                    if(otp.length>1)
                    {
                        name = otp[1];

                    }

                    if(otp.length>2)
                    {

                        DealerName = otp[2];
                    }



                    if(otp.length>3)
                    {
                        zone = otp[3];
                    }

                    if(otp.length>4)
                    {

                        vmskey = otp[4];
                    }

                    if(otp.length>5)
                    {

                        mcid = otp[5];
                    }

                    if(otp.length>6)
                    {
                        QrCodeDealerId = otp[6];
                    }

                    if(otp.length>7)
                    {
                        BalanceCount = Integer.parseInt(otp[7]);
                    }

                    if (otp.length>8)
                    {
                        RequestedFilmSize = otp[8];
                    }

                    if(otp.length>9)
                    {
                        RequestedRechargeCount = Integer.parseInt(otp[9]);
                    }

                    showDialouge();
                }

                catch (Exception e)
                {
                    Toast.makeText(this, "Invalid QR Code"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            } else {
                 Toast.makeText(this, "Qr Code not supported", Toast.LENGTH_SHORT).show();

               //shareWhatsapp(serviceOTP);
            }
        } else {
            Toast.makeText(this, "Select a QR code Or QR Code is Not Clear", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnScan(View view)
    {
        ScanNshare();

    }




    public void SelectImg(View view)
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Qr Code"), PICK_IMAGE);
        txtresult.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            uri = data.getData();
            try {
                mbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                img.setImageBitmap(mbitmap);
                imglogo.setVisibility(View.GONE);
                //    imglogo.setVisibility(View.VISIBLE);
                nameser.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                wnames.setVisibility(View.GONE);
                ServiceEdit.setVisibility(View.GONE);
                Or.setVisibility(View.GONE);
                OpenCameraScanner.setVisibility(View.GONE);
                Or1.setVisibility(View.GONE);
                OR2.setVisibility(View.GONE);
                scan.setVisibility(View.VISIBLE);
                Entrprnt.setVisibility(View.GONE);
                TxtVersion.setVisibility(View.GONE);
                CameraButton2.setVisibility(View.GONE);
            } catch (IOException e) {

                Toast.makeText(this, "224"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(1, 101, 0, "Logout");
        return true;
    }
    public void getLogoutdata(){
        StringRequest request=new StringRequest(Request.Method.POST, Util.splashLoginUserCheck, response -> {
            try {
                JSONObject object=new JSONObject(response);
                JSONArray array=object.getJSONArray("students");
                String message=object.getString("message");
                if (message.contains("Sucessful")){
                    for (int i=0;i<array.length();i++){
                        JSONObject object1=array.getJSONObject(i);
                        loginph = object1.getString("LogInPh");
                        Log.w("LoginPhdival", loginph);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        } ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map=new HashMap<>();
                map.put("Phone",LogedInPhoneNo);
                Log.w("ehvaluedekhli", LogedInPhoneNo);
                return map;
            }
        };
        requestQueue.add(request);
    }

    public void updateLogoutdata(){
        StringRequest request=new StringRequest(Request.Method.POST, Util.updateLogout, response -> {
            try {
                JSONObject object=new JSONObject(response);
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(this, LoginActivity.class));



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        } ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map=new HashMap<>();
                map.put("LogInPh",updatedresult+",");
                map.put("uname",wname.toString().trim());
                Log.w("llio",updatedresult+",");
                Log.w("lliop",wname);
                //  map.put("Phone",LogedInPhoneNo);
                //Log.w("ehvaluedekhli", LogedInPhoneNo);
                return map;
            }
        };
        requestQueue.add(request);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case 101:


                // String logoutkenwalaphone = "8888888888";
                // loginph = "7777777777,8888888888,55555,88888,9999999";
                Log.w("lllll", loginph);
                String[] otp = loginph.split(",");
                Log.w("Stringotpval", ""+otp[0]);

                otpList = new ArrayList<>(Arrays.asList(otp));
                Log.w("ehotplistva", otpList.toString());
                Log.w("lgin", LogedInPhoneNo);
                // if (otpList.contains(LogedInPhoneNo)) {
                otpList.remove(LogedInPhoneNo);
                //   }

                StringJoiner sj = new StringJoiner(",");
                for (String s : otpList) {
                    if (s != null && !s.isEmpty() && !s.trim().equals(",")) {
                        sj.add(s.trim());
                    }
                }
                updatedresult = sj.toString();


                Log.w("updatedlisttosend", updatedresult);





                //   for (String element : otpList) {
                //     otpList.add(",");
                //   Log.w("Valuetosend",element+",");


                //}

                //Log.w("afterlooplist", otpList.toString());

                updateLogoutdata();

                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }
        return true;
    }



    //  eh shayad serive otp da logic va

    String scanTxt(String raw)
    {
        StringBuilder Id = new StringBuilder();
        if  (Character.isAlphabetic(raw.charAt(0)) & Character.isDigit(raw.charAt(1)) & Character.isDigit(raw.charAt(2)) & Character.isDigit(raw.charAt(3))& Character.isDigit(raw.charAt(4))& Character.isDigit(raw.charAt(5))& Character.isDigit(raw.charAt(6))& Character.isDigit(raw.charAt(7))& Character.isAlphabetic(raw.charAt(8))& Character.isDigit(raw.charAt(9))& Character.isDigit(raw.charAt(10))& Character.isDigit(raw.charAt(11))) {


            return raw;
        } else {


            // Etho step 1 suru hunda for Admin OTP
            // Get the String


            // Get the index
            int index = 3;
            char character = raw.charAt(3);
            Log.w("valueofchar",""+character);
            int asc = (int) character;

            // Get the character
            if(asc==49)
            {
                asc = 57;
            }

            else if(asc==65)
            {
                asc = 90;

            }

            else {
                asc = asc - 1;


            }

            char charAscii = (char) asc;

            raw = raw.substring(0, index)
                    + charAscii
                    + raw.substring(index + 1);

            Log.w("finalrawvalue",raw);

            // Etho step 2 suru hunda va for Admin OTP

            for (int i = 0; i <= 11; i++) {
                if (i == 4 || i == 8) {
                    Id.append(" ");
                }
                char d = raw.charAt(i);

                if (Character.isLetter(d)) {
                    int   ASCII = d;
                    ASCII = ASCII + i;
                    if (ASCII == 91) {
                        d = 'o';
                    } else if (ASCII == 92) {
                        d = 'p';
                    } else if (ASCII == 93) {
                        d = 'q';
                    } else if (ASCII == 94) {
                        d = 'r';
                    } else if (ASCII == 95) {
                        d = 's';
                    } else if (ASCII == 96) {
                        d = 't';
                    } else {
                        d = (char) ASCII;
                    }
                    Id.append(d);
                } else {
                    Id.append(d);
                }
            }


            //  Toast.makeText(this, Id, Toast.LENGTH_SHORT).show();
            Log.w("newid",String.valueOf(Id));
            Log.w("newlastraw",raw);
            return String.valueOf(Id);
        }
    }


    void shareWhatsapp(String text)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Service OTP");
        intent.putExtra(Intent.EXTRA_TEXT,  text+" \nThis is Service OTP" );
        startActivity(Intent.createChooser(intent,  text+" \nThis is Service OTP \n"));
    }

    void shareWhatsappprint(String text)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Service OTP");
        intent.putExtra(Intent.EXTRA_TEXT,  text+" \nThis is Print Count OTP \n \n FilmSize is "+SheetTpe+"\n \n Recharge Count is "+String.valueOf(printCount));
        startActivity(Intent.createChooser(intent,  text+" \nThis is Print Count OTP"));
        Log.w("sizesekected",SheetTpe);
    }

    void showDialouge()
    {
        // Eh oh dialog va jahera scan and share button de click krn te aanda va
        // Toast.makeText(this, "scan&share", Toast.LENGTH_SHORT).show();

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.print_count_dialouge);
        dialog.setTitle("Print Count OTP");
        dialog.setCancelable(false);
        cardViewButton = dialog.findViewById(R.id.generate);
        cardViewButton1 = dialog.findViewById(R.id.cancel);
        editText = dialog.findViewById(R.id.editText3);
        textViewName = dialog.findViewById(R.id.textName);
        editText.setText(String.valueOf(RequestedRechargeCount).trim());

        textViewDealerName = dialog.findViewById(R.id.textDealerName);
        txtBalanceCount = dialog.findViewById(R.id.balancecount);   // this one

        textViewZone = dialog.findViewById(R.id.textVersion);
        txtQrCode = dialog.findViewById(R.id.txtarcode);
        textViewVmsKey = dialog.findViewById(R.id.vmskey);
        textViewMcid = dialog.findViewById(R.id.mcid);
        textViewName.setText(name);
        txtQrCode.setText(qrcode);
        txtBalanceCount.setText(String.valueOf(BalanceCount));   // value set

        textViewZone.setText(zone);
        textViewVmsKey.setText(vmskey);
        textViewMcid.setText(mcid);
        textViewDealerName.setText(DealerName);
        //editText.setText(OTPCODE);
        spinner = dialog.findViewById(R.id.typespinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Log.w("rcc",RequestedFilmSize);

        if(RequestedFilmSize.trim().equals("8X10"))
        {

            spinner.setSelection(1);
        }

        if(RequestedFilmSize.trim().equals("14X17"))
        {

            spinner.setSelection(2);
        }

        if(RequestedFilmSize.trim().equals("PDF"))
        {

            spinner.setSelection(3);
        }

        cardViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a = editText.getText().toString();
                if (a.isEmpty()) {
                    editText.setError("Enter the Value of Print Count");
                    editText.requestFocus();
                    return;
                }

                if(textViewName.getText().toString().isEmpty())
                {
                    textViewName.setError("Enter the name");
                    textViewName.requestFocus();
                    return;
                }

                if(textViewDealerName.getText().toString().isEmpty())
                {
                    textViewDealerName.setError("Enter the Dealer name");
                    textViewDealerName.requestFocus();
                    return;
                }

                if (a.startsWith(String.valueOf(0))) {
                    editText.setError("Enter Correct Value");
                    editText.requestFocus();
                    return;

                }

                if (spinner.getSelectedItem().toString().trim().equals("Select Filmsize....")) {
                    View sv = spinner.getSelectedView();
                    TextView tv = (TextView)sv;
                    tv.setError("Please select sheet");
                    tv.setTextColor(Color.RED);
                    spinner.requestFocus();
                    return;
                }


                if (ConnectionCheck.isConnected(connectivityManager, networkInfo, MainActivity.this)) {
                    if (isVarified()) {
                        printCount = Integer.parseInt(editText.getText().toString());
                        if (count > printCount || count == printCount) {
                            count = printCount;
                            sex = textViewName.getText().toString();
                            Log.w("sex",sex);
                            sexd = textViewDealerName.getText().toString();
                            Log.w("sexd",sexd);
                            // uploadDetails();
                            if(OtpUserTableDealerId.equals(QrCodeDealerId)){

                                CheckPaperCount();


                            }

                            else {
                                Toast.makeText(MainActivity.this, "DealerId not matching", Toast.LENGTH_LONG).show();

                            }



                        } else {
                            Toast.makeText(MainActivity.this, "Requested count is more than available count", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }
            }
        });
        cardViewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                SheetTpe = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                //  Toast.makeText(MainActivity.this, ""+SheetTpe, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dialog.show();

    }

    boolean isVarified()
    {
        boolean isValidate = true;
        String i = editText.getText().toString();


        try {

            int printint = Integer.parseInt(i);
            if (printint == 00) {
                editText.setError("Enter correct value");
                editText.requestFocus();
                isValidate = false;
            }


        } catch (Exception e) {
            Toast.makeText(this, "486 toast"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }


        if (Integer.parseInt(i) % 50 != 0) {
            isValidate = false;
            editText.setError("Not Multiple of 50");
        }
        if (Integer.parseInt(i) > 10000) {
            isValidate = false;
            editText.setError("Greater Than 10000");
        }


        return isValidate;
    }

    boolean isgVarified()
    {
        boolean isgValidate = true;
        String i = geditText.getText().toString();


        try {

            int printint = Integer.parseInt(i);
            if (printint == 00) {
                geditText.setError("Enter correct value");
                geditText.requestFocus();
                isgValidate = false;
            }


        } catch (Exception e) {
            Toast.makeText(this, "520 toast"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }


        if (Integer.parseInt(i) % 50 != 0) {
            isgValidate = false;
            geditText.setError("Not Multiple of 50");
        }
        if (Integer.parseInt(i) > 10000) {
            isgValidate = false;
            geditText.setError("Greater Than 10000");
        }


        return isgValidate;
    }


    // compiled otp, print count otp da logic va
    public String compileOTP(String raw)
    {
        // Etho step 1 suru hunda va PrintCount da
        raw = raw + " ";
        Log.w("ff",raw);

        // Print Count divide / 50 wala logic and sheet type check wala logic same for all starts from here
        int divide = printCount / 50;
        int finalfirstgc = 0;
        int FinalFirstGroup = Integer.parseInt(raw.substring(1,4));


        Log.w("1ndraw",""+FinalFirstGroup);
        //   int FinalSecondGroup = Integer.parseInt(raw.substring(4,8));
        String FinalSecondGroup = raw.substring(5,8);
        Log.w("2ndraw",""+FinalSecondGroup);
        int FinalThirdGroup = Integer.parseInt(raw.substring(9,12));
        Log.w("3ndraw",""+FinalThirdGroup);



        if(SheetTpe.equals("8X10"))
        {


            FinalFirstGroup +=  divide;
            Log.w("fgrp",""+FinalFirstGroup);

        }


        else if(SheetTpe.equals("14X17"))
        {

            int Scndfnl = Integer.parseInt(FinalSecondGroup);
            //   Scndfnl = Scndfnl + divide;
            // Log.w("2ndsgrp",""+Scndfnl);
            FinalSecondGroup  = String.valueOf(Scndfnl + divide);
            Log.w("2ndplus",FinalSecondGroup);



            if(Integer.parseInt(FinalSecondGroup)<100)
            {

                FinalSecondGroup = ""+0+ FinalSecondGroup;

                Log.w("ifscnd",FinalSecondGroup);


            }


        }

        else if(SheetTpe.equals("PDF"))
        {

            FinalThirdGroup += divide;
            Log.w("tgrp",""+FinalThirdGroup);

        }

        // Print Count divide / 50 wala logic and sheet type check wala logic same for all ends here

        // 1st Digit of First Group

        char firstgc = raw.charAt(0);
        Log.w("firstchar"," "+firstgc);
        String DoneFirstGroup;



        if(firstgc == 'Z')
        {
            finalfirstgc = 8;

        }

        else if(firstgc == 'W')
        {
            finalfirstgc = 3;
        }

        else if(firstgc == 'R')
        {
            finalfirstgc = 9;
        }

        else if(firstgc == 'N')
        {
            finalfirstgc = 2;
        }

        // 1st Digit of First Group ends here


        if(FinalFirstGroup<10)
        {
            DoneFirstGroup = finalfirstgc + ""+0+""+0+ + FinalFirstGroup;
            Log.w("ifbracketif",DoneFirstGroup);

        }


        else if(FinalFirstGroup<100)
        {
            DoneFirstGroup = finalfirstgc + ""+0+ + FinalFirstGroup;
            Log.w("elseifbracketif",DoneFirstGroup);

        }

        else {

            DoneFirstGroup = finalfirstgc + "" + FinalFirstGroup;
            Log.w("onlyelse1fcuk",DoneFirstGroup);
        }



        Log.w("1rd",""+finalfirstgc);

        // 2nd Digit of First Group

        char cam = DoneFirstGroup.charAt(1);
        int ddd = Integer.parseInt(String.valueOf(cam));
        int resltsg = ddd +2;
        Log.w("chard",""+cam);

        if(resltsg>9)
        {

            resltsg = resltsg - 10;

        }

        Log.w("2rd",""+resltsg);


        // 2nd Digit of First Group ends here

        // 3rd Digit of First Group
        char cam3 = DoneFirstGroup.charAt(2);
        int ddd3 = Integer.parseInt(String.valueOf(cam3));
        int resltsg3 = ddd3 +3       ;
        Log.w("chard",""+cam3);

        if(resltsg3>9)
        {

            resltsg3 = resltsg3 - 10;



        }

        Log.w("3rd",""+resltsg3);

        // 3rd Digit of First Group ends here

        // 4th digit of First Group
        char cam4 = DoneFirstGroup.charAt(3);
        int ddd4 = Integer.parseInt(String.valueOf(cam4));
        int resltsg4 = ddd4 +4       ;
        Log.w("1st4chard",""+cam4);

        if(resltsg4>9)
        {

            resltsg4 = resltsg4 - 10;


        }
        Log.w("4rd",""+resltsg4);

        // 4th digit of First Group ends here

        String DoneFinalFirstGroup = finalfirstgc + "" + resltsg + "" + resltsg3 + "" + resltsg4;
        Log.w("1stfinalnigga",DoneFinalFirstGroup);
        // First Group all 4 Digits Logic ends here



        //******************* 2nd Group logic starts from here *********************



        char scam = FinalSecondGroup.charAt(0);
        int sddd = Integer.parseInt(String.valueOf(scam));
        int sresltsg = sddd +6;
        Log.w("s2chard",""+scam);

        if(sresltsg>9)
        {

            sresltsg = sresltsg - 10;

        }

        Log.w("s22rd",""+sresltsg);


        // 2nd Digit of Scond Group ends here


        // 3rd Digit of Second Group
        char scam3 = FinalSecondGroup.charAt(1);
        int sddd3 = Integer.parseInt(String.valueOf(scam3));
        int sresltsg3 = sddd3 +7;
        Log.w("2da2chard",""+scam3);

        if(sresltsg3>9)
        {

            sresltsg3 = sresltsg3 - 10;



        }

        Log.w("2da2s3rd",""+sresltsg3);

        // 3rd Digit of Second Group ends here

        // 4th digit of Second Group
        char scam4 = FinalSecondGroup.charAt(2);
        int sddd4 = Integer.parseInt(String.valueOf(scam4));
        int sresltsg4 = sddd4 +8       ;
        Log.w("2da3schard",""+scam4);

        if(sresltsg4>9)
        {

            sresltsg4 = sresltsg4 - 10;


        }
        Log.w("2da3s4rd",""+sresltsg4);

        // 4th digit of Second Group ends here

        String DoneFinalSecondGroup = raw.charAt(4) + "" + sresltsg + "" + sresltsg3 + "" + sresltsg4;
        Log.w("2dafinalnigga",DoneFinalSecondGroup);
        // Second Group all 4 Digits Logic ends here


        //******************************************************************//

        // 3rd Group starts from here

        // 1st Digit of Third Group
        int finalthirdgc = 0;
        //  FinalThirdGroup = Integer.parseInt(raw.substring(9,12));

        char thirdgc = raw.charAt(8);
        Log.w("3rdfirstchar"," "+thirdgc);



        if(thirdgc == 'A')
        {
            finalthirdgc = 4;

        }

        else if(thirdgc == 'D')
        {
            finalthirdgc = 5;
        }

        else if(thirdgc == 'H')
        {
            finalthirdgc = 6;
        }

        else if(thirdgc == 'M')
        {
            finalthirdgc = 7;
        }


        // 1st Digit of 3rd Group ends here



        String DoneThirdGroup;


        if(FinalThirdGroup<10)
        {
            DoneThirdGroup = finalthirdgc + ""+0+""+0+ + FinalThirdGroup;
            Log.w("if1trdfcuk",DoneThirdGroup);

        }

        else if(FinalThirdGroup<100)
        {

            DoneThirdGroup = finalthirdgc + ""+0+ + FinalThirdGroup;
            Log.w("if3fcuk",DoneThirdGroup);

        }

        else {

            DoneThirdGroup = finalthirdgc + "" + FinalThirdGroup;
            Log.w("else3fcuk",DoneThirdGroup);
        }




        Log.w("1rd",""+finalthirdgc);

        // 2nd Digit of 3rd Group

        char tcam = DoneThirdGroup.charAt(1);
        int tresltsg = Integer.parseInt(String.valueOf(tcam));
        Log.w("3rd2chard",""+tcam);

        if(tresltsg>9)
        {

            tresltsg = tresltsg - 10;

        }

        Log.w("2rd",""+tresltsg);


        // 2nd Digit of 3rd Group ends here

        // 3rd Digit of Third Group
        char tcam3 = DoneThirdGroup.charAt(2);
        int tddd3 = Integer.parseInt(String.valueOf(tcam3));
        int tresltsg3 = tddd3 +1       ;
        Log.w("3rd3chard",""+tcam3);

        if(tresltsg3>9)
        {

            tresltsg3 = tresltsg3 - 10;



        }

        Log.w("3rd",""+tresltsg3);

        // 3rd Digit of Third Group ends here

        // 4th digit of Third Group
        char tcam4 = DoneThirdGroup.charAt(3);
        int tddd4 = Integer.parseInt(String.valueOf(tcam4));
        int tresltsg4 = tddd4 +2;
        Log.w("3rd4chard",""+tcam4);

        if(tresltsg4>9)
        {

            tresltsg4 = tresltsg4 - 10;


        }
        Log.w("4rd",""+tresltsg4);

        // 4th digit of Third Group ends here

        String DoneFinalThirdGroup = finalthirdgc + "" + tresltsg + "" + tresltsg3 + "" + tresltsg4;
        Log.w("nigga",DoneFinalThirdGroup);

        // 3rd Group all 4 Digits Logic ends here


        return  DoneFinalFirstGroup +" "+DoneFinalSecondGroup+" "+DoneFinalThirdGroup;

    }



    void CheckPaperCount()
    {


        StringRequest request=new StringRequest(Request.Method.POST, Util.getpapercount, response -> {

            Log.w("asiis",""+response);

            try {
                JSONObject object=new JSONObject(response);
                JSONArray array=object.getJSONArray("students");
                String message=object.getString("message");
                if (message.contains("Login Sucessful")){


                    for (int i=0;i<array.length();i++){
                        JSONObject object1=array.getJSONObject(i);

                        PaperCount = object1.getInt("Papercount");



                    }



                    updatedPaperCount = PaperCount - printCount;

                    if(PaperCount >=printCount)
                    {
                        CheckExistingRequestId();
                        // uploadDetails();

                    }

                    else if(PaperCount < printCount)
                    {



                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage("Remaining Dealer Paper Count is "+PaperCount+".\nDo You Still Want To Continue ?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                (dialog, id) -> {
                                    //  uploadDetails();
                                    CheckExistingRequestId();
                                });

                        builder1.setNegativeButton(
                                "No",
                                (dialog, id) -> dialog.cancel());

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    }

                }

                else if(message.contains("Login Failure")){
                    noDataDoalog();

                    //    uploadDetails();


                }

                else{

                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // ethe dialog lagu agar koi data nhi milea

                // Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                noDataDoalog();
                // uploadDetails();

                //  Toast.makeText(MainActivity.this,"Login Fail "+e.getMessage(), Toast.LENGTH_LONG).show();
                //e.printStackTrace();
            }
        }, error -> Toast.makeText(MainActivity.this,""+error.getMessage(), Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("DealerID", QrCodeDealerId);

                map.put("paperSize",SheetTpe);
                Log.w("ddf1",SheetTpe);
                return map;
            }
        };
        requestQueue.add(request);
    }



    void CheckExistingRequestId()

    {
        hi = compileOTP(OTPCODE);

        StringRequest request=new StringRequest(Request.Method.POST, Util.CheckExistingRequestId, response -> {

            try {
                JSONObject object=new JSONObject(response);
                String message=object.getString("message");
                Log.w("msgehaareha",message);
                if (message.contains("Login Sucessful"))
                {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Recharge Already Done!!!.\nDo You Want To Recharge Again ?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            (dialog, id) -> shareWhatsappprint(hi));

                    builder1.setNegativeButton(
                            "No",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    //  Intent intent = getIntent();
                    // finish();
                    // startActivity(intent);
                    // if requestid already existed for the same date
                    // Toast.makeText(MainActivity.this, "Recharge Already Done", Toast.LENGTH_LONG).show();

                }
                else {
                    GetHardwareId();
                    //uploadDetails();

                }
            } catch (JSONException e) {
                //pb.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(MainActivity.this,"" +error.getMessage(), Toast.LENGTH_LONG).show();
            //  pb.setVisibility(View.GONE);
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map=new HashMap<>();
                map.put("RequestId", qrcode);
                Log.w("sendingid",qrcode);
                map.put("Date", ctd);
                Log.w("sendingdate",ctd);
                return map;
            }
        };
        requestQueue.add(request);

    }

    void GetHardwareId()
    {


        StringRequest request=new StringRequest(Request.Method.POST, Util.GetHardwareId, response -> {
            Log.w("GetHardwareIdResponse",""+response);

            try {
                JSONObject object=new JSONObject(response);
                String message=object.getString("message");
                Log.w("msgehaareha",message);
                if (message.contains("Login Sucessful"))
                {

                    uploadDetails();

                }
                else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("You are trying to recharge with different MAC id.\nDo You Still Want To Continue ?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            (dialog, id) -> uploadDetails());

                    builder1.setNegativeButton(
                            "No",
                            (dialog, id) -> dialog.cancel());

                    AlertDialog alert11 = builder1.create();
                    alert11.show();



                }
            } catch (JSONException e) {
                //pb.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(MainActivity.this,"" +error.getMessage(), Toast.LENGTH_LONG).show();
            //  pb.setVisibility(View.GONE);
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("HardwareId", mcid);
                map.put("dealerid", QrCodeDealerId);

                return map;
            }
        };
        requestQueue.add(request);

    }

    void uploadDetails() {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.updateAndInsert, response -> {
            try {
                JSONObject object = new JSONObject(response);
                String message = object.getString("message");
                if (message.contains("Sucessfully")) {
                    pd.dismiss();
                    shareWhatsappprint(hi);
                    if(pdialog !=null)
                    {
                        pdialog.dismiss();

                    }
                    if(dialog!=null)
                    {
                        dialog.dismiss();

                    }



                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, "852 toast", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "857"+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                pd.dismiss();
            }
        }, error -> pd.dismiss()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if (!sex.equals(" ")) {


                    map.put("Client", sex);
                    Log.w("nonemptyclient", sex);


                    if (! sexd.equals(" "))
                    {
                        map.put("Dealer", sexd);
                        Log.w("nonemptydealer", sexd);
                    }
                }
                if(sex.equals(" "))
                {
                    map.put("Client", name);
                    Log.w("clientempty",name);



                }

                if(sexd.equals(" "))
                {
                    map.put("Dealer",DealerName);
                    Log.w("Dealerempty",DealerName);
                }

                map.put("Zone", zone);
                Log.w("Zone",zone);
                map.put("vmskey",vmskey);
                map.put("mcid",mcid);
                map.put("Doneby", userName);
                map.put("RequestId",qrcode);
                Log.w("doneby",userName);

                map.put("Printcnt", String.valueOf(printCount));
                Log.w("Prinycountval",String.valueOf(printCount));
                map.put("User_ID", String.valueOf(id));
                Log.w("Userid",String.valueOf(id));
                map.put("Count", String.valueOf(count));
                Log.w("Countdival",String.valueOf(count));

                map.put("Filmsize",SheetTpe);
                map.put("updatedpapercount", ""+updatedPaperCount);
                Log.w("updatedpapercount",""+updatedPaperCount);
                map.put("DealerID", QrCodeDealerId);
                Log.w("",QrCodeDealerId);
                map.put("LastBalance", ""+BalanceCount);
                //Log.w("this is balance",""+BalanceCount);

                return map;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        RemainingCount.setText(String.valueOf("Remaining Print Counts " + count));
        nameser.setText("Welcome ");
        wnames.setText(wname);
        codeScanner.startPreview();

    }



    public void generate(View view)
    {
        //  String serviceOTP = scanTxt(ServiceEdit.getText().toString());
        //  ServiceEdit.setText("");
        // shareWhatsapp(serviceOTP);
    }

    public void GeneratePrint(View view)
    {


        ShowDialog();


    }


    void noDataDoalog()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("No Record Found.\nDo You Still Want To Continue ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {


                    //uploadDetails();
                    CheckExistingRequestId();
                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    void ShowDialog()
    {  // Eh Dialog oh va jahera button de click krn te aanda va

        //  Toast.makeText(this, "scan&share", Toast.LENGTH_SHORT).show();

        pdialog = new Dialog(this);
        pdialog.setContentView(R.layout.generate_print_dialog);
        pdialog.setTitle("Print Count OTP");
        pdialog.setCancelable(false);
        PrintID = pdialog.findViewById(R.id.printid);
        gcardViewButton = pdialog.findViewById(R.id.ggenerate);
        gcardViewButton1 = pdialog.findViewById(R.id.gcancel);
        geditText = pdialog.findViewById(R.id.geditText3);
        gtextViewName = pdialog.findViewById(R.id.gtextName);
        gtextViewDealerName = pdialog.findViewById(R.id.gtextDName);
        gtextViewZone = pdialog.findViewById(R.id.gtextVersion);
        spinner = pdialog.findViewById(R.id.typespinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        gcardViewButton.setOnClickListener(v -> {
            String a = geditText.getText().toString();
            String b = PrintID.getText().toString();
            if(Character.isDigit(b.charAt(0)))
            {
                PrintID.setError("Print Id should be in the form of A123 1234 B123");
                PrintID.requestFocus();
                return;
            }

            if(Character.isDigit(b.charAt(8)))
            {
                PrintID.setError("Print Id should be in the form of A123 1234 B123");
                PrintID.requestFocus();
                return;
            }

            if(gtextViewName.getText().toString().isEmpty())
            {
                gtextViewName.setError("Enter the name");
                gtextViewName.requestFocus();
                return;
            }

            if(gtextViewDealerName.getText().toString().isEmpty())
            {

                gtextViewDealerName.setError("Enter the name");
                gtextViewDealerName.requestFocus();
                return;
            }
            if(b.length()<12)
            {
                PrintID.setError("Incorrect value");
                PrintID.requestFocus();
                return;

            }
            if (b.isEmpty()) {
                PrintID.setError("Print Id is require");
                PrintID.requestFocus();
                return;
            }
            if (a.isEmpty()) {
                geditText.setError("Enter the Value of Print Count");
                geditText.requestFocus();
                return;
            }

            if (a.startsWith(String.valueOf(0))) {
                geditText.setError("Enter Correct Value");
                geditText.requestFocus();
                return;

            }



            if (spinner.getSelectedItem().toString().trim().equals("Select Film size....")) {
                View sv = spinner.getSelectedView();
                TextView tv = (TextView)sv;
                tv.setError("Please select sheet");
                tv.setTextColor(Color.RED);
                spinner.requestFocus();
                return;
            }

            if (ConnectionCheck.isConnected(connectivityManager, networkInfo, MainActivity.this)) {
                if (isgVarified()) {
                    name = gtextViewName.getText().toString().trim();
                    Log.w("Name", gtextViewName.getText().toString().trim());
                    OTPCODE = PrintID.getText().toString();
                    zone = gtextViewZone.getText().toString();
                    DealerName = gtextViewDealerName.getText().toString();
                    Log.w("DealerName", gtextViewDealerName.getText().toString());
                    printCount = Integer.parseInt(geditText.getText().toString());
                    if (count > printCount || count == printCount) {
                        count = count - printCount;
                        //  uploadDetails();
                        CheckPaperCount();
                    } else {
                        Toast.makeText(MainActivity.this, "Requested count is more than available count", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Internet Connection Required", Toast.LENGTH_LONG).show();
                }
            }

        });
        gcardViewButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                SheetTpe = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                //  Toast.makeText(MainActivity.this, ""+SheetTpe, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pdialog.show();

    }

    public void ChoosePhoto(View view)
    {

        checkPemission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);




    }

    @Override
    public void onPause()
    {
        codeScanner.releaseResources();
        super.onPause();
    }

    // Here i am using the duplicate code again, so later i will create one method to to generate print count
    void runCodeScanner()
    {


        // codeScanner.setDecodeCallback(true);
        // codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        //codeScanner.setScanMode(ScanMode.CONTINUOUS);

        codeScanner.setDecodeCallback(result -> runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String dats = result.getText();
                //scanTxt scanTxt(dats);
                codeScannerView.setVisibility(View.GONE);
//codeScanner.releaseResources();
                codeScanner.stopPreview();
                String serviceOTP = scanTxt(dats);
                if (Character.isAlphabetic(serviceOTP.charAt(0)) & Character.isDigit(serviceOTP.charAt(1)) & Character.isDigit(serviceOTP.charAt(2)) & Character.isDigit(serviceOTP.charAt(3))& Character.isDigit(serviceOTP.charAt(4))& Character.isDigit(serviceOTP.charAt(5))& Character.isDigit(serviceOTP.charAt(6))& Character.isDigit(serviceOTP.charAt(7))& Character.isAlphabetic(serviceOTP.charAt(8))& Character.isDigit(serviceOTP.charAt(9))& Character.isDigit(serviceOTP.charAt(10))& Character.isDigit(serviceOTP.charAt(11))) {
                    try {

                        String[] otp = serviceOTP.split(";");
                        OTPCODE = otp[0];

                        qrcode = OTPCODE;



                        if(otp.length>1)
                        {
                            name = otp[1];

                        }

                        if(otp.length>2)
                        {

                            DealerName = otp[2];
                        }



                        if(otp.length>3)
                        {
                            zone = otp[3];
                        }

                        if(otp.length>4)
                        {

                            vmskey = otp[4];
                        }

                        if(otp.length>5)
                        {

                            mcid = otp[5];
                        }

                        if(otp.length>6)
                        {
                            QrCodeDealerId = otp[6];
                        }
                        if(otp.length>7)
                        {
                            BalanceCount = Integer.parseInt(otp[7]);

                        }

                        if(otp.length>8)
                        {
                            RequestedFilmSize = otp[8];

                        }

                        if(otp.length>9)
                        {

                            RequestedRechargeCount = Integer.parseInt(otp[9]);
                        }




                        showDialouge();

                    }

                    catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    // comment this for service otp
                    Toast.makeText(MainActivity.this, "Qr not supported", Toast.LENGTH_LONG).show();
                    // shareWhatsapp(serviceOTP);
                }

            }
        }));

        codeScannerView.setOnClickListener(view -> codeScanner.startPreview());
    }

    public void checkPemission(String permission, int requestCode)
    {

        if(ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED)
        {
// Now take permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission},requestCode);

        }

        else {
            codeScannerView.setVisibility(View.VISIBLE);
            runCodeScanner();
            codeScanner.startPreview();
// this else is for permission already granted

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==CAMERA_PERMISSION_CODE)
        {

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                // permission granted
                codeScannerView.setVisibility(View.VISIBLE);
                runCodeScanner();
                codeScanner.startPreview();

            }

            else {

                // permission denied
            }
        }


    }

    public void ChoosePhotoSecondCam(View view) {
        runCodeScanner2();

    }

    void runCodeScanner2()
    {


        cameraPreview = (findViewById(R.id.camera_view));
        cresult = (findViewById(R.id.cresult));

        cameraPreview.setVisibility(View.VISIBLE);

        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, detector)
                .setRequestedPreviewSize(640, 480)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //   ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},RequestCameraPermissionId);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {



            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {


                cameraSource.stop();



            }
        });
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {


            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {


                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                if(qrcodes.size()!=0)
                {
                    // detector.release();



                    cresult.post(() -> {




                        // create vibrate when detected qr code
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(100);


                        String dats=qrcodes.valueAt(0).displayValue;

                        cameraPreview.setVisibility(View.GONE);
                        // cameraSource.release();


                        String serviceOTP = scanTxt(dats);

                        if (Character.isAlphabetic(serviceOTP.charAt(0)) & Character.isDigit(serviceOTP.charAt(1)) & Character.isDigit(serviceOTP.charAt(2)) & Character.isDigit(serviceOTP.charAt(3))& Character.isDigit(serviceOTP.charAt(4))& Character.isDigit(serviceOTP.charAt(5))& Character.isDigit(serviceOTP.charAt(6))& Character.isDigit(serviceOTP.charAt(7))& Character.isAlphabetic(serviceOTP.charAt(8))& Character.isDigit(serviceOTP.charAt(9))& Character.isDigit(serviceOTP.charAt(10))& Character.isDigit(serviceOTP.charAt(11))) {
                            try {

                                String[] otp = serviceOTP.split(";");
                                OTPCODE = otp[0];

                                qrcode = OTPCODE;



                                if(otp.length>1)
                                {
                                    name = otp[1];

                                }

                                if(otp.length>2)
                                {

                                    DealerName = otp[2];
                                }



                                if(otp.length>3)
                                {
                                    zone = otp[3];
                                }

                                if(otp.length>4)
                                {

                                    vmskey = otp[4];
                                }

                                if(otp.length>5)
                                {

                                    mcid = otp[5];
                                }

                                if(otp.length>6)
                                {
                                    QrCodeDealerId = otp[6];
                                }
                                if(otp.length>7)
                                {
                                    BalanceCount = Integer.parseInt(otp[7]);

                                }

                                if(otp.length>8)
                                {
                                    RequestedFilmSize = otp[8];

                                }

                                if(otp.length>9)
                                {

                                    RequestedRechargeCount = Integer.parseInt(otp[9]);
                                }




                                showDialouge();

                            }

                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            // comment this for service otp
                            Toast.makeText(MainActivity.this, "Qr not supported", Toast.LENGTH_LONG).show();
                          //   shareWhatsapp(serviceOTP);
                           //    finish();
                          //  startActivity(getIntent());
                            //  shareWhatsapp(serviceOTP);


                        }



                    });

                    //  codeScannerView.setOnClickListener(view -> codeScanner.startPreview());

                }
            }
        });

    }
}