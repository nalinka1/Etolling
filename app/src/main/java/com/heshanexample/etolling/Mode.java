package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Mode extends AppCompatActivity {

    private int check;
    private int mode_id;
    RadioGroup modes;
    RadioButton mode;
    Button Select;
    TextView showSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        modes = (RadioGroup) findViewById(R.id.radioGroup);
        showSelection = (TextView)findViewById(R.id.Selected_mode);

        Select =(Button)findViewById(R.id.select_button);
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = modes.getCheckedRadioButtonId();
                mode = findViewById(state);
                String modeText= mode.getText().toString();
                if(modeText.equalsIgnoreCase("Highway")){
                    mode_id=1;

                }
                else if(modeText.equalsIgnoreCase("Parking")){
                    mode_id=2;
                }
                SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                SharedPreferences.Editor edit = storeInput.edit();
                edit.putInt("mode",mode_id);
                edit.commit();

                Intent highway = new Intent(Mode.this,updateData.class);
                highway.putExtra("check_connection",1);
                highway.putExtra("mode",mode_id);
                startActivity(highway);
                finish();

            }
        });


    }
    protected void checkButton(View v){
        int state = modes.getCheckedRadioButtonId();
        mode = (RadioButton) findViewById(state);
        showSelection.setText("Selected Mode : "+mode.getText());
    }
}
