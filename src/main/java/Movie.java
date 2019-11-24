import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
    private String title = null;
    private ArrayList<Product> products = null;
    private ArrayList<String> genres = null;
    private ArrayList<String> starrings = null;
    private ArrayList<String> supportingActors = null;
    private ArrayList<String> actors = null;
    private ArrayList<String> directors = null;
    private String runTime = null;
    private String releaseDate = null;
    private Double ranking = null;

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", products=" + products +
                ", genres=" + genres +
                ", starrings=" + starrings +
                ", supportingActors=" + supportingActors +
                ", actors=" + actors +
                ", directors=" + directors +
                ", runTime='" + runTime + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", ranking=" + ranking +
                '}';
    }

    public Double getRanking() {
        return ranking;
    }

    public void setRanking(Double ranking) {
        this.ranking = ranking;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getStarrings() {
        return starrings;
    }

    public void setStarrings(ArrayList<String> starrings) {
        this.starrings = starrings;
    }

    public ArrayList<String> getSupportingActors() {
        return supportingActors;
    }

    public void setSupportingActors(ArrayList<String> supportingActors) {
        this.supportingActors = supportingActors;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<String> directors) {
        this.directors = directors;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
