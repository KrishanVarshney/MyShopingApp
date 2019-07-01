package com.example.keshu.myshopingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class buyer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    ImageButton b1;
    ListView lv;
    TextView t1,t2,t3;
    String name,email,result,data[];


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.enableDefaults();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);


        t1=(TextView)findViewById(R.id.buyer_t1);//Welcomes page name
        t2=(TextView)headerview.findViewById(R.id.nev_t1);//nevigation name
        t3=(TextView)headerview.findViewById(R.id.nev_t2);//nevigation email

        lv=(ListView)findViewById(R.id.buyer_lv);//buyer page list to show products

        b1=(ImageButton)findViewById(R.id.buyer_b1);//search button



        SharedPreferences pref= getSharedPreferences("mypref",MODE_PRIVATE);
        email=pref.getString("email","no email defined");

        t3.setText(email);

        fetchConnection();

        t1.setText("Welcome "+name);
        t2.setText(name);

        init_adapter();

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i1=new Intent(buyer.this,search_product.class);
                startActivity(i1);
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            {

            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.view_profile)
        {
            Intent i=new Intent(buyer.this,buyer_profile.class);
            startActivity(i);
        }
        else if (id == R.id.orders)
        {
            Intent i=new Intent(buyer.this,order_buyer.class);
            startActivity(i);

        }
        else if (id == R.id.wishlist)
        {
            Intent i=new Intent(buyer.this,wish_list.class);
            startActivity(i);

        }
        else if (id == R.id.cart)
        {
            Intent i=new Intent(buyer.this,cart_list.class);
            startActivity(i);

        }
        else if (id == R.id.cahnge_password)
        {
            changePassword();

        }
        else if (id == R.id.log_out)
        {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changePassword()
    {
        LayoutInflater li=LayoutInflater.from(buyer.this);
        View v1=li.inflate(R.layout.change_password,null);
        AlertDialog.Builder adb=new AlertDialog.Builder(buyer.this);
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
                    Toast.makeText(buyer.this, result, Toast.LENGTH_SHORT).show();

                    if (result != null) result = result.trim();
                    Toast.makeText(buyer.this, result, Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    Toast.makeText(buyer.this, "Error : " + e, Toast.LENGTH_LONG).show();
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
    public void logOut()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(buyer.this);
        adb.setMessage("Are you sure..??");
        adb.setCancelable(false);
        adb.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences pref=getSharedPreferences("mypref",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.remove("email");
                editor.commit();
                Intent i1=new Intent(buyer.this,MainActivity.class);
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


                    }




                }
                else
                {

                    Toast.makeText(buyer.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(buyer.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }


        }
        catch(Exception e)
        {
            Toast.makeText(buyer.this, "JSON : "+e, Toast.LENGTH_LONG).show();
        }
    }
//setting up list of product's
    private void init_adapter()
    {
        try
        {
            HttpClient httpclient=new DefaultHttpClient();

            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/buyer_product_data.php");

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
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(buyer.this,android.R.layout.simple_list_item_1,android.R.id.text1,data);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String em = eemail[i];
                                    String ti=title[i];
                                    String pr=price[i];
                                    String di=details[i];
                                    Intent i1 = new Intent(buyer.this, buyer_product_view.class);
                                    i1.putExtra("title",ti);
                                    i1.putExtra("price",pr);
                                    i1.putExtra("details",di);
                                    i1.putExtra("email",em);
                                    startActivity(i1);
                                }

                            });
                        } else {
                            Toast.makeText(buyer.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(buyer.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(buyer.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(buyer.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }
}


