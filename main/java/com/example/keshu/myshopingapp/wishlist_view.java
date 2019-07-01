package com.example.keshu.myshopingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class wishlist_view extends AppCompatActivity
{
    TextView t1,t2,t3,t4;
    String price,title,details,bemail,semail,result;
    int flag=1;
    Button b1,b2,b3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_view);

        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        bemail=pref.getString("email","no email defined");

        title=getIntent().getExtras().getString("title");
        price=getIntent().getExtras().getString("price");
        details=getIntent().getExtras().getString("details");
        semail=getIntent().getExtras().getString("semail");

        t1=findViewById(R.id.wishlist_view_t1);//title
        t2=findViewById(R.id.wishlist_view_t2);//price
        t3=findViewById(R.id.wishlist_view_t3);//details
        t4=findViewById(R.id.wishlist_view_t4);//sellers email

        b1=(Button)findViewById(R.id.wishlist_view_b1);//buy now
        b2=(Button)findViewById(R.id.wishlist_view_b2);//remove from wishlist
        b3=(Button)findViewById(R.id.wishlist_view_b3);//add to cart

        if(flag==1)
        {
            b2.setText("Remove from Wishlist");
        }
        else
        {
            b2.setText("Add to Wishlist");

        }



        //information of product
        t1.setText(title);
        t2.setText(price);
        t3.setText(details);
        t4.setText(semail);
//buy now
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater li= LayoutInflater.from(wishlist_view.this);
                AlertDialog.Builder adb=new AlertDialog.Builder(wishlist_view.this);
                View v1=li.inflate(R.layout.buy_now_dialoge,null);
                adb.setView(v1);
                adb.setCancelable(false);

                final EditText tv1=(EditText)v1.findViewById(R.id.buy_now_t1);//name
                final EditText tv2=(EditText)v1.findViewById(R.id.buy_now_t2);//address
                final EditText tv3=(EditText)v1.findViewById(R.id.buy_now_t3);//mob.no.

                adb.setPositiveButton("Place Order", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String name=tv1.getText().toString();
                        String address=tv2.getText().toString();
                        String mob=tv3.getText().toString();

                        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
                        list.add(new BasicNameValuePair("name",name));
                        list.add(new BasicNameValuePair("title",title));
                        list.add(new BasicNameValuePair("adress",address));
                        list.add(new BasicNameValuePair("mob",mob));
                        list.add(new BasicNameValuePair("semail",semail));
                        list.add(new BasicNameValuePair("bemail",bemail));
                        try
                        {

                            HttpClient httpclient = new DefaultHttpClient();

                            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/OrderPlace.php");

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

                            Toast.makeText(wishlist_view.this, result, Toast.LENGTH_SHORT).show();



                        }
                        catch(Exception e)
                        {
                            Toast.makeText(wishlist_view.this, "Error : "+e, Toast.LENGTH_LONG).show();
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

//add or remove to wishlist
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(flag==1)
                {
                    remove_from_wishlist();
                    if(result.equalsIgnoreCase("Successfull"))
                    {
                        flag=0;
                        b2.setText("Add To Wishlist");
                    }

                }
                else
                {
                    add_to_wishlist();
                    if(result.equalsIgnoreCase("Successfull"))
                    {
                        flag=1;
                        b2.setText("Remove From Wishlist");
                    }

                }

            }
        });


//add to cart
        b3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("title",title));
                list.add(new BasicNameValuePair("price",price));
                list.add(new BasicNameValuePair("semail",semail));
                list.add(new BasicNameValuePair("bemail",bemail));

                try
                {

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/add_to_cart.php");

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

                    Toast.makeText(wishlist_view.this, result, Toast.LENGTH_SHORT).show();




                }
                catch(Exception e)
                {
                    Toast.makeText(wishlist_view.this, "Error : "+e, Toast.LENGTH_LONG).show();
                }


            }
        });
    }
    protected void add_to_wishlist()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("title",title));
        list.add(new BasicNameValuePair("price",price));
        list.add(new BasicNameValuePair("semail",semail));
        list.add(new BasicNameValuePair("bemail",bemail));
        list.add(new BasicNameValuePair("details",details));

        try
        {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/add_wishlist.php");

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

            Toast.makeText(wishlist_view.this, result, Toast.LENGTH_SHORT).show();




        }
        catch(Exception e)
        {
            Toast.makeText(wishlist_view.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }

    protected void remove_from_wishlist()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("title",title));
        list.add(new BasicNameValuePair("bemail",bemail));


        try
        {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/remove_from_wishlist.php");

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

            Toast.makeText(wishlist_view.this, result, Toast.LENGTH_SHORT).show();




        }
        catch(Exception e)
        {
            Toast.makeText(wishlist_view.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }
}
