package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import Utils.Utils;

public class Movie implements Serializable {
    private String title = null;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<String> genres = new ArrayList<>();
    private ArrayList<String> starrings = new ArrayList<>();
    private ArrayList<String> supportingActors = new ArrayList<>();
    private ArrayList<String> actors = new ArrayList<>();
    private ArrayList<String> directors = new ArrayList<>();
    private Integer runTime = null;
    private String releaseDate = null;
    private Integer releaseMonth = null;
    private Integer releaseDay = null;
    private Integer releaseYear = null;
    private Integer releaseWeekDay = null;
    private Double ranking = null;

    public Integer getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(Integer releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public Integer getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(Integer releaseDay) {
        this.releaseDay = releaseDay;
    }

    public Integer getReleaseWeekDay() {
        return releaseWeekDay;
    }

    public void setReleaseWeekDay(Integer releaseWeekDay) {
        this.releaseWeekDay = releaseWeekDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        if(getTitle() == null){
            return false;
        }
        if(getRunTime() == null){
            return getTitle().equals(movie.getTitle());
        }
        boolean titleSame = Utils.isSame(getTitle(), movie.getTitle());
        boolean runTimeSame = getRunTime().equals(movie.getRunTime());
        return titleSame && runTimeSame;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getActors(), getDirectors());
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
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

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
