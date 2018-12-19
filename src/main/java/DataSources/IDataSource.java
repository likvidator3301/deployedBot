package DataSources;

import DataStructure.Pair;

import java.io.IOException;
import java.util.ArrayList;

public interface IDataSource {

    ArrayList<Pair<String, String>> getInfoByTag(String tag) throws IOException;

}
