
function handleMetadataResult(resultData) {
    console.log("handleMetadataResult: populating metadata tables from resultData");
    console.log(resultData);

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    //let starTableBodyElement = jQuery("#star_table_body");
    let metadataTableBodyElement = jQuery(`#metadata-table-${i}`);

}

console.log('index.js activated');
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/metadata", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleMetadataResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});