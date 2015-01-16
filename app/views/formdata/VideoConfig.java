package views.formdata;

import com.kyrioslab.jffmpegw.attributes.parser.StreamInfo;

import java.util.List;

/**
 * A Class aggregates client video configuration.
 */
public class VideoConfig {

    private List<StreamInfo> streams;

    private String vpath;
    private String vname;
    private String inputFormat;
    private String oformat;
    private String duration;

    public VideoConfig () {}

    public List<StreamInfo> getStreams() {
        return streams;
    }

    public void setStreams(List<StreamInfo> streams) {
        this.streams = streams;
    }

    public String getVpath() {
        return vpath;
    }

    public void setVpath(String vpath) {
        this.vpath = vpath;
    }

    public String getOformat() {
        return oformat;
    }

    public void setOformat(String oformat) {
        this.oformat = oformat;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
