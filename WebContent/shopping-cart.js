

function handleShoppingCartData(resultData) {
    let shoppingCartElement = jQuery("#shopping-cart");
    console.log(shoppingCartElement);
    const cartArray = JSON.parse(resultData["cart"]);
    console.log(resultData);
    console.log(cartArray);
    console.log(typeof cartArray);
    if (cartArray.length == 0) {
        shoppingCartElement.append("<li>No items in cart</li>");
    } else {
        cartArray.forEach((item) => {
            console.log("appending item " + item + " to cart as li");
            shoppingCartElement.append("<li>" + item + "</li>");
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