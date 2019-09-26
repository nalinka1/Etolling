package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class InternetFailure extends AppCompatActivity {
    private String MacListString;
    private int check;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_failure);
        ImageView failure = (ImageView)findViewById(R.id.internet);
        extras = getIntent().getExtras();
        check = extras.getInt("check_connection",0);
        MacListString = extras.getString("macAddressListB",null);

        Button tryAgain = (Button)findViewById(R.id.tryButton);
        Button offline = (Button)findViewById(R.id.offline);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackToHome = new Intent(InternetFailure.this,updateData.class);
                goBackToHome.putExtra("check_connection",check);
                goBackToHome.putExtra("macAddressListB",MacListString);
                startActivity(goBackToHome);
                finish();
            }
        });
        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check==1){
//                    Intent goFailure= new Intent(InternetFailure.this,MainActivity.class);
//                    startActivity(goFailure);
//                    finish();
                }
                else {
                    Intent offlineMethod = new Intent(InternetFailure.this,Home.class);
                    offlineMethod.putExtra("macAddressListB",MacListString);
                    startActivity(offlineMethod);
                    finish();
                }

            }
        });
    }
}
