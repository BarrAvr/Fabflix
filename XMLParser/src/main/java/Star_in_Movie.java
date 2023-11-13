import java.util.Objects;

public class Star_in_Movie {

    private final String starId;

    private final String movieId;

    public Star_in_Movie(String starId, String movieId) {
        this.starId = starId;
        this.movieId = movieId;
    }

    public String getStarId() {
        return starId;
    }

    public String getMovieId() { return movieId; }

    public String toString() {

        return "starId:" + getStarId() + ", " +
                "movieId:" + getMovieId() +  ".";
    }

    public String toSQLInsertString() {
        return String.format("INSERT INTO stars_in_movies (starId, movieId) VALUES ('%s', '%s')", getStarId(), getMovieId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Star_in_Movie other = (Star_in_Movie) obj;
        return this.movieId.equals(other.movieId) && this.starId.equals(other.starId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(starId, movieId);
    }
}
