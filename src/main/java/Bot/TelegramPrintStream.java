package Bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.PrintStream;
import java.util.ArrayList;

public class TelegramPrintStream extends PrintStream {
    private long chatId;

    public TelegramPrintStream(long chatId){
        super(System.out);
        this.chatId = chatId;
    }

    public void println(String x) {
        SendMessage s = new SendMessage();
        s.setChatId(chatId);
        s.setText(x);
        try {
            Main.telegram.execute(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
