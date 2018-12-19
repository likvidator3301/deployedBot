package DataSources;

import DataStructure.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


public class ApiVK implements IDataSource {

    public ArrayList<Pair<String, String>> getInfoByTag(String tag) throws IOException {
        var data = getResponseByTag(tag);
        var parsed_data = parseJson(data);
        var result = findByTag(tag, parsed_data);
        return result;
    }

    private String getResponseByTag(String tag) throws IOException {
        var url = "https://api.vk.com/method/wall.search?query=" + tag + "&owner_id=-30666517&owners_only=1&domain=tproger&count=50&access_token=2304416f2304416f2304416f5823530eb8223042304416f79101fab155e4b6420c44a98&v=5.85";
        var obj = new URL(url);
        var connection = (HttpsURLConnection) obj.openConnection();
        connection.setDoOutput(true);

        var in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        var response = new ArrayList<String>();

        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }
        in.close();

        return response.toString();
    }

    public ArrayList<Pair<String, String>> parseJson (String data) {
        var gson = new Gson();
        var clear_response = gson.fromJson(data, JsonArray.class);
        var apiVkResponse = gson.fromJson(clear_response.get(0).toString(), ApiVkResponse.class);
        var response = gson.fromJson(apiVkResponse.response, Response.class);
        var result = new ArrayList<Pair<String, String>>();
        for (var itemAsJson : response.items) {
            try {
                var item = gson.fromJson(itemAsJson, ApiVkResponseItem.class);
                var text = item.text;
                var attachmentAsJson = item.attachments.get(0);
                var attachment = gson.fromJson(attachmentAsJson, Attachment.class);
                if (attachment == null || attachment.link == null || attachment.link.get("url") == null) {
                    result.add(new Pair(text, ""));
                    continue;
                }
                var link = attachment.link.get("url");
                result.add(new Pair(text, link.getAsString()));
            } catch (NullPointerException e) {
                continue;
            }
        }
        return result;
    }

    private ArrayList<Pair<String, String>> findByTag (String tag, ArrayList<Pair<String, String>> data) {
        var result = new ArrayList<Pair<String, String>>();
        for (var pair : data) {
            if (pair.first.contains("#" + tag + "@"))
                result.add(pair);
            if (result.size() == 3)
                return result;
        }
        return result;
    }
}
