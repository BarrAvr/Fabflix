let payment_form = $("#payment_form");

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handlePaymentResult(resultDataString) {
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
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataString["message"]);
        $("#payment_error_message").text(resultDataString["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitPaymentForm(formSubmitEvent) {
    console.log("submit payment form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/payment", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: payment_form.serialize(),
            success: handlePaymentResult
        }
    );
}

// Bind the submit action of the form to a handler function
payment_form.submit(submitPaymentForm);

