package ru.nerv.coin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

import com.googlecode.fannj.ActivationFunction;
import com.googlecode.fannj.Fann;
import com.googlecode.fannj.Layer;
import com.googlecode.fannj.Trainer;
import com.googlecode.fannj.TrainingAlgorithm;

import ChartDirector.ArrayMath;


public abstract class Exchange {

	String name;
	List<Balance> balance = new ArrayList<Balance>();
	List<Pair> pair = new ArrayList<Pair>();
	List<String> sourcePair = new ArrayList<String>();
	long MaxSizeDataExchange = 0;
	protected int HeadPairID;
	protected int queryPeriod;
	protected long MinDataExchang = 0;
		
	public Exchange(String name, int queryPeriod) {
		super();
		this.name = name;
		this.queryPeriod = queryPeriod;
	}
	
	public abstract void updatePair();
	
	public abstract void updateBalance();
	
	public List<Balance> getBalance() {
		return balance;
	}

	public List<Pair> getPairAll() {
		return pair;
	}
	
	public Pair getPairName(String name) {
		Iterator<Pair> tempPairList =  pair.iterator();
        while(tempPairList.hasNext()){
            Pair tempPair = tempPairList.next();
            if (tempPair.name == name) { return tempPair; }
        }
		return null;
	}
	
	public List<String> getSourcePair() {
		return sourcePair;
	}

	public void addSourcePair(String sourcePair) {
		this.sourcePair.add(sourcePair);
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
        layerList.add(Layer.create(6, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(17, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(8, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        //layerList.add(Layer.create(12, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
        layerList.add(Layer.create(6, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
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
				trainer.train(fileTrain.getAbsolutePath(), countRepeat, 100, 0.00000015f);
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
			tempDataExchange = tempPair.getLastDataExchange();
			DateDataExchange = tempDataExchange.getDate();
			rezult = new float[] {tempDataExchange.getDayWeek(),tempDataExchange.getHourDay(), tempDataExchange.getOpen(), tempDataExchange.getClose(), tempDataExchange.getLow(), tempDataExchange.getHigh()};
			
			Fann fann = new Fann("train/" + this.name + "_"+ tempPair.getName());
		    for (int j = 0; j < CountIterations; j++){
		    	rezult = fann.run(rezult);
		    	DateDataExchange += this.queryPeriod;
		    	tempPair.addDataExchange(rezult[2], rezult[3], rezult[4], rezult[5], DateDataExchange, (byte) 1);
				tempDataExchange = tempPair.getLastDataExchange();
				DateDataExchange = tempDataExchange.getDate();
				rezult = new float[] {tempDataExchange.getDayWeek(),tempDataExchange.getHourDay(), tempDataExchange.getOpen(), tempDataExchange.getClose(), tempDataExchange.getLow(), tempDataExchange.getHigh()};

		    }
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
	
	public void SaveToFileTrain(){
		String writeTrain;
		
		for(int i = 0; i < pair.size(); i++){
		Pair tempPair = pair.get(i);
			
	       try(FileWriter writer = new FileWriter("src_train/" + this.name + "_"+ tempPair.getName() + ".data", false))
	        {	     	    	   
	    	    writer.write(tempPair.getSizeDataExchange() - 1 + " 6 6\n");
	    	    this.MinDataExchang = tempPair.getDataExchange().get(0).getDate();
	    	    
	    	    writeTrain = tempPair.getNextDataExchange(this.MinDataExchang);
	    	    writer.append(writeTrain);
	    	    
	    	    this.MinDataExchang += this.queryPeriod;
	    	    
	    	    do {
		    	    writeTrain = tempPair.getNextDataExchange(this.MinDataExchang);
		    	    if (!tempPair.isEOF()) {
				    	    writer.append("\n" + writeTrain + "\n");
				    	    writer.append( writeTrain);
				    	    this.MinDataExchang += this.queryPeriod;
		    	    } else {
		    	    	writer.append("\n" +  writeTrain);
		    	    	break;
		    	    }
	    	    } while(true);
	        }
	        catch(IOException ex){
	            System.out.println(ex.getMessage());
	        } 
		}
	}
		
}
