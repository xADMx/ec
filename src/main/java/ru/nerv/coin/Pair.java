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

	List<Trade> trade = new ArrayList<Trade>();
	List<DataExchange> dataExchange = new ArrayList<DataExchange>();
	int queryPeriod;
	
	protected final OkHttpClient client = new OkHttpClient();
	
	public Pair(String name, int queryPeriod) {
		super();
		this.metka = -1;
		this.name = name;
		this.queryPeriod = queryPeriod;
		this.EOF = false;
	}	
	
	public boolean isEOF() {
		return EOF;
	}
	
	public void update(){
		this.setFirstDataExchange();
		this.updateDataExchange(0);
		this.updateTrade();
	};
	
	protected abstract void updateTrade();
	protected abstract void updateDataExchange(int time);

	public List<Trade> getTrade() {
		return trade;
	}
	
	public void setFirstDataExchange() {
		this.EOF = false;
		this.metka = -1;
	}
	
	public long getSizeDataExchange() {
		return dataExchange.size();
	}
	
	public void CreateWorkSheet(Workbook book){

        Sheet sheet = book.createSheet(this.name);
        Row row = sheet.createRow(0);
        Cell Date = row.createCell(0);
        Cell High = row.createCell(1);
        Cell Low = row.createCell(2);
        Cell Open = row.createCell(3);
        Cell Close = row.createCell(4);
        Cell Type = row.createCell(5);
        
        Date.setCellValue("Дата");
        High.setCellValue("Максимум");
        Low.setCellValue("Минимун");
        Open.setCellValue("Открытие");
        Close.setCellValue("Закрытие");
        Type.setCellValue("Тип (прогноз=1)");
        
        for (int i = 0; i < dataExchange.size(); i++){
        	DataExchange tempDataExchange = dataExchange.get(i);
        	SimpleDateFormat formatter;

        	formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        	
	        row = sheet.createRow(i+1);
	        Date = row.createCell(0);
	        High = row.createCell(1);
	        Low = row.createCell(2);
	        Open = row.createCell(3);
	        Close = row.createCell(4);
	        Type = row.createCell(5);
	        
	        //DataFormat format = book.createDataFormat();
	        //CellStyle dateStyle = book.createCellStyle();
	        //dateStyle.setDataFormat(format.getFormat("dd/mm/yy HH:nn;@"));
	        //Date.setCellStyle(dateStyle);
	        Date.setCellValue(formatter.format(tempDataExchange.getDate()*1000));
	        
	        High.setCellValue(tempDataExchange.getHigh());
	        Low.setCellValue(tempDataExchange.getLow());
	        Open.setCellValue(tempDataExchange.getOpen());
	        Close.setCellValue(tempDataExchange.getClose());
	        Type.setCellValue(tempDataExchange.getType());
        }
	}
	
	public String getNextDataExchangeMod(long date) {
		if (dataExchange.get(0).getDate() > date) { return "0 0"; } 	
		while (!this.EOF) {
			this.metka++;
			if (dataExchange.size() <= this.metka + 1)
				this.EOF = true; 
			DataExchange tempDataExchange = dataExchange.get(this.metka);
			if (tempDataExchange.getDate() == date) { return tempDataExchange.toStringHLNorm(this.high, this.low); } 			
		}
		return "0 0";
	}
	
	public String getNextDataExchange(long date) {
		if (dataExchange.get(0).getDate() > date) { return "0 0 0 0"; } 	
		while (!this.EOF) {
			this.metka++;
			if (dataExchange.size() <= this.metka + 1)
				this.EOF = true; 
			DataExchange tempDataExchange = dataExchange.get(this.metka);
			if (tempDataExchange.getDate() == date) { return tempDataExchange.toStringAllNorm(this.high, this.low); } 			
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
	
	public DataExchange getLastDataExchange() {
		DataExchange temp = dataExchange.get(this.dataExchange.size()-1);
		return new DataExchange(Normal(temp.getHigh()), Normal(temp.getLow()), Normal(temp.getOpen()), Normal(temp.getClose()), temp.getDate());
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

}
