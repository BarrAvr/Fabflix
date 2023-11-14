# Team Sravan - Project 3

## Project 3 Demo Video URL:

[https://drive.google.com/file/d/17Cc2N0F7U1aOs2dr69L8AkoO4qhQd0wS/view?usp=sharing](https://drive.google.com/file/d/17Cc2N0F7U1aOs2dr69L8AkoO4qhQd0wS/view?usp=sharing)

## Team Member Contributions

Mingjia:
- Set up Recaptcha on the customer login and employee login pages
- Added support for logging in with encrypted passwords
- Set up https and made necessary changes to EC2 instance and code to support it
- Implemented and polished a mrethod for testing out all new servlets with the Postman app
- Wrote stored procedures and created the employee dashboard to support add star & add movie features


Barr:
- Wrote the XMLParser, debugged parsing process, analyzed parsed output, and implemented parser optimizations.
- Helped to write the preparedStatements to replace all of the regular statements.
- Helped with updating LoginFilter for _dashboard page.
- Helped with restructuring file and folder structure for static pages
- Worked with Mingjia to debug sqlProcedure because the id generation had to change to work with the new data the XMLParser added.
- Fixed bugs relating to movies not being inserted and queried properly from the DB
- Spearheaded debugging for URL-related issues on AWS and determined how tp utilize redirects correctly in Tomcat

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

