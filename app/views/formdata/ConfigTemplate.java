package views.formdata;

import com.kyrioslab.jffmpegw.attributes.parser.StreamInfo;

import java.util.List;

/**
 * A Class aggregates client video configuration.
 */
public class ConfigTemplate {

    private String name;
    private String format;

    private StreamInfo video;
    private StreamInfo audio;

    public ConfigTemplate() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public StreamInfo getVideo() {
        return video;
    }

    public void setVideo(StreamInfo video) {
        this.video = video;
    }

    public StreamInfo getAudio() {
        return audio;
    }

    public void setAudio(StreamInfo audio) {
        this.audio = audio;
    }
}
