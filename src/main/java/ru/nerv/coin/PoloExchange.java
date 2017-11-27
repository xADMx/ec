package ru.nerv.coin;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.googlecode.fannj.Fann;

public class PoloExchange extends Exchange {

	public PoloExchange(String name, int queryPeriod) {
		super(name, queryPeriod);
		// TODO Auto-generated constructor stub
	}
	
	public boolean testAndSaveToFileTwoPair(String path, String namePairOne, String namePairTwo, long startDate, int countEpoch){
		float[] rezult;
		DataExchange tempDataExchangeOne = null;
		DataExchange tempDataExchangeTwo = null;
		Pair tempPairOne = null;
		Pair tempPairTwo = null;
		Pair tempNewPair = new PoloPair(namePairOne, queryPeriod);
		int indexPairOne = this.existPair(namePairOne);
		int indexPairTwo = this.existPair(namePairTwo);
		
		if (indexPairOne != -1 && indexPairTwo != -1){
				tempPairOne = pair.get(indexPairOne); 
				tempPairTwo = pair.get(indexPairTwo);
				tempNewPair.setHigh(tempPairOne.getHigh());
				tempNewPair.setLow(tempPairTwo.getLow());
				
				for (int i = 0; i < countEpoch; i++){
					tempPairOne.setFirstDataExchange();
					tempPairTwo.setFirstDataExchange();
					tempDataExchangeOne = tempPairOne.getNextDataExchange(startDate);
					tempDataExchangeTwo = tempPairTwo.getNextDataExchange(startDate);
					
					tempNewPair.addDataExchange(tempDataExchangeOne.getOpen(), 
												tempDataExchangeOne.getClose(), 
												tempDataExchangeOne.getLow(), 
												tempDataExchangeOne.getHigh(), 
												tempDataExchangeOne.getDate(),
												(byte) 0);
					
					rezult = new float[] {tempDataExchangeOne.getDayWeek(),
											tempDataExchangeOne.getHourDay(), 
											tempDataExchangeOne.getOpen(), 
											tempDataExchangeOne.getClose(), 
											tempDataExchangeOne.getLow(), 
											tempDataExchangeOne.getHigh(),
											tempDataExchangeTwo.getOpen(), 
											tempDataExchangeTwo.getClose(), 
											tempDataExchangeTwo.getLow(), 
											tempDataExchangeTwo.getHigh()};
					
					Fann fann = new Fann("train/" + this.name + "_("+ namePairOne + "," + namePairTwo + ")");
				    rezult = fann.run(rezult);
				    startDate += this.queryPeriod;
				    tempNewPair.addDataExchange(rezult[0], rezult[1], rezult[2], rezult[3], startDate, (byte) 1);
				}
				
				
		        try (Workbook book = new XSSFWorkbook()) {
		        	tempNewPair.CreateWorkSheet(book);
					book.write(new FileOutputStream(path));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    return true;
		    
		} else {
			return false;
		}
		
	}

	@Override
	public void updatePairAll(long startTime, long endTime) {
		for(int i = 0; i < pair.size(); i++){ 
				PoloPair tempPair = new PoloPair(pair.get(i).getName(), queryPeriod);
				tempPair.update(startTime, endTime);
				pair.add(i, tempPair);
		}
	}
	
	@Override
	public void updatePair(String name, long startTime, long endTime) {
		for(int i = 0; i < pair.size(); i++){ 
			if (pair.get(i).getName() == name) {
				PoloPair tempPair = new PoloPair(name, queryPeriod);
				tempPair.update(startTime, endTime);
				pair.add(i, tempPair);
			}
		}
	}
	
	@Override
	public boolean addPair(String name, long startTime, long endTime) {
		int index = existPair(name);
		
		PoloPair tempPair = new PoloPair(name, queryPeriod);
		tempPair.update(startTime, endTime);
		
		if (index < 0) { 
			pair.add(tempPair); 
			return true;
		} else { 
			return false;
		}
	}

	@Override
	public void updateBalance() {

	}



}
