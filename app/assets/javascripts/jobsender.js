function manageSocket(socket) {
    socket.onmessage = function (msg) {
        var m = JSON.parse(msg.data);
        if (m.downloadURL != undefined) {
            window.location.href = m.downloadURL;
        }
        if (m.exceptions != undefined) {
            $("<ul>").appendTo("#exceptions");
            $.each(m.exceptions, function (i,e) {
                $("<li>" + e.reason + "</li>").appendTo("#exceptions");
            });
            $("</ul>").appendTo("#exceptions");
            $("#exceptions").show();
        }
    }
}

function sendJob(socket, vpath) {
    var dataObject = serializeConfigs();
    dataObject.vpath = vpath;
    $("#exceptions").empty().hide();
    socket.send(JSON.stringify(dataObject));
}

function serializeConfigs() {
    var v_config = {};
    v_config.streams = [];

    //serialize streams configs
    $("fieldset[name=stream]").each(
        function (i, fset) {
            var stream = {};
            $.each($(fset).serializeArray(),
                function (i, config) {
                    if (config.value === "on") {
                        stream[config.name] = "true";
                    } else if (config.value === "off") {
                        stream[config.name] = "false";
                    } else {
                        stream[config.name] = config.value;
                    }
                });
            v_config.streams.push(stream);
        }
    );

    //serialize common data
    $("fieldset[name=common]").each(
        function (i, fset) {
            $.each($(fset).serializeArray(),
                function (i, config) {
                    if (config.value === "on") {
                        v_config[config.name] = "true";
                    } else if (config.value === "off") {
                        v_config[config.name] = "false";
                    } else {
                        v_config[config.name] = config.value;
                    }
                });
        }
    );
    return v_config;
}