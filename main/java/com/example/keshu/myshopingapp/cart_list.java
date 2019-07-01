package com.example.keshu.myshopingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class cart_list extends AppCompatActivity
{
    ListView lv;
    String bemail,result,data[],ti,se;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        StrictMode.enableDefaults();

        lv=(ListView)findViewById(R.id.cart_list_lv);

        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        bemail=pref.getString("email","no email defined");

        init_adapter();
    }

    //setting up list of product's
    private void init_adapter()
    {

        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("bemail",bemail));

        try
        {
            HttpClient httpclient=new DefaultHttpClient();

            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/setting_cart_list.php");
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
            br.close();
            result=sb.toString();
            if(result!=null)
                result=result.trim();
            //Toast.makeText(show_student_data.this, result+"/"+result.length(), Toast.LENGTH_LONG).show();

            try
            {
                if(result.length()>0) {
                    JSONArray jarray = new JSONArray(result);
                    int len = jarray.length();
                    if (len > 0) {
                        data = new String[len];


                        final String title[] = new String[len];
                        final String price[] = new String[len];
                        final String semail[] = new String[len];




                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject obj = jarray.getJSONObject(i);
                            String c = obj.getString("title");
                            title[i] = c;
                            String b = obj.getString("price");
                            price[i] = b;
                            String a = obj.getString("semail");
                            semail[i] = a;
                            String text = obj.getString("title") + "\n" + obj.getString("price") ;
                            data[i] = text;
                        }



                        if (data.length > 0)
                        {
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(cart_list.this,android.R.layout.simple_list_item_1,android.R.id.text1,data);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    ti=title[i];
                                    se=semail[i];
                                    String pr=price[i];
                                    //setting up dialoge
                                    AlertDialog.Builder adb=new AlertDialog.Builder(cart_list.this);
                                    adb.setMessage("Procced with  ");
                                    //buy now
                                    adb.setPositiveButton("Buy Now", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            Buy_Now();
                                        }
                                    });
                                    adb.setNegativeButton("delete", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            remove_from_cart();

                                        }
                                    });
                                    adb.create();
                                    adb.show();
                                }

                            });
                        } else {
                            Toast.makeText(cart_list.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(cart_list.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(cart_list.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(cart_list.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }

    private void Buy_Now()
    {
        LayoutInflater li= LayoutInflater.from(cart_list.this);
        AlertDialog.Builder adb=new AlertDialog.Builder(cart_list.this);
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
                list.add(new BasicNameValuePair("title",ti));
                list.add(new BasicNameValuePair("adress",address));
                list.add(new BasicNameValuePair("mob",mob));
                list.add(new BasicNameValuePair("semail",se));
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

                    Toast.makeText(cart_list.this, result, Toast.LENGTH_SHORT).show();

                    if(result.equalsIgnoreCase("Order Placed"));
                    {
                        remove_from_cart();
                    }



                }
                catch(Exception e)
                {
                    Toast.makeText(cart_list.this, "Error : "+e, Toast.LENGTH_LONG).show();
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

    protected void remove_from_cart()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("title",ti));
        list.add(new BasicNameValuePair("bemail",bemail));


        try
        {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/remove_from_cart.php");

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

            Toast.makeText(cart_list.this, result, Toast.LENGTH_SHORT).show();
            if(result.equalsIgnoreCase("Successfull"))
            {
                recreate();
            }




        }
        catch(Exception e)
        {
            Toast.makeText(cart_list.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }
    }

}