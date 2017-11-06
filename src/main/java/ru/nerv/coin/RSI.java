package ru.nerv.coin;

public class RSI {
	protected long date;
	protected float result;
	
	public RSI(long date, float result) {
		super();
		this.date = date;
		this.result = result;
	}
	
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public float getResult() {
		return result;
	}
	public void setResult(float result) {
		this.result = result;
	}
	
}
