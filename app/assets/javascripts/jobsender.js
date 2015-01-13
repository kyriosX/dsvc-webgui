function manageSocket(socket) {
    socket.onmessage = function (msg) {
        var m = JSON.parse(msg.data);
        if (m.downloadURL != undefined) {
            window.location.href = m.downloadURL;
        }
    }
}

function sendJob(socket, vpath) {
    var dataObject = serializeConfigs();
    dataObject.vpath = vpath;
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
                    stream[config.name] = config.value;
                });
            v_config.streams.push(stream);
        }
    );

    //serialize common data
    $("fieldset[name=common]").each(
        function (i, fset) {
            $.each($(fset).serializeArray(),
                function (i, config) {
                    v_config[config.name] = config.value;
                });

        }
    );
    return v_config;
}

