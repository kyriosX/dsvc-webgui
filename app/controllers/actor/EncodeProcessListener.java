package controllers.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.kyrioslab.dsvc.node.messages.LocalMessage;
import com.kyrioslab.jffmpegw.attributes.AudioAttributes;
import com.kyrioslab.jffmpegw.attributes.CommonAttributes;
import com.kyrioslab.jffmpegw.attributes.VideoAttributes;
import com.kyrioslab.jffmpegw.attributes.VideoSize;
import controllers.routes;
import play.libs.Json;
import views.formdata.EncodeResult;
import views.formdata.VideoConfig;

/**
 * Created by Ivan Kirilyuk on 04.01.15.
 *
 */
public class EncodeProcessListener extends UntypedActor {

    public static Props props(ActorRef out, ActorRef client) {
        return Props.create(EncodeProcessListener.class, out, client);
    }

    private final ActorRef out;

    private final ActorRef client;


    public EncodeProcessListener(ActorRef out, ActorRef client) {
        this.out = out;
        this.client = client;
    }

    public void onReceive(Object message) throws Exception {

        if (message instanceof String) {
            JsonNode msgNode = Json.parse(message.toString());
            VideoConfig config = Json.fromJson(msgNode, VideoConfig.class);

            CommonAttributes ca = new CommonAttributes();
            AudioAttributes aa = new AudioAttributes();
            VideoAttributes va = new VideoAttributes();

            ca.setFormat(config.getFormat());

            aa.setCodec(config.getAcodec());
            aa.setBitRate(config.getAbitrate());
            aa.setSamplingRate(Integer.valueOf(config.getSampling_rate()));
            aa.setChannels(Integer.valueOf(config.getChannels()));

            va.setCodec(config.getVcodec());
            va.setBitRate(config.getVbitrate());
            va.setFrameRate(Double.valueOf(config.getVframerate()));
            va.setVideoSize(new VideoSize(config.getVsize()));

            LocalMessage.EncodeVideoMessage encodeJob = new LocalMessage.EncodeVideoMessage(
                    config.getVpath(),
                    ca,
                    aa,
                    va
            );
            client.tell(encodeJob, getSelf());
        } else if (message instanceof LocalMessage.EncodeResult) {
            String result = ((LocalMessage.EncodeResult) message).getResultPath();
            String url = routes.Application.download(result).url();

            //create internal encodeResult
            EncodeResult encodeResult = new EncodeResult();
            encodeResult.setDownloadURL(url);
            out.tell(Json.toJson(encodeResult).toString(), getSelf());
        }
    }
}
