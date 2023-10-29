
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
    let messageElement = document.getElementById("message");
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
           shoppingCartElement.append(`<li>${item} <a class="decrement-quantity">-</a> <span class="count">${count}</span> <a class="increment-quantity">+</a> Price: <a class="delete" color="green">Delete</a> <span class="price">${99 * count}</span> USD</li>`);
        });
       const counts = document.getElementsByClassName("count");
        const prices = document.getElementsByClassName("price");
       const incrementButtons = document.getElementsByClassName("increment-quantity");
       const decrementButtons = document.getElementsByClassName("decrement-quantity");
       const deleteButtons = document.getElementsByClassName("delete");
       for (let i = 0; i < incrementButtons.length; i++) {
           incrementButtons[i].onclick = function () {
               console.log("message");
               console.log(message);
               messageElement.innerHTML = "Incremented quantity of " + movieTitles[i] + " by 1";
               oldCount = movieTitlesAndQuantities[movieTitles[i]]
               newCount = oldCount + 1;
               movieTitlesAndQuantities[movieTitles[i]] = newCount;
               counts[i].innerHTML = newCount;
               prices[i].innerHTML = 99 * newCount;
               console.log(movieTitlesAndQuantities);
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
               console.log("message");
               console.log(message);
               messageElement.innerHTML = "Decremented quantity of " + movieTitles[i] + " by 1";
               oldCount = movieTitlesAndQuantities[movieTitles[i]]
               newCount = oldCount - 1;
               movieTitlesAndQuantities[movieTitles[i]] = newCount;
               counts[i].innerHTML = newCount;
               prices[i].innerHTML = 99 * newCount;
               console.log(movieTitlesAndQuantities);
               if (newCount === 0) {
                   // this.parentElement.remove();
                   // totalMovieCount -= 1;
                   // console.log("totalMovieCount is " + totalMovieCount);

               }
               else {
                   counts[i].innerHTML = newCount;
               }
               let data = {
                   type: "remove",
                   itemToRemove: movieTitles[i]
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

           deleteButtons[i].onclick = function () {
               console.log("message");
               console.log(message);
               messageElement.innerHTML = "Deleted " + movieTitles[i] + " from cart";
               delete movieTitlesAndQuantities[movieTitles[i]];
               console.log(movieTitlesAndQuantities);
               // movieTitlesAndQuantities[movieTitles[i]] = newCount;
               // counts[i].innerHTML = newCount;
               // prices[i].innerHTML = 99 * newCount;
               this.parentElement.remove();
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