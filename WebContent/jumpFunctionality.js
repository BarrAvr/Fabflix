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