package Bot;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TelegramInputStream extends InputStream {
    private ArrayList<String> data = new ArrayList<String>();
    private byte[] bData;
    private int pointer = 0;
    private int dataPointer = 0;
    private long chatId;
    private boolean canReturn;
    private boolean needSetBData;
    private boolean locked;

    public TelegramInputStream(long chatId) {
        super();
        this.chatId = chatId;
        needSetBData = true;
    }

    public void addData(String x)
    {
        if (locked)
            return;
        x += "\n";
        this.data.add(x);
        canReturn = true;
    }

    public int read() {
        locked = false;
        while (!canReturn) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (needSetBData) {
            bData = data.get(dataPointer).getBytes();
            needSetBData = false;
        }

        if (pointer >= bData.length) {
            pointer = 0;
            if (++dataPointer < data.size())
                bData = data.get(dataPointer).getBytes();
            else {
                needSetBData = true;
                canReturn = false;
            }
            locked = true;
            return -1;
        }
        return bData[pointer++];
    }
}
