package Loader;

import Entity.Movie;
import Entity.Product;
import Utils.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class MovieDataLoader {

    private final HashMap<Integer, ArrayList<Movie>> runTime2Movies = new HashMap<>();

    public void consumeMovie(Movie newMovie){
        if(newMovie == null){
            return;
        }
        Integer currRunTime = newMovie.getRunTime() == null ? 0 : newMovie.getRunTime();
        ArrayList<Movie> movies;

        if(!runTime2Movies.containsKey(currRunTime)){
            ArrayList<Movie> ms = new ArrayList<>(1);
            ms.add(newMovie);
            runTime2Movies.put(currRunTime, ms);
            return;
        }

        movies = runTime2Movies.get(currRunTime);

        Movie sameMovie = null;
        for (Movie m : movies) {
            if(m.equals(newMovie)){
                sameMovie = m;
                break;
            }
        }
        if(sameMovie == null){
            movies.add(newMovie);
            return;
        }
        addRightToLeft(sameMovie.getActors(), newMovie.getActors());
        addRightToLeft(sameMovie.getDirectors(), newMovie.getDirectors());
        addRightToLeft(sameMovie.getGenres(), newMovie.getGenres());
        addRightToLeft(sameMovie.getStarrings(), newMovie.getStarrings());
        addRightToLeft(sameMovie.getSupportingActors(), newMovie.getSupportingActors());
        for (Product s : newMovie.getProducts()) {
            if(!Utils.hasSameInArray(s, sameMovie.getProducts())){
                sameMovie.getProducts().add(s);
            }
        }
        Method[] movieMethods = Movie.class.getMethods();
        try{
            for (Method movieMethod : movieMethods) {
                if(movieMethod.getName().startsWith("get")){
                    Object o = movieMethod.invoke(sameMovie);
                    if(o == null){
                        Method setMethod = Movie.class.getMethod("set" + movieMethod.getName().replaceAll("get", ""), movieMethod.getReturnType());
                        setMethod.invoke(sameMovie, movieMethod.invoke(newMovie));
                    }
                }
            }
        }catch (Exception e){
            //Never should happen
            System.out.println("plz debug");
        }

    }

    private void addRightToLeft(ArrayList<String> l, ArrayList<String> r){
        for (String s : r) {
            if(!Utils.hasSameInArray(s, l)){
                l.add(s);
            }
        }
    }

    public HashMap<Integer, ArrayList<Movie>> getRunTime2Movies() {
        return runTime2Movies;
    }

    public static void main(String[] args) {
        //Test
        Movie nullM = new Movie();
        nullM.setTitle("aaaa");
        Movie left = new Movie();
        left.setTitle("fua");
        left.setRunTime(1);
        Movie right = new Movie();
        right.setTitle("fu");
        right.setReleaseMonth(2);
        right.setReleaseDay(1);
        right.setActors(new ArrayList<String>(){{add("asd"); add("asd");}});
        right.setDirectors(new ArrayList<String>(){{add("as"); add("fucccc");}});
        right.setRunTime(1);
        MovieDataLoader loader = new MovieDataLoader();
        loader.consumeMovie(nullM);
        loader.consumeMovie(left);
        loader.consumeMovie(right);
        System.out.println(loader.getRunTime2Movies());
    }
}
