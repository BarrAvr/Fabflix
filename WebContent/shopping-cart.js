

function handleShoppingCartData(resultData) {
    let shoppingCartElement = jQuery("#shopping-cart");
    console.log(shoppingCartElement);
    console.log(resultData);
    // const cartArray = JSON.parse(resultData);

    console.log(typeof cartArray);
    if (resultData.length == 0) {
        shoppingCartElement.append("<li>No items in cart</li>");
    } else {
       resultData.forEach((item) => {
            console.log("appending item " + item + " to cart as li");
            shoppingCartElement.append("<li>" + item["itemName"] + "</li>");
        })
    }
}

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "items",
    success: (resultData) => handleShoppingCartData(resultData)
});

// search_results.submit(submitSearch);