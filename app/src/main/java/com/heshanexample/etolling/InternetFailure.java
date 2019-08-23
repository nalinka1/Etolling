package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class InternetFailure extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_failure);
        ImageView failure = (ImageView)findViewById(R.id.internet);

        Button tryAgain = (Button)findViewById(R.id.tryButton);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackToHome = new Intent(InternetFailure.this,Home.class);
                startActivity(goBackToHome);
            }
        });
    }
}
