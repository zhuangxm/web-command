function eval_clojure(code) {
    var data;
    $.ajax({
        url: "eval.json",
        data: { expr : code },
        async: false,
        success: function(res) { data = res; }
    });
    return data;
}

function html_escape(val) {
    var result = val;
    result = result.replace(/\n/g, "<br/>");
    result = result.replace(/[<]/g, "&lt;");
    result = result.replace(/[>]/g, "&gt;");
    return result;
}

function onValidate(input) {
    return (input != "");
}

function onHandle(line, report) {
    var input = line.trim();

    // perform evaluation
    var data = eval_clojure(input);

    // handle error
    if (data.error) {
        return [{msg: data.message, className: "jquery-console-message-error"}];
    }

    // display expr results
    return [{msg: data.result, className: "jquery-console-message-value"}];
}

/**
 * This should be called anytime the changer div is updated so it can rebind event listeners.
 * Currently this is just to make the code elements clickable.
 */
function changerUpdated() {
    $("#changer code.expr").each(function() {
        $(this).css("cursor", "pointer");
        $(this).attr("title", "Click to insert '" + $(this).text() + "' into the console.");
        $(this).click(function(e) {
            controller.promptText($(this).text());
            controller.inner.click();
        });
    });
}

var controller;

$(document).ready(function() {
    controller = $("#console").console({
        welcomeMessage:'Enter some Clojure code, and it will be evaluated.',
        promptLabel: 'Command> ',
        commandValidate: onValidate,
        commandHandle: onHandle,
        autofocus:true,
        animateScroll:true,
        promptHistory:true
    });

    changerUpdated();
});
