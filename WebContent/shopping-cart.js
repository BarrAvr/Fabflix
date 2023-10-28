

function handleShoppingCartData(resultData) {
    let shoppingCartElement = jQuery("#shopping-cart");
    console.log(shoppingCartElement);
    const cartArray = JSON.parse(resultData["cart"]);
    console.log(resultData);
    console.log(cartArray);
    console.log(typeof cartArray);
    if (cartArray.length == 0) {
        shoppingCartElement.innerHTML = "<p>No items in cart</p>";
    }
    cartArray.forEach((item) => {
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