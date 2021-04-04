package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PassReserveTicketActivity extends AppCompatActivity {
    ImageView imageView_back;
    RadioGroup radioGroup_trip;
    RadioButton radioButton_one,radioButton_two,radioButton_day,radioButton_night,radioButton_bus,radioButton_eco;
    LinearLayout linearLayout_to;
    Spinner spinner_from,spinner_to;
    Button button_reserve;
    DatePicker datePicker_dep,datePicker_ret;
    EditText editText_a,editText_c,editText_i;
    Intent intent;
    String mobile;
    Context context;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_reserve_ticket);

        context = this;
        intent = getIntent();
        mobile = intent.getExtras().getString("mobile");


        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context,PassTicketsActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
        spinner_from = (Spinner) findViewById(R.id.spinner_from);
        spinner_to = (Spinner) findViewById(R.id.spinner_to);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.stations));
        spinner_from.setAdapter(stringArrayAdapter);
        spinner_to.setAdapter(stringArrayAdapter);

        linearLayout_to = (LinearLayout) findViewById(R.id.ll_to);
        radioGroup_trip = (RadioGroup) findViewById(R.id.rg__trip_type);


        radioGroup_trip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
              if(checkedId == R.id.radioButton_one){
                  linearLayout_to.setVisibility(View.GONE);
              }else{
                  linearLayout_to.setVisibility(View.VISIBLE);
              }
            }
        });

        radioButton_one = (RadioButton) findViewById(R.id.radioButton_one);
        radioButton_two = (RadioButton) findViewById(R.id.radioButton_two);
        radioButton_day = (RadioButton) findViewById(R.id.radioButton_day);
        radioButton_night = (RadioButton) findViewById(R.id.radioButton_night);
        radioButton_bus = (RadioButton) findViewById(R.id.radioButton_bus);
        radioButton_eco = (RadioButton) findViewById(R.id.radioButton_eco);
        datePicker_dep = (DatePicker) findViewById(R.id.dp_dep);
        datePicker_ret = (DatePicker) findViewById(R.id.dp_return);
        editText_a = (EditText) findViewById(R.id.editText_adu);
        editText_c = (EditText) findViewById(R.id.editText_chi);
        editText_i = (EditText) findViewById(R.id.editText_inf);

        button_reserve = (Button) findViewById(R.id.button_res);
        button_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region validation start
                if(!radioButton_one.isChecked() && !radioButton_two.isChecked()){
                    Toast.makeText(context, "You must choose trip type", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!radioButton_day.isChecked() && !radioButton_night.isChecked()){
                    Toast.makeText(context, "You must choose train type", Toast.LENGTH_LONG).show();
                    return;
                }
                if(spinner_from.getSelectedItemPosition() == 0 || spinner_to.getSelectedItemPosition() == 0){
                    Toast.makeText(context, "You must choose leaving from and going to stations", Toast.LENGTH_LONG).show();
                    return;
                }
                if(spinner_from.getSelectedItemPosition() == spinner_to.getSelectedItemPosition()){
                    Toast.makeText(context, "You cannot choose the same station for leaving and going", Toast.LENGTH_LONG).show();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                String date_dep = datePicker_dep.getDayOfMonth() + "/"+ (datePicker_dep.getMonth()+1)+"/"+datePicker_dep.getYear();
                String date_ret = datePicker_ret.getDayOfMonth() + "/"+ (datePicker_ret.getMonth()+1)+"/"+datePicker_ret.getYear();
                String current = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+"/"+
                        (calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                try{
                    Date d1 = sdf1.parse(date_dep);
                    Date d2 = sdf1.parse(date_ret);
                    Date n = sdf1.parse(current);
                    if (d1.before(n) || d2.before(n)){
                        Toast.makeText(context, "The depart or return date before current date.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(linearLayout_to.getVisibility() == View.VISIBLE){
                        if(d2.before(d1)){
                            Toast.makeText(context, "The return date before the depart date.", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                }catch (Exception e){

                }
                if(!radioButton_bus.isChecked() && !radioButton_eco.isChecked()){
                    Toast.makeText(context, "You must choose class type", Toast.LENGTH_LONG).show();
                    return;
                }
                String a = editText_a.getText().toString();
                String c = editText_c.getText().toString();
                String i = editText_i.getText().toString();
                if(a.equals("0") || Integer.parseInt(a)>10 || Integer.parseInt(c)>10 || Integer.parseInt(i)>10){
                    Toast.makeText(context, "You cannot leave Adult zero or number of passenger greater than 10.", Toast.LENGTH_LONG).show();
                    return;
                }
                //endregion validation

                //region upload data
                String jt ="";//journay type one way or roundtrip
                String tt="";//train type dat or night
                String classtype="";//class
                if(radioButton_one.isChecked()){jt="0";}
                if(radioButton_two.isChecked()){jt="1";}
                if(radioButton_day.isChecked()){tt="0";}
                if(radioButton_night.isChecked()){tt="1";}
                if(radioButton_bus.isChecked()){classtype="Busniess";}
                if(radioButton_eco.isChecked()){classtype="Economy";}
                if(linearLayout_to.getVisibility() == View.GONE){
                    date_ret="";
                }
                AndroidNetworking.upload("http://psauprojects.com/ksatrain/tickets.php ")
                        .addMultipartParameter("function","1")
                        .addMultipartParameter("mobile",mobile)
                        .addMultipartParameter("jt",jt)
                        .addMultipartParameter("tt",tt)
                        .addMultipartParameter("sf",String.valueOf(spinner_from.getSelectedItemPosition()))
                        .addMultipartParameter("st",String.valueOf(spinner_to.getSelectedItemPosition()))
                        .addMultipartParameter("dd",date_dep)
                        .addMultipartParameter("rd",date_ret)
                        .addMultipartParameter("class",classtype)
                        .addMultipartParameter("a",a)
                        .addMultipartParameter("c",c)
                        .addMultipartParameter("i",i)
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {
                                progressDialog = new ProgressDialog(context,R.style.MyProgessDailog);
                                progressDialog.setMessage("Reserve your ticket");
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle(getString(R.string.information));
                                progressDialog.show();

                            }
                        })
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Your ticket reserved. Press back to see your ticket.",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Toast.makeText(context, anError.toString(),Toast.LENGTH_LONG).show();
                            }
                        });

                //endregion
            }
        });
    }
}
