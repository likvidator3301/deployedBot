package DataSources;

import DataStructure.Pair;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleDataSource implements IDataSource {

    public ArrayList<Pair<String, String>> getInfoByTag(String tag) throws IOException {
        var result = new ArrayList<Pair<String, String>>();
        result.add(new Pair<>("Здесь будет информация по тегу " + tag, "А здесь ссылка на статью, если такая есть"));
        return result;
    }
}
