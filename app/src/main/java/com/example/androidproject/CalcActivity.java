package com.example.androidproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class CalcActivity extends AppCompatActivity {
    private final int maxDigit = 10;
    private TextView tvResult, tvExpression;
    private double memory = 0;
    private boolean needClear, isErrorDisplayed;
    private String zeroDigit, dotSymbol, minusSymbol;
    private String currentOperator = "";
    private double firstOperand = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        tvResult = findViewById(R.id.calc_tv_result);
        tvExpression = findViewById(R.id.calc_tv_expression);
        zeroDigit = getString(R.string.calc_btn_0);
        dotSymbol = getString(R.string.calc_btn_dot);
        minusSymbol = getString(R.string.calc_btn_sub);

        setButtonListeners();

        if (savedInstanceState != null) {
            firstOperand = savedInstanceState.getDouble("firstOperand", Double.NaN);
            currentOperator = savedInstanceState.getString("currentOperator", "");
            tvResult.setText(savedInstanceState.getString("tvResult", zeroDigit));
            tvExpression.setText(savedInstanceState.getString("tvExpression", ""));
        } else {
            onClearClick(null);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("firstOperand", firstOperand);
        outState.putString("currentOperator", currentOperator);
        outState.putString("tvResult", tvResult.getText().toString());
        outState.putString("tvExpression", tvExpression.getText().toString());
    }

    private void setButtonListeners() {
        int[] digitButtons = {R.id.calc_btn_0, R.id.calc_btn_1, R.id.calc_btn_2, R.id.calc_btn_3,
                R.id.calc_btn_4, R.id.calc_btn_5, R.id.calc_btn_6, R.id.calc_btn_7,
                R.id.calc_btn_8, R.id.calc_btn_9};
        for (int id : digitButtons) findViewById(id).setOnClickListener(this::onDigitClick);

        findViewById(R.id.calc_btn_c).setOnClickListener(this::onClearClick);
        findViewById(R.id.calc_btn_dot).setOnClickListener(this::onDotClick);
        findViewById(R.id.calc_btn_pm).setOnClickListener(this::onPMClick);
        findViewById(R.id.calc_btn_backspace).setOnClickListener(this::onBackspaceClick);
        findViewById(R.id.calc_btn_inv).setOnClickListener(this::onInverseClick);
        findViewById(R.id.calc_btn_sqr).setOnClickListener(this::onSquareClick);
        findViewById(R.id.calc_btn_sqrt).setOnClickListener(this::onSqrtClick);
        findViewById(R.id.calc_btn_percent).setOnClickListener(this::onPercentClick);

        int[] operators = {R.id.calc_btn_add, R.id.calc_btn_sub, R.id.calc_btn_mult, R.id.calc_btn_div};
        for (int id : operators) findViewById(id).setOnClickListener(this::onOperatorClick);

        findViewById(R.id.calc_btn_equals).setOnClickListener(this::onEqualsClick);
    }

    private void onDigitClick(View view) {
        String resText = tvResult.getText().toString();
        if (needClear || resText.equals(zeroDigit)) resText = "";
        if (resText.length() < maxDigit) resText += ((Button) view).getText();
        tvResult.setText(resText);
        needClear = false;
    }

    private void onClearClick(View view) {
        tvResult.setText(zeroDigit);
        tvExpression.setText("");
        firstOperand = Double.NaN;
        currentOperator = "";
    }

    private void onDotClick(View view) {
        if (!tvResult.getText().toString().contains(dotSymbol))
            tvResult.append(dotSymbol);
    }

    private void onPMClick(View view) {
        String resText = tvResult.getText().toString();
        tvResult.setText(resText.startsWith(minusSymbol) ? resText.substring(1) : minusSymbol + resText);
    }

    private void onBackspaceClick(View view) {
        String resText = tvResult.getText().toString();
        tvResult.setText(resText.length() > 1 ? resText.substring(0, resText.length() - 1) : zeroDigit);
    }

    private void onOperatorClick(View view) {
        if (!Double.isNaN(firstOperand)) evaluate();
        firstOperand = parseResult(tvResult.getText().toString());
        currentOperator = ((Button) view).getText().toString();
        tvExpression.setText(tvResult.getText().toString() + " " + currentOperator);
        needClear = true;
    }

    private void onEqualsClick(View view) {
        if (!Double.isNaN(firstOperand)) evaluate();
    }

    private void evaluate() {
        double secondOperand = parseResult(tvResult.getText().toString());
        double result = 0;
        switch (currentOperator) {
            case "+": result = firstOperand + secondOperand; break;
            case "-": result = firstOperand - secondOperand; break;
            case "*": result = firstOperand * secondOperand; break;
            case "÷": result = secondOperand != 0 ? firstOperand / secondOperand : Double.NaN; break;
        }
        tvResult.setText(Double.isNaN(result) ? getString(R.string.calc_err_div_zero) : formatResult(result));
        tvExpression.setText("");
        firstOperand = Double.NaN;
        needClear = true;
    }

    private void onInverseClick(View view) {
        double x = parseResult(tvResult.getText().toString());
        tvResult.setText(x == 0 ? getString(R.string.calc_err_div_zero) : formatResult(1.0 / x));
        needClear = true;
    }

    private void onSquareClick(View view) {
        double x = parseResult(tvResult.getText().toString());
        tvResult.setText(formatResult(x * x));
        needClear = true;
    }

    private void onSqrtClick(View view) {
        double x = parseResult(tvResult.getText().toString());
        tvResult.setText(x < 0 ? getString(R.string.calc_err_div_zero) : formatResult(Math.sqrt(x)));
        needClear = true;
    }

    private void onPercentClick(View view) {
        double x = parseResult(tvResult.getText().toString());
        tvResult.setText(formatResult(x / 100));
        needClear = true;
    }

    private double parseResult(String resText) {
        return Double.parseDouble(resText.replace(dotSymbol, ".").replace(minusSymbol, "-").replace(zeroDigit, "0"));
    }

    private String formatResult(double x) {
        return new DecimalFormat("0.######").format(x).replace(".", dotSymbol);
    }
}
