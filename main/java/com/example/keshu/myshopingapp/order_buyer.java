package com.example.keshu.myshopingapp;

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

public class order_buyer extends AppCompatActivity
{
    ListView lv;
    String bemail,result,data[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_buyer);

        StrictMode.enableDefaults();

        lv=(ListView)findViewById(R.id.order_buyer_lv);

        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        bemail=pref.getString("email","no email defined");

        init_adapter();


    }

    private void init_adapter()
    {
        try
        {
            ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("bemail",bemail));

            HttpClient httpclient=new DefaultHttpClient();

            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/setting_order_list.php");
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

                        final String orderid[] = new String[len];
                        final String name[] = new String[len];
                        final String address[] = new String[len];
                        final String mob[] = new String[len];
                        final String orderstatus[] = new String[len];
                        final String title[] = new String[len];



                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject obj = jarray.getJSONObject(i);
                            String a = String.valueOf(obj.getInt("order_id"));
                            orderid[i] = a;
                            String c = obj.getString("name");
                            name[i] = c;
                            String b = obj.getString("adress");
                            address[i] = b;
                            String d = obj.getString("mob");
                            mob[i] = d;
                            String g = obj.getString("title");
                            title[i] = g;
                            String f = obj.getString("order_status");
                            orderstatus[i] = f;

                            String text = obj.getString("title" )+ "\n" + obj.getString("order_status");
                            data[i] = text;
                        }



                        if (data.length > 0)
                        {
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(order_buyer.this,android.R.layout.simple_list_item_1,android.R.id.text1,data);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String oi = orderid[i];
                                    String ti=title[i];
                                    String na=name[i];
                                    String ad=address[i];
                                    String mo=mob[i];
                                    String os=orderstatus[i];
                                    Intent i1 = new Intent(order_buyer.this, view_order_buyer.class);
                                    i1.putExtra("title",ti);
                                    i1.putExtra("name",na);
                                    i1.putExtra("orderid",oi);
                                    i1.putExtra("adress",ad);
                                    i1.putExtra("mob",mo);
                                    i1.putExtra("orderstatus",os);
                                    startActivity(i1);
                                }

                            });
                        } else {
                            Toast.makeText(order_buyer.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(order_buyer.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(order_buyer.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(order_buyer.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }
}



