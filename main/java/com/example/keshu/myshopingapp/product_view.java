package com.example.keshu.myshopingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class product_view extends AppCompatActivity
{
    TextView t1,t2,t3,t4;
    String title,price,details,email,result,newtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        title=getIntent().getExtras().getString("title");
        price=getIntent().getExtras().getString("price");
        details=getIntent().getExtras().getString("details");
        email=getIntent().getExtras().getString("email");

        t1=findViewById(R.id.pro_v_t1);
        t2=findViewById(R.id.pro_v_t2);
        t3=findViewById(R.id.pro_v_t3);
        t4=findViewById(R.id.pro_v_t4);



        t1.setText(title);
        t2.setText(price);
        t3.setText(details);
        t4.setText(email);
    }

    //menue code
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi= getMenuInflater();
        mi.inflate(R.menu.pro_view_seller_menu,menu);
        return true;
    }
    //menue on click code
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.pro_v_i1://editproduct
                editProduct();
                return true;
            case R.id.pro_v_i2://deleteproduct
                deleteProduct();
                return true;

        }
        return false;
    }
    protected void editProduct()
    {
        LayoutInflater li=LayoutInflater.from(product_view.this);
        View v1=li.inflate(R.layout.edit_product,null);
        AlertDialog.Builder adb=new AlertDialog.Builder(product_view.this);
        adb.setView(v1);
        adb.setCancelable(false);

        final EditText et1=(EditText)v1.findViewById(R.id.edit_prod_t1);//title
        final EditText et2=(EditText)v1.findViewById(R.id.edit_prod_t2);//price
        final EditText et3=(EditText)v1.findViewById(R.id.edit_prod_t3);//details

        et1.setText(title);
        et2.setText(price);
        et3.setText(details);

        adb.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                newtitle=et1.getText().toString();
                price=et2.getText().toString();
                details=et3.getText().toString();

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
    protected void deleteProduct()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(product_view.this);
        adb.setMessage("Are You Sure You Want to Delete...!!");
        adb.setCancelable(false);
        adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                deleteConnection();
                Intent i1=new Intent(product_view.this,seller_profile.class);
                startActivity(i1);

            }
        });
        adb.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        adb.create();
        adb.show();
    }
//making connection to php for deleting product
    protected  void deleteConnection()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("title",title));
        list.add(new BasicNameValuePair("email",email));


        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/delete_product_data.php");
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
            Toast.makeText(product_view.this, result, Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            Toast.makeText(product_view.this, "Error : " + e, Toast.LENGTH_LONG).show();
        }

    }
//making connection to php for update product data
    protected  void update_connection()
    {
        ArrayList<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("title",title));
        list.add(new BasicNameValuePair("newtitle",newtitle));
        list.add(new BasicNameValuePair("price",price));
        list.add(new BasicNameValuePair("details",details));
        list.add(new BasicNameValuePair("email",email));


        try {


            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://10.0.2.2/shopingApp/update_product_data.php");
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
            Toast.makeText(product_view.this, result, Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            Toast.makeText(product_view.this, "Error : " + e, Toast.LENGTH_LONG).show();
        }

    }
}
