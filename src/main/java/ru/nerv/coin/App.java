package ru.nerv.coin;


/**
 * Hello world!
 *
 */
public class App 
{

	public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        /*NervStart Polo = new PoloStart();
        List<DataExchange> XRPBTC = Polo.getDataExchangePire("BTC_XRP");
        List<DataExchange> BTC = Polo.getDataExchangePire("USDT_BTC");
        Polo.balance(BTC, XRPBTC);
        Polo.saveDataTrainToFile(BTC, XRPBTC, "BTC_XRP");*/
        
        Exchange polo = new PoloExchange("Poloniex", 14400);
        polo.addPair("USDT_XMR");
        polo.addPair("USDT_BTC");
        //polo.addSourcePair("BTC_XRP");
        //polo.addSourcePair("USDT_XMR");
        //polo.addSourcePair("BTC_DASH");
        //polo.addSourcePair("BTC_ZEC");
        //polo.addSourcePair("BTC_ETH");
        //polo.addSourcePair("BTC_LTC");
        //polo.updatePairAll("0", "9999999999");
        System.out.println( "Пары обновились!" );
        polo.SaveToFileTrainTwoPair("USDT_XMR", "USDT_BTC");
        System.out.println( "Начинаем обучение!" );
        polo.TrainTwoPair("USDT_XMR", "USDT_BTC", 50000);
        //polo.TrainCascade();
        polo.getTrain(100);
        //polo.getPairName("USDT_XMR").updateRSI();
        //polo.SaveForecastToExcel("WorkBook.xlsx");
        System.out.println( "Finish!" );
    }
}
