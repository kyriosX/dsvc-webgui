package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.kyrioslab.dsvc.node.client.ClientMain;
import com.kyrioslab.dsvc.node.util.FFMPEGService;
import controllers.actor.EncodeProcessListener;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.index;
import views.html.track;

import java.io.File;

public class Application extends Controller {

    private static ActorRef client;

    public static Result index() {
        if (client == null) {
            client = ClientMain.startClient("0", new FFMPEGService(
                    "/home/wizzard/diploma_work/dsvc/ffmpeg/ffmpeg",
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

            //redirect to job tracker
            return redirect(routes.Application.track(part.getFilename(), video.getAbsolutePath()));
        } else {
            flash("error", "Missing video file");
            return redirect(routes.Application.index());
        }
    }

    public static Result track(String vName, String vPath) {
        return ok(track.render(vName, vPath));
    }

    public static Result download(String videoPath) {
        response().setContentType("application/x-download");
        File resultVideo = new File(videoPath);
        response().setHeader("Content-disposition", "attachment; filename=" + resultVideo.getName());
        return ok(resultVideo);
    }


    public static WebSocket<String> encodeProcessSocket() {
        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
            public Props apply(ActorRef out) throws Throwable {
                return EncodeProcessListener.props(out, client);
            }
        });
    }

}
