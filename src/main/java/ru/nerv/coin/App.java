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
        //polo.addSourcePair("USDT_BTC");
        polo.addSourcePair("BTC_XRP");
        polo.addSourcePair("BTC_XMR");
        //polo.addSourcePair("BTC_DASH");
        //polo.addSourcePair("BTC_ZEC");
        //polo.addSourcePair("BTC_ETH");
        //polo.addSourcePair("BTC_LTC");
        polo.updatePair();
        System.out.println( "Пары обновились!" );
        //polo.SaveToFileTrain();
        System.out.println( "Начинаем обучение!" );
        //polo.Train();
        polo.getTrain(100);
        polo.SaveForecastToExcel("WorkBook.xlsx");
        System.out.println( "Finish!" );
    }
}
