package ru.nerv.coin;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class DataExchange {

	private float high;
	private float low;
	private float open;
	private float close;
    private long date;
    private byte type;
    private float rsi;
    
	@JsonIgnoreProperties(ignoreUnknown = true)
	public DataExchange() {	}
      
	public DataExchange(float high, float low, float open, float close, long date, byte type) {
		if (type == 1) {
			this.high = high;
			this.low = low;
			this.open = open;
			this.close = close;
			this.date = date;
			this.type = type;
		}
	}
	
	public DataExchange(float high, float low, float open, float close, long date) {
		this.high = high;
		this.low = low;
		this.open = open;
		this.close = close;
		this.date = date;
		this.type = 0;
	}
		
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public float getHigh() {
		return high;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public float getLow() {
		return low;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public float getClose() {
		return close;
	}

	public void setClose(float close) {
		this.close = close;
	}
	
	public float getweightedAverage() {
		return ((this.high+this.low)/2);
	}
	
	public String toStringAll() {
		return open + " " + close  + " " + low + " " +  high;
	}
	
	public String toStringHL() {
		return low + " " +  high;
	}
	
	public String toStringHLNorm(float high, float low) {
		return this.getDayWeek() + " " + this.getHourDay()  + " " + (this.low - low)/(high - low) + " " +  (this.high - low)/(high - low);
	}
	
	public String toStringAllNorm(float high, float low) {
		return this.getDayWeek() + " " + this.getHourDay() + " " + (this.open - low)/(high - low) + " " +  (this.close - low)/(high - low) + " " + (this.low - low)/(high - low) + " " +  (this.high - low)/(high - low);
	}
	
	public String toStringNorm(float high, float low) {
		return (this.open - low)/(high - low) + " " +  (this.close - low)/(high - low) + " " + (this.low - low)/(high - low) + " " +  (this.high - low)/(high - low);
	}
	
	public float getDayWeek(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(this.date * 1000));
		return (float) (c.get(Calendar.DAY_OF_WEEK) - 1)/(7 - 1);
	}
	
	public float getHourDay(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(this.date * 1000));
		return (float) (c.get(Calendar.HOUR_OF_DAY) - 0)/(24 - 0);
	}
			
	public String toStringOC() {
		return open + " " +  close;
	}
	
    public float getRSI() {
		return rsi;
	}

	public void setRSI(float rsi) {
		this.rsi = rsi;
	}

}
