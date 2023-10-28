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
    iToIndexMapping = new Array();
    console.log("handleResult: populating movie info from resultData");
    console.log("JSON from api call:");
    console.log(resultData);

    let movieTableBodyElement = jQuery("#movie_table_body");

    let index = 0;
    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
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
            genreString += `${genres[i]}, `;
        }

        genreString = genreString.slice(0, genreString.length - 2);

        rowHTML += "<th>" + genreString + "</th>";
        rowHTML += "<th>" + starString + "</th>";
        rowHTML += `<th class="clickable-text add-to-cart">Add ${i} ${index} ${genreIndex} to Cart</th>`;
        rowHTML += "</tr>";
        console.log(`During iToIndexMapping construction, as we're pushing index ${i} to the mapping array, row ${i} with title ${resultData[index]["movie_title"]} has index ${index}`)
        console.log(iToIndexMapping.push(index));
        movieTableBodyElement.append(rowHTML);
    }
    console.log("iToIndexMapping");
    console.log(iToIndexMapping);
    let addToCartButtons = document.getElementsByClassName("add-to-cart");
    for (let i = 0; i < addToCartButtons.length; i++) {
        console.log("Row Info in addToCartButtons");
        console.log(addToCartButtons[i]);
        addToCartButtons[i].onclick = function () {
            const data = {
                newItem: resultData[index]["movie_title"]
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
            console.log(resultData[i]);
            alert("Added " + i + " to cart!");
        };
    }
}

let movieTitle = encodeURIComponent(getParameterByName('title'));
let movieStar = encodeURIComponent(getParameterByName('star'));
let movieYear = encodeURIComponent(getParameterByName('year'));
let movieDirector = encodeURIComponent(getParameterByName('director'));

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/search-results?type=general&title=" + movieTitle + "&star=" + movieStar + "&year=" + movieYear + "&director=" + movieDirector,
    success: (resultData) => handleSearchResult(resultData)
});

let genre = getParameterByName('genre');

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/search-results?type=genre&genre=" + genre,
    success: (resultData) => handleSearchResult(resultData)
});

let prefix = getParameterByName('prefix');

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/search-results?type=prefix&prefix=" + prefix,
    success: (resultData) => handleSearchResult(resultData)
});

// search_results.submit(submitSearch);