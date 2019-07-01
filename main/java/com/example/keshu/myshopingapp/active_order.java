package com.example.keshu.myshopingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

public class active_order extends AppCompatActivity
{
    ListView lv;

    static String[]items={"Dispatched","Shiped","Out for Delivery","Delivered","cancel"};
    String semail,orderid,name,address,mob,bemail,title,orderstatus,data[],result,oi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);
        StrictMode.enableDefaults();

        lv=findViewById(R.id.active_order_lv);

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

            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/setting_active_order_list.php");
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
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(active_order.this,android.R.layout.simple_list_item_1,android.R.id.text1,data);
                            lv.setAdapter(adapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                                {
                                    oi = orderid[i];
                                    LayoutInflater li=LayoutInflater.from(active_order.this);
                                    View v1=li.inflate(R.layout.update_status,null);
                                    AlertDialog.Builder aa=new AlertDialog.Builder(active_order.this);
                                    aa.setView(v1);
                                    aa.setCancelable(false);
                                    final Spinner sp=v1.findViewById(R.id.update_dd);
                                    ArrayAdapter<String>adapter1=new ArrayAdapter<String>(active_order.this,android.R.layout.simple_spinner_item,items);
                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sp.setAdapter(adapter1);
                                    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                        {
                                            switch (position)
                                            {
                                                case 0:
                                                    orderstatus="Dispached";
                                                    break;
                                                case 1:
                                                    orderstatus="Shiped";
                                                    break;
                                                case 2:
                                                    orderstatus="Out For Delivery";
                                                    break;
                                                case 3:
                                                    orderstatus="Delivered";
                                                    break;
                                                case 4:
                                                    orderstatus="CANCELED";
                                                    break;
                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });


                                    aa.setPositiveButton("DONE", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            seting_dropdown_menu();
                                            recreate();

                                        }
                                    });
                                    aa.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    aa.create();
                                    aa.show();
                                }

                            });

                        } else {
                            Toast.makeText(active_order.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(active_order.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(active_order.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(active_order.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }

    protected void seting_dropdown_menu()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("orderstatus",orderstatus));
        list.add(new BasicNameValuePair("orderid",oi));


        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/update_status.php");
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
            //Toast.makeText(active_order.this, result, Toast.LENGTH_SHORT).show();

            if (result != null) result = result.trim();
            Toast.makeText(active_order.this, result, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            Toast.makeText(active_order.this, "Error : " + e, Toast.LENGTH_LONG).show();
        }

    }


}
