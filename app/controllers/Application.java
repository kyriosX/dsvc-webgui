package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.kyrioslab.dsvc.node.client.ClientMain;
import com.kyrioslab.dsvc.node.util.FFMPEGService;
import com.kyrioslab.jffmpegw.attributes.parser.InfoParser;
import com.kyrioslab.jffmpegw.attributes.parser.MultimediaInfo;
import controllers.actor.EncodeProcessSocket;
import controllers.actor.EncoderManagementSocket;
import play.Logger;
import play.Play;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.formdata.ConfigTemplate;
import views.html.index;
import views.html.manage;
import views.html.track;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Application extends Controller {

    private static final String TEMPLATES_FILE_NAME = "templates.json";
    private static final String CLIENT_PROPERTIES = "dsvc_client.properties";

    public static final String FFPROBE_LOCATION = "ffprobe_location";
    public static final String FFMPEG_LOCATION = "ffmpeg_location";
    public static final String SEGMENT_LENGTH = "segment_length";
    public static final String TMP_DIR = "tmp_dir";

    private static Gson gson = new GsonBuilder().setFieldNamingStrategy(
            FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private static Properties clientProps = new Properties();
    private static ActorRef client;

    public static Result index() throws IOException {

        //load dsvc-client
        if (client == null) {
            clientProps.load(Play.application().resourceAsStream(CLIENT_PROPERTIES));
            client = startClient();
        }
        return ok(index.render());
    }

    /**
     * After handling file by this method, encode
     * message goes to client and then reflects to WebSocket
     * connection.
     *
     * @return redirect to websocket
     */
    public static Result uploadVideo() {
        Http.MultipartFormData.FilePart part =
                request().body().asMultipartFormData().getFile("video");
        File video = part.getFile();

        if (video != null) {
            Logger.info("Uploaded video {}", video.getAbsoluteFile());

            //get video info
            try {
                MultimediaInfo videoInfo = InfoParser.getInfo(clientProps.getProperty(FFPROBE_LOCATION), video.getAbsolutePath());

                List<ConfigTemplate> configTemplateList = loadTemplates();
                //redirect to job tracker
                return ok(track.render(part.getFilename(), video.getAbsolutePath(), videoInfo,
                        configTemplateList));
            } catch (Exception e) {
                Logger.error("Exception while parsing video info: {}", e);
                return redirect(routes.Application.index());
            }
        } else {
            flash("error", "Missing video file");
            return redirect(routes.Application.index());
        }
    }

    private static List<ConfigTemplate> loadTemplates() {
        JsonReader reader = new JsonReader(
                new InputStreamReader(Play.application().resourceAsStream(TEMPLATES_FILE_NAME)));

        return gson.fromJson(reader, new TypeToken<ArrayList<ConfigTemplate>>() {
        }.getType());
    }

    public static Result download(String videoPath) {
        response().setContentType("application/x-download");
        File resultVideo = new File(videoPath);
        response().setHeader("Content-disposition", "attachment; filename=" + resultVideo.getName());
        return ok(resultVideo);
    }


    public static Result manage() {
        return ok(manage.render());
    }

    public static Result getTemplate(String name) {
        List<ConfigTemplate> configTemplateList = loadTemplates();
        ConfigTemplate template = null;
        for (ConfigTemplate t : configTemplateList) {
            if (t.getName().equals(name)) {
                template = t;
                break;
            }
        }
        if (template != null) {
            return ok(gson.toJson(template));
        } else {
            return notFound();
        }
    }

    public static WebSocket<String> encodeProcessSocket() {
        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            public Props apply(ActorRef out) throws Throwable {
                return EncodeProcessSocket.props(out, client, clientProps);
            }
        });
    }

    public static WebSocket<String> encoderManagementSocket() {
        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            public Props apply(ActorRef out) throws Throwable {
                return EncoderManagementSocket.props(out, client);
            }
        });
    }

    private static ActorRef startClient() {
        final String clientAdress = clientProps.getProperty("dsvc_client_address");
        final String clientPort = clientProps.getProperty("dsvc_client_port");
        final String seedNodes = clientProps.getProperty("seed_nodes");

        return ClientMain.startClient(new FFMPEGService(
                        clientProps.getProperty(FFMPEG_LOCATION),
                        Integer.parseInt(clientProps.getProperty(SEGMENT_LENGTH)),
                        clientProps.getProperty(TMP_DIR)
                ),
                clientAdress,
                clientPort,
                seedNodes);
    }
}
