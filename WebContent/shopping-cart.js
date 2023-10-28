

function handleShoppingCartData(resultData) {
    let shoppingCartElement = jQuery("#shopping-cart");
    console.log(resultData);
    resultData["cart"].forEach((item) => {
        shoppingCartElement.append(`<li>${item}</li>`)
    });
}

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "items",
    success: (resultData) => handleShoppingCartData(resultData)
});

// search_results.submit(submitSearch);