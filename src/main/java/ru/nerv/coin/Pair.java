package ru.nerv.coin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import okhttp3.OkHttpClient;

public abstract class Pair {
	protected String name;
	protected int metka;
	protected boolean EOF;
	protected float high;
	protected float low;

	List<RSI> rsi = new ArrayList<RSI>();
	List<Trade> trade = new ArrayList<Trade>();
	List<DataExchange> dataExchange = new ArrayList<DataExchange>();
	int queryPeriod;
	
	protected final OkHttpClient client = new OkHttpClient();
	
	public Pair(String name, int queryPeriod) {
		super();
		this.metka = 0;
		this.name = name;
		this.queryPeriod = queryPeriod;
		this.EOF = false;
	}	
	
	public boolean isEOF() {
		return EOF;
	}
	
	public void update(long startTime, long endTime){
		this.setFirstDataExchange();
		this.updateDataExchange(startTime, endTime);
		this.updateTrade();
	};
	
	protected abstract void updateTrade();
	protected abstract void updateDataExchange(long startTime, long endTime);

	public List<Trade> getTrade() {
		return trade;
	}
	
	public void setFirstDataExchange() {
		this.EOF = false;
		this.metka = 0;
	}
	
	public long getSizeDataExchange() {
		return dataExchange.size();
	}
	
	public void CreateWorkSheet(Workbook book){

        Sheet sheet = book.createSheet(this.name);
        Row row = sheet.createRow(0);
        Cell date = row.createCell(0);
        Cell high = row.createCell(1);
        Cell low = row.createCell(2);
        Cell open = row.createCell(3);
        Cell close = row.createCell(4);
        Cell type = row.createCell(5);
        Cell rsi = row.createCell(6);
        
        date.setCellValue("Дата");
        high.setCellValue("Максимум");
        low.setCellValue("Минимун");
        open.setCellValue("Открытие");
        close.setCellValue("Закрытие");
        type.setCellValue("Тип (прогноз=1)");
        rsi.setCellValue("RSI");
        
        for (int i = 0; i < dataExchange.size(); i++){
        	DataExchange tempDataExchange = dataExchange.get(i);
        	SimpleDateFormat formatter;

        	formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        	
	        row = sheet.createRow(i+1);
	        date = row.createCell(0);
	        high = row.createCell(1);
	        low = row.createCell(2);
	        open = row.createCell(3);
	        close = row.createCell(4);
	        type = row.createCell(5);
	        rsi = row.createCell(6);
	        		
	        //DataFormat format = book.createDataFormat();
	        //CellStyle dateStyle = book.createCellStyle();
	        //dateStyle.setDataFormat(format.getFormat("dd/mm/yy HH:nn;@"));
	        //Date.setCellStyle(dateStyle);
	        date.setCellValue(formatter.format(tempDataExchange.getDate()*1000));
	        
	        high.setCellValue(tempDataExchange.getHigh());
	        low.setCellValue(tempDataExchange.getLow());
	        open.setCellValue(tempDataExchange.getOpen());
	        close.setCellValue(tempDataExchange.getClose());
	        type.setCellValue(tempDataExchange.getType());
	        rsi.setCellValue(tempDataExchange.getRSI());
        }
	}
	
	public String getNextDataExchangeModToString(long date) {
		if (dataExchange.get(0).getDate() > date) { return "0 0"; } 	
		while (!this.EOF) {
			DataExchange tempDataExchange = dataExchange.get(this.metka);
			if (tempDataExchange.getDate() == date) { return tempDataExchange.toStringHLNorm(this.high, this.low); } 	
			this.next();	
		}
		return "0 0";
	}
	
	public String getNextDataExchangeAllNormToString(long date) {
		if (dataExchange.get(0).getDate() > date) { return "0 0 0 0"; } 
		while (!this.EOF) { 
			DataExchange tempDataExchange = dataExchange.get(this.metka);
			if (tempDataExchange.getDate() == date) { return tempDataExchange.toStringAllNorm(this.high, this.low); } 
			this.next();
		}
		return "0 0 0 0";
	}
	
	private void next(){
		this.metka++;
		this.EOF = (dataExchange.size() <= this.metka) ? true : false ;
	}
	
