package ru.nerv.coin;


public class PoloExchange extends Exchange {

	public PoloExchange(String name, int queryPeriod) {
		super(name, queryPeriod);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePair() {
		pair.clear();
		for(String name : sourcePair){
			PoloPair tempPair = new PoloPair(name, queryPeriod);
			tempPair.update();
			pair.add(tempPair);
		}
	}

	@Override
	public void updateBalance() {

	}



}
