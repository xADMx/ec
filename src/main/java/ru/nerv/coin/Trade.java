package ru.nerv.coin;

public class Trade {

	byte type;
	double sum;
	String id;
	
	public Trade(byte type, double sum, String id) {
		super();
		this.type = type;
		this.sum = sum;
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public double getSum() {
		return sum;
	}

	public String getId() {
		return id;
	}

	 
}