	public String getNextDataExchangeNormToString(long date) {
		if (dataExchange.get(0).getDate() > date) { return "0 0 0 0"; } 	
		while (!this.EOF) {
			DataExchange tempDataExchange = dataExchange.get(this.metka);
			if (tempDataExchange.getDate() == date) { return tempDataExchange.toStringNorm(this.high, this.low); } 	
			this.next();
		}
		return "0 0 0 0";
	}
	
	public long getFirstDate() {
		return (dataExchange.size() > 0) ? dataExchange.get(0).getDate() : null ;
	}
	
	public double[] getDataExchangeCloseToDouble() {
		double[] tempDataExchange = new double[dataExchange.size()];
	
		for (int i = 0; i < dataExchange.size(); i++ ){
			tempDataExchange[i] = dataExchange.get(i).getClose();
		}
				
		return tempDataExchange;
	}

	public List<DataExchange> getDataExchange() {
		return dataExchange;
	}
	
	public DataExchange getLastDataExchangeNorm() {
		DataExchange temp = dataExchange.get(this.dataExchange.size()-1);
		return new DataExchange(Normal(temp.getHigh()), Normal(temp.getLow()), Normal(temp.getOpen()), Normal(temp.getClose()), temp.getDate());
	}
	
	public DataExchange getFindDataExchangeNorm(long date) {
		for (DataExchange tempDataExchange : dataExchange) {
			if (date == tempDataExchange.getDate()) {
				return new DataExchange(Normal(tempDataExchange.getHigh()), Normal(tempDataExchange.getLow()), Normal(tempDataExchange.getOpen()), Normal(tempDataExchange.getClose()), tempDataExchange.getDate());
			}
		}
		
		return null;
	}
	
	public DataExchange getNextDataExchangeNorm(long date) {
		
		if (dataExchange.get(0).getDate() > date) { return null; } 	
		while (!this.EOF) {
			DataExchange tempDataExchange = dataExchange.get(this.metka);
			if (tempDataExchange.getDate() == date) { return new DataExchange(Normal(tempDataExchange.getHigh()), Normal(tempDataExchange.getLow()), Normal(tempDataExchange.getOpen()), Normal(tempDataExchange.getClose()), tempDataExchange.getDate()); } 	
			this.next();
		}
		
		return null;
	}
	
	public void addDataExchange(float open, float close, float low, float high, long date, byte type) {
		dataExchange.add(new DataExchange( DeNormal(high), DeNormal(low), DeNormal(open), DeNormal(close), date, type));
	}
	
	private float DeNormal(float num) {
		return this.low + num * (this.high - this.low);
	}
	
	private float Normal(float num) {
		return (num - this.low)/(this.high - this.low);
	}

	public String getName() {
		return name;
	}

	public void updateRSI(){
		DataExchange tempDataExchange;
		float averageClose;
		float sumH = 0;
		float sumL = 0;
		float rs = 0;
		tempDataExchange = this.dataExchange.get(0);
		averageClose = tempDataExchange.getClose();
		tempDataExchange.setRSI(0);
		
		for (int i=1; i < this.dataExchange.size(); i++) {
			tempDataExchange = this.dataExchange.get(i);
			
			if (i < 16) {
				if (tempDataExchange.getClose() - averageClose > 0) {
					sumH += tempDataExchange.getClose() - averageClose;
				} else {
					sumL += (tempDataExchange.getClose() - averageClose) * -1;
				}
				
				if (i == 15){
					sumH = sumH / i;
					sumL = sumL / i;
					
					rs = sumH / sumL;
					tempDataExchange.setRSI((rs == 0) ? 100 : (float) 100 - (100 / (1 + rs)));
				} else {
					tempDataExchange.setRSI(0);
				}
				
			} else if (i > 15) {
				if (tempDataExchange.getClose() - averageClose > 0) {
					sumH = ((sumH * 13) + tempDataExchange.getClose() - averageClose) / 14;
					sumL = (sumL * 13) / 14;
				} else {
					sumH = (sumH * 13) / 14;
					sumL = ((sumL * 13) + (tempDataExchange.getClose() - averageClose) * -1) / 14;
				}
				
				rs = (float) sumH / sumL;
				
				tempDataExchange.setRSI((rs == 0) ? 100 : (float) 100 - (100 / (1 + rs)));
			}
		
		averageClose = tempDataExchange.getClose();	
		}
	}
}
