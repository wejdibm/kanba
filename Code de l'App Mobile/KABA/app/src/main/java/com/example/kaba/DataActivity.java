package com.example.kaba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.example.kaba.ChoixActivity.fk_kaba;
import static com.example.kaba.ChoixActivity.fk_poste;
import static com.example.kaba.ChoixActivity.nom_composant;
import static com.example.kaba.ChoixActivity.nom_linge;
import static com.example.kaba.ChoixActivity.num_poste;

public class DataActivity extends AppCompatActivity {


    public TextView data, ligne, poste, composant, nombre, date, timek;
    public static final String CHANNEL_ID = "simplified_coding";
    public static final String CHANNEL_NAME = "simplified coding";
    public static final String CHANNEL_DESC = "simplified coding Notifications";
    private String url_data ;
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        ligne = (TextView) findViewById(R.id.ligne);
        poste = (TextView) findViewById(R.id.poste);
        composant = (TextView) findViewById(R.id.composant);
        nombre = (TextView) findViewById(R.id.nombre);
        date = (TextView) findViewById(R.id.date);
        timek = (TextView) findViewById(R.id.timek);

        ligne.setText(nom_linge);
        poste.setText(num_poste);
        composant.setText(nom_composant);

        t = new Thread(){
            @Override
            public void run() {
                while (!isInterrupted()){

                    try {

                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getData(fk_poste,fk_kaba);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    private void getData(String fp , String fk) {

        url_data =  Config.Data+"?fk_poste="+ fp +"&fk_kaba="+ fk;
        StringRequest stringRequest = new StringRequest(url_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
                    JSONObject jo = result.getJSONObject(result.length()-1);
                    nombre.setText(jo.getString("quantity"));
                    date.setText(jo.getString("date"));
                    timek.setText(jo.getString("time"));

                    if (jo.getInt("quantity") < 6){
                        manqueNotification();
                    }
                    if (jo.getInt("quantity") > 30){
                        excesNotification();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DataActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void manqueNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Kaba En Manque De Composant")
                        .setContentText("ligne:"+nom_linge +"\n" +"/poste:"+ num_poste+"\n" +"/composant:"+nom_composant)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT) ;
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
        mNotificationMgr.notify(1,mBuilder.build());
    }
    public void excesNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Kaba En Excès De Composant")
                        .setContentText("ligne:"+nom_linge +"/poste:"+ num_poste+"/composant:"+nom_composant)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT) ;
        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
        mNotificationMgr.notify(1,mBuilder.build());
    }
}