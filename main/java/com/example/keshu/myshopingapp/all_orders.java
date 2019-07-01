package com.example.keshu.myshopingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class all_orders extends AppCompatActivity
{
    ListView lv;
    String semail,orderid,name,address,mob,bemail,title,orderstatus,data[],result,oi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        StrictMode.enableDefaults();

        lv=findViewById(R.id.all_order_lv);

        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        semail=pref.getString("email","no email defined");

        init_adapter();

    }

    private void init_adapter()
    {
        try
        {
            ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("semail",semail));

            HttpClient httpclient=new DefaultHttpClient();

            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/setting_all_order_list.php");
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
            //Toast.makeText(all_orders.this, result, Toast.LENGTH_LONG).show();

            try
            {
                if(result.length()>0) {
                    JSONArray jarray = new JSONArray(result);
                    int len = jarray.length();
                    if (len > 0) {
                        data = new String[len];

                        final String orderid[] = new String[len];




                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject obj = jarray.getJSONObject(i);
                            String a = String.valueOf(obj.getInt("order_id"));
                            orderid[i] = a;
                            String text ="Order ID : "+obj.getInt("order_id" )+"\n"+"Title : "+obj.getString("title")+"\n"+"Customer Name : "+obj.getString("name")+"\n"+"Customer Mob. : "+obj.getString("mob")+"\n"+"Customer Address : "+ obj.getString("adress")+"\n"+"Customer Email : "+ obj.getString("bemail")+"\n"+"Order Status : "+ obj.getString("order_status");

                            //String text =obj.getString("title")+"\n"+obj.getString("name")+"\n"+obj.getString("mob")+"\n"+obj.getString("adress")+"\n"+ obj.getString("bemail")+"\n"+ obj.getString("order_status");
                            data[i] = text;
                        }



                        if (data.length > 0)
                        {
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(all_orders.this,android.R.layout.simple_list_item_1,android.R.id.text1,data);
                            lv.setAdapter(adapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    oi = orderid[i];
                                    AlertDialog.Builder aadb=new AlertDialog.Builder(all_orders.this);
                                    aadb.setCancelable(false);
                                    aadb.setMessage("Do You Want To Delete Order.....");
                                    aadb.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            deleteConnection();
                                            recreate();

                                        }
                                    });
                                    aadb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    aadb.create();
                                    aadb.show();



                                }

                            });
                        } else {
                            Toast.makeText(all_orders.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(all_orders.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(all_orders.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(all_orders.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }

    //making connection to php for deleting order
    protected  void deleteConnection()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("orderid",oi));


        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/delete_order_data.php");
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
            Toast.makeText(all_orders.this, result, Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            Toast.makeText(all_orders.this, "Error : " + e, Toast.LENGTH_LONG).show();
        }

    }


}
