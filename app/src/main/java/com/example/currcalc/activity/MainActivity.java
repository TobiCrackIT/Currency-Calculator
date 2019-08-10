package com.example.currcalc.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.currcalc.R;
import com.example.currcalc.model.API;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //Import necessary views into main activity
    private Spinner spinner1,spinner2;
    private TextView origin_currency_spinner_textview, destination_currency_spinner_textview, old_currency_textview, new_currency_TV;
    private EditText old_currency_edit_text,new_currency_edit_text;
    private Button convertButton;


    //Declared as global variable to allow inner classes have access
    JSONObject currencies;
    String[] items={};
    double oldExchange,newExchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        old_currency_edit_text=findViewById(R.id.old_currency_edittext);
        new_currency_edit_text=findViewById(R.id.new_currency_edittext);
        origin_currency_spinner_textview =findViewById(R.id.origin_curr_TV);
        destination_currency_spinner_textview =findViewById(R.id.dest_curr_spinner_textview);
        old_currency_textview =findViewById(R.id.old_currency_textview);
        new_currency_TV =findViewById(R.id.new_currency_textview);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        convertButton=findViewById(R.id.convert_button);


        //Retrofit
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        API api=retrofit.create(API.class);

        final Call<Object> call=api.getCurr();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                //initialize JSONObject to null-  global best practice
                JSONObject jsonObject = null;
                currencies=null;



                //LOG MESSAGES
                //Aids development and debugging process
                try {
                    //parse Object response into JSONObject
                    jsonObject=new JSONObject(response.body().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    currencies=jsonObject.getJSONObject("rates");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("CURR",response.body().toString());
                try{
                    Log.d("SUCCESS",jsonObject.getString("success"));
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Log.d("BASE",jsonObject.getString("base"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("ERROR",t.getMessage());
            }
        });




        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    oldExchange=currencies.getDouble(old_currency_textview.getText().toString());
                    newExchange=currencies.getDouble(new_currency_TV.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Perform calculation to get exchange value
                double newValue=Double.parseDouble(old_currency_edit_text.getText().toString())*newExchange/oldExchange;

                //Format result to 2 decimal places which is the accepted currency format
                String result = String.format("%.2f", newValue);

                new_currency_edit_text.setText(result);
            }
        });




        items= new String[] { "AED","AFN","ALL","AUD","CAD","EUR","GBP","GHS","JPY","LAK","NGN","PHP","PLN","QAR","SAR","USD" ,"YER"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                origin_currency_spinner_textview.setText((String) parent.getItemAtPosition(position));
                old_currency_textview.setText((String) parent.getItemAtPosition(position));
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                destination_currency_spinner_textview.setText((String) parent.getItemAtPosition(position));
                new_currency_TV.setText((String) parent.getItemAtPosition(position));
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



    }


}
