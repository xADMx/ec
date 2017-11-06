package ru.nerv.coin;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.utils.URIBuilder;


import okhttp3.Request;
import okhttp3.Response;

public class PoloPair extends Pair {

	public PoloPair(String name, int queryPeriod) {
		super(name, queryPeriod);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updateTrade() {
		// TODO Auto-generated method stub
		/*ObjectMapper mapper = new ObjectMapper();
        JsonNode array = null;

        String PublicUrl = "https://poloniex.com/tradingApi/returnBalances";
        
        try {
        	
        	URIBuilder b = new URIBuilder(PublicUrl)
                    .addParameter("command", "returnChartData")
                    .addParameter("currencyPair", name) 
                    .addParameter("start", "0")
                    .addParameter("end", "9999999999")
                    .addParameter("period", "14400");
        	
        	    RequestBody formBody = new FormBody.Builder()
        	        .add("search", "Jurassic Park")
        	        .build();
        	    Request request = new Request.Builder()
        	        .url(b.toString())   
        	        //.post(formBody)
        	        .build();
        	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	    
        	    try (Response response = client.newCall(request).execute()) {
        	      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        	     // ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
	              	array = mapper.readTree(response.body().string());// string() вызывать можно один раз.
	                Iterator<?> chartdataiterator = array.iterator();
	                while(chartdataiterator.hasNext() ) {
	                	dataExchange.add(mapper.readValue(chartdataiterator.next().toString(), DataExchange.class));
	                }  
        	    }
        } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(DataExchange.class.getName()).log(Level.INFO, null, ex);
        }	*/
	}

	@Override
	protected void updateDataExchange(int time) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
        JsonNode array = null;
        
        String PublicUrl = "https://poloniex.com/public";
        
        try {
        	
        	URIBuilder b = new URIBuilder(PublicUrl)
                    .addParameter("command", "returnChartData")
                    .addParameter("currencyPair", name) 
                    .addParameter("start", String.valueOf(time))
                    //.addParameter("end", "9999999999")
                    .addParameter("end", "9999999999")
                    .addParameter("period", String.valueOf(queryPeriod));
        	
        	   /* RequestBody formBody = new FormBody.Builder()
        	        .add("search", "Jurassic Park")
        	        .build();*/
        	    Request request = new Request.Builder()
        	        .url(b.toString())   
        	        //.post(formBody)
        	        .build();
        	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	    
        	    try (Response response = client.newCall(request).execute()) {
        	      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        	     // ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
	              	array = mapper.readTree(response.body().string());// string() вызывать можно один раз.
	                Iterator<?> chartdataiterator = array.iterator();
	                DataExchange temp;
	                while(chartdataiterator.hasNext() ) {
	                	temp = mapper.readValue(chartdataiterator.next().toString(), DataExchange.class);
	                	if (this.high < temp.getHigh()){
	                		this.high = temp.getHigh();
	                	}
	                	if (this.low > temp.getLow()){
	                		this.low = temp.getLow();
	                	}
	                	dataExchange.add(temp);
	                }  
        	    }
        } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(DataExchange.class.getName()).log(Level.INFO, null, ex);
        }
	}


}
