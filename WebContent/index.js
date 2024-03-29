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
function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");
    console.log(resultData);

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    //let starTableBodyElement = jQuery("#star_table_body");
    let movieTableBodyElement = jQuery("#movie_table_body");

    let index = 0;
    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        //let index = i + j;
        let genres = [];
        let stars = [];
        let movieId = resultData[index]['movie_id'];
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + movieId + '">'
            + resultData[index]["movie_title"] +     // display star_name for the link text
            '</a>' +
            "</th>";
        // rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        rowHTML += "<th>" + resultData[index]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[index]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[index]["movie_rating"] + "</th>";

        let genreIndex = 1;
        genres.push(resultData[index]['genre_name']);
        while(index < resultData.length && resultData[index]['movie_id'] == movieId){
            if(resultData[index]['genre_name'] == genres[0]){
                stars.push([resultData[index]['star_id'], resultData[index]['star_name']]);
            }else{
                if (resultData[index]['genre_name'] != genres[genreIndex-1]) {
                    genres.push(resultData[index]['genre_name']);
                    genreIndex++;
                }
            }
            index++
        }

        let starString = "";
        for (let i = 0; i < Math.min(3, stars.length); i++) {
            starString += `<a href="single-star.html?id=${stars[i][0]}">${stars[i][1]}</a>, `;
        }
        starString = starString.slice(0, starString.length-2);

        let genreString = "";
        for (let i = 0; i < Math.min(3, genres.length); i++) {
            genreString += `${genres[i]}, `;
        }
        genreString = genreString.slice(0, genreString.length-2);
        // stars.forEach((star) => {
        //    starString += `<a href="single-star.html?id=${star[0]}"></a>, `;
        //    break;
        // });
        // starString = starString.slice(starString.length-2);
        // starString += '</th>';


        // index++;
        rowHTML += "<th>" + genreString+ "</th>";
        rowHTML += "<th>" + starString + "</th>";
        rowHTML += `<th class="clickable-text add-to-cart">Add to Cart</th>`;
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        // starTableBodyElement.append(rowHTML);
        movieTableBodyElement.append(rowHTML);

    }
    let addToCartButtons = document.getElementsByClassName("add-to-cart");

    for (let i = 0; i < addToCartButtons.length; i++) {
        addToCartButtons[i].onclick = function () {
            const data = {
                newItem: "hi"
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
            // console.log("Added " + resultData[i] + " to cart!");
            console.log(resultData[i]);
        };
    }
    console.log(addToCartButtons);
}



/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movies", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});