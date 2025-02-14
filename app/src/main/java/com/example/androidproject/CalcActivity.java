package com.example.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CalcActivity extends AppCompatActivity {
    private final int maxDigit = 10;

    private TextView tvResult;
    private TextView tvExpression;
    private String zeroDigit;
    private String dotSymbol;
    private String minusSymbol;
    private boolean needClear;
    private boolean isErrorDisplayed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        findViewById(R.id.calc_btn_0).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_1).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_2).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_3).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_4).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_5).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_6).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_7).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_8).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_9).setOnClickListener((this::onDigitClick));
        findViewById(R.id.calc_btn_c).setOnClickListener((this::onClearClick));
        findViewById(R.id.calc_btn_dot).setOnClickListener((this::onDotClick));
        findViewById(R.id.calc_btn_pm).setOnClickListener((this::onPMClick));
        findViewById(R.id.calc_btn_backspace).setOnClickListener((this::onBackspaceClick));
        findViewById(R.id.calc_btn_inv).setOnClickListener((this::onInverseClick));
        tvResult = findViewById(R.id.calc_tv_result);
        tvExpression = findViewById(R.id.calc_tv_expression);
        onClearClick(null);
        zeroDigit = getString(R.string.calc_btn_0);
        dotSymbol = getString(R.string.calc_btn_dot);
        minusSymbol = getString(R.string.calc_btn_sub);
    }

    @Override
    protected  void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putCharSequence("tvResult", tvResult.getText());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        tvResult.setText(savedInstanceState.getCharSequence("tvResult"));
    }


    private void onClearClick(View view){
        tvResult.setText(zeroDigit);
        tvExpression.setText("");
    }

    private void onDotClick(View view){
        String resText = tvResult.getText().toString();
        if(resText.contains(dotSymbol)){
            return;
        }
        resText += dotSymbol;
        tvResult.setText(resText);
    }

    private void onInverseClick(View view){
        String resText = tvResult.getText().toString();
        tvExpression.setText(getString(R.string.calc_inv_tpl, resText));
        double x = parseResult(resText);
        if(x == 0){
            resText = getString(R.string.calc_err_div_zero);
        }
        else{
            resText = toResult(1.0 /x);
        }
        resText = toResult(1.0 / x);
        tvResult.setText(resText);
        needClear = true;
    }

    private void onPMClick(View view){
        String resText = tvResult.getText().toString();
        if(resText.startsWith(minusSymbol)){
            resText = resText.substring(1);
        }
        else if(!resText.equals(zeroDigit)){
            resText = minusSymbol + resText;
        }
        tvResult.setText(resText);
    }

    private void onBackspaceClick(View view){
        String resText = tvResult.getText().toString();
        int len = resText.length();
        if(len <= 1){
            resText = zeroDigit;
        }
        else{
            resText = resText.substring(0, len - 1);
            if(resText.equals(minusSymbol)){
                resText = zeroDigit;
            }
        }
        tvResult.setText((resText));

    }

    private void onDigitClick(View view){
        String resText = tvResult.getText().toString();
        if(resText.equals(zeroDigit)){
            resText = "";
        }
        if(digitlength(resText) < maxDigit){
            resText += ((Button) view).getText();
        }
        tvResult.setText(resText);
    }

    private int digitlength(String resText){
        int len = resText.length();
        if(needClear || isErrorDisplayed){
            resText = "";
            tvExpression.setText("");
        }
        needClear = false;
        isErrorDisplayed = false;
        if(resText.contains(dotSymbol)){
            len-=1;
        }
        if(resText.contains(minusSymbol)){
            len-=1;
        }
        return len;
    }

    private String toResult(double x){
        String res = String.valueOf(x).replace(".", dotSymbol).replace("-", minusSymbol).replace("0", zeroDigit);
        if(digitlength(res) > maxDigit){
            res = res.substring(0, maxDigit);
        }
        return res;
    }

    private double parseResult(String resText){
        return Double.parseDouble(resText.replace(dotSymbol,".").replace(minusSymbol, "-").replace(zeroDigit, "0"));
    }
}