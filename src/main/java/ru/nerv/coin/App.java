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
        
        Exchange polo = new PoloExchange("Poloniex", 86400);
        polo.addPair("USDT_XMR", 0, 1505703600);
        polo.addPair("USDT_BTC", 0, 1505703600);
        polo.addPair("USDT_ETH", 0, 1505703600);
        polo.addPair("USDT_XRP", 0, 1505703600);
        polo.addPair("USDT_LTC", 0, 1505703600);
        System.out.println( "Пары обновились!" );
        //polo.SaveToFileTrainTwoPair("USDT_XMR", "USDT_BTC");
        //polo.SaveToFileTrainTwoPair("USDT_ETH", "USDT_BTC");
        //polo.SaveToFileTrainTwoPair("USDT_XRP", "USDT_BTC");
        //polo.SaveToFileTrainTwoPair("USDT_LTC", "USDT_BTC");
        System.out.println( "Начинаем обучение!" );
        //polo.TrainCascadeTwoPair("USDT_XMR", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_ETH", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_XRP", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_LTC", "USDT_BTC");
        //polo.TrainCascade();
        polo.getTrainTwoPair("USDT_XMR", "USDT_BTC");
        polo.getTrainTwoPair("USDT_ETH", "USDT_BTC");
        polo.getTrainTwoPair("USDT_XRP", "USDT_BTC");
        polo.getTrainTwoPair("USDT_LTC", "USDT_BTC");
        polo.getPair("USDT_XMR").updateRSI();
        polo.getPair("USDT_ETH").updateRSI();
        polo.getPair("USDT_ETH").updateRSI();
        polo.getPair("USDT_LTC").updateRSI();
        polo.SaveForecastToExcel("WorkBook.xlsx");
        polo.testAndSaveToFileTwoPair("WorkBookTest.xlsx", "USDT_XMR", "USDT_BTC", 1505790000, 5);
        System.out.println( "Finish!" );
    }
}
