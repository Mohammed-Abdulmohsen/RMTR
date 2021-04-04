package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PassTicketsActivity extends AppCompatActivity {
    RecyclerView recyclerView = null;
    RecyclerViewAdapterTickets recyclerViewAdapterTickets = null;
    ProgressDialog progressDialog;
    List<Ticket> ticketList = new ArrayList<>();
    String stations[];
    ImageView imageView_close,imageView_info,imageView_stations;
    Button button_res;
    Intent intent;
    String mobile;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_tickets);

        intent = getIntent();
        mobile = intent.getExtras().getString("mobile");
        context = this;
        stations = getResources().getStringArray(R.array.stations);

        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView_info = (ImageView) findViewById(R.id.imageView_info);
        imageView_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context,PassAccountActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        imageView_stations = (ImageView) findViewById(R.id.imageView_stations);
        imageView_stations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context,PassViewStationsActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });

        button_res = (Button) findViewById(R.id.button_reservation);
        button_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context,PassReserveTicketActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTickets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTickets();
    }

    protected void getTickets(){
        AndroidNetworking.upload("http://psauprojects.com/ksatrain/tickets.php")
                .addMultipartParameter("function","2")
                .addMultipartParameter("mobile",mobile)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog = new ProgressDialog(context,R.style.MyProgessDailog);
                        progressDialog.setMessage("Getting all your tickets");
                        progressDialog.setCancelable(false);
                        progressDialog.setTitle(getString(R.string.information));
                        progressDialog.show();
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("MYAPP",response);
                        ticketList.clear();

                        try{
                            if (response == null){return;}

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Ticket ticket = new Ticket();
                                ticket.setTicketid(jsonObject.getInt("ticketid"));
                                ticket.setPassid(jsonObject.getInt("passid"));
                                ticket.setClass_type(jsonObject.getString("ticket_type"));
                                ticket.setJournyType((byte)jsonObject.getInt("trip_type"));
                                ticket.setDep_time(jsonObject.getString("dep_time"));
                                ticket.setDep_station(stations[jsonObject.getInt("dep_from")]);
                                ticket.setArr_station(stations[jsonObject.getInt("arr_to")]);
                                ticket.setDep_date(jsonObject.getString("dep_date"));
                                ticket.setRet_date(jsonObject.getString("ret_date"));
                                ticket.setAdult((byte)jsonObject.getInt("adult"));
                                ticket.setChild((byte)jsonObject.getInt("child"));
                                ticket.setInfant((byte)jsonObject.getInt("infant"));
                                if(jsonObject.getString("seats").indexOf(",")>0){
                                    ticket.setSeats(jsonObject.getString("seats").substring(0,jsonObject.getString("seats").length()-1));
                                }else{
                                    ticket.setSeats(jsonObject.getString("seats"));
                                }


                                ticketList.add(ticket);
                            }
                        }catch (JSONException e){
                            Toast.makeText(context, "No tikets found.", Toast.LENGTH_LONG).show();
                        }
                        recyclerViewAdapterTickets = new RecyclerViewAdapterTickets(ticketList, context);
                        recyclerViewAdapterTickets.notifyDataSetChanged();
                        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView_tickets);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(recyclerViewAdapterTickets);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Cannot complete loading tickets",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
