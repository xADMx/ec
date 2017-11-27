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
        polo.addPair("USDT_XMR");
        polo.addPair("USDT_BTC");
        polo.addPair("USDT_ETH");
        polo.addPair("USDT_XRP");
        polo.addPair("USDT_LTC");
        polo.addPair("USDT_ZEC");
        //polo.addPair("BTC_XEM");
        polo.addPair("USDT_DASH");
        polo.addPair("USDT_ETC");
        //polo.addPair("USDT_STR");
        System.out.println( "Пары обновились!" );
        //polo.saveToFileTrainTwoPair("USDT_XMR", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("USDT_ETH", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("USDT_XRP", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("USDT_LTC", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("BTC_XEM", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("USDT_ZEC", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("USDT_DASH", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("USDT_ETC", "USDT_BTC");
        //polo.saveToFileTrainTwoPair("USDT_STR", "USDT_BTC");
        //polo.saveToFileTrainAllPair();
        System.out.println( "Начинаем обучение!" );
        //polo.TrainCascadeAllPair();
        //polo.TrainCascadeTwoPair("USDT_XMR", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_ETH", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_XRP", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_LTC", "USDT_BTC");
        //polo.TrainCascadeTwoPair("BTC_XEM", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_ZEC", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_DASH", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_ETC", "USDT_BTC");
        //polo.TrainCascadeTwoPair("USDT_STR", "USDT_BTC");
        //polo.TrainCascade();
        polo.getTrainAllPair("USDT_ZEC");
        polo.getTrainAllPair("USDT_BTC");
        //polo.getTrainTwoPair("USDT_XMR", "USDT_BTC");
        //polo.getTrainTwoPair("USDT_ETH", "USDT_BTC");
        //polo.getTrainTwoPair("USDT_XRP", "USDT_BTC");
        //polo.getTrainTwoPair("USDT_LTC", "USDT_BTC");
        //polo.getTrainTwoPair("BTC_XEM", "USDT_BTC");
        //polo.getTrainTwoPair("USDT_ZEC", "USDT_BTC");
        //polo.getTrainTwoPair("USDT_DASH", "USDT_BTC");
        //polo.getTrainTwoPair("USDT_ETC", "USDT_BTC");
        //polo.getTrainTwoPair("USDT_STR", "USDT_BTC");
        polo.getPair("USDT_ETC").updateRSI();
        //polo.getPair("USDT_STR").updateRSI();
        polo.getPair("USDT_DASH").updateRSI();
        polo.getPair("USDT_ZEC").updateRSI();
        polo.getPair("USDT_XMR").updateRSI();
        polo.getPair("USDT_BTC").updateRSI();
        polo.getPair("USDT_ETH").updateRSI();
        polo.getPair("USDT_LTC").updateRSI();
        polo.getPair("USDT_XRP").updateRSI();
        //polo.getPair("BTC_XEM").updateRSI();
        polo.SaveForecastToExcel("WorkBook.xlsx");
        polo.testAndSaveToFileTwoPair("WorkBookTest.xlsx", "USDT_ZEC", "USDT_BTC", 1505779200, 10);
        System.out.println( "Finish!" );
    }
}
