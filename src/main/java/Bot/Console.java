package Bot;

import DataSources.IDataSource;
import DataStructure.Pair;
import Utils.FileWorker;

import java.io.*;
import java.util.*;



public class Console {

    public PrintStream out;
    public InputStream in;
    public long chatId;
    public boolean isRunning;

    private String userName;
    private ArrayList<String> tags;
    private HashMap<String, Pair<String, String>> dictOfNameAndTags;
    private Scanner scanner;
    private List<IDataSource> dataSources;
    private boolean needToAddUser;

    private static final String pathToFileWithNames = "info.txt";


    public Console(PrintStream out, InputStream in, long chatId) throws FileNotFoundException {
        this.out = out;
        this.in = in;
        this.chatId = chatId;
        scanner = new Scanner(in);
        var data = FileWorker.getLinesFromFile(pathToFileWithNames);
        dictOfNameAndTags = new HashMap<>();
        for (var item : data) {
            var nameAndTags = item.split("-");
            var id = nameAndTags[0];
            Pair pair;
            if (nameAndTags.length == 2) {
                pair = new Pair<>(nameAndTags[1], "");
            } else {
                pair = new Pair<>(nameAndTags[1], nameAndTags[2]);
            }
            dictOfNameAndTags.put(id, pair);
        }
        var chatIdAsStr = Long.toString(chatId);
        if (dictOfNameAndTags.containsKey(chatIdAsStr))
            userName = dictOfNameAndTags.get(chatIdAsStr).first;
        tags = FileWorker.getLinesFromFile("tags.txt");
        dataSources = new ArrayList<IDataSource>();
    }

    public void startDialog() throws IOException {
        isRunning = true;
        var chatIdAsStr = Long.toString(chatId);
        if (dictOfNameAndTags.containsKey(chatIdAsStr)) {
            out.println("Привет, " + userName + "!");
            if (dictOfNameAndTags.get(chatIdAsStr).second.equals(""))
                out.println("Ты ничего не искал(");
            else
                out.println("Вот что тебе было интересно: " + dictOfNameAndTags.get(chatIdAsStr).second);
        } else {
            out.println("Привет, представься, пожалуйста!");
            userName = scanner.nextLine();
            var pair = new Pair<>(userName, "");
            dictOfNameAndTags.put(chatIdAsStr, pair);
            needToAddUser = true;
        }
        out.println("Выбери интересующий тебя тег из предложенных:");

        while (true) {
            var listOfTags = new StringBuilder();
            for (var tag : tags) {
                listOfTags.append(tag);
                listOfTags.append("\n");
            }
            out.println(listOfTags.toString());
            out.println("Введи /stop если хочешь выйти");
            var needTag = scanner.nextLine().toLowerCase();

            if (needTag.equals("/stop"))
                break;

            if (tags.contains(needTag)) {
                if (!dictOfNameAndTags.get(chatIdAsStr).second.equals("") && !dictOfNameAndTags.get(chatIdAsStr).second.contains(needTag))
                    dictOfNameAndTags.put(chatIdAsStr, new Pair<>(userName, dictOfNameAndTags.get(chatIdAsStr).second + " " + needTag));
                else if (dictOfNameAndTags.get(chatIdAsStr).second.equals(""))
                    dictOfNameAndTags.put(chatIdAsStr, new Pair<>(userName, needTag));
                var data = getInfoByTag(needTag);
                if (data.size() == 0)
                    out.println("Я не нашел свежих новостей на эту тему.\nДавай я попробую поискать о чем-нибудь другом (:");
                for (var article : data) {
                    out.println(article.first + "\n\n" + article.second);
                }
            } else
                out.println("Извини, я не знаю такого тега");
        }
        out.println("Возвращайтесь, " + userName);
        stopDialog();
    }

    public void addDataSource(IDataSource dataSource) {
        dataSources.add(dataSource);
    }

    private ArrayList<Pair<String, String>> getInfoByTag(String tag) throws IOException {
        var data = new ArrayList<Pair<String, String>>();
        for (var dataSource : dataSources) {
            for (var pair : dataSource.getInfoByTag(tag)) {
                data.add(pair);
            }
        }
        return data;
    }

    private void stopDialog() {
        isRunning = false;
        var result = new StringBuilder();
        for (var key : dictOfNameAndTags.keySet()) {
            result.append(key);
            var pair = dictOfNameAndTags.get(key);
            result.append("-");
            result.append(pair.first);
            if (!pair.second.equals("")) {
                result.append("-");
                result.append(pair.second);
            }
            result.append('\n');
        }
        FileWorker.writeToFile(pathToFileWithNames, result.toString());
    }
}
