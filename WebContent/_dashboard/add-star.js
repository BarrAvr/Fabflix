let add_star_form = $("#add_star_form");

function submitNewStar() {

}

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleNewStarResult(resultDataString) {
    console.log("Result Data String");
    console.log(resultDataString);
    // let resultDataJson = JSON.stringify(resultDataString);
    // console.log("Result Data JSON");
    // console.log(resultDataJson);

    // console.log("handle login response");
    // console.log(resultDataJson);
    // console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to index.html
    if (resultDataString["status"] === "success") {
        console.log(resultDataString);
        $("#add_star_message").text(resultDataString["message"]);
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataString["message"]);
        $("#add_star_message").text(resultDataString["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitNewStarForm(formSubmitEvent) {
    console.log("submit new star form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    const data = {
        name: document.getElementById("name").value,
        birthYear: document.getElementById("birthYear").value,
    };

    console.log("data");
    console.log(data);

    $.ajax(
        "api/add-star", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: data,
            success: handleNewStarResult
        }
    );
}

// Bind the submit action of the form to a handler function
add_star_form.submit(submitNewStarForm);

