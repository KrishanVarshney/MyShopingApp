package com.example.keshu.myshopingapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class buyer_profile extends AppCompatActivity
{
    TextView t1,t2,t3;
    Button b1;
    String name,mob,email,result;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile);
        StrictMode.enableDefaults();

        t1=(TextView)findViewById(R.id.buyer_pro_t1);//name
        t2=(TextView)findViewById(R.id.buyer_pro_t2);//mob
        t3=(TextView)findViewById(R.id.buyer_pro_t3);//email

        b1=(Button)findViewById(R.id.buyer_pro_b1);//edit profile

        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        email=pref.getString("email","no email defined");

        t3.setText(email);

        fetchConnection();

        t1.setText(name);
        t2.setText(mob);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fetchConnection();
                LayoutInflater li=LayoutInflater.from(buyer_profile.this);
                View v1=li.inflate(R.layout.edit_profile_dialoge,null);
                AlertDialog.Builder adb=new AlertDialog.Builder(buyer_profile.this);
                adb.setView(v1);
                adb.setCancelable(false);

                final EditText et1=(EditText)v1.findViewById(R.id.edit_pro_t1);//name
                final EditText et2=(EditText)v1.findViewById(R.id.edit_pro_t2);//mob
                final EditText et3=(EditText)v1.findViewById(R.id.edit_pro_t3);//email

                et1.setText(name);
                et2.setText(mob);
                et3.setText(email);
                et3.setEnabled(false);

                adb.setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        name=et1.getText().toString();
                        mob=et2.getText().toString();

                        update_connection();

                        recreate();

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


    //making connection to php server
    protected void fetchConnection()
    {
        try
        {
            HttpClient httpclient=new DefaultHttpClient();

            ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("email",email));


            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/fetch_buyer_data.php");
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
                if (result.length() > 0)
                {
                    JSONArray jarray = new JSONArray(result);


                    for (int i = 0; i < jarray.length(); i++)
                    {

                        JSONObject obj = jarray.getJSONObject(i);
                        name = obj.getString("name");
                        mob = obj.getString("mob");

                    }



                }
                else
                {

                    Toast.makeText(buyer_profile.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(buyer_profile.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }


        }
        catch(Exception e)
        {
            Toast.makeText(buyer_profile.this, "JSON : "+e, Toast.LENGTH_LONG).show();
        }
    }
//making update profile connection
    protected  void update_connection()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("name",name));
        list.add(new BasicNameValuePair("mob",mob));
        list.add(new BasicNameValuePair("email",email));


        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/update_buyer_data.php");
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
            Toast.makeText(buyer_profile.this, result, Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            Toast.makeText(buyer_profile.this, "Error : " + e, Toast.LENGTH_LONG).show();
        }

    }
}
