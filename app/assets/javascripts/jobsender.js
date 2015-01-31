function manageSocket(socket) {
    socket.onmessage = function (msg) {
        var m = JSON.parse(msg.data);
        if (m.downloadURL != undefined) {
            window.location.href = m.downloadURL;
        } else if (m.exceptions != undefined) {
            $("<ul>").appendTo("#exceptions");
            $.each(m.exceptions, function (i,e) {
                $("<li>" + e.reason + "</li>").appendTo("#exceptions");
            });
            $("</ul>").appendTo("#exceptions");
            $("#exceptions").show();
        } else if (m.failed != undefined) {
            $("<p>" + "Encode failed. Reason: " + m.failed + " Command: " +
                m.command +"</p>").appendTo("#exceptions");
            $("#exceptions").show();
        } else if (m.progress != undefined) {
            var progressbar = $("#progressBar");
            if (progressbar.is(":hidden")) {
                progressbar.attr("max", m.progress);
                progressbar.show();
                $("#encodeButton").hide();
            } else {
                var curr = progressbar.val();
                progressbar.val(curr + m.progress);
            }
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

function loadTemplate(path) {
    var tmplName = $("#config_template").val();
    $.get(path + tmplName, function (tmpl) {
        var t = JSON.parse(tmpl);
        if (t.name != undefined) {
            $("#vcodec" ).val( t.video.codec_name);
            $("#width" ).val( t.video.width);
            $("#height" ).val( t.video.height);
            $("#video > #bit_rate" ).val( t.video.bit_rate);
            $("#avg_frame_rate" ).val( t.video.avg_frame_rate);

            $("#acodec" ).val( t.audio.codec_name);
            $("#audio > #bit_rate" ).val( t.audio.bit_rate);
            $("#sample_rate" ).val( t.audio.sample_rate);
            $("#channels" ).val( t.audio.channels);

            $("#oformat").val(t.format);
        }
    });
}