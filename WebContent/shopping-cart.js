
function buildShoppingCartFreqMap(arr) {
    const map = {};
    arr.forEach(item => {
        if (map[item["itemName"]]) {
            map[item["itemName"]]++;
        }else {
            map[item["itemName"]] = 1;
        }
    });
    return map;
}
function handleShoppingCartData(resultData) {
    let shoppingCartElement = jQuery("#shopping-cart");
    let message = jQuery("#message");
    console.log(shoppingCartElement);
    console.log(resultData);

    if (resultData.length == 0) {
        shoppingCartElement.append("<li>No items in cart</li>");
    } else {
       const movieTitlesAndQuantities = buildShoppingCartFreqMap(resultData);
        let totalMovieCount = movieTitlesAndQuantities.length;
            console.log("movieTitlesAndQuantities:");
       console.log(movieTitlesAndQuantities);
       const movieTitles = Object.keys(movieTitlesAndQuantities);
       movieTitles.forEach((item, index) => {
           let count = movieTitlesAndQuantities[item];
           console.log("appending item " + item + " to cart as li");
            // shoppingCartElement.append(`<li>${item["itemName"]}<a id="decrement-quantity">-</a> ${} <a id="increment-quantity">+</a></li>`);
           shoppingCartElement.append(`<li>${item} <a class="decrement-quantity">-</a> <span class="count">${count}</span> <a class="increment-quantity">+</a></li>`);
        });
       const counts = document.getElementsByClassName("count");
       const incrementButtons = document.getElementsByClassName("increment-quantity");
       const decrementButtons = document.getElementsByClassName("decrement-quantity");
       for (let i = 0; i < incrementButtons.length; i++) {
           incrementButtons[i].onclick = function () {
               alert("Incremented quantity of " + movieTitles[i] + " by 1");
               oldCount = movieTitlesAndQuantities[movieTitles[i]]
               newCount = oldCount + 1;
               movieTitlesAndQuantities[movieTitles[i]] = newCount;
               counts[i].innerHTML = newCount;
               const data = {
                   type: "add",
                   newItem: movieTitles[i]
               };

               $.ajax({
                   type: "POST",
                   url: "items",
                   data: data,
                   success: function (result) {
                       console.log(result);
                   },
                   dataType: "json"
               });
           };
           decrementButtons[i].onclick = function () {
               alert("Decremented quantity of " + movieTitles[i] + " by 1");
                   oldCount = movieTitlesAndQuantities[movieTitles[i]]
               newCount = oldCount - 1;
               movieTitlesAndQuantities[movieTitles[i]] = newCount;
               if (newCount === 0) {
                   this.parentElement.remove();
                   // totalMovieCount -= 1;
                   // console.log("totalMovieCount is " + totalMovieCount);

               }
               else {
                   counts[i].innerHTML = newCount;
               }
               let data = {
                   type: "delete",
                   itemToDelete: movieTitles[i]
               };

               $.ajax({
                   type: "POST",
                   url: "items",
                   data: data,
                   success: function (result) {
                       console.log(result);
                   },
                   dataType: "json"
               });
           };
       }
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