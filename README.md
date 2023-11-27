# Team Sravan - Project 4

## Project 4 Demo Video URL:

[https://drive.google.com/file/d/1VtulH0Kd2tZyOflrhqeHBs_WfzPNTDoj/view?usp=sharing](https://drive.google.com/file/d/1VtulH0Kd2tZyOflrhqeHBs_WfzPNTDoj/view?usp=sharing)

### Team Member Contributions

Mingjia:
- Task 1 - Full Text Search and autocomplete

Barr:
- Task 2 - Android app

### Stored Procedure:

See stored-procedure.sql in the base directory of the repo, [here](stored-procedure.sql)

### Filenames with PreparedStatements

SingleStarServlet, SingleMovieServlet, SearchServlet, PrefixesDisplayServlet, PaymentServlet, LoginServlet, EmployeeLoginServlet, GenreDisplayServlet, idToTitleServlet, SearchCountServlet, FormRecaptcha



### Parser Optimizations
The two main optimization strategies used were multi-threading and batch inserting.
 - The inconsistencies report is stored in ParseMetaData.txt
 - For the XMLParser we modeled it after the domParser example.
 - We stored the information in hashmaps for the movie, star, and genre data
 - Stored the data for the genres_in_movies and stars_in_movies in a list
 - Used multi-threading to parse the data to the hashmaps and lists more efficiently
 - Read through each respective data structure and inserted the data.
 - Used 3 threads to insert the movies, stars, and genres data and then awaited until completion.
 - Followed these insertions by using 2 threads for the genres_in_movies and stars_in_movies.
 - Used batching for stars, genres, stars_in_movies, and genres_in_movies insertions



### Inconsistent Data Report

The inconsistencies report is stored in ParsingMetaData.txt, [here](/XMLParser/ParsingMetaData.txt)

