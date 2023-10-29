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

    window.location.replace(url);
}

function jumpback() {
    jQuery.ajax({
        dataType: "json",
        method: "GET",
        url: "list-state",
        success: (resultData) => handleJumpBack(resultData)
    });
}