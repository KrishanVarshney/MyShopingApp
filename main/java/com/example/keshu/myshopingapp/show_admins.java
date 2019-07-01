package com.example.keshu.myshopingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class show_admins extends AppCompatActivity
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
        setContentView(R.layout.activity_show_admins);

        init_adapter();

        if(adapter!=null) {
            t1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    show_admins.this.adapter.getFilter().filter(charSequence);

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

            HttpPost httppost= new HttpPost("http://10.0.2.2/shopingApp/ShowAdmins.php");

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

                        final String email[] = new String[len];


                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject obj = jarray.getJSONObject(i);
                            String c = obj.getString("email");
                            email[i] = c;
                            String text = obj.getString("email") + "\n" + obj.getString("name") + "\n" + obj.getString("mob");
                            data[i] = text;
                        }


                        lv = (ListView) findViewById(R.id.show_admin_lv);
                        t1 = (EditText) findViewById(R.id.show_admin_t1);
                        if (data.length > 0) {
                            adapter = new ArrayAdapter<String>(show_admins.this, R.layout.list_item, R.id.list_item_t1, data);
                            lv.setAdapter(adapter);
                        } else {
                            Toast.makeText(show_admins.this, "No data", Toast.LENGTH_LONG).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(show_admins.this, "No Data Found !!", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(show_admins.this, "JSON : "+e, Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(show_admins.this, "Error : "+e, Toast.LENGTH_LONG).show();
        }

    }
}


