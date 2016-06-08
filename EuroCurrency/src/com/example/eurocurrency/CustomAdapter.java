package com.example.eurocurrency;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList data;
	private static LayoutInflater inflater = null;
	public Resources res;
	Currency tempValues = null;
	int i = 0;

	public CustomAdapter(Activity a, ArrayList d, Resources resLocal) {

		activity = a;
		data = d;
		res = resLocal;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView currency;
		public TextView rate;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			vi = inflater.inflate(R.layout.simplerow, null);

			holder = new ViewHolder();
			holder.currency = (TextView) vi
					.findViewById(R.id.rowTextViewCurrency);
			holder.rate = (TextView) vi.findViewById(R.id.rowTextViewRate);

			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		if (data.size() <= 0) {
			holder.currency.setText("No Data, check your internet connection");
			holder.rate.setText("");

		} else {

			tempValues = null;
			tempValues = (Currency) data.get(position);

			holder.currency.setText(tempValues.getCurrency());
			holder.rate.setText(tempValues.getRate());

			// vi.setOnClickListener(new OnItemClickListener(position));
		}
		vi.setOnClickListener(new OnItemClickListener(position));
		return vi;
	}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			MainActivity sct = (MainActivity) activity;

			sct.onItemClick(mPosition);
		}
	}

}