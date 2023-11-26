package edu.uci.ics.fabflixmobile.data.model;

/**
 * Movie class that captures movie information for movies retrieved from MovieListActivity
 */
public class Movie {
    private final String id;
    private final String name;
    private final short year;
    private final String director;
    private final String[] genres;
    private final String[] stars;

//    public Movie(String name, short year, String director) {
//        this.name = name;
//        this.year = year;
//        this.director = director;
//    }

    public Movie(String id, String name, short year, String director, String[] genres, String[] stars) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.director = director;
        this.genres = genres;
        this.stars = stars;
    }

    public String getID() {
        return id;
    }
    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }
    public String getDirector() {
        return director;
    }

    public String getGenresString() {
        String result = "";
        if (genres != null && genres.length > 0) {
            result = genres[0];
            for (int i = 1; i < genres.length; i++) {
                result += ", " + genres[i];
            }
        }
        return result;
    }

    public String getStarsString() {
        String result = "";
        if (stars != null && stars.length > 0) {
            result = stars[0];
            for (int i = 1; i < stars.length; i++) {
                result += ", " + stars[i];
            }
        }
        return result;
    }
}