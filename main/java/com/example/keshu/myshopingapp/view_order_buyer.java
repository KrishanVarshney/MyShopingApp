package com.example.keshu.myshopingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class view_order_buyer extends AppCompatActivity
{
    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    String name,title,mob,bemail,order_id,address,orderstatus,result;
    Button b1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_buyer);

        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        bemail=pref.getString("email","no email defined");

        title=getIntent().getExtras().getString("title");
        name=getIntent().getExtras().getString("name");
        order_id=getIntent().getExtras().getString("orderid");
        address=getIntent().getExtras().getString("adress");
        mob=getIntent().getExtras().getString("mob");
        orderstatus=getIntent().getExtras().getString("orderstatus");


        t1=findViewById(R.id.view_order_buy_t1);//orderid
        t2=findViewById(R.id.view_order_buy_t2);//title
        t4=findViewById(R.id.view_order_buy_t4);//name
        t5=findViewById(R.id.view_order_buy_t5);//adress
        t6=findViewById(R.id.view_order_buy_t6);//mob
        t7=findViewById(R.id.view_order_buy_t7);//email
        t8=findViewById(R.id.view_order_buy_t8);//order status

        b1=findViewById(R.id.view_order_buy_b1);//cancel button

        t1.setText(order_id);
        t2.setText(title);
        t4.setText(name);
        t5.setText(address);
        t6.setText(mob);
        t7.setText(bemail);


        t1.setTextColor(Color.BLUE);
        t2.setTextColor(Color.BLUE);
        t4.setTextColor(Color.BLUE);
        t5.setTextColor(Color.BLUE);
        t6.setTextColor(Color.BLUE);
        t7.setTextColor(Color.BLUE);
        if(orderstatus.equalsIgnoreCase("CANCELED"))
        {
            t8.setTextColor(Color.RED);
            t8.setText(orderstatus);
        }
        else
        {
            t8.setTextColor(Color.BLUE);
            t8.setText(orderstatus);
        }
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder adb=new AlertDialog.Builder(view_order_buyer.this);
                adb.setCancelable(false);
                adb.setMessage("Are You Sure You Want To Cancel Order...!!");
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        cancel_order_connection();
                        if(result.equals("CANCELED"))
                        {
                            t8.setTextColor(Color.RED);
                            t8.setText("CANCELED");
                        }
                    }
                });
                adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.create();
                adb.show();

            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent i=new Intent(view_order_buyer.this,buyer.class);
        startActivity(i);
    }

    protected void cancel_order_connection()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("bemail",bemail));
        list.add(new BasicNameValuePair("orderid",order_id));


        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/cancel_order.php");
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
            Toast.makeText(view_order_buyer.this, result, Toast.LENGTH_SHORT).show();

            if (result != null) result = result.trim();
            Toast.makeText(view_order_buyer.this, result, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            Toast.makeText(view_order_buyer.this, "Error : " + e, Toast.LENGTH_LONG).show();
        }
    }
}
