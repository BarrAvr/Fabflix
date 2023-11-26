
function handleMetadataResult(resultData) {
    console.log("handleMetadataResult: populating metadata tables from resultData");
    console.log(resultData);

for (let i = 0; i < 10; i++) {
    let metadataTableBodyElement = jQuery(`#metadata-table-${i}`);
    for (let j = 0; j < resultData[i].length; j++) {
        let rowHTML = `<tr><th style="width: 300px; height: 75px; border: 1px solid black;">${resultData[i][j]["columnName"]}</th><th style="width: 300px; height: 75px; border: 1px solid black;">${resultData[i][j]["columnType"]}</th></tr>`;
        metadataTableBodyElement.append(rowHTML);
    }
}


}

// console.log('autocomplete.js activated');
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/metadata", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleMetadataResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});