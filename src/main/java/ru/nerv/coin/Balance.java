package ru.nerv.coin;

public class Balance {
	private String name;
	private double sum;
	
	public Balance(String name, double sum) {
		this.name = name;
		this.sum = sum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}
	
}
