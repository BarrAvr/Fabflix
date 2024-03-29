// let search_results = $("#search_results");
// let sort_option_form = $("#sort-option");

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
    iToIndexMapping = new Array();
    iToIndexMapping.push(0);
    // console.log("handleResult: populating movie info from resultData");
    // console.log("JSON from api call:");
    // console.log(resultData);

    let movieTableBodyElement = jQuery("#movie_table_body");

    // let index = 0;
    for (let index = 0; index < resultData.length;) {
        let genres = [];
        let stars = [];
        let movieId = resultData[index]['movie_id'];
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            '<a href="single-movie.html?id=' + movieId + '">'
            + resultData[index]["movie_title"] +
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[index]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[index]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[index]["movie_rating"] + "</th>";

        let genreIndex = 1;
        genres.push(resultData[index]['genre_name']);
        while (index < resultData.length && resultData[index]['movie_id'] == movieId) {
            if (resultData[index]['genre_name'] == genres[0]) {
                stars.push([resultData[index]['star_id'], resultData[index]['star_name']]);
            } else {
                if (resultData[index]['genre_name'] != genres[genreIndex - 1]) {
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
        starString = starString.slice(0, starString.length - 2);

        let genreString = "";
        for (let i = 0; i < Math.min(3, genres.length); i++) {
            let genre = encodeURIComponent(genres[i]);
            genreString = `<a href="search-results.html?type=genre&genre=${genre}&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=10">${genre}</a>, `;
            // rowHTML += "<th>" +
                // Add a link to single-star.html with id passed with GET url parameter
            // genreString += search-results.html?type=genre&genre=' + genre + '&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=10">'
            //
        }
            // + genre +
        //     '</a>' +
        //     "</th>";
        // genreString += `${genres[i]}, `;

        genreString = genreString.slice(0, genreString.length - 2);

        rowHTML += "<th>" + genreString + "</th>";
        rowHTML += "<th>" + starString + "</th>";
        rowHTML += `<th class="clickable-text add-to-cart">Add to Cart</th>`;
        rowHTML += "</tr>";

        // console.log(iToIndexMapping.push(index));
        movieTableBodyElement.append(rowHTML);

    }
    let addToCartButtons = document.getElementsByClassName("add-to-cart");
    for (let i = 0; i < addToCartButtons.length; i++) {
        // console.log("Row Info in addToCartButtons");
        // console.log(addToCartButtons[i]);
        addToCartButtons[i].onclick = function () {

            alert("Added " + resultData[iToIndexMapping[i]]["movie_title"] + " to cart!");
            console.log(resultData);
            console.log("The index we retrieved from iToIndexMapping:");
            console.log(iToIndexMapping[i])
            console.log("The json object at that specific index:");
            console.log(resultData[iToIndexMapping[i]]);

            const data = {
                type: "add",
                newItem: resultData[iToIndexMapping[i]]["movie_id"]
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

        };
    }
}

let type = getParameterByName("type");

let sortBy = getParameterByName('sortBy');
let titleOrder = getParameterByName('titleOrder');
let ratingOrder = getParameterByName('ratingOrder');
let page = getParameterByName('page');
let count = getParameterByName('count');


let movieTitle = encodeURIComponent(getParameterByName('title'));
let movieStar = encodeURIComponent(getParameterByName('star'));
let movieYear = encodeURIComponent(getParameterByName('year'));
let movieDirector = encodeURIComponent(getParameterByName('director'));

// Makes the HTTP GET request and registers on success callback function handleStarResult
if (type === "general") {
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        // url: "list-state?type=general&title=" + movieTitle + "&star=" + movieStar + "&year=" + movieYear + "&director=" + movieDirector + "&sortBy=" + sortBy + "&titleOrder=" + titleOrder + "asc&ratingOrder=" + ratingOrder,
        url: "list-state",
        data : {
            type: "general",
            title: movieTitle,
            star: movieStar,
            year: movieYear,
            director: movieDirector,
            sortBy: sortBy,
            titleOrder: titleOrder,
            ratingOrder: ratingOrder,
            page: page,
            count: count
        },
        success: function (result) {
            // console.log(result);
            jQuery.ajax({
                dataType: "json",
                method: "GET",
                url: "list-state",
                success: function (listResult) {
                    // console.log(listResult.type)
                    jQuery.ajax({
                        dataType: "json",
                        method: "GET",
                        url: "api/search-results",
                        data : listResult,
                        success: (resultData) => handleSearchResult(resultData)
                    });
                }
            });
        }
    });
}


else if (type === "genre") {
    let genre = getParameterByName('genre');
    // Makes the HTTP GET request and registers on success callback function handleStarResult
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "list-state",
        data : {
            type: "genre",
            genre: genre,
            sortBy: sortBy,
            titleOrder: titleOrder,
            ratingOrder: ratingOrder,
            page: page,
            count: count
        },
        success: function (result) {
            // console.log(result);
            jQuery.ajax({
                dataType: "json",
                method: "GET",
                url: "list-state",
                success: function (listResult) {
                    console.log(listResult.type)
                    jQuery.ajax({
                        dataType: "json",
                        method: "GET",
                        url: "api/search-results",
                        data : listResult,
                        success: (resultData) => handleSearchResult(resultData)
                    });
                }
            });
        }
    });
}
else {
    let prefix = getParameterByName('prefix');

    // Makes the HTTP GET request and registers on success callback function handleStarResult
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "list-state",
        data : {
            type: "prefix",
            prefix: prefix,
            sortBy: sortBy,
            titleOrder: titleOrder,
            ratingOrder: ratingOrder,
            page: page,
            count: count
        },
        success: function (result) {
            // console.log(result);
            jQuery.ajax({
                dataType: "json",
                method: "GET",
                url: "list-state",
                success: function (listResult) {
                    console.log(listResult.type)
                    jQuery.ajax({
                        dataType: "json",
                        method: "GET",
                        url: "api/search-results",
                        data : listResult,
                        success: (resultData) => handleSearchResult(resultData)
                    });
                }
            });
        }
    });
}
