function sendAjax(login) {

    // get inputs
    var message = new Object();
    message.login = $('#login').val();
    message.receiver = $('#receiver').val();
    message.message = $('#message').val();
    message.time = $('#time').val();

    $.ajax({
        url: "users",
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(message),
        contentType: 'application/json',
        mimeType: 'application/json',

        success: function (data) {
            $("tr:has(td)").remove();
        },
        error: function (data, status, er) {
            alert("error: " + data + " status: " + status + " er:" + er);
        }
    });
}