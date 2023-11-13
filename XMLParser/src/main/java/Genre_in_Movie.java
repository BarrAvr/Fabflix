import java.util.Objects;

public class Genre_in_Movie {

    private final int genreId;

    private final String movieId;

    public Genre_in_Movie(int genreId, String movieId) {
        this.genreId = genreId;
        this.movieId = movieId;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getMovieId() { return movieId; }

    public String toString() {

        return "genreId:" + getGenreId() + ", " +
                "movieId:" + getMovieId() +  ".";
    }

    public String toSQLInsertString() {
        return String.format("INSERT INTO genres_in_movies (genreId, movieId) VALUES (%s, '%s')", getGenreId(), getMovieId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Genre_in_Movie other = (Genre_in_Movie) obj;
        return this.movieId.equals(other.movieId) && this.genreId == other.genreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, movieId);
    }

}
