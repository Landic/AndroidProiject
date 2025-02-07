package com.example.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;

    private TextView tvCounter;
    private Button btnMinus, btnPlus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //    return insets;
        //});
        tv1 = findViewById(R.id.textView1);
        findViewById(R.id.button1).setOnClickListener(this::onButton1Click);
        tvCounter = findViewById(R.id.tvCounter);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCounter(-1);
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCounter(1);
            }
        });
    }

    private void updateCounter(int delta) {
        String currentText = tvCounter.getText().toString();
        try {
            int value = Integer.parseInt(currentText);
            value += delta;
            tvCounter.setText(String.valueOf(value));
        } catch (NumberFormatException e) {
            tvCounter.setText("10");
        }
    }

    private void onButton1Click(View view) {

        String gText = tv1.getText().toString();
        gText += "!!";
        tv1.setText(gText);
    }
}