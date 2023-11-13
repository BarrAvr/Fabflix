import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;


public class MovieDBDomParser {

    FileWriter fileWriter;
    PrintWriter printWriter;
    String filePath = "ParsingMetaData.txt";


    //<movieId, movie>
    HashMap<String, Movie> moviesHashmap = new HashMap<String, Movie>();
    HashMap<String, Movie> initialMoviesHashmap = new HashMap<String, Movie>();

    //key = name, value = genre_id
    HashMap<String, Integer> genreHashMap = new HashMap<String, Integer>();
    HashMap<String, Integer> initialGenreHashMap = new HashMap<String, Integer>();
//    int initNumGenres = 0;

    List<Genre_in_Movie> genreInMovies = new ArrayList<>();
    //genreId, movieId
    List<Genre_in_Movie> initialGenreInMovies = new ArrayList<>();

    //<stageName, star>
    HashMap<String, Star> starHashMap = new HashMap<String, Star>();
    HashMap<String, Star> initialStarHashMap = new HashMap<String, Star>();
    int initNumStars = 0;

    List<Star_in_Movie> starInMovies = new ArrayList<>();
    //starId, movieId
    List<Star_in_Movie> initialStarInMovies = new ArrayList<>();


    Document dom;

    private DataSource dataSource;

    public void runActorsTest() {

        try {
            fileWriter = new FileWriter(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printWriter = new PrintWriter(fileWriter);

        populateInitialStarHashmap();
//        System.out.println("Stars initially populated with " + initialStarHashMap.size() + " entries");
//        System.out.println("Stars table initially has " + initNumStars + " entries");
        parseXmlFile("actors63.xml");
        parseStarsDocument();
        insertData();
    }

    private void insertActorData() {
//        System.out.println("Star SQL Inserts:");
        AtomicInteger i = new AtomicInteger();
        starHashMap.forEach((name, star) -> {
//            System.out.println(star.toSQLInsertString());
            i.getAndIncrement();
        });
//        System.out.println("Values inserted: " + i);
    }

    public void runExample() {

        try {
            fileWriter = new FileWriter(filePath);
            printWriter = new PrintWriter(fileWriter);

            populateInitialMovieHashmap();
            populateInitialGenreHashmap();
            populateInitialStarHashmap();
            populateInitialStarInMoviesHashmap();
            populateInitialGenreInMoviesHashmap();

            // parse the xml file and get the dom object
            parseXmlFile("mains243.xml");
            parseMoviesDocument();

            parseXmlFile("actors63.xml");
            parseStarsDocument();

            parseXmlFile("casts124.xml");
            parseStarsInMoviesDocument();

            // iterate through the list and print the data
            insertData();

//            printWriter.close();
//            fileWriter.close();

//            System.out.println("Parsing Meta Data has been written to the file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateInitialMovieHashmap() {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            String sqlQuery = "SELECT * from movies";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String id = rs.getString("id");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                String director = rs.getString("director");
                initialMoviesHashmap.put(id, new Movie(id, title, year, director));
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
//            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
//        initNumGenres = genreHashMap.size();
        printWriter.println("Movies initally populated with " + initialMoviesHashmap.size() + " entries");
    }

    private void populateInitialGenreHashmap() {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            String sqlQuery = "SELECT * from genres";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int genreId = rs.getInt("id");
                String genreName = rs.getString("name");
                initialGenreHashMap.put(genreName, genreId);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
//            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
//        initNumGenres = genreHashMap.size();
        printWriter.println("Genres initially populated with " + initialGenreHashMap.size() + " entries");
    }

    private void populateInitialStarHashmap() {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            String sqlQuery = "SELECT * from stars";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String starId = rs.getString("id");
                String stageName = rs.getString("name");
//                int birthYear = rs.getInt("birthYear");
                int birthYear = -1;
                if (rs.getObject("birthYear") != null) {
                    // The column is not NULL, it's safe to retrieve the int value
                    birthYear = rs.getInt("birthYear");
                }
                initialStarHashMap.put(stageName, new Star(starId, stageName, birthYear));
                initNumStars++;
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
//            System.out.println(e.toString());
            throw new RuntimeException(e);
        }
        printWriter.println("Stars table initially has " + initNumStars + " entries");
        printWriter.println("Stars hashmap initially populated with " + initialStarHashMap.size() + " entries");
    }

    private void populateInitialStarInMoviesHashmap() {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            String sqlQuery = "SELECT * from stars_in_movies";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String starId = rs.getString("starId");
                String movieId = rs.getString("movieId");
                initialStarInMovies.add(new Star_in_Movie(starId, movieId));
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
//            System.out.println(e.toString());
            throw new RuntimeException(e);
        }

        printWriter.println("stars_in_movies initially populated with " + initialStarInMovies.size() + " entries");
    }

    private void populateInitialGenreInMoviesHashmap() {
        String loginUser = "mytestuser";
        String loginPasswd = "My6$Password";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            String sqlQuery = "SELECT * from genres_in_movies";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int genreId = rs.getInt("genreId");
                String movieId = rs.getString("movieId");
                initialGenreInMovies.add(new Genre_in_Movie(genreId, movieId));
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
//            System.out.println(e.toString());
            throw new RuntimeException(e);
        }

        printWriter.println("genres_in_movies initially populated with " + initialGenreInMovies.size() + " entries");
    }

    private void parseXmlFile(String fileName) {
        // get the factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {

            // using factory get an instance of document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
//            dom = documentBuilder.parse("mains243.xml");
            dom = documentBuilder.parse(fileName);

        } catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }
    }

    private void parseMoviesDocument() {
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        NodeList dfList = documentElement.getElementsByTagName("directorfilms");

        for (int i = 0; i < dfList.getLength(); i++) {
            Element directorElement = (Element) documentElement.getElementsByTagName("director").item(i);
            String director = getTextValue(directorElement, "dirname");
//            System.out.println("Director: " + director);

            Element moviesElement = (Element) documentElement.getElementsByTagName("films").item(i);

            NodeList filmNodes = moviesElement.getElementsByTagName("film");

//            ExecutorService executor = Executors.newFixedThreadPool(6);
//            for (int l = 0; l < 6; l++) {
//                int finalL = l;
//                executor.submit(() -> {
//                    int start = finalL * filmNodes.getLength()/5;
//                    int end = (finalL + 1) * filmNodes.getLength()/5;
//                    if(finalL == 5 && end != filmNodes.getLength()){
//                        end = filmNodes.getLength();
//                    }
                    for (int j = 0; j < filmNodes.getLength(); j++) {
//                    for (int j = start; j < end; j++) {
                        Element filmElement = (Element) filmNodes.item(j);
                        String id = getTextValue(filmElement, "fid");
                        String title = getTextValue(filmElement, "t");
                        int year = getIntValue(filmElement, "year");
                        if(year != -1){
                            if(id != null){
//                                if(!initialMoviesHashmap.containsKey(id)){
                                if(true){
                                    if(title == null){
                                        //todo
                                        printWriter.println("Movie Parser Error: Title set to empty string since movie entry with id: " + id + " has no title.");
                                    }
                                    Movie film = new Movie(id, title, year, director);
                                    moviesHashmap.put(film.getId(), film);
                                }else{
                                    //todo
//                                    System.out.println("Movie Parser Error: Insert prevented because movie id already exists in table.");
                                }
                                parseGenres(filmElement, id);
                            }else{
                                //todo
                                printWriter.println("Movie Parser Error: Insert prevented because movie entry as no id.");
                            }
                        }else{
                            //todo
                            printWriter.println("Movie Parser Error: Insert prevented because movie entry with id: " + id + " has invalid year value.");
                        }
                    }
//                });
//            }
//            executor.shutdown();
//            while (!executor.isTerminated()) {}
        }
    }

    private void parseStarsDocument() {
        // get the document root Element
//        System.out.println("Start Parsing:");
        Element documentElement = dom.getDocumentElement();

        NodeList actorList = documentElement.getElementsByTagName("actor");
//        System.out.println("actor list size: " + actorList.getLength());
//        ExecutorService executor = Executors.newFixedThreadPool(6);
//        for (int l = 0; l < 6; l++) {
//            int finalL = l;
//            executor.submit(() -> {
//                int start = finalL * actorList.getLength()/5;
//                int end = (finalL + 1) * actorList.getLength()/5;
//                if(finalL == 5 && end != actorList.getLength()){
//                    end = actorList.getLength();
//                }
                for (int i = 0; i < actorList.getLength(); i++) {
//                for (int i = start; i < end; i++) {
                        Element actorElement = (Element) actorList.item(i);
                        String stageName = getTextValue(actorElement, "stagename");

                        if(!starHashMap.containsKey(stageName) && !initialStarHashMap.containsKey(stageName)){
                            int birthYear = getIntValue(actorElement, "dob");
                            if(birthYear == -1) {
                                printWriter.println("Stars Parser Error: Star with stageName " + stageName + " has an invalid birthYear value");
                            }
                            String starId = "s" + (initNumStars + starHashMap.size());
//                            String starId = "s" + (initialStarHashMap.size() + starHashMap.size());
                            starHashMap.put(stageName, new Star(starId, stageName, birthYear));
                        }else{
//                            System.out.println("Stars Parser Error: Insert prevented because star with stageName " + stageName + " already exists in table");
                        }
                }
//            });
//        }
//        executor.shutdown();
//        while (!executor.isTerminated()) {}
    }

    private void parseStarsInMoviesDocument() {
//        System.out.println("Parsing casts...");
        Element documentElement = dom.getDocumentElement();

        NodeList dirfilmsList = documentElement.getElementsByTagName("dirfilms");
//        System.out.println("Number of dirfilms = " + dirfilmsList.getLength());
        for (int i = 0; i < dirfilmsList.getLength(); i++) {
            Element dirFilmElement = (Element) dirfilmsList.item(i);
            NodeList filmcList = dirFilmElement.getElementsByTagName("filmc");
//            System.out.println("Number of filmc = " + filmcList.getLength());

            for(int j = 0; j < filmcList.getLength(); j++){
                Element filmcElement = (Element) filmcList.item(j);
                NodeList mList = filmcElement.getElementsByTagName("m");
                String movieId = getTextValue((Element) mList.item(0), "f");
//                System.out.println("Number of mList = " + mList.getLength() + " with movieId = " + movieId);
                if(moviesHashmap.containsKey(movieId) || initialMoviesHashmap.containsKey(movieId)){
                    for(int k = 0; k < mList.getLength(); k++){
                        String stageName = getTextValue((Element) mList.item(k), "a");
                        if(starHashMap.containsKey(stageName)){
                            String starId = starHashMap.get(stageName).getId();
                            Star_in_Movie starInMovie = new Star_in_Movie(starId, movieId);
                            if(!initialStarInMovies.contains(starInMovie) && !starInMovies.contains(starInMovie)){
                                starInMovies.add(starInMovie);
                            }else{
//                                System.out.println("Stars In Movies Parser Error: Insert prevented because starId " + stageName + " and movieId " + movieId + " already exist in the table");
                            }
                        }else if(initialStarHashMap.containsKey(stageName)){
                            String starId = initialStarHashMap.get(stageName).getId();
                            Star_in_Movie starInMovie = new Star_in_Movie(starId, movieId);
                            if(!initialStarInMovies.contains(starInMovie) && !starInMovies.contains(starInMovie)){
                                starInMovies.add(starInMovie);
                            }else{
//                                System.out.println("Stars In Movies Parser Error: Insert prevented because starId " + stageName + " and movieId " + movieId + " already exist in the table");
                            }
                        }else{
//                            System.out.println("Stars In Movies Parser Error: Insert prevented because no such star with stageName " + stageName);
                        }
                    }
                }else{
//                    System.out.println("Stars In Movies Parser Error: Insert prevented because no such movie with id " + movieId);
                }
            }
        }
    }

    /**
     * It takes an employee Element, reads the values in, creates
     * an Employee object for return
     */
    private Movie parseFilm(Element element) {

        // for each <employee> element get text or int values of
        // name ,id, age and name
        String id = getTextValue(element, "fid");
        String title = getTextValue(element, "t");
        int year = getIntValue(element, "year");
        String director = "";

        // create a new Employee with the value read from the xml nodes
        return new Movie(id, title, year, director);
    }

    private Movie parseFilm(Element element, String dir) {

        // for each <employee> element get text or int values of
        // name ,id, age and name
        String id = getTextValue(element, "fid");
        String title = getTextValue(element, "t");
        int year = getIntValue(element, "year");

        // create a new Employee with the value read from the xml nodes
        return new Movie(id, title, year, dir);
    }

    private void parseGenres(Element element, String movieId) {

        Element genreElement = (Element) element.getElementsByTagName("cats").item(0);
        if(genreElement != null){
            NodeList genreNodes = genreElement.getElementsByTagName("cat");
            for (int j = 0; j < genreNodes.getLength(); j++) {
                Node firstChild = genreNodes.item(j).getFirstChild();
                if(firstChild != null){
                    String name = firstChild.getNodeValue();
                    if(name != null){
                        name = name.trim();
                        if(!genreHashMap.containsKey(name) && !initialGenreHashMap.containsKey(name)){
                            int genreId = initialGenreHashMap.size() + genreHashMap.size();
                            genreHashMap.put(name, genreId);
                            Genre_in_Movie genreInMovie = new Genre_in_Movie(genreId, movieId);
                            if(!initialGenreInMovies.contains(genreInMovie) && !genreInMovies.contains(genreInMovie)){
                                genreInMovies.add(genreInMovie);
                            }else{
//                                System.out.println("Genres In Movies Parser Error: Insert prevented because genreId " + genreId + " and movieId " + movieId + " already exist in the table");
                            }
                        }else{
                            int genreId = 0;
                            if(genreHashMap.get(name) != null){
                                genreId = genreHashMap.get(name);
                            }else{
                                genreId = initialGenreHashMap.get(name);
                            }
//                        int genreId = genreHashMap.get(name);
                            Genre_in_Movie genreInMovie = new Genre_in_Movie(genreId, movieId);
                            if(!initialGenreInMovies.contains(genreInMovie) && !genreInMovies.contains(genreInMovie)){
                                genreInMovies.add(genreInMovie);
                            }else{
//                                System.out.println("Genres In Movies Parser Error: Insert prevented because genreId " + genreId + " and movieId " + movieId + " already exist in the table");
                            }
                        }
                    }
                }
            }
        }else{
            //todo
            printWriter.println("Genre Parser Error: Movie with id " + movieId + " has no genres associated.");
//            System.out.println("No cats at " + movieId);
        }

        // create a new Employee with the value read from the xml nodes
    }


    /**
     * It takes an XML element and the tag name, look for the tag and get
     * the text content
     * i.e for <Employee><Name>John</Name></Employee> xml snippet if
     * the Element points to employee node and tagName is name it will return John
     */
    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            // here we expect only one <Name> would present in the <Employee>
            Node firstChild = nodeList.item(0).getFirstChild();
            if(firstChild == null){
                textVal = null;
                //todo
                printWriter.println("Error: No value for " + tagName + " detected");
            }else{
                textVal = nodeList.item(0).getFirstChild().getNodeValue();
            }
        }
        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     */
    private int getIntValue(Element ele, String tagName) {
        // in production application you would catch the exception
        int val = -1;
        try {
            val = Integer.parseInt(getTextValue(ele, tagName));
        }catch(Exception e){
            //todo
            printWriter.println("Error: No int detected at " + tagName);
        }
        return val;
    }



    private void insertData() {
        try {
            String loginUser = "mytestuser";
            String loginPasswd = "My6$Password";
            String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
            AtomicInteger numMoviesInserted = new AtomicInteger();
            AtomicInteger numStarsInserted = new AtomicInteger();
            AtomicInteger numGenresInserted = new AtomicInteger();
            AtomicInteger numStarsInMoviesInserted = new AtomicInteger();
            AtomicInteger numGenresInMoviesInserted = new AtomicInteger();

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            ExecutorService executor = Executors.newFixedThreadPool(3); // Create a thread pool
            CountDownLatch latch = new CountDownLatch(3); // CountDownLatch for synchronization

            executor.submit(() -> {
                // Task to be executed in parallel
//                System.out.println("Movie entries are being inserted on thread: " + Thread.currentThread().getName());
                try (Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
                    moviesHashmap.forEach((movieId, movie) -> {
//                        String insertQuery = movie.toSQLInsertString();
//                        System.out.println(movie.toSQLInsertString());
                        String sql = "INSERT INTO movies (id, title, year, director) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setString(1, movie.getId());
                            preparedStatement.setString(2, movie.getTitle());
                            preparedStatement.setInt(3, movie.getYear());
                            preparedStatement.setString(4, movie.getDirector());
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                            numMoviesInserted.getAndIncrement();
                        } catch (SQLException e) {
                            //todo
//                            printWriter.println("SQL Error: " + e.toString());
//                            throw new RuntimeException(e);
                        }
                    });
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown(); // Signal task completion
            });

            executor.submit(() -> {
//                System.out.println("Genre entries are being inserted on thread: " + Thread.currentThread().getName());
                try (Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
                    genreHashMap.forEach((name, id) -> {
                        String sql = "INSERT INTO genres (id, name) VALUES (?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setInt(1, id);
                            preparedStatement.setString(2, name);
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                            numGenresInserted.getAndIncrement();
                        } catch (SQLException e) {
                            //todo
//                            printWriter.println("SQL Error: " + e.toString());
//                            throw new RuntimeException(e);
                        }
                    });
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown(); // Signal task completion
            });

            executor.submit(() -> {
//                System.out.println("Star entries are being inserted on thread: " + Thread.currentThread().getName());
                try (Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
//                    connection.setAutoCommit(false);
                    String sql = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)";
//                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    starHashMap.forEach((name, star) -> {
//                        System.out.println(star.toSQLInsertString());
//                        String sql = "INSERT INTO stars (id, name, birthYear) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                        try {


                            preparedStatement.setString(1, star.getId());
                            preparedStatement.setString(2, star.getName());
                            preparedStatement.setInt(3, star.getBirthYear());
//                            preparedStatement.addBatch();
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                            numStarsInserted.getAndIncrement();
                        } catch (SQLException e) {
                            //todo
//                            System.out.println("SQL Error: " + e.toString());
//                            throw new RuntimeException(e);
                        }
                    });

//                    preparedStatement.executeBatch();
//                    connection.commit();
//                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown(); // Signal task completion
            });


            try {
                latch.await(); // Wait for all parallel tasks to complete
                executor.submit(() -> {

//                    ExecutorService executor2 = Executors.newFixedThreadPool(2); // Create a thread pool
//                    CountDownLatch latch2 = new CountDownLatch(2); // CountDownLatch for synchronization

//                    executor2.submit(() -> {
                        // Task to be executed in parallel
//                        System.out.println("Movie entries are being inserted on thread: " + Thread.currentThread().getName());
//                        try (Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
//                            System.out.println("Genre_in_Movie in movies SQL Inserts:");
//                            for (Genre_in_Movie genreInMovie : genreInMovies) {
//                                String sql = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?)";
//                                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                                    preparedStatement.setInt(1, genreInMovie.getGenreId());
//                                    preparedStatement.setString(2, genreInMovie.getMovieId());
//                                    preparedStatement.executeUpdate();
//                                    preparedStatement.close();
//                                    numGenresInMoviesInserted.getAndIncrement();
//                                } catch (SQLException e) {
//                                    //todo
////                                    System.out.println("SQL Error: " + e.toString());
//                                }
//                                //todo
////                                System.out.println(genreInMovie.toSQLInsertString());
//                            }
//                            connection.close();
//                        } catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }

                    try (Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
//                        System.out.println("Genre_in_Movie in movies SQL Inserts:");

                        String sql = "INSERT INTO genres_in_movies (genreId, movieId) VALUES (?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            for (Genre_in_Movie genreInMovie : genreInMovies) {
                                preparedStatement.setInt(1, genreInMovie.getGenreId());
                                preparedStatement.setString(2, genreInMovie.getMovieId());
                                preparedStatement.addBatch();
                                numGenresInMoviesInserted.getAndIncrement();
                                // System.out.println(genreInMovie.toSQLInsertString());
                            }
                            preparedStatement.executeBatch();
                        } catch (SQLException e) {
                            // Handle SQLException, you may want to log or throw an exception
                            // System.out.println("SQL Error: " + e.toString());
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
//                        latch2.countDown(); // Signal task completion
//                    });


//                    executor2.submit(() -> {
                        // Task to be executed in parallel
//                        System.out.println("Movie entries are being inserted on thread: " + Thread.currentThread().getName());
//                        try (Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
//                            System.out.println("Star_in_Movie in movies SQL Inserts:");
//                            for (Star_in_Movie starInMovie : starInMovies) {
//                                String sql = "INSERT INTO stars_in_movies (starId, movieId) VALUES (?, ?)";
//                                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                                    preparedStatement.setString(1, starInMovie.getStarId());
//                                    preparedStatement.setString(2, starInMovie.getMovieId());
//                                    preparedStatement.executeUpdate();
//                                    preparedStatement.close();
//                                    numStarsInMoviesInserted.getAndIncrement();
//                                } catch (SQLException e) {
//                                    System.out.println("SQL Error: " + e.toString());
//                                }
//                                System.out.println(starInMovie.toSQLInsertString());
//                            }
//                            connection.close();
//                        } catch (SQLException e) {
//                            throw new RuntimeException(e);
//                        }
                    try (Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd)) {
//                        System.out.println("Star_in_Movie in movies SQL Inserts:");

                        String sql = "INSERT INTO stars_in_movies (starId, movieId) VALUES (?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                            for (Star_in_Movie starInMovie : starInMovies) {
                                preparedStatement.setString(1, starInMovie.getStarId());
                                preparedStatement.setString(2, starInMovie.getMovieId());
                                preparedStatement.addBatch();
                                numStarsInMoviesInserted.getAndIncrement();
//                                System.out.println(starInMovie.toSQLInsertString());
                            }
                            preparedStatement.executeBatch();
                        } catch (SQLException e) {
//                            System.out.println("SQL Error: " + e.toString());
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
//                        latch2.countDown(); // Signal task completion
//                    });

//                    System.out.println("Parsed successfully " + moviesHashmap.size() + " movies");
//                    System.out.println("Parsed successfully " + starHashMap.size() + " stars");
//                    System.out.println("Parsed successfully " + genreHashMap.size() + " genres");
//                    System.out.println("Parsed successfully " + starInMovies.size() + " stars_in_movies");
//                    System.out.println("Parsed successfully " + genreInMovies.size() + " genres_in_movies");

//                    System.out.println("Inserted " + numMoviesInserted.get() + " movies");
//                    System.out.println("Inserted " + numStarsInserted.get() + " stars");
//                    System.out.println("Inserted " + numGenresInserted.get() + " genres");
//                    System.out.println("Inserted " + numStarsInMoviesInserted.get() + " stars_in_movies");
//                    System.out.println("Inserted " + numGenresInMoviesInserted.get() + " genres_in_movies");


                    printWriter.println("Inserted " + numMoviesInserted.get() + " movies");
                    printWriter.println("Inserted " + numStarsInserted.get() + " stars");
                    printWriter.println("Inserted " + numGenresInserted.get() + " genres");
                    printWriter.println("Inserted " + numStarsInMoviesInserted.get() + " stars_in_movies");
                    printWriter.println("Inserted " + numGenresInMoviesInserted.get() + " genres_in_movies");
                    printWriter.close();
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.shutdown(); // Shutdown the executor service

//            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // create an instance
        MovieDBDomParser domParserExample = new MovieDBDomParser();

        // call run example
        domParserExample.runExample();
//        domParserExample.runActorsTest();
    }

}
