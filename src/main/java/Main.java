import Entity.Movie;
import Loader.MovieDataLoader;
import Transformer.HtmlTransformer;
import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        String dir = "E:\\TestMovieInfos\\htmls";
        String resPath = "E:\\TestMovieInfos\\res.json";
        File htmlsDir = new File(dir);
        File[] oldFiles = htmlsDir.listFiles((f) -> f.length() > 500 * 1000);
        File[] newFiles = htmlsDir.listFiles((f) -> f.length() <= 500 * 1000);
        assert oldFiles != null && newFiles != null;
        HtmlTransformer transformer = new HtmlTransformer();
        MovieDataLoader loader = new MovieDataLoader();
        processFiles(oldFiles, transformer, loader);
        processFiles(newFiles, transformer, loader);
        ConcurrentHashMap<Integer, ArrayList<Movie>> runTime2Movies = loader.getRunTime2Movies();
        StringBuilder jsonBuilder = new StringBuilder();
        runTime2Movies.forEach((k, v) -> {
            for (Movie movie : v) {
                jsonBuilder.append(JSON.toJSONString(movie));
            }
        });
        saveFile(resPath, jsonBuilder.toString().replaceAll("\n", ""));
    }

    private static void processFiles(File[] files, HtmlTransformer transformer, MovieDataLoader loader) {
        for (int i = 0; i < 5000 && i < files.length; i++) {
            File htmlFile = files[i];
            System.out.println("processing " + htmlFile.getName());
            String content = readFile(htmlFile);
            if(content == null){
                continue;
            }
            String id = htmlFile.getName().split("\\.html")[0];
            loader.consumeMovie(transformer.parseDoc(id, Jsoup.parse(content)));
        }
    }

    private static String readFile(File html){
        try{
            FileReader fileReader = new FileReader(html);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder builder = new StringBuilder();
            while(true){
                java.lang.String line = bufferedReader.readLine();
                if(line == null){
                    break;
                }
                builder.append(line);
            }
            return builder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void saveFile(String path, String content){
        try{
            BufferedWriter bf = new BufferedWriter(new FileWriter(new File(path)));
            bf.write(content);
            bf.flush();
            bf.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
