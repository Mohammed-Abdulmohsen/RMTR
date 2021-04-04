package com.psauprojects.riyadhmetroticket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class SignupActivity extends AppCompatActivity {
EditText editText_name,editText_password,editText_password2,editText_email,editText_mobile;
Button button_signup;

    private final String filePath="http://psauprojects.com/ksatrain/signup.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_password= (EditText) findViewById(R.id.editText_password);
        editText_password2 = (EditText) findViewById(R.id.editText_password2);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_mobile = (EditText) findViewById(R.id.editText_mobile);
        button_signup = (Button) findViewById(R.id.button_signup);

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();
                String pass = editText_password.getText().toString();
                String pass2 = editText_password2.getText().toString();
                String mobi = editText_mobile.getText().toString();
                String email = editText_email.getText().toString();

                if (name.isEmpty() || pass.isEmpty() || pass2.isEmpty() || mobi.isEmpty()){
                    Toast.makeText(SignupActivity.this, R.string.signup_hint1,Toast.LENGTH_LONG).show();
                    return;
                }

                if (!pass.equals(pass2)){
                    Toast.makeText(SignupActivity.this, R.string.signup_hint2,Toast.LENGTH_LONG).show();
                    return;
                }

                Signup signup = new Signup(SignupActivity.this);
                signup.execute(filePath,name,pass,email,mobi);

            }
        });
    }

    private class Signup extends AsyncTask<String,Void,String> {
        Context context;
        ProgressDialog progressDialog;
        AlertDialog alertDialog;
        URL url = null;
        HttpURLConnection connection;

        public Signup(Context context){
            this.context = context;
            progressDialog = new ProgressDialog(context,R.style.MyProgessDailog);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage(getString(R.string.register_hint));
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
                post_data += URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(mobile,"UTF-8");


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
            Toast.makeText(context, R.string.register_hint2,Toast.LENGTH_LONG).show();
        }
    }
}
