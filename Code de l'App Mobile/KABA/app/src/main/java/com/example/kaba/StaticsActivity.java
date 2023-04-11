package com.example.kaba;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import static com.example.kaba.ChoixActivity.fk_kaba;
import static com.example.kaba.ChoixActivity.fk_poste;


public class StaticsActivity extends AppCompatActivity {

    TextView txtvalue;
    Button showstatistic;
    ImageButton calend;
    LineChart mpLineChart;
    public static final String TAG = "ShowStatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics);

        txtvalue = (TextView) findViewById(R.id.date_j);
        calend = (ImageButton) findViewById(R.id.calend);
        showstatistic = (Button)findViewById(R.id.show_statistic);
        mpLineChart = (LineChart) findViewById(R.id.line_chart);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        calend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        StaticsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        txtvalue.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        showstatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {

        String value =txtvalue.getText().toString().trim();

        if (value.equals("")) {
            Toast.makeText(this, "Please Enter Data Value", Toast.LENGTH_LONG).show();
            return;
        }

        String url = Config.Statistic + "?fk_poste="+ fk_poste +"&fk_kaba="+ fk_kaba+ "&date="+ value;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StaticsActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    int itr =0;
    public void showJSON(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            ArrayList<Entry> yvalues = new ArrayList<>();
            ArrayList<Entry> yvalues1 = new ArrayList<>();
            ArrayList<Entry> yvalues2 = new ArrayList<>();
            final ArrayList<String> tvalues = new ArrayList<>();
            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);
                int q = jo.getInt("quantity");
                String t = jo.getString("time");
                mpLineChart.setDragEnabled(false);
                mpLineChart.setScaleEnabled(true);
                mpLineChart.getXAxis().setTextSize(1f);
                mpLineChart.getAxisLeft().setTextSize(1f);
                yvalues.add(new Entry(itr,q));
                tvalues.add(t);
                yvalues1.add(new Entry(itr,30));
                yvalues2.add(new Entry(itr,6));
                itr++;
            }
            Log.i(TAG,"size = "+ yvalues.size());
            String label ="Variation de nbre de douille";
            LineDataSet set1 = new LineDataSet(yvalues,label);
            String label1 ="Limit max ";
            LineDataSet set2 = new LineDataSet(yvalues1,label1);
            String label3 ="Limit min";
            LineDataSet set3 = new LineDataSet(yvalues2,label3);
            set1.setColor(Color.BLUE);
            set1.setFillAlpha(110);
            set1.setDrawCircles(false);
            set1.setLineWidth(2f);
            set1.setValueTextSize(5f);
            set1.setValueTextColor(Color.GREEN);
            set2.setColor(Color.rgb(255,140,0));
            set2.setDrawCircles(false);
            set2.setLineWidth(2f);
            set3.setColor(Color.RED);
            set3.setDrawCircles(false);
            set3.setLineWidth(2f);
            ArrayList<ILineDataSet> dataSets =new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);
            LineData data2 = new LineData(dataSets);
            mpLineChart.setData(data2);
            XAxis xAxis = mpLineChart.getXAxis();
            mpLineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(tvalues));
            xAxis.setGranularity(1);
            xAxis.setTextSize(10f);
            YAxis yAxis = mpLineChart.getAxisLeft();
            yAxis.setTextSize(10f);
            mpLineChart.getDescription().setText("Variation de la quantité pendant la journée "+txtvalue.getText().toString().trim());
            mpLineChart.getDescription().setTextSize(10f);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}