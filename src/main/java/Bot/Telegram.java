package Bot;

import DataSources.ApiVK;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;

public class Telegram extends TelegramLongPollingBot {

    private HashMap<String, Runnable> threadsDict;

    public Telegram() {
        super();
        threadsDict = new HashMap<String, Runnable>();
    }

    @Override
    public String getBotUsername() {
        return "NewITNewsBot";
    }

    @Override
    public void onUpdateReceived(Update e) {
        var msg = e.getMessage(); // Это нам понадобится
        var chatId = msg.getChatId();
        var txt = msg.getText();
        if (!Main.telegramConsoleDictionary.containsKey(chatId.toString())) {
            var myThready = new Thread(new Runnable()
            {
                public void run()
                {
                    try {
                    var console = new Console(new TelegramPrintStream(chatId), new TelegramInputStream(chatId), chatId);
                    var vkApi = new ApiVK();
                    console.addDataSource(vkApi);
                    Main.telegramConsoleDictionary.put(chatId.toString(), console);

                    console.startDialog();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            threadsDict.put(chatId.toString(), myThready);
            myThready.start();
        }
        else {
            var console = Main.telegramConsoleDictionary.get(chatId.toString());
            var in = console.in;
            if (txt != null)
                ((TelegramInputStream)in).addData(txt);
            else
                sendMsg(msg, "Да, я тоже люблю стикеры, картинки и тд, но я не умею работать с этим");

            if (txt != null && txt.equals("/stop")) {
                try {
                    StopConsole(console);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void StopConsole(Console console) throws InterruptedException {
        while(console.isRunning)
            Thread.sleep(100);
        var chatId = console.chatId;
        var thread = threadsDict.get(Long.toString(chatId));
        threadsDict.remove(Long.toString(chatId));
        ((Thread)thread).interrupt();
        Main.telegramConsoleDictionary.remove(Long.toString(chatId));
    }



    @Override
    public String getBotToken() {
        return "728945111:AAGk9iNDHmiYKpD7gupKPkqtHsJuTs4beWo";
    }

    private void sendMsg(Message msg, String text) {
        var s = new SendMessage();
        s.setChatId(msg.getChatId());
        s.setText(text);
        try {
            execute(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
