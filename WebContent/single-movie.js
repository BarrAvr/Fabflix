/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    // console.log("handleResult: populating star info from resultData");
    // console.log("JSON from api call:");
    // console.log(resultData);

    // console.log("stars object");
    // console.log();

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let movieInfoElement = jQuery("#movie_info");

    // append two html <p> created to the h3 body, which will refresh the page
    movieInfoElement.append("<p>Movie Name: " + resultData[0]["movie_title"] + "</p>" +
        "<p>Year: " + resultData[0]["movie_year"] + "</p>" +
        "<p>Director: " + resultData[0]["movie_director"] + "</p>" +
        "<p>Rating: " + resultData[0]["movie_rating"] + "</p>");

    // console.log("handleResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    let i = 0;
    for (; i < Math.min(resultData[1].length, resultData[2].length); i++) {
        // if (resultData[i]["end"] == "end") {
        //     break;
        // }
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-star.html?id=' + resultData[1][i]['star_id'] + '">'
            + resultData[1][i]["star_name"] +     // display star_name for the link text
            '</a>' +
            "</th>";
        // rowHTML += "<th>" + resultData[1][i]["star_name"] + "</th>";
        let genre = resultData[2][i]["genre_name"];
        rowHTML += "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="search-results.html?type=genre&genre=' + genre + '&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=10">'
            + genre +     // display star_name for the link text
            '</a>' +
            "</th>";
        // rowHTML += "<th>" + resultData[2][i]["genre_name"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
    if (resultData[1].length > resultData[2].length) {
        for (; i < resultData[1].length; i++) {
            let rowHTML = "";
            rowHTML += "<tr>";
            rowHTML +=
                "<th>" +
                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="single-star.html?id=' + resultData[1][i]['star_id'] + '">'
                + resultData[1][i]["star_name"] +     // display star_name for the link text
                '</a>' +
                "</th>";
            rowHTML += "</tr>";

            // Append the row created to the table body, which will refresh the page
            movieTableBodyElement.append(rowHTML);
        }
    } else {
        for (; i < resultData[2].length; i++) {
            let rowHTML = "";
            rowHTML += "<tr>";

            rowHTML += "<th>" + resultData[2][i]["genre_name"] + "</th>";
            rowHTML += `<th class="clickable-text add-to-cart" id="add-to-cart-button">Add to Cart</th>`;
            rowHTML += "</tr>";
            rowHTML += "</tr>";

            // Append the row created to the table body, which will refresh the page
            movieTableBodyElement.append(rowHTML);

        }
    }


    $("#single-movie-add-to-cart-button").click(function () {

        alert("Added " + resultData[0]["movie_title"] + " to cart!");
        console.log(resultData);

        const data = {
            type: "add",
            newItem: resultData[0]["movie_id"]
        };


        $.ajax({
            type: "POST",
            url: "items",
            data: data,
            success: function (result) {
                console.log(result);
            },
            dataType: "json"
        });

    });
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let starId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-movie?id=" + starId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});