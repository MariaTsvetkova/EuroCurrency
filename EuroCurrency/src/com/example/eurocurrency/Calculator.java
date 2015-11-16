package com.example.eurocurrency;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Calculator extends Activity implements OnClickListener {
	private TextView etNum1;
	private TextView etNum2;
	private Button btnAdd;
	private Button btnSub;
	private Button btnMult;
	private Button btnDiv;
	private TextView currencuRate;
	private String euroValue = "";
	private String selectedRate;
	private String currencuResultValue;
	private String currencuResultForOneEur;
	private DecimalFormat percentageFormat = new DecimalFormat("00.00");
	private Button one, two, three, four, five, six, seven, eight, nine, zero;
	private boolean flag_text = false;
	private boolean flag_p = false;
	private boolean dclick;
	private boolean flag_rez;
	private String rez = "0";
	private char sign;
	private Button btnDecimalPoint;
	private Button btnEquals;
	private Button btnClear;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);

		initButtons();

		Bundle extras = getIntent().getExtras();
		euroValue = extras.getString("EuroValue");
		selectedRate = extras.getString("Currencu");
		currencuResultValue = extras.getString("CurrencuValue");
		currencuResultForOneEur = extras.getString("CurrencuResultForOneEur");

		etNum1.setText(euroValue);
		currencuRate.setText(selectedRate);

		etNum2.setText(currencuResultValue);

	}

	public void getResult() {
		// if (android.text.TextUtils.isDigitsOnly(etNum1.getText())) {
		try {
			double euro = Double.parseDouble(etNum1.getText().toString());
			double curValue = Double.parseDouble(currencuResultForOneEur);
			etNum2.setText(percentageFormat.format(euro * curValue));
		} catch (Exception e) {

		}
		// }
	}

	public void initButtons() {
		etNum1 = (TextView) findViewById(R.id.etNumEur);
		etNum2 = (TextView) findViewById(R.id.etNumCur);
		btnAdd = (Button) findViewById(R.id.buttonAdd);
		btnSub = (Button) findViewById(R.id.buttonSubtract);
		btnMult = (Button) findViewById(R.id.buttonMultiply);
		btnDiv = (Button) findViewById(R.id.buttonDivide);
		btnDecimalPoint = (Button) findViewById(R.id.buttonDecimalPoint);
		btnEquals = (Button) findViewById(R.id.buttonEquals);
		btnClear = (Button) findViewById(R.id.buttonClear);

		one = (Button) findViewById(R.id.button1);
		two = (Button) findViewById(R.id.button2);
		three = (Button) findViewById(R.id.button3);
		four = (Button) findViewById(R.id.button4);
		five = (Button) findViewById(R.id.button5);
		six = (Button) findViewById(R.id.button6);
		seven = (Button) findViewById(R.id.button7);
		eight = (Button) findViewById(R.id.button8);
		nine = (Button) findViewById(R.id.button9);
		zero = (Button) findViewById(R.id.button0);

		btnAdd.setOnClickListener(this);
		btnSub.setOnClickListener(this);
		btnMult.setOnClickListener(this);
		btnDiv.setOnClickListener(this);
		btnDecimalPoint.setOnClickListener(this);
		btnEquals.setOnClickListener(this);
		btnClear.setOnClickListener(this);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		six.setOnClickListener(this);
		seven.setOnClickListener(this);
		eight.setOnClickListener(this);
		nine.setOnClickListener(this);
		zero.setOnClickListener(this);

		currencuRate = (TextView) findViewById(R.id.currencuRate);
	}

	@Override
	public void onClick(View v) {
		Button btn = (Button) v;
		char chOperator = btn.getText().charAt(0);

		if (Character.isDigit(chOperator)) {
			if (flag_text == false) {
				etNum1.setText(String.valueOf(btn.getText()));
				if (chOperator != '0') {
					flag_text = true;
				}

			} else {
				if (etNum1.getText().length() < 15) {
					etNum1.setText(etNum1.getText()
							+ String.valueOf(btn.getText()));
				}

			}
			dclick = false;
		}

		else if (chOperator == '.' && flag_p == false
				&& etNum1.getText().length() < 14) {

			if (etNum1.getText().toString().contains(".")) {
				return;
			}

			etNum1.setText(etNum1.getText() + String.valueOf(btn.getText()));

			flag_text = true;
			flag_p = true;
			dclick = false;
		}

		else if (chOperator == 'C') {
			etNum1.setText("0");
			flag_text = false;
			flag_p = false;
			flag_rez = false;

			rez = "";
			dclick = false;
		}

		else if (btn.getText() == "<-") {

			String temp = (String) etNum1.getText();
			if (temp.length() > 1) {
				if (etNum1.getText().charAt(etNum1.getText().length() - 1) == '.')
					flag_p = false;
				etNum1.setText(temp.substring(0, temp.length() - 1));
			} else
				etNum1.setText("0");

		}

		else if (chOperator == '+' || chOperator == '-' || chOperator == '='
				|| chOperator == '*' || chOperator == '/') {

			if ((dclick == true && chOperator != '=')
					|| (chOperator == '=' && sign != '=' && dclick == true)) {
				rez = (String) etNum1.getText();
				etNum1.setText(rez);
			} else {

				dclick = true;
				if (flag_rez == false)
					flag_rez = true;
				else
					flag_rez = false;

				if (flag_rez == false) {
					if (sign == '/' && etNum1.getText().equals("0")) {
						etNum1.setText("ERR!");
						sign = ' ';
						flag_text = false;
						flag_p = false;
						flag_rez = false;
					} else {

						etNum1.setText(getRez(rez, sign,
								(String) etNum1.getText()));

						sign = ' ';
						flag_text = false;
						flag_p = false;
						flag_rez = false;
						rez = "0";
						if (chOperator != '=') {
							rez = (String) etNum1.getText();

							sign = chOperator;
							flag_rez = true;
						} else {
							dclick = false;
						}

					}
				} else if (flag_rez == true && chOperator != '=') {
					rez = (String) etNum1.getText();

					sign = chOperator;
					flag_rez = true;
				} else if (flag_rez == true) {
					etNum1.setText(rez);
					flag_text = false;
					flag_p = false;
					flag_rez = true;
					rez = "";
				}

				flag_text = false;
				flag_p = false;

			}
		}

		getResult();

	}

	public String getRez(String ar, char ch, String br) {

		double a = 0;
		double b = 0;

		try {

			a = Double.parseDouble(ar);
			b = Double.parseDouble(br);
			switch (ch) {

			case '/':
				rez = String.valueOf(a / b);
				break;
			case '*':
				rez = String.valueOf(a * b);
				break;
			case '+':
				rez = String.valueOf(a + b);
				break;
			case '-':
				rez = String.valueOf(a - b);
				break;
			}

		} catch (Exception e) {
			rez = (String) etNum1.getText();
			flag_text = false;
			flag_p = false;
			flag_rez = false;

		}

		return rez;
	}

	@Override
	public void onBackPressed() {

		String currencyValue = "";
		String euroValue = "";

		try {
			if (flag_rez) {
				if (etNum1.getText().toString().equals("ERR!")) {
					euroValue = "0";
				} else {
					euroValue = rez;
				}
			} else {
				if (etNum1.getText().toString().equals("ERR!")) {
					euroValue = "0";
				} else {
					euroValue = etNum1.getText().toString();
				}
			}
			double euro = Double.parseDouble(euroValue);
			double curValue = Double.parseDouble(currencuResultForOneEur);
			currencyValue = percentageFormat.format(euro * curValue);
		} catch (Exception e) {

		}

		Intent intent = new Intent();
		intent.putExtra("euroValue", euroValue);
		intent.putExtra("currencyValue", currencyValue);
		setResult(Activity.RESULT_OK, intent);
		super.onBackPressed();

	}

}
