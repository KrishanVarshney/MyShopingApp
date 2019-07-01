package com.example.keshu.myshopingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class seller_profile extends AppCompatActivity
{
    TextView t1,t2,t3;
    Button b1;
    ListView lv;
    String name,mob,email,result,password,data[],title[],price[],details[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        StrictMode.enableDefaults();

        t1=(TextView)findViewById(R.id.seller_p_t1);//name
        t2=(TextView)findViewById(R.id.seller_p_t2);//mob
        t3=(TextView)findViewById(R.id.seller_p_t3);//email

        b1=(Button)findViewById(R.id.seller_p_b1);//add product

        lv=(ListView)findViewById(R.id.seller_p_lv);//product list

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
                Intent i=new Intent(seller_profile.this,add_prduct.class);
                startActivity(i);
            }
        });


        init_adapter();



    }
//connection to seller's data php
    protected void fetchConnection()
    {
        try
        {
            HttpClient httpclient=new DefaultHttpClient();

            ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("email",email));


            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/fetch_seller_data.php");
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

                    Toast.makeText(seller_profile.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(seller_profile.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }


        }
        catch(Exception e)
        {
            Toast.makeText(seller_profile.this, "JSON : "+e, Toast.LENGTH_LONG).show();
        }

    }
//connection to making update sellers data
    protected  void update_connection()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("name",name));
        list.add(new BasicNameValuePair("mob",mob));
        list.add(new BasicNameValuePair("email",email));


        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/update_seller_data.php");
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
            Toast.makeText(seller_profile.this, result, Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            Toast.makeText(seller_profile.this, "Error : " + e, Toast.LENGTH_LONG).show();
        }

    }

    //menue code
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi= getMenuInflater();
        mi.inflate(R.menu.seller_profile_menu,menu);
        return true;
    }
    //menue on click code
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.s_p_i1://editprofile
                editProfile();
                return true;
            case R.id.s_p_i2://view order
                viewOrder();
                return true;
            case R.id.s_p_i3://change password
                changePassword();
                return true;
            case R.id.s_p_i4://logout
                logOut();
                return true;
        }
        return false;
    }
    protected void editProfile()
    {
        fetchConnection();
        LayoutInflater li=LayoutInflater.from(seller_profile.this);
        View v1=li.inflate(R.layout.edit_profile_dialoge,null);
        AlertDialog.Builder adb=new AlertDialog.Builder(seller_profile.this);
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
    protected void viewOrder()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(seller_profile.this);
        adb.setCancelable(true);
        adb.setMessage("Which Type of Order you want See...");
        adb.setPositiveButton("Active Order", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i=new Intent(seller_profile.this,active_order.class);
                startActivity(i);
            }
        });
        adb.setNegativeButton("All Orders", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i=new Intent(seller_profile.this,all_orders.class);
                startActivity(i);
            }
        });
        adb.create();
        adb.show();

    }
    protected void changePassword()
    {
        LayoutInflater li=LayoutInflater.from(seller_profile.this);
        View v1=li.inflate(R.layout.change_password,null);
        AlertDialog.Builder adb=new AlertDialog.Builder(seller_profile.this);
        adb.setView(v1);
        adb.setCancelable(false);

        final EditText tv1=(EditText)v1.findViewById(R.id.change_pass_t1);//old
        final EditText tv2=(EditText)v1.findViewById(R.id.change_pass_t2);//new
        final EditText tv3=(EditText)v1.findViewById(R.id.change_pass_t3);//confirm
        adb.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                String oldp=tv1.getText().toString();
                String newp=tv2.getText().toString();
                String confirm=tv3.getText().toString();


                ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("email",email));
                list.add(new BasicNameValuePair("oldp",oldp));
                list.add(new BasicNameValuePair("newp",newp));
                list.add(new BasicNameValuePair("confirm",confirm));

                try {


                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/change_password.php");
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
                    Toast.makeText(seller_profile.this, result, Toast.LENGTH_SHORT).show();

                    if (result != null) result = result.trim();
                    Toast.makeText(seller_profile.this, result, Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(seller_profile.this, "Error : " + e, Toast.LENGTH_LONG).show();
                }
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        adb.create();
        adb.show();



    }
    protected void logOut()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(seller_profile.this);
        adb.setMessage("Are you sure..??");
        adb.setCancelable(false);
        adb.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences pref=getSharedPreferences("mypref",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.remove("email");
                editor.commit();
                Intent i1=new Intent(seller_profile.this,MainActivity.class);
                startActivity(i1);

            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        adb.create();
        adb.show();

    }

    private void init_adapter()
    {
        try
        {
            HttpClient httpclient=new DefaultHttpClient();


            ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("email",email));
            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/ProductData.php");
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

                        final String eemail[] = new String[len];
                        final String title[] = new String[len];
                        final String price[] = new String[len];
                        final String details[] = new String[len];



                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject obj = jarray.getJSONObject(i);
                            String c = obj.getString("title");
                            title[i] = c;
                            String b = obj.getString("price");
                            price[i] = b;
                            String d = obj.getString("details");
                            details[i] = d;
                            String a = obj.getString("email");
                            eemail[i] = a;
                            String text = obj.getString("title") + "\n" + obj.getString("price") ;
                            data[i] = text;
                        }



                        if (data.length > 0)
                        {
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(seller_profile.this,android.R.layout.simple_list_item_1,android.R.id.text1,data);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String em = eemail[i];
                                    String ti=title[i];
                                    String pr=price[i];
                                    String di=details[i];
                                    Intent i1 = new Intent(seller_profile.this, product_view.class);
                                    i1.putExtra("title",ti);
                                    i1.putExtra("price",pr);
                                    i1.putExtra("details",di);
                                    i1.putExtra("email",em);
                                    startActivity(i1);
                                }

                            });
                        } else {
                            Toast.makeText(seller_profile.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(seller_profile.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(seller_profile.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(seller_profile.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }
}
