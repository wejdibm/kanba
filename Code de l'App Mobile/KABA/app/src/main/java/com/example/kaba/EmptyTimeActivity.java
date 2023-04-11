package com.example.kaba;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import static com.example.kaba.ChoixActivity.fk_kaba;
import static com.example.kaba.ChoixActivity.fk_poste;
import static com.example.kaba.ChoixActivity.nom_composant;
import static com.example.kaba.ChoixActivity.nom_linge;
import static com.example.kaba.ChoixActivity.num_poste;

public class EmptyTimeActivity extends AppCompatActivity {

    TextView date_empty;
    Button show_empty_time;
    TextView empty_time;
    ImageButton cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_time);

        date_empty = (TextView) findViewById(R.id.date_empty);
        cal = (ImageButton) findViewById(R.id.calendar);
        show_empty_time = (Button)findViewById(R.id.show_empty_time);
        empty_time = (TextView) findViewById(R.id.empty_time);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EmptyTimeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date;
                        if ((month+1<10) && (dayOfMonth<10)){
                            date= year + "-0" + (month + 1)+ "-0" + dayOfMonth;

                        }else if ((month+1>9) && (dayOfMonth<10)){
                            date= year + "-" + (month + 1)+ "-0" + dayOfMonth;

                        }else if ((month+1<10) && (dayOfMonth>9)){
                            date= year + "-0" + (month + 1)+ "-" + dayOfMonth;

                        }else {
                            date= year + "-" + (month + 1)+ "-" + dayOfMonth;

                        }
                        date_empty.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        show_empty_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {

    String value = date_empty.getText().toString().trim();

    if (value.equals("")) {
        Toast.makeText(this, "Please Enter Data Value", Toast.LENGTH_LONG).show();
        return;
    }

    String url = Config.Empty + "?fk_poste="+ fk_poste +"&fk_kaba="+ fk_kaba+ "&date="+ value;

    StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResponse(String response) {

            showJSON(response);
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(EmptyTimeActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showJSON(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            ArrayList<Integer> val_nombre = new ArrayList<>();
            ArrayList<String> val_time = new ArrayList<>();
            String empty = "";
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                int q = jo.getInt("quantity");
                String t = jo.getString(Config.KEY_time);
                val_nombre.add(q);
                val_time.add(t);
            }
            int k =0;
            for (int i = 0; i < val_nombre.size(); i++) {
                if (val_nombre.get(i) != 0) {
                DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                Date date1 = (Date)formatter.parse(val_time.get(i-(1*k)));
                Date date2 = (Date)formatter.parse(val_time.get(i));
                long millse =Math.abs (date2.getTime() - date1.getTime());
                if(millse != 0) {
                    int Hours = (int) (millse / (1000 * 60 * 60));
                    int Mins = (int) (millse / (1000 * 60)) % 60;
                    long Secs = (int) (millse / 1000) % 60;

                    String diff = Hours + " hours - " + Mins + " minutes - " + Secs + " seconds";
                    empty = empty + "Ligne : "+ nom_linge +'\n'
                            + "Numero du poste :"+num_poste+ '\n'
                            + "Nom de composant :"+nom_composant
                            + '\n'+ "Date : " + date_empty.getText().toString().trim() + '\n'
                            + "Time : " + val_time.get(i) + '\n'
                            + "Empty Time : " + diff + "\n"
                            +"_________________________________" +"\n";}
                empty_time.setText(empty);
                k=0;
                }
                else{
                    k++;
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}