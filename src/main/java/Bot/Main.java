package Bot;

import DataSources.ApiVK;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;

public class Main
{
    public static HashMap<String, Console> telegramConsoleDictionary;
    public static Telegram telegram;

    public static void main(String[] args) throws IOException {
        ApiContextInitializer.init();
        telegramConsoleDictionary = new HashMap<String, Console>();
        telegram = new Telegram();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            System.out.println("Try");
            botsApi.registerBot(telegram);
            System.out.println("Connected!");
        } catch (TelegramApiException e) {
            System.out.println("Не конектед");
            e.printStackTrace();
        }
        /*var console = new Console(System.out, System.in, 0);
        console.startDialog();*/

    }
}
