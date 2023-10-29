function handleJumpBack(listState) {
    let url = "search-results.html?";

       if (listState["type"] === "genre") {
           url += "type=genre&";
           url += "genre=" + listState["genre"];
           url += "&";
       } else if (listState["type"] === "prefix") {
           url += "type=prefix&";
           url += "prefix=" + listState["prefix"];
           url += "&";
       } else {
           url += "type=general&";
           url += "title=";
           if (listState["title"] != null) {
               url += listState["title"];
           }
           url += "&";
           url += "star=";
           if (listState["star"] != null) {
               url += listState["star"];
           }
           url += "&";
           url += "year=";
           if (listState["year"] != null) {
               url += listState["year"];
           }
           url += "&";
           url += "director=";
           if (listState["director"] != null) {
               url += listState["director"];
           }
           url += "&";

       }
       url += "sortBy=" + listState["sortBy"];
       url += "&";

       url += "titleOrder=" + listState["titleOrder"];
       url += "&";

       url += "ratingOrder=" + listState["ratingOrder"];
       url += "&";

       url += "page=" + listState["page"];
       url += "&";

       url += "count=" + listState["count"];

    window.location.replace(url);
}

function handleUpdateSort(listState, sortBy, titleOrder, ratingOrder) {
    let url = "search-results.html?";

    if (listState["type"] === "genre") {
        url += "type=genre&";
        url += "genre=" + listState["genre"];
        url += "&";
    } else if (listState["type"] === "prefix") {
        url += "type=prefix&";
        url += "prefix=" + listState["prefix"];
        url += "&";
    } else {
        url += "type=general&";
        url += "title=";
        if (listState["title"] != null) {
            url += listState["title"];
        }
        url += "&";
        url += "star=";
        if (listState["star"] != null) {
            url += listState["star"];
        }
        url += "&";
        url += "year=";
        if (listState["year"] != null) {
            url += listState["year"];
        }
        url += "&";
        url += "director=";
        if (listState["director"] != null) {
            url += listState["director"];
        }
        url += "&";

    }
    url += "sortBy=" + sortBy;
    url += "&";

    url += "titleOrder=" + titleOrder;
    url += "&";

    url += "ratingOrder=" + ratingOrder;
    url += "&";

    url += "page=" + listState["page"];
    url += "&";

    url += "count=" + listState["count"];

    window.location.replace(url);
}

function jumpBack() {
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "list-state",
        success: (resultData) => handleJumpBack(resultData)
    });
}

function updateSort(sortBy, titleOrder, ratingOrder) {
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "list-state",
        success: (resultData) => handleUpdateSort(resultData, sortBy, titleOrder, ratingOrder)
    });
}

function handleSelection(){
    let selectElement = document.getElementById("selection");
    let selectedValue = selectElement.value;
    console.log(selectedValue);
    jumpBack();
    switch(selectedValue) {
        case "title-asc-asc":
            updateSort("title", "asc", "asc");
            break;
        case "title-asc-desc":
            updateSort("title", "asc", "desc");
            break;
        case "title-desc-asc":
            updateSort("title", "desc", "asc");
            break;
        case "title-desc-desc":
            updateSort("title", "desc", "desc");
            break;
        case "rating-asc-asc":
            updateSort("rating", "asc", "asc");
            break;
        case "rating-asc-desc":
            updateSort("rating", "desc", "asc");
            break;
        case "rating-desc-asc":
            updateSort("rating", "asc", "desc");
            break;
        case "rating-desc-desc":
            updateSort("rating", "desc", "desc");
            break;
        default:
            jumpBack();
    }
}