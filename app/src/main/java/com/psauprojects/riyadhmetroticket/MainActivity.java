package com.psauprojects.riyadhmetroticket;

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

public class MainActivity extends AppCompatActivity {
EditText editText_mobile,editText_password;
Button button_signup,button_singin;
Intent intent;

    private final String filePath="http://psauprojects.com/ksatrain/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_mobile = (EditText) findViewById(R.id.editText_mobile);
        editText_password = (EditText) findViewById(R.id.editText_password);
        button_singin =(Button) findViewById(R.id.button_signin);
        button_signup = (Button) findViewById(R.id.button_signup);

        button_singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent = new Intent(MainActivity.this,PassTicketsActivity.class);
                intent.putExtra("mobile","0566682636");//admin: 0500310305 	 passenger:0566682636
                startActivity(intent);
                finish();*/
                if (editText_mobile.getText().toString().isEmpty() || editText_password.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.login_hint1,Toast.LENGTH_LONG).show();
                    return;
                }

                Signin signin = new Signin(MainActivity.this);
                signin.execute(filePath,editText_mobile.getText().toString(),editText_password.getText().toString());
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private class Signin extends AsyncTask<String,Void,String> {
        Context context;
        ProgressDialog progressDialog;
        URL url = null;
        HttpURLConnection connection;

        public Signin(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(context,R.style.MyProgessDailog);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.login));
            progressDialog.setCancelable(false);
            progressDialog.setTitle(getString(R.string.information));
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try{
                url = new URL(params[0]);
                String mobile = params[1];
                String password = params[2];

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data= URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8")+"&";
                post_data += URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

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
            int user_type = Integer.parseInt(result);
            Intent intent = null;

            switch (user_type){
                case -1:
                    Toast.makeText(MainActivity.this, R.string.wrong_login,Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    intent = new Intent(context,AdminAccountActivity.class);
                    intent.putExtra("mobile","12345");
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    intent = new Intent(context,PassMainActivity.class);
                    intent.putExtra("mobile",editText_mobile.getText().toString());
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    intent = new Intent(context,AdminMainActivity.class);
                    intent.putExtra("mobile",editText_mobile.getText().toString());
                    startActivity(intent);
                    finish();
            }
        }

    }
}
