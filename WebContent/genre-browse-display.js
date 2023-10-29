/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleBrowseGenresDisplay(resultData) {
    console.log("Displaying Browsing Genres");
    console.log(resultData);

    let genresBodyElement = jQuery("#genres");

    for (let i = 0; i < resultData.length; i++) {
        // let rowHTML = "<p>" + resultData[i]["genre_name"] + "</p>";
        let genre = resultData[i]["genre_name"];
        let rowHTML =
            "<p>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="search-results.html?type=genre&genre=' + genre + '&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=20">'
            + genre +     // display star_name for the link text
            '</a>' +
            "</p>";
        genresBodyElement.append(rowHTML);
    }
}



/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/genres",
    success: (resultData) => handleBrowseGenresDisplay(resultData)
});