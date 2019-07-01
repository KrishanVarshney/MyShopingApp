package com.example.keshu.myshopingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class add_prduct extends AppCompatActivity
{
    EditText t1,t2,t3;
    Button b1,b2,b3;
    String title,price,details,email,result;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prduct);

        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        email=pref.getString("email","no email defined");

        t1=(EditText)findViewById(R.id.add_prod_t1);//title
        t2=(EditText)findViewById(R.id.add_prod_t2);//price
        t3=(EditText)findViewById(R.id.add_prod_t3);//details

        b3=(Button)findViewById(R.id.add_prod_b3);//add product

        b3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                title=t1.getText().toString();
                price=t2.getText().toString();
                details=t3.getText().toString();

                ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("title",title));
                list.add(new BasicNameValuePair("price",price));
                list.add(new BasicNameValuePair("email",email));
                list.add(new BasicNameValuePair("details",details));

                try
                {

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/add_product.php");

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

                    Toast.makeText(add_prduct.this, result, Toast.LENGTH_SHORT).show();

                    Intent i1=new Intent(add_prduct.this,seller_profile.class);
                    startActivity(i1);


                }
                catch(Exception e)
                {
                    Toast.makeText(add_prduct.this, "Error : "+e, Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
