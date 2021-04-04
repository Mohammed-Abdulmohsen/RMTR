package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class AdminAccountActivity extends AppCompatActivity {

    EditText editText_name,editText_password,editText_password2,editText_email,editText_mobile;
    Button button_update;
    ImageView imageView_close,imageView_add_station,imageView_add_train;
    Intent intent;
    String old;
    private final String filePath="http://psauprojects.com/ksatrain/update_accounts.php";
    private final String filePath2="http://psauprojects.com/ksatrain/get_account.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);

        editText_name = (EditText) findViewById(R.id.editText_admin_name);
        editText_password = (EditText) findViewById(R.id.editText_admin_password);
        editText_password2 = (EditText) findViewById(R.id.editText_admin_password2);
        editText_email = (EditText) findViewById(R.id.editText_admin_email);
        editText_mobile = (EditText) findViewById(R.id.editText_admin_mobile);
        button_update = (Button) findViewById(R.id.button_admin_update);

        intent = getIntent();
        if (intent.hasExtra("mobile") && intent.getExtras().getString("mobile").equals("12345")){
            Toast.makeText(this, R.string.change_default,Toast.LENGTH_LONG).show();
            editText_name.setText("Administrator");
            editText_password.setText("12345");
            editText_password2.setText("12345");
            editText_mobile.setText("12345");
            old="12345";
        }else{
            old = intent.getExtras().getString("mobile");

            GetAccount getAccount = new GetAccount(AdminAccountActivity.this);
            getAccount.execute(filePath2,old);
        }

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();
                String pass = editText_password.getText().toString();
                String pass2 = editText_password2.getText().toString();
                String mobi = editText_mobile.getText().toString();
                String email = editText_email.getText().toString();

                if (name.isEmpty() || pass.isEmpty() || pass2.isEmpty() || mobi.isEmpty()){
                    Toast.makeText(AdminAccountActivity.this, R.string.signup_hint1,Toast.LENGTH_LONG).show();
                    return;
                }

                if (!pass.equals(pass2)){
                    Toast.makeText(AdminAccountActivity.this, R.string.signup_hint2,Toast.LENGTH_LONG).show();
                    return;
                }

                if(pass.equals("12345") || mobi.equals("12345")){
                    Toast.makeText(AdminAccountActivity.this,R.string.change_default,Toast.LENGTH_LONG).show();
                    return;
                }

                Update update = new Update(AdminAccountActivity.this);
                update.execute(filePath,name,pass,email,mobi,old);
            }
        });

        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAccountActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageView_add_station = (ImageView) findViewById(R.id.imageView_add_station);
        imageView_add_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAccountActivity.this,AdminAddStationActivity.class);
                intent.putExtra("mobile",old);
                startActivity(intent);
                finish();
            }
        });

        imageView_add_train =(ImageView) findViewById(R.id.imageView_add_train);
        imageView_add_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AdminAccountActivity.this,AdminAddTrainActivity.class);
                intent.putExtra("mobile",old);
                startActivity(intent);
                finish();
            }
        });
    }

    private class Update extends AsyncTask<String,Void,String> {
        Context context;
        ProgressDialog progressDialog;
        URL url = null;
        HttpURLConnection connection;

        public Update(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(context,R.style.MyProgessDailog);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(getString(R.string.update_hint2));
            progressDialog.setCancelable(false);
            progressDialog.setTitle(getString(R.string.information));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                url = new URL(params[0]);
                String name = params[1];
                String pass = params[2];
                String email = params[3];
                String mobile = params[4];


                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&";
                post_data += URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8")+"&";
                post_data += URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&";
                post_data += URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8") + "&";
                post_data += URLEncoder.encode("ut","UTF-8")+"="+URLEncoder.encode("2","UTF-8") + "&";
                post_data += URLEncoder.encode("old","UTF-8")+"="+URLEncoder.encode(old,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream =connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result ="";
                String line ="";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result){
            progressDialog.dismiss();
            Toast.makeText(context, R.string.update_hint,Toast.LENGTH_LONG).show();
        }
    }

    private class GetAccount extends AsyncTask<String,Void,String> {
        Context context;
        ProgressDialog progressDialog;
        URL url = null;
        HttpURLConnection connection;

        public GetAccount(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(context,R.style.MyProgessDailog);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("get your account information");
            progressDialog.setTitle(getString(R.string.information));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                url = new URL(params[0]);
                String mobile_string = params[1];

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data= URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(mobile_string,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream =connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result ="";
                String line ="";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result){
            progressDialog.dismiss();
            String[] info=result.split(",");

            editText_name.setText(info[0]);
            editText_password.setText(info[1]);
            editText_password2.setText(info[1]);
            editText_mobile.setText(info[2]);
            if (info.length == 4){editText_email.setText(info[3]);}
        }
    }
}
