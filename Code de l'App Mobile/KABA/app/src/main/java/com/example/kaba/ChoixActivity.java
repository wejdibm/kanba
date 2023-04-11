package com.example.kaba;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChoixActivity extends AppCompatActivity {

    public static String nom_linge;
    public static String num_poste;
    public static String nom_composant;
    public static String fk_ligne;
    public static String fk_poste;
    public static String fk_kaba;
    Button bd ,bs ,be;
    ArrayList<String> listItems1=new ArrayList<>();
    ArrayList<String> listItems2=new ArrayList<>();
    ArrayList<String> listItems3=new ArrayList<>();
    ArrayList<String> listligne=new ArrayList<>();
    ArrayList<String> listposte=new ArrayList<>();
    ArrayList<String> listcomposant=new ArrayList<>();
    ArrayAdapter<String> adapter1,adapter2,adapter3;
    Spinner sp1,sp2,sp3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);

        bd=findViewById(R.id.btn_data);
        bs=findViewById(R.id.btn_statistic);
        be=findViewById(R.id.btn_time);

        GetLignes gl=new GetLignes();
        gl.execute();
        GetComposants gc=new GetComposants();
        gc.execute();

        sp1=(Spinner)findViewById(R.id.sp_ligne);
        adapter1=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems1);
        sp1.setAdapter(adapter1);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nom_linge = parent.getItemAtPosition(position).toString();
                for (int i=0;i< listItems1.size();i++){
                    if (nom_linge.equals(listItems1.get(i))){
                        fk_ligne = listligne.get(i);
                    }
                }
                GetPostes gp=new GetPostes();
                gp.execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp2=(Spinner)findViewById(R.id.sp_poste);
        adapter2=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems2);
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num_poste = parent.getItemAtPosition(position).toString();
                for (int i=0;i< listItems2.size();i++){
                    if (num_poste.equals(listItems2.get(i))){
                        fk_poste = listposte.get(i);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp3=(Spinner)findViewById(R.id.sp_composant);
        adapter3=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,listItems3);
        sp3.setAdapter(adapter3);
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nom_composant = parent.getItemAtPosition(position).toString();
                for (int i=0;i< listItems3.size();i++){
                    if (nom_composant.equals(listItems3.get(i))){
                        fk_kaba = listcomposant.get(i);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                startActivity(intent);
            }
        });
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StaticsActivity.class);
                startActivity(intent);
            }
        });
        be.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmptyTimeActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private class GetLignes extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://lee.leocontact.tn/connected_kaba/get_lignes.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    list.add(jsonObject.getString("nom_ligne"));
                    listligne.add(jsonObject.getString("id_ligne"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems1.addAll(list);
            adapter1.notifyDataSetChanged();
        }
    }

    private class GetPostes extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            listItems2.clear();
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://lee.leocontact.tn/connected_kaba/get_postes.php?fk_ligne="+fk_ligne);
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            listposte.clear();
            try{
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    list.add(jsonObject.getString("num_poste"));
                    listposte.add(jsonObject.getString("id_poste"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems2.addAll(list);
            adapter2.notifyDataSetChanged();
        }
    }

    private class GetComposants extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://lee.leocontact.tn/connected_kaba/get_composants.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    list.add(jsonObject.getString("nom_composant"));
                    listcomposant.add(jsonObject.getString("id_kaba"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems3.addAll(list);
            adapter3.notifyDataSetChanged();
        }
    }
}