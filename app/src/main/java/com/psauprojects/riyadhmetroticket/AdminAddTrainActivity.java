package com.psauprojects.riyadhmetroticket;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import java.security.acl.Group;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminAddTrainActivity extends AppCompatActivity {
    Intent intent;
    String mobile;
    ImageView imageView_close,imageView_info,imageView_add_station;
    Spinner spinner_from,spinner_to;
    RadioGroup rg_type;
    EditText editText_cars,editText_seats;
    Button button_Add;
    TimePicker timePicker;
    byte train_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_train);

        intent = getIntent();
        if (intent.hasExtra("mobile")){
            mobile = intent.getExtras().getString("mobile");
        }

        rg_type = (RadioGroup) findViewById(R.id.rg_train_type);
        editText_cars = (EditText) findViewById(R.id.editText_cars);
        editText_seats = (EditText) findViewById(R.id.editText_seats);
        button_Add = (Button) findViewById(R.id.button_add);
        timePicker = (TimePicker) findViewById(R.id.TimePicker);
        spinner_from = (Spinner) findViewById(R.id.spinner_from);
        spinner_to = (Spinner) findViewById(R.id.spinner_to);

        String stationsArray[] = getResources().getStringArray(R.array.stations);
        ArrayAdapter<String> stationsArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,stationsArray);
        spinner_from.setAdapter(stationsArrayAdapter);
        spinner_to.setAdapter(stationsArrayAdapter);

        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAddTrainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView_info = (ImageView) findViewById(R.id.imageView_info);
        imageView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAddTrainActivity.this,AdminAccountActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        imageView_add_station = (ImageView) findViewById(R.id.imageView_add_station);
        imageView_add_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAddTrainActivity.this,AdminAddStationActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        button_Add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (rg_type.getCheckedRadioButtonId() == -1){
                    Toast.makeText(AdminAddTrainActivity.this, "You must choose train type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editText_cars.getText().toString().isEmpty() || editText_seats.getText().toString().isEmpty()){
                    Toast.makeText(AdminAddTrainActivity.this, "Enter number of cars and seats on this train", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinner_from.getSelectedItemPosition() == 0 || spinner_to.getSelectedItemPosition() == 0){
                    Toast.makeText(AdminAddTrainActivity.this, "You must choose station for from and to", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = timePicker.getHour() + ":"+timePicker.getMinute();
                switch (rg_type.getCheckedRadioButtonId()){
                    case R.id.day:
                        train_type= 0;
                        break;
                    case R.id.night:
                        train_type = 1;
                }
               final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(AdminAddTrainActivity.this,R.style.MyProgessDailog);
                progressDialog.setMessage("Add train to database");
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Information");
                AndroidNetworking.upload("http://psauprojects.com/ksatrain/add_train.php")
                        .addMultipartParameter("type",String.valueOf(train_type))
                        .addMultipartParameter("cars",editText_cars.getText().toString())
                        .addMultipartParameter("seats",editText_seats.getText().toString())
                        .addMultipartParameter("from",String.valueOf(spinner_from.getSelectedItemPosition()))
                        .addMultipartParameter("to",String.valueOf(spinner_to.getSelectedItemPosition()))
                        .addMultipartParameter("time",time)
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {
                                progressDialog.show();
                            }
                        })
                        .getAsString(new StringRequestListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Toast.makeText(AdminAddTrainActivity.this, "Train added successfully",Toast.LENGTH_LONG).show();
                                rg_type.clearCheck();
                                editText_cars.setText("");
                                editText_seats.setText("");
                                spinner_from.setSelection(0);
                                spinner_to.setSelection(0);
                            }

                            @Override
                            public void onError(ANError anError) {
                                progressDialog.dismiss();
                                Toast.makeText(AdminAddTrainActivity.this, "Train not added.",Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }
}
