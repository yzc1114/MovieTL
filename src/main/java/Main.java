import Entity.Movie;
import Loader.MovieDataLoader;
import Transformer.HtmlTransformer;
import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static class Product{
        String content;
        String productId;
        Product(String c, String i) { content = c; productId = i;}
    }

    private static ExecutorService esProvider = Executors.newFixedThreadPool(10);

    private static ExecutorService esConsumer = Executors.newFixedThreadPool(1);

    private static final LinkedBlockingQueue<Product> cache = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        //init provider;
        String dir = "/Users/purchaser/Desktop/MovieInfos/htmls";
        String resPath = "/Users/purchaser/Desktop/MovieInfos";
        File htmlsDir = new File(dir);
        File desPath = new File(resPath);
        if(!desPath.isDirectory()){
            System.out.println("目标位置不是文件夹！");
            return;
        }
        HtmlTransformer transformer = new HtmlTransformer();
        MovieDataLoader loader = new MovieDataLoader();
        esConsumer.submit(() -> {
            while(true){
                Product product = cache.poll();

                if(product == null){
                    if(esProvider.isTerminated()){
                        return;
                    }
                    continue;
                }
                String id = product.productId;
                loader.consumeMovie(transformer.parseDoc(id, Jsoup.parse(product.content)));
            }
        });

        File[] htmls = htmlsDir.listFiles();
        if(htmls == null){
            System.out.println("文件夹为空！");
            return;
        }

        for (File html : htmls) {
            if(html.getName().split("\\.").length != 2)
                continue;
            esProvider.submit(() -> {
                while(cache.size() > 100);
                String content = readFile(html);
                String id = html.getName().split("\\.")[0];
                System.out.println("processing " + html.getName());
                while(!cache.offer(new Product(content, id)));
            });

        }

        esProvider.shutdown();
        esConsumer.shutdown();
        while(!esProvider.isTerminated() || !esConsumer.isTerminated()){
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        HashMap<Integer, ArrayList<Movie>> runTime2Movies = loader.getRunTime2Movies();
        StringBuilder jsonBuilder = new StringBuilder();
        runTime2Movies.forEach((k, v) -> {
            for (Movie movie : v) {
                jsonBuilder.append(JSON.toJSONString(movie));
            }
        });
        saveFile(resPath, jsonBuilder.toString().replaceAll("\n", ""));
    }

//    private static void processFiles(File[] files, HtmlTransformer transformer, MovieDataLoader loader) {
//        for (int i = 0; i < 5000 && i < files.length; i++) {
//            File htmlFile = files[i];
//            System.out.println("processing " + htmlFile.getName());
//            String content = readFile(htmlFile);
//            if(content == null){
//                continue;
//            }
//            String id = htmlFile.getName().split("\\.html")[0];
//            loader.consumeMovie(transformer.parseDoc(id, Jsoup.parse(content)));
//        }
//    }

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
            BufferedWriter bf = new BufferedWriter(new FileWriter(new File(path, "res.json")));
            bf.write(content);
            bf.flush();
            bf.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
