package com.example.keshu.myshopingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    Button b1,b2;
    EditText t1,t2;
    String email,password,result,name,mob;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.enableDefaults();

        t1=(EditText)findViewById(R.id.login_et1);//email
        t2=(EditText)findViewById(R.id.login_et2);//password
        b1=(Button)findViewById(R.id.login_b1);//login
        b2=(Button)findViewById(R.id.login_b2);//signup


        //login
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email=t1.getText().toString();
                password=t2.getText().toString();



                ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("email",email));
                list.add(new BasicNameValuePair("password",password));

                try
                {
                    HttpClient httpclient=new DefaultHttpClient();

                    HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/login.php");

                    httppost.setEntity(new UrlEncodedFormEntity(list));

                    HttpResponse response=httpclient.execute(httppost);

                    HttpEntity entity=response.getEntity();
                    InputStream in=entity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in,"iso-8859-1"),8);

                    StringBuilder sb = new StringBuilder();
                    String line=null;
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line+"\n");
                    }
                    result=sb.toString();
                    br.close();

                    if(result!=null) result=result.trim();

                    if(result.equalsIgnoreCase("Failure"))
                    {
                        Toast.makeText(MainActivity.this, "Invalid email or password ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        SharedPreferences sp=getSharedPreferences("mypref",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();

                        if(result.equalsIgnoreCase("admin"))
                        {
                            Toast.makeText(MainActivity.this, "Welcome ADMIN", Toast.LENGTH_SHORT).show();
                            editor.clear();
                            editor.putString("email",email);
                            editor.commit();
                            Intent i1=new Intent(MainActivity.this,admin_profile.class);
                            startActivity(i1);
                        }
                        else if(result.equalsIgnoreCase("seller"))
                        {
                            Toast.makeText(MainActivity.this, "Welcome SELLER", Toast.LENGTH_SHORT).show();
                            editor.clear();
                            editor.putString("email",email);
                            editor.commit();
                            Intent i1=new Intent(MainActivity.this,seller_profile.class);
                            startActivity(i1);

                        }
                        else if(result.equalsIgnoreCase("buyer"))
                        {
                            Toast.makeText(MainActivity.this, "Welcome BUYER", Toast.LENGTH_SHORT).show();
                            editor.clear();
                            editor.putString("email",email);
                            editor.commit();
                            Intent i1=new Intent(MainActivity.this,buyer.class);
                            startActivity(i1);

                        }
                    }

                }
                catch(Exception e)
                {
                    Toast.makeText(MainActivity.this, "Error : "+e, Toast.LENGTH_LONG).show();
                }

            }
        });


        //signup
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LayoutInflater li=LayoutInflater.from(MainActivity.this);
                View v1=li.inflate(R.layout.signup_form,null);
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                adb.setView(v1);
                adb.setCancelable(false);

                final EditText et1=(EditText)v1.findViewById(R.id.signup_t1);//name
                final EditText et2=(EditText)v1.findViewById(R.id.signup_t2);//mob
                final EditText et3=(EditText)v1.findViewById(R.id.signup_t3);//email
                final EditText et4=(EditText)v1.findViewById(R.id.signup_t4);//password

                adb.setPositiveButton("Sign up", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        name=et1.getText().toString();
                        mob=et2.getText().toString();
                        email=et3.getText().toString();
                        password=et4.getText().toString();

                        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
                        list.add(new BasicNameValuePair("name",name));
                        list.add(new BasicNameValuePair("mob",mob));
                        list.add(new BasicNameValuePair("email",email));
                        list.add(new BasicNameValuePair("password",password));

                        try
                        {

                            HttpClient httpclient = new DefaultHttpClient();

                            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/sign_up.php");

                            httppost.setEntity(new UrlEncodedFormEntity(list));

                            HttpResponse response = httpclient.execute(httppost);

                            HttpEntity entity = response.getEntity();
                            InputStream in = entity.getContent();
                            BufferedReader br = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);

                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            result = sb.toString();
                            br.close();

                            if (result != null) result = result.trim();

                            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();

                        }
                        catch(Exception e)
                        {
                            Toast.makeText(MainActivity.this, "Error : "+e, Toast.LENGTH_LONG).show();
                        }

                    }

                });
                adb.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();

                    }
                });
                adb.create();
                adb.show();

            }
        });

    }
}
