package com.example.keshu.myshopingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class search_product extends AppCompatActivity
{

    ListView lv;
    EditText t1;
    String result="",name[]=null,mob[]=null;
    String data[]=null;
    ArrayAdapter<String> adapter=null;
    ArrayList<HashMap<String,String>> studentlist;
    String email[]=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        init_adapter();

        if(adapter!=null) {
            t1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    search_product.this.adapter.getFilter().filter(charSequence);

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

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

                        lv = (ListView) findViewById(R.id.search_prod_lv);
                        t1 = (EditText) findViewById(R.id.search_prod_t1);


                        if (data.length > 0)
                        {
                            adapter = new ArrayAdapter<String>(search_product.this, R.layout.list_item, R.id.list_item_t1, data);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String em = eemail[i];
                                    String ti=title[i];
                                    String pr=price[i];
                                    String di=details[i];
                                    Intent i1 = new Intent(search_product.this, buyer_product_view.class);
                                    i1.putExtra("title",ti);
                                    i1.putExtra("price",pr);
                                    i1.putExtra("details",di);
                                    i1.putExtra("email",em);
                                    startActivity(i1);
                                }

                            });
                        } else {
                            Toast.makeText(search_product.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(search_product.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(search_product.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(search_product.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }
}
