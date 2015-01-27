package controllers.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.kyrioslab.dsvc.node.messages.LocalMessage;
import com.kyrioslab.jffmpegw.attributes.parser.InfoParser;
import com.kyrioslab.jffmpegw.attributes.parser.MultimediaInfo;
import com.kyrioslab.jffmpegw.command.BuilderException;
import com.kyrioslab.jffmpegw.command.EncodeCommand;
import com.kyrioslab.jffmpegw.command.EncodeCommandBuilder;
import controllers.Application;
import controllers.routes;
import play.Logger;
import play.libs.Json;
import validator.GUIException;
import validator.ValidationException;
import validator.Validator;
import views.formdata.EncodeFailed;
import views.formdata.EncodeResult;
import views.formdata.ProgressEvent;
import views.formdata.VideoConfig;

import java.util.List;

/**
 * Created by Ivan Kirilyuk on 04.01.15.
 */
public class EncodeProcessSocket extends UntypedActor {

    public static Props props(ActorRef out, ActorRef client) {
        return Props.create(EncodeProcessSocket.class, out, client);
    }

    private final ActorRef out;

    private final ActorRef client;

    private double progressIncrement = 0;

    public EncodeProcessSocket(ActorRef out, ActorRef client) {
        this.out = out;
        this.client = client;
    }

    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            VideoConfig config = new GsonBuilder().setFieldNamingStrategy(
                    FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
                    .fromJson(message.toString(), VideoConfig.class);

            MultimediaInfo videoInfo = InfoParser.getInfo(Application.PROBE_LOCATION,
                    config.getVpath());

            //tell exceptions to browser and return
            List<ValidationException> errors = Validator.validate(videoInfo, config);
            if (errors.size() > 0) {
                out.tell(Json.toJson(
                        new GUIException(errors)
                ).toString(), getSelf());
                return;
            }

            try {
                EncodeCommand command = new EncodeCommandBuilder("",
                        config.getStreams()).build();
                command.setFormats(extractFormat(config.getVname()),
                        config.getOformat());
                LocalMessage.EncodeVideoMessage encodeJob = new LocalMessage.EncodeVideoMessage(
                        config.getVpath(),
                        command,
                        config.getDuration()
                );
                client.tell(encodeJob, getSelf());

                //initialize progress bar
                int duration = Integer.parseInt(config.getDuration());
                int segmentCount = duration / Application.SEGMENT_TIME;
                progressIncrement = 100 / segmentCount;
                sendProgress();
            } catch (BuilderException e) {
                Logger.error("Build Exception: {}", e);
            }
        } else if (message instanceof LocalMessage.EncodeResult) {
            String result = ((LocalMessage.EncodeResult) message).getResultPath();
            String url = routes.Application.download(result).url();

            //create internal encodeResult
            EncodeResult encodeResult = new EncodeResult();
            encodeResult.setDownloadURL(url);
            out.tell(Json.toJson(encodeResult).toString(), getSelf());
        } else if (message instanceof LocalMessage.EncodeJobFailedMessage) {
            LocalMessage.EncodeJobFailedMessage failedMessage = (LocalMessage.EncodeJobFailedMessage) message;
            out.tell(Json.toJson(new EncodeFailed(failedMessage.getReason(),
                    failedMessage.getCommand().getCommand().toString())).toString(), getSelf());
        } else if (message instanceof LocalMessage.ProgressMessage) {
            sendProgress();
        } else {
            unhandled(message);
        }
    }

    private void sendProgress() {
        out.tell(Json.toJson(
                new ProgressEvent(progressIncrement)
        ).toString(), getSelf());
    }

    private String extractFormat(String vname) {
        return vname.substring(vname.lastIndexOf(".") + 1);
    }

}
