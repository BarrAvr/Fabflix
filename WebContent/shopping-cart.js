
function getTotalPrice(movieObject) {
    let sum = 0;
    const movieCounts = Object.values(movieObject);
    movieCounts.forEach(c => {
        sum += c;
    })
    return sum * 99;
}
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
async function handleShoppingCartData(resultData) {
    let shoppingCartElement = jQuery("#shopping-cart");
    let totalPriceElement = document.getElementById("total-price");
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
       for (const item of movieTitles) {
           const searched_title = await fetchData(item);
           let count = movieTitlesAndQuantities[item];

           console.log("appending item " + searched_title + " to cart as li");
           console.log("appending item " + item + " to cart as li");
           shoppingCartElement.append(`<li>${searched_title} <a class="decrement-quantity">-</a> <span class="count">${count}</span> <a class="increment-quantity">+</a> Price: <a class="delete" color="green">Delete</a> <span class="price">${99 * count}</span> USD</li>`);
       }
       totalPriceElement.innerHTML = "Total Price: $" + getTotalPrice(movieTitlesAndQuantities);
       const counts = document.getElementsByClassName("count");
       const prices = document.getElementsByClassName("price");
       const incrementButtons = document.getElementsByClassName("increment-quantity");
       const decrementButtons = document.getElementsByClassName("decrement-quantity");
       const deleteButtons = document.getElementsByClassName("delete");
       for (let i = 0; i < incrementButtons.length; i++) {
           incrementButtons[i].onclick = async function () {
               console.log("message");
               console.log(message);
               let title = await fetchData(movieTitles[i]);
               messageElement.innerHTML = "Incremented quantity of " + title + " by 1";
               // messageElement.innerHTML = "Incremented quantity of " + movieTitles[i] + " by 1";
               oldCount = movieTitlesAndQuantities[movieTitles[i]]
               newCount = oldCount + 1;
               movieTitlesAndQuantities[movieTitles[i]] = newCount;
               counts[i].innerHTML = newCount;
               prices[i].innerHTML = 99 * newCount;
               totalPriceElement.innerHTML = "Total Price: $" + getTotalPrice(movieTitlesAndQuantities);
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
           decrementButtons[i].onclick = async function () {
               console.log("message");
               console.log(message);
               if(movieTitlesAndQuantities[movieTitles[i]] > 0){
                   let title = await fetchData(movieTitles[i]);
                   messageElement.innerHTML = "Decremented quantity of " + title + " by 1";
                   // messageElement.innerHTML = "Decremented quantity of " + movieTitles[i] + " by 1";
                   oldCount = movieTitlesAndQuantities[movieTitles[i]]
                   newCount = oldCount - 1;
                   movieTitlesAndQuantities[movieTitles[i]] = newCount;
                   counts[i].innerHTML = newCount;
                   prices[i].innerHTML = 99 * newCount;
                   totalPriceElement.innerHTML = "Total Price: $" + getTotalPrice(movieTitlesAndQuantities);
                   console.log(movieTitlesAndQuantities);
                   if (newCount === 0) {
                       // doesn't properly handle empty cart after deletion of last movie
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
               }
               // let title = await fetchData(movieTitles[i]);
               // messageElement.innerHTML = "Decremented quantity of " + title + " by 1";
               // // messageElement.innerHTML = "Decremented quantity of " + movieTitles[i] + " by 1";
               // oldCount = movieTitlesAndQuantities[movieTitles[i]]
               // newCount = oldCount - 1;
               // movieTitlesAndQuantities[movieTitles[i]] = newCount;
               // counts[i].innerHTML = newCount;
               // prices[i].innerHTML = 99 * newCount;
               // totalPriceElement.innerHTML = "Total Price: $" + getTotalPrice(movieTitlesAndQuantities);
               // console.log(movieTitlesAndQuantities);
               // if (newCount === 0) {
               //     // doesn't properly handle empty cart after deletion of last movie
               // }
               // else {
               //     counts[i].innerHTML = newCount;
               // }
               // let data = {
               //     type: "remove",
               //     itemToRemove: movieTitles[i]
               // };
               //
               // $.ajax({
               //     type: "POST",
               //     url: "items",
               //     data: data,
               //     success: function (result) {
               //         console.log(result);
               //     },
               //     dataType: "json"
               // });
           };

           deleteButtons[i].onclick = async function () {
               console.log("message");
               console.log(message);
               let title = await fetchData(movieTitles[i]);
               messageElement.innerHTML = "Deleted " + title + " from cart";
               // messageElement.innerHTML = "Deleted " + movieTitles[i] + " from cart";
               delete movieTitlesAndQuantities[movieTitles[i]];
               console.log(movieTitlesAndQuantities);
               this.parentElement.remove();
               totalPriceElement.innerHTML = "Total Price: $" + getTotalPrice(movieTitlesAndQuantities);
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

async function fetchData(movie_id) {
    let title = "";
    try {
        // let title = "";
        await $.ajax({
            type: "GET",
            url: "id-to-title",
            data: { movie_id: movie_id },
            success: function (result) {
                title = result["movie_title"];
            }
        });
        console.log("AJAX call completed with:", title);
    } catch (error) {
        console.error("Error in AJAX call:", error);
    }
    return title;
}




// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "items",
    success: (resultData) => handleShoppingCartData(resultData)
});

// search_results.submit(submitSearch);