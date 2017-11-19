package ru.nerv.coin;

import java.nio.file.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.rmi.CORBA.Util;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

import com.googlecode.fannj.ActivationFunction;
import com.googlecode.fannj.Fann;
import com.googlecode.fannj.FannShortcut;
import com.googlecode.fannj.Layer;
import com.googlecode.fannj.Trainer;
import com.googlecode.fannj.TrainingAlgorithm;



import ChartDirector.ArrayMath;


public abstract class Exchange {

	String name;
	List<Balance> balance = new ArrayList<Balance>();
	List<Pair> pair = new ArrayList<Pair>();
	long MaxSizeDataExchange = 0;
	protected int HeadPairID;
	protected int queryPeriod;
	protected long MinDataExchang = 0;
		
	public Exchange(String name, int queryPeriod) {
		super();
		this.name = name;
		this.queryPeriod = queryPeriod;
	}
	
	public abstract void updatePairAll(long startTime, long endTime);
	public abstract void updatePair(String name, long startTime, long endTime);	
	public abstract boolean addPair(String name, long startTime, long endTime);	
	public abstract void updateBalance();
	
	public List<Balance> getBalance() {
		return balance;
	}

	public List<Pair> getPairAll() {
		return pair;
	}
		
	public int existPair(String namePair) {
		for(int i = 0; i < pair.size(); i++){ 
			if (pair.get(i).getName() == namePair) {
				return i;
			}
		}
		return -1;
	}
	
	public Pair getPair(String namePair) {
		for(int i = 0; i < pair.size(); i++){ 
			if (pair.get(i).getName() == namePair) {
				return pair.get(i);
			}
		}
		return null;
	}
			
	public boolean addPair(String namePair) {
		return this.addPair(namePair, 0, Long.valueOf("9999999999"));
	}
	
	protected void getMaxDatePair() {
		int i = 0;
		for(i = 0; i < pair.size(); i++){
			if (this.MaxSizeDataExchange < pair.get(i).getSizeDataExchange()) { 
				this.MaxSizeDataExchange = pair.get(i).getSizeDataExchange();
				this.HeadPairID = i;
				this.MinDataExchang = pair.get(i).getDataExchange().get(0).getDate();
			}
		}
	}
	
    public double[][] getMACD(Pair pair)
    {
    	int period1 = 12;
    	int period2 = 26;
    	int period3 = 9;
	    //MACD is defined as the difference between two exponential averages (typically 12/26 days)
	    double[] expAvg1 = new ArrayMath(pair.getDataExchangeCloseToDouble()).expAvg(2.0 / (period1 + 1)).result();
	    double[] macd = new ArrayMath(pair.getDataExchangeCloseToDouble()).expAvg(2.0 / (period2 + 1)).sub(expAvg1).result();
	
	    //MACD signal line
	    double[] macdSignal = new ArrayMath(macd).expAvg(2.0 / (period3 + 1)).result();
	    
	    return new double[][] {macd,macdSignal};
	    
    }
	
	public void Train(int countRepeat) {
        //Для сборки новой ИНС необходимо создасть список слоев
        List<Layer> layerList = new ArrayList<Layer>();
        layerList.add(Layer.create(6));
        layerList.add(Layer.create(44, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        //layerList.add(Layer.create(12, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(4, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        Fann fann = new Fann(layerList);
        
        //Создаем тренера и определяем алгоритм обучения
        Trainer trainer = new Trainer(fann);
        
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_RPROP);
        /* Проведем обучение взяв уроки из файла, с максимальным колличеством
           циклов 100000, показывая отчет каждую 100ю итерацию и добиваемся
        ошибки меньше 0.0001 */
		for(int i = 0; i < pair.size(); i++){
			File fileTrain = new File("src_train/" + this.name + "_"+ pair.get(i).getName() + ".data");
			if(fileTrain.exists()) {
				trainer.train(fileTrain.getAbsolutePath(), countRepeat, 100, 0.00009f);
				fann.save("train/" + this.name + "_"+ pair.get(i).getName());
			}
		}
	}	
	
	public void TrainTwoPair(String namePairOne, String namePairTwo, int countRepeat) {
        //Для сборки новой ИНС необходимо создасть список слоев
        List<Layer> layerList = new ArrayList<Layer>();
        layerList.add(Layer.create(10));
        layerList.add(Layer.create(44, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        //layerList.add(Layer.create(12, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(4, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        Fann fann = new Fann(layerList);
        
        //Создаем тренера и определяем алгоритм обучения
        Trainer trainer = new Trainer(fann);
        
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_RPROP);
        /* Проведем обучение взяв уроки из файла, с максимальным колличеством
           циклов 100000, показывая отчет каждую 100ю итерацию и добиваемся
        ошибки меньше 0.0001 */
			File fileTrain = new File("src_train/" + this.name + "_("+ namePairOne + "," + namePairTwo +  ").data");
			if(fileTrain.exists()) {
				trainer.train(fileTrain.getAbsolutePath(), countRepeat, 100, 0.0000009f);
				fann.save("train/" + this.name + "_("+ namePairOne + "," + namePairTwo + ")");
			}
	}	
	
	public void TrainCascadeTwoPair(String namePairOne, String namePairTwo) {
		Fann fann = new FannShortcut(10, 4);
        Trainer trainer = new Trainer(fann);
        /* Проведем обучение взяв уроки из файла, с максимальным колличеством
           циклов 100000, показывая отчет каждую 100ю итерацию и добиваемся
        ошибки меньше 0.0001 */
		File fileTrain = new File("src_train/" + this.name + "_("+ namePairOne + "," + namePairTwo +  ").data");
		if(fileTrain.exists()) {
			trainer.cascadeTrain(fileTrain.getAbsolutePath(), 50, 2, 0.00009f);
			fann.save("train/" + this.name + "_("+ namePairOne + "," + namePairTwo +  ")");
		}
		
	}
	
	public void TrainCascade() {
        //Для сборки новой ИНС необходимо создасть список слоев
        List<Layer> layerList = new ArrayList<Layer>();
        layerList.add(Layer.create(6));
        layerList.add(Layer.create(44, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        //layerList.add(Layer.create(12, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(4, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        Fann fann = new Fann(layerList);
        
        //Создаем тренера и определяем алгоритм обучения
        Trainer trainer = new Trainer(fann);
        
        trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_RPROP);
        /* Проведем обучение взяв уроки из файла, с максимальным колличеством
           циклов 100000, показывая отчет каждую 100ю итерацию и добиваемся
        ошибки меньше 0.0001 */
		for(int i = 0; i < pair.size(); i++){
			File fileTrain = new File("src_train/" + this.name + "_"+ pair.get(i).getName() + ".data");
			if(fileTrain.exists()) {
				trainer.cascadeTrain(fileTrain.getAbsolutePath(), 30, 1, 0.00009f);
				fann.save("train/" + this.name + "_"+ pair.get(i).getName());
			}
		}
	}	
	
	public void SaveForecastToExcel(String path){
		Workbook book = new XSSFWorkbook();
		
		for(int i = 0; i < pair.size(); i++){
	        pair.get(i).CreateWorkSheet(book);
		}
		
        try {
			book.write(new FileOutputStream(path));
			book.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getTrain(int CountIterations) {	
		float[] rezult;
		long DateDataExchange;
		DataExchange tempDataExchange;
		Pair tempPair;
		for(int i = 0; i < pair.size(); i++){
			tempPair = pair.get(i);
			tempDataExchange = tempPair.getLastDataExchangeNorm();
			DateDataExchange = tempDataExchange.getDate();
			rezult = new float[] {tempDataExchange.getDayWeek(),tempDataExchange.getHourDay(), tempDataExchange.getOpen(), tempDataExchange.getClose(), tempDataExchange.getLow(), tempDataExchange.getHigh()};
			
			Fann fann = new Fann("train/" + this.name + "_"+ tempPair.getName());
		    for (int j = 0; j < CountIterations; j++){
		    	rezult = fann.run(rezult);
		    	DateDataExchange += this.queryPeriod;
		    	tempPair.addDataExchange(rezult[0], rezult[1], rezult[2], rezult[3], DateDataExchange, (byte) 1);
				tempDataExchange = tempPair.getLastDataExchangeNorm();
				DateDataExchange = tempDataExchange.getDate();
				rezult = new float[] {tempDataExchange.getDayWeek(),tempDataExchange.getHourDay(), tempDataExchange.getOpen(), tempDataExchange.getClose(), tempDataExchange.getLow(), tempDataExchange.getHigh()};

		    }
		}
	}
	
	public boolean getTrain(String namePair, int CountIterations) {	
		float[] rezult;
		long DateDataExchange;
		DataExchange tempDataExchange = null;
		Pair tempPair = null;
		int index = this.existPair(namePair);
		
		if (index != -1){
			tempPair = pair.get(index);
			tempDataExchange = tempPair.getLastDataExchangeNorm();
			DateDataExchange = tempDataExchange.getDate();
			rezult = new float[] {tempDataExchange.getDayWeek(),tempDataExchange.getHourDay(), tempDataExchange.getOpen(), tempDataExchange.getClose(), tempDataExchange.getLow(), tempDataExchange.getHigh()};
			
			Fann fann = new Fann("train/" + this.name + "_"+ tempPair.getName());
		    for (int j = 0; j < CountIterations; j++){
		    	rezult = fann.run(rezult);
		    	DateDataExchange += this.queryPeriod;
		    	tempPair.addDataExchange(rezult[0], rezult[1], rezult[2], rezult[3], DateDataExchange, (byte) 1);
				tempDataExchange = tempPair.getLastDataExchangeNorm();
				DateDataExchange = tempDataExchange.getDate();
				rezult = new float[] {tempDataExchange.getDayWeek(),tempDataExchange.getHourDay(), tempDataExchange.getOpen(), tempDataExchange.getClose(), tempDataExchange.getLow(), tempDataExchange.getHigh()};
		    }
		    
		    return true;
		} else {
			return false;
		}
	}
	
	public boolean getTrainTwoPair(String namePairOne, String namePairTwo) {	
		float[] rezult;
		long DateDataExchange;
		DataExchange tempDataExchangeOne = null;
		DataExchange tempDataExchangeTwo = null;
		Pair tempPairOne = null;
		Pair tempPairTwo = null;
		int indexPairOne = this.existPair(namePairOne);
		int indexPairTwo = this.existPair(namePairTwo);
		
		if (indexPairOne != -1 && indexPairTwo != -1){
			tempPairOne = pair.get(indexPairOne); 
			tempPairTwo = pair.get(indexPairTwo);

			tempDataExchangeOne = tempPairOne.getLastDataExchangeNorm();
			DateDataExchange = tempDataExchangeOne.getDate();
			tempDataExchangeTwo = tempPairTwo.getFindDataExchangeNorm(DateDataExchange);
			
			
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
		    DateDataExchange += this.queryPeriod;
		    tempPairOne.addDataExchange(rezult[0], rezult[1], rezult[2], rezult[3], DateDataExchange, (byte) 1);
		    
		    return true;
		} else {
			return false;
		}
	}
			    
	/*public void SaveToFileTrain(){
		String writeTrain;
	       try(FileWriter writer = new FileWriter(this.name + ".data", false))
	        {	     
	    	   	this.getMaxDatePair();
	    		
	    	    writer.write(this.MaxSizeDataExchange - 1 + " " + (pair.size() * 2) + " " + (pair.size() * 2) + "\n");
	    	    
    	    	writeTrain = "";
	    	    for(int i = 0; i < pair.size(); i++){
	    	    		writeTrain += pair.get(i).getNextDataExchange(MinDataExchang) + " ";
	    	    }
	    	    writer.append(writeTrain.trim());
	    	    MinDataExchang += queryPeriod;
	    	    do {
	    	    	writeTrain = "";
		    	    for(int i = 0; i < pair.size(); i++){
		    	    		writeTrain += pair.get(i).getNextDataExchange(MinDataExchang) + " ";
		    	    }
		    	    if (!pair.get(this.HeadPairID).isEOF()) {
				    	    writer.append("\n" + writeTrain.trim() + "\n");
				    	    writer.append( writeTrain.trim());
			    	    MinDataExchang += queryPeriod;
		    	    } else {
		    	    	writer.append("\n" +  writeTrain.trim());
		    	    	break;
		    	    }
		    	    //writeTrain = pair.get(this.HeadPairID).getNextDataExchange(MinDataExchang)+ " ";
	    	    } while(true);
	    	    
	            //writer.flush();
	        }
	        catch(IOException ex){
	            System.out.println(ex.getMessage());
	        } 
	}*/
	
	public void SaveToFileTrainAll(){
		String writeTrain;
		
		for(int i = 0; i < pair.size(); i++){
		Pair tempPair = pair.get(i);
			
	       try(FileWriter writer = new FileWriter("src_train/" + this.name + "_"+ tempPair.getName() + ".data", false))
	        {	     	    	   
	    	    writer.write(tempPair.getSizeDataExchange() - 1 + " 6 4");
	    	    this.MinDataExchang = tempPair.getDataExchange().get(0).getDate();
	    	    tempPair.setFirstDataExchange();
	    	    while (true) {
		    	    writeTrain = "\n" + tempPair.getNextDataExchangeAllNormToString(this.MinDataExchang) + "\n";
		    	    this.MinDataExchang += this.queryPeriod;
		    	    writeTrain += tempPair.getNextDataExchangeNormToString(this.MinDataExchang);
		    	    if (!tempPair.isEOF()) {
		    	    	writer.append(writeTrain);
		    	    } else {
		    	    	break;
		    	    }   
	    	    }
	        }
	        catch(IOException ex){
	            System.out.println(ex.getMessage());
	        } 
		}
	}
	
	public abstract boolean testAndSaveToFileTwoPair(String path, String namePairOne, String namePairTwo, long startDate, int countEpoch);
	
	public boolean saveToFileTrainTwoPair(String namePairOne, String namePairTwo){
		String writeTrain = "";
		String writeTrain2 = "";
		Pair tempPairOne = null;
		Pair tempPairTwo = null;
		int indexData = 0;
		int indexPairOne = this.existPair(namePairOne);
		int indexPairTwo = this.existPair(namePairTwo);
		
		if (indexPairOne != -1 && indexPairTwo != -1){
			tempPairOne = pair.get(indexPairOne); 
			tempPairTwo = pair.get(indexPairTwo); 
			
	       try(FileWriter writer = new FileWriter("src_train/" + this.name + "_("+ namePairOne + "," + namePairTwo +  ").data", false))
	        {	     	    	   
	    	    //writer.write(tempPairOne.getSizeDataExchange() - 1 + " 6 4");
	    	    this.MinDataExchang = tempPairOne.getDataExchange().get(0).getDate();
	    	    tempPairOne.setFirstDataExchange();
	    	    tempPairTwo.setFirstDataExchange();
	    	    
	    	    while (true) {
	    	    	writeTrain2 = "\n" + tempPairOne.getNextDataExchangeAllNormToString(this.MinDataExchang) + " " + tempPairTwo.getNextDataExchangeNormToString(this.MinDataExchang) + "\n";
		    	    this.MinDataExchang += this.queryPeriod;
		    	    writeTrain2 += tempPairOne.getNextDataExchangeNorm(this.MinDataExchang);
		    	    if (!tempPairOne.isEOF()) {
		    	    	if(writeTrain2.indexOf("0 0 0 0") == -1) {
		    	    		writeTrain += writeTrain2;
		    	    		indexData++;
		    	    	}
		    	    } else {
		    	    	break;
		    	    }   
	    	    }
	    	    
	    	    writer.write(indexData + " 10 4" + writeTrain);
	        }
	        catch(IOException ex){
	            System.out.println(ex.getMessage());
	        }
	       return true;
		} else {
			return false;
		}
		
	}
		
}
