package com.example.eurocurrency;

public class Currency {
	private String rate;
	private String currency;

	public Currency(String currency, String rate) {
		this.currency = currency;
		this.rate = rate;
	}

	public String getRate() {
		return rate;
	}

	public String getCurrency() {
		return currency;
	}

}
