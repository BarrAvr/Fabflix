# Team Sravan - Project 2

## Project 2 Demo Video URL:

[https://drive.google.com/file/d/1aYo_nx-XsU2et7UHShoKnEGk_puAT01U/view?usp=sharing](https://drive.google.com/file/d/1aYo_nx-XsU2et7UHShoKnEGk_puAT01U/view?usp=sharing)

## Team Member Contributions


Mingjia:
- Set up LoginServlet to handle login logic and querying from the customers table
- Wrote frontend code for shopping cart and payment features
- Added links between pages, like the "Checkout" button, and CSS styling to all pages
- Implemented and polished a mrethod for testing out all servlets with the Postman app
- Thoroughly tested all API endpoints and set up new ajax post calls on the frontend to modify session attributes properly
- Collaborated on the adding the shopping cart as an attribute in the session, modifying how the user is stored in the session, and inserting new sales into the sales table

Barr:
- Wrote the general movie list page which is variable and changes based on URL and prior data.
- Completed the searching which redirects user to respective movie list page.
- Completed browsing which generated the display based on movies and genres in the database and redirects user to respective movie list page.
- Incorporated sorting, page number, and movie count in movie list.
- Incorporated sorting options, prev/next buttons, and changing movie count options.
- Collaborated on the list-state which stores the state of the list in a session.
- Used list-state for jump functionality which allows users to return back to a previous movie after selecting star or single movie.
- Also used list-state and jump functionality for changing the sort, page, and count of the movie list page.
- Drove debugging in search and browsing functionality and figured out the best way to refactor backend code to handle movie titles as JSON objects instead of strings
- Collaborated on the insertion of the paid items into the sales table.

### Substring matching design:
Used "LIKE" predicate with '%blank%' wildcard for title, director, and star's name.
Used = for year.
