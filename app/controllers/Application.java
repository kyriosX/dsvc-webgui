package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.kyrioslab.dsvc.node.client.ClientMain;
import com.kyrioslab.dsvc.node.util.FFMPEGService;
import com.kyrioslab.jffmpegw.attributes.parser.InfoParser;
import com.kyrioslab.jffmpegw.attributes.parser.MultimediaInfo;
import controllers.actor.EncodeProcessSocket;
import controllers.actor.EncoderManagementSocket;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.index;
import views.html.manage;
import views.html.track;

import java.io.File;
import java.io.IOException;

public class Application extends Controller {

    public static final String PROBE_LOCATION = Application.class.getResource("ffprobe").getPath();
    public static final String FFMPEG_LOCATION = Application.class.getResource("ffmpeg").getPath();

    private static ActorRef client;

    public static Result index() {
        if (client == null) {
            client = ClientMain.startClient(new FFMPEGService(
                    FFMPEG_LOCATION,
                    30,
                    System.getProperty("java.io.tmpdir")));
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

            //get video info
            try {
                MultimediaInfo videoInfo = InfoParser.getInfo(PROBE_LOCATION, video.getAbsolutePath());

                //redirect to job tracker
                return ok(track.render(part.getFilename(), video.getAbsolutePath(), videoInfo));
            } catch (Exception e) {
                flash("error", "Exception while parsing video info.");
                return redirect(routes.Application.index());
            }
        } else {
            flash("error", "Missing video file");
            return redirect(routes.Application.index());
        }
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

    public static WebSocket<String> encodeProcessSocket() {
        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            public Props apply(ActorRef out) throws Throwable {
                return EncodeProcessSocket.props(out, client);
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

}
