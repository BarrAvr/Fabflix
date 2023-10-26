// let search_results = $("#search_results");
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


function handleSearchResult(resultData) {

    console.log("handleResult: populating movie info from resultData");
    console.log("JSON from api call:");
    console.log(resultData);

    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    let i = 0;
    for (let i = 0; i < resultData.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["movie_id"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "</tr>";
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }

    // let movieTableBodyElement = jQuery("#movie_table_body");
    //
    // let index = 0;
    // // Iterate through resultData, no more than 10 entries
    // for (let i = 0; i < Math.min(20, resultData.length); i++) {
    //     let genres = [];
    //     let stars = [];
    //     let movieId = resultData[index]['movie_id'];
    //     let rowHTML = "";
    //     rowHTML += "<tr>";
    //     rowHTML +=
    //         "<th>" +
    //         '<a href="single-movie.html?id=' + movieId + '">'
    //         + resultData[index]["movie_title"] +
    //         '</a>' +
    //         "</th>";
    //     rowHTML += "<th>" + resultData[index]["movie_year"] + "</th>";
    //     rowHTML += "<th>" + resultData[index]["movie_director"] + "</th>";
    //     rowHTML += "<th>" + resultData[index]["movie_rating"] + "</th>";
    //
    //     let genreIndex = 1;
    //     genres.push(resultData[index]['genre_name']);
    //     while (index < resultData.length && resultData[index]['movie_id'] == movieId) {
    //         if (resultData[index]['genre_name'] == genres[0]) {
    //             stars.push([resultData[index]['star_id'], resultData[index]['star_name']]);
    //         } else {
    //             if (resultData[index]['genre_name'] != genres[genreIndex - 1]) {
    //                 genres.push(resultData[index]['genre_name']);
    //                 genreIndex++;
    //             }
    //         }
    //         index++
    //     }
    //
    //     let starString = "";
    //     for (let i = 0; i < Math.min(3, stars.length); i++) {
    //         starString += `<a href="single-star.html?id=${stars[i][0]}">${stars[i][1]}</a>, `;
    //     }
    //     starString = starString.slice(0, starString.length - 2);
    //
    //     let genreString = "";
    //     for (let i = 0; i < Math.min(3, genres.length); i++) {
    //         genreString += `${genres[i]}, `;
    //     }
    //
    //     genreString = genreString.slice(0, genreString.length - 2);
    //
    //     rowHTML += "<th>" + genreString + "</th>";
    //     rowHTML += "<th>" + starString + "</th>";
    //     rowHTML += "</tr>";
    //
    //     movieTableBodyElement.append(rowHTML);
    // }
}

let movieTitle = encodeURIComponent(getParameterByName('title'));
let movieStar = encodeURIComponent(getParameterByName('star'));
let movieYear = encodeURIComponent(getParameterByName('year'));
let movieDirector = encodeURIComponent(getParameterByName('director'));

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/search-results?title=" + movieTitle + "&star=" + movieStar + "&year=" + movieYear + "&director=" + movieDirector,
    success: (resultData) => handleSearchResult(resultData)
});

// search_results.submit(submitSearch);