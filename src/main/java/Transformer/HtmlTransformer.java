package Transformer;

import Entity.Movie;
import Entity.Product;
import Utils.Utils;
import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import static Utils.Utils.transformTitle;

public class HtmlTransformer {
    enum MovieType{
        Normal, Prime, Null
    }

    private MovieType getDocType(Document doc){
        Elements lisOld = doc.select("#detail-bullets > table > tbody > tr > td > div > ul > li");
        Elements lisNew = doc.select("#a-page > div.av-page-desktop.avu-retail-page > div.avu-content.avu-section > div > div > div.DVWebNode-detail-atf-wrapper.DVWebNode > div.av-detail-section > div > div._2vWb4y.dv-dp-node-meta-info > div > div");
        boolean normal = lisOld.size() != 0;
        boolean prime = lisNew.size() != 0;
        if(normal){
            return MovieType.Normal;
        }else if(prime){
            return MovieType.Prime;
        }else{
            return MovieType.Null;
        }
    }

    public Movie parseDoc(String productId, Document doc){
        MovieType type = getDocType(doc);
        switch (type){
            case Null:
                //never should happen
                //System.out.println("parseDoc遇到Null");
                //System.exit(-1);
                break;
            case Prime:
                return parsePrimeDoc(productId, doc);
            case Normal:
                return parseNormalDoc(productId, doc);
        }
        return null;
    }

    private Movie parseNormalDoc(String selfProductId, Document doc) {
        try{
            ArrayList<Product> products = new ArrayList<>();
            ArrayList<String> actors = new ArrayList<>();
            ArrayList<String> directors = new ArrayList<>();
            String title = null;
            Integer runTime = null;
            String runTimeStr = null;
            String releaseDate = null;
            Integer releaseYear = null;
            Integer releaseMonth = null;
            Integer releaseDay = null;
            Integer releaseWeekDay = null;
            Double ranking = null;
            Movie movie = new Movie();

            Elements titleElements = doc.select("#productTitle");
            title = titleElements.first().text().trim();
            title = transformTitle(title);
            //System.out.println(title);
            movie.setTitle(title);
            Elements formats = doc.select("#tmmSwatches > ul > li");
            for (Element format : formats) {
                //System.out.println(format);
                String a = format.select("a").first().attr("href");
                //System.out.println(a);
                String productId = Utils.getProductIdFromUrl(a);
                Elements formatAndPrice = format.select("span > span > span > a > span");
                if(formatAndPrice.size() != 2){
                    continue;
                }
                String formatStr = formatAndPrice.first().text();
                String priceStr = Utils.parseMoneyFromStr(formatAndPrice.last().text());
                Product product = new Product();
                product.setFormat(formatStr);
                product.setPrice(priceStr);
                product.setProductId(productId);
                products.add(product);
            }
            Elements contents = doc.select("#detail-bullets > table > tbody > tr > td > div > ul > li");
            for (Element content : contents) {
                //System.out.println(content);
                String b = content.select("b").first().text();
                if ("Actors:".equals(b)) {
                    //全部演员
                    Elements actorsEles = content.select("a");
                    for (Element actorsEle : actorsEles) {
                        String actorName = actorsEle.text();
                        actors.add(actorName);
                        //System.out.println(actorName);
                    }
                }else if("Directors:".equals(b)){
                    //全部导演
                    Elements directorsEles = content.select("a");
                    for (Element directorsEle : directorsEles) {
                        String directorName = directorsEle.text();
                        //System.out.println(directorName);
                        directors.add(directorName);
                    }
                }else if("DVD Release Date:".equals(b)){
                    releaseDate = content.text().split(": ")[1];
                    int[] yearMonthDayWeekDay = Utils.parseYearMonthDayWeekDay(releaseDate);
                    if(yearMonthDayWeekDay == null){
                        continue;
                    }
                    releaseYear = yearMonthDayWeekDay[0];
                    releaseMonth = yearMonthDayWeekDay[1];
                    releaseDay = yearMonthDayWeekDay[2];
                    releaseWeekDay = yearMonthDayWeekDay[3];
                    //System.out.println(releaseDate);
                }else if("Run Time:".equals(b)){
                    runTimeStr = content.text().split(": ")[1];
                    runTime = Utils.parseNormalRunTime(runTimeStr);
                    //System.out.println(runTime);
                }else if("Average Customer Review:".equals(b)){
                    //System.out.println(content);
                    Elements elements = content.select("span > span > a > i > span");
                    if(elements.size() != 1){
                        continue;
                    }
                    String rankingStr = elements.first().text();
                    if(!rankingStr.contains(" out")){
                        continue;
                    }
                    rankingStr = rankingStr.split(" out")[0];
                    try{
                        ranking = Double.parseDouble(rankingStr);
                    }catch (Exception e){
                        continue;
                    }
                }
            }
            movie.setTitle(title);
            movie.setActors((ArrayList<String>)Utils.removeDuplicated(actors));
            movie.setDirectors((ArrayList<String>)Utils.removeDuplicated(directors));
            movie.setProducts((ArrayList<Product>)Utils.removeDuplicated(products));
            movie.setRanking(ranking);
            movie.setRunTime(runTime);
            movie.setReleaseDate(releaseDate);
            movie.setReleaseYear(releaseYear);
            movie.setReleaseDay(releaseDay);
            movie.setReleaseMonth(releaseMonth);
            movie.setReleaseWeekDay(releaseWeekDay);
            return movie;
        }catch (Exception e){
            e.printStackTrace();
            //System.out.println("error in parsing " + selfProductId);
            return null;
        }
    }


    private Movie parsePrimeDoc(String selfProductId, Document doc){
        try{
            ArrayList<Product> products = new ArrayList<>();
            ArrayList<String> actors = new ArrayList<>();
            ArrayList<String> supportingActors = new ArrayList<>();
            ArrayList<String> starrings = new ArrayList<>();
            ArrayList<String> directors = new ArrayList<>();
            ArrayList<String> genres = new ArrayList<>();
            Integer releaseYear = null;
            String releaseYearStr = null;
            Integer runTime = null;
            String runTimeStr = null;
            String title = null;
            Double ranking = null;
            Movie movie = new Movie();

            title = doc.select("#a-page > div.av-page-desktop.avu-retail-page > div.avu-content.avu-section > div > div > div.DVWebNode-detail-atf-wrapper.DVWebNode > div.av-detail-section > div > h1").first().text();
            title = transformTitle(title);
            //System.out.println(title);
            Elements introLine = doc.select("#a-page > div.av-page-desktop.avu-retail-page > div.avu-content.avu-section > div > div > div.DVWebNode-detail-atf-wrapper.DVWebNode > div.av-detail-section > div > div.Ljc8d7._3-UIK-._38qi5F.dv-node-dp-badges.uAeEjV");
            Element t = introLine.select("[data-automation-id=runtime-badge]").first();
            runTimeStr = t == null ? null : t.text();
            runTime = Utils.parsePrimeRunTime(runTimeStr);
            t = introLine.select("[data-automation-id=release-year-badge]").first();
            releaseYearStr = t == null ? null : t.text();
            try{
                releaseYear = releaseYearStr == null ? null : Integer.parseInt(releaseYearStr);
            }catch (Exception e){
                //
            }

            Elements intros = doc.select("#a-page > div.av-page-desktop.avu-retail-page > div.avu-content.avu-section > div > div > div.DVWebNode-detail-atf-wrapper.DVWebNode > div.av-detail-section > div > div._2vWb4y.dv-dp-node-meta-info > div > div").first().children();
            for(Element e : intros){
                String type = e.select("dt > span").first().text();
                if("Genres".equals(type)){
                    Elements es = e.select("dd > a");
                    for (Element ee : es) {
                        String genre = ee.text();
                        //System.out.println(genre);
                        genres.add(genre);
                    }
                }else if("Director".equals(type)){
                    Elements es = e.select("dd > a");
                    for (Element ee : es) {
                        String director = ee.text();
                        //System.out.println(director);
                        directors.add(director);
                    }
                }else if("Starring".equals(type)){
                    Elements es = e.select("dd > a");
                    for (Element ee : es) {
                        String[] starringsSplited = ee.text().split(",");
                        for (int i = 0; i < starringsSplited.length; i++) {
                            starringsSplited[i] = starringsSplited[i].trim();
                            starrings.add(starringsSplited[i]);
                        }
                        //System.out.println(starring);
                    }
                }
                //System.out.println(e);
            }

            //String primePrice = doc.select("#tvod-btn-B07PQNR23J-ab > button > span").text();
            ////System.out.println(primePrice);
            Product selfProduct = new Product();
            selfProduct.setProductId(selfProductId);
            selfProduct.setFormat("Prime Video");
            products.add(selfProduct);

            Elements contents = doc.select("#btf-product-details > div").first().children();
            for (Element content : contents) {
                String type = content.select("dt > span").first().text();
                if("Supporting actors".equals(type)){

                    for (Element element : content.select("a")) {
                        String supportingActor = element.text();
                        //System.out.println(supportingActor);
                        supportingActors.add(supportingActor);
                    }
                }
            }

            actors.addAll(starrings);
            actors.addAll(supportingActors);

            Element otherFormatsEle = doc.select("#a-page > div.av-page-desktop.avu-retail-page > div:nth-child(25) > div > section:nth-child(5) > div > div > div").first();
            if(otherFormatsEle != null){
                Elements otherFormats = otherFormatsEle.children();
                for (Element otherFormat : otherFormats) {
                    String a = otherFormat.attr("href");
                    String productId = Utils.getProductIdFromUrl(a);
                    //System.out.println(productId);
                    String formatAndPriceFullText = otherFormat.select("div > div").first().text().trim().replace(" +", " ");
                    String[] splits = formatAndPriceFullText.split(" ");
                    String format = splits[0];
                    String price = splits[splits.length - 1];
                    //System.out.println(format);
                    //System.out.println(price);
                    Product p = new Product();
                    p.setProductId(productId);
                    p.setFormat(format);
                    p.setPrice(price);
                    products.add(p);
                }
            }

            Element rankingStrEle = doc.select("#reviewsMedley > div > div.a-fixed-left-grid-col.a-col-left > div.a-section.a-spacing-none.a-spacing-top-mini.cr-widget-ACR > div.a-fixed-left-grid.AverageCustomerReviews.a-spacing-small > div > div.a-fixed-left-grid-col.aok-align-center.a-col-right > div > span > span > a > span").first();
            if(rankingStrEle != null){
                String rankingStr = rankingStrEle.text();
                //System.out.println(rankingStr);
                if(rankingStr.contains(" out")){
                    try{
                        ranking = Double.parseDouble(rankingStr.split(" out")[0]);
                    }catch (Exception e){
                        //
                    }
                }
            }
            movie.setDirectors((ArrayList<String>) Utils.removeDuplicated(directors));
            movie.setActors((ArrayList<String>) Utils.removeDuplicated(actors));
            movie.setProducts((ArrayList<Product>) Utils.removeDuplicated(products));
            movie.setTitle(title);
            movie.setGenres((ArrayList<String>) Utils.removeDuplicated(genres));
            movie.setStarrings((ArrayList<String>) Utils.removeDuplicated(starrings));
            movie.setSupportingActors((ArrayList<String>) Utils.removeDuplicated(supportingActors));
            movie.setRanking(ranking);
            movie.setReleaseYear(releaseYear);
            movie.setRunTime(runTime);
            return movie;
        }catch (Exception e){
            e.printStackTrace();
            //System.out.println("error in parsing " + selfProductId);
            return null;
        }

    }


    public static void main(String[] args) {
        HtmlTransformer htmlTransformer = new HtmlTransformer();
        File file = new File("E:\\TestMovieInfos\\htmls\\B000V3NPJE.html");
        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder builder = new StringBuilder();
            while(true){
                java.lang.String line = bufferedReader.readLine();
                if(line == null){
                    break;
                }
                builder.append(line);
            }
            Document doc = Jsoup.parse(builder.toString());
            MovieType type = htmlTransformer.getDocType(doc);
            Movie movie = htmlTransformer.parseDoc("", doc);
            //System.out.println(movie);
            String jsonStr = JSON.toJSONString(movie, true);
            //System.out.println(jsonStr);
            ////System.out.println(doc.html());
            //System.out.println(type);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
