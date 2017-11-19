package ru.nerv.coin;


public class PoloExchange extends Exchange {

	public PoloExchange(String name, int queryPeriod) {
		super(name, queryPeriod);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePairAll(String startTime, String endTime) {
		for(int i = 0; i < pair.size(); i++){ 
				PoloPair tempPair = new PoloPair(pair.get(i).getName(), queryPeriod);
				tempPair.update(startTime, endTime);
				pair.add(i, tempPair);
		}
	}
	
	@Override
	public void updatePair(String name, String startTime, String endTime) {
		for(int i = 0; i < pair.size(); i++){ 
			if (pair.get(i).getName() == name) {
				PoloPair tempPair = new PoloPair(name, queryPeriod);
				tempPair.update(startTime, endTime);
				pair.add(i, tempPair);
			}
		}
	}
	
	@Override
	public boolean addPair(String name, String startTime, String endTime) {
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
