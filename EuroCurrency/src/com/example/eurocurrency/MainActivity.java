package com.example.eurocurrency;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final String LOG_TAG = "myLogs";
	public final String URL_STRING = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
	private String time = "";
	private String currency = "";
	private String rate = "";
	private ArrayList<Currency> currencyList;
	private TextView timeTxtView, selectedRate, currencuResultValue;
	private EditText euroValue;
	private ListView list;
	private CustomAdapter adapter;
	public MainActivity customListView = null;
	private double curRate = 0.0;
	private DecimalFormat percentageFormat = new DecimalFormat("00.00");
	private LinearLayout calculateButton;
	private String currencuResultForOneEur;
	private int requestCode = 100;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate(Bundle savedInstanceState) {

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		// if (android.os.Build.VERSION.SDK_INT > 9) {
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectNetwork().penaltyDialog().build());
		// }
		selectedRate = (TextView) findViewById(R.id.selectedCur);
		currencuResultValue = (TextView) findViewById(R.id.calculatedValue);
		euroValue = (EditText) findViewById(R.id.euroValue);

		customListView = this;
		Resources res = getResources();
		list = (ListView) findViewById(R.id.mainListView);
		timeTxtView = (TextView) findViewById(R.id.timeTxtView);

		currencyList = new ArrayList<Currency>();

		String tmp = "";
		calculateButton = (LinearLayout) findViewById(R.id.calculatorButton);

		try {
			XmlPullParser xpp = prepareXpp();

			while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
				switch (xpp.getEventType()) {

				case XmlPullParser.START_TAG:
					Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName()
							+ ", depth = " + xpp.getDepth() + ", attrCount = "
							+ xpp.getAttributeCount());
					tmp = "";
					rate = "";
					currency = "";
					for (int i = 0; i < xpp.getAttributeCount(); i++) {
						if (xpp.getAttributeName(i).equals("time")) {
							time = xpp.getAttributeValue(i);
						}
						if (xpp.getAttributeName(i).equals("currency")) {
							currency = xpp.getAttributeValue(i);
						}
						if (xpp.getAttributeName(i).equals("rate")) {
							rate = xpp.getAttributeValue(i);
						}

						tmp = tmp + xpp.getAttributeName(i) + " atribute= "
								+ xpp.getAttributeValue(i) + ", ";
					}
					if (!TextUtils.isEmpty(rate)
							&& !TextUtils.isEmpty(currency)) {
						currencyList.add(new Currency(currency, rate));
					}

					if (!TextUtils.isEmpty(tmp))
						Log.d(LOG_TAG, "Attributes: " + tmp);
					break;

				default:
					break;
				}
				xpp.next();
			}

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		adapter = new CustomAdapter(customListView, currencyList, res);
		list.setAdapter(adapter);
		timeTxtView.setText(time);

		euroValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(euroValue.getText().toString())) {
					selectedRate.setText("Currency");
					currencuResultValue.setText("0.0");

				} else if (!selectedRate.getText().equals("Currency")) {
					double euro = 0.0;
					euro = Double.parseDouble(euroValue.getText().toString()
							.trim());
					currencuResultValue.setText(percentageFormat.format(euro
							* curRate));

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		calculateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!selectedRate.getText().equals("Currency")) {
					Intent i = new Intent(getApplicationContext(),
							Calculator.class);
					i.putExtra("EuroValue", euroValue.getText().toString());
					i.putExtra("Currencu", selectedRate.getText());
					i.putExtra("CurrencuValue", currencuResultValue.getText()
							.toString());
					i.putExtra("CurrencuResultForOneEur",
							currencuResultForOneEur);
					startActivityForResult(i, requestCode);

				}
			}
		});

	}

	XmlPullParser prepareXpp() throws XmlPullParserException, IOException {

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

		factory.setNamespaceAware(true);

		XmlPullParser xpp = factory.newPullParser();
		final URL sourceUrl = new URL(URL_STRING);

		xpp.setInput(new InputStreamReader(sourceUrl.openStream()));
		return xpp;
	}

	public void onItemClick(int mPosition) {
		Currency tempValues = (Currency) currencyList.get(mPosition);
		selectedRate = (TextView) findViewById(R.id.selectedCur);
		currencuResultValue = (TextView) findViewById(R.id.calculatedValue);
		euroValue = (EditText) findViewById(R.id.euroValue);
		if (!TextUtils.isEmpty(euroValue.getText().toString())) {
			double euro = 0.0;
			double curValue = 0.0;
			euro = Double.parseDouble(euroValue.getText().toString().trim());
			curValue = Double.parseDouble(tempValues.getRate().trim());
			currencuResultForOneEur = String.valueOf(curValue);
			selectedRate.setText(tempValues.getCurrency());
			currencuResultValue.setText(percentageFormat
					.format(euro * curValue));
			curRate = curValue;
		} else {
			Toast.makeText(customListView, "Put on value to the field",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == this.requestCode) {
			if (resultCode == RESULT_OK) {
				String message = data.getStringExtra("euroValue");
				euroValue.setText(message);
				message = data.getStringExtra("currencyValue");
				currencuResultValue.setText(message);

			}
		}

	}

}
