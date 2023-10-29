function handleBrowsePrefixesDisplay(resultData) {
    console.log("Displaying Browsing prefixes");
    console.log(resultData);

    let prefixBodyElement = jQuery("#prefixes");

    for (let i = 0; i < resultData.length; i++) {
        let prefix = resultData[i]["prefix"];
        let rowHTML =
            "<p>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="search-results.html?type=prefix&prefix=' + prefix + '&sortBy=title&titleOrder=asc&ratingOrder=desc&page=1&count=20">'
            + prefix +     // display star_name for the link text
            '</a>' +
            "</p>";
        prefixBodyElement.append(rowHTML);
    }
}


jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/prefixes",
    success: (resultData) => handleBrowsePrefixesDisplay(resultData)
});