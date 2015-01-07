package views.formdata;

public class VideoConfig {

    public String vpath = "";
    public String format = "mp4";
    public String vcodec = "";
    public String vbitrate = "";
    public String vframerate = "";
    public String vsize = "";
    public String acodec = "";
    public String abitrate = "";
    public String sampling_rate = "";
    public String channels = "";

    /**
     * Required for form instantiation.
     */
    public VideoConfig() {
    }

    public String getVpath() {
        return vpath;
    }

    public void setVpath(String vpath) {
        this.vpath = vpath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVcodec() {
        return vcodec;
    }

    public void setVcodec(String vcodec) {
        this.vcodec = vcodec;
    }

    public String getVbitrate() {
        return vbitrate;
    }

    public void setVbitrate(String vbitrate) {
        this.vbitrate = vbitrate;
    }

    public String getVframerate() {
        return vframerate;
    }

    public void setVframerate(String vframerate) {
        this.vframerate = vframerate;
    }

    public String getVsize() {
        return vsize;
    }

    public void setVsize(String vsize) {
        this.vsize = vsize;
    }

    public String getAcodec() {
        return acodec;
    }

    public void setAcodec(String acodec) {
        this.acodec = acodec;
    }

    public String getAbitrate() {
        return abitrate;
    }

    public void setAbitrate(String abitrate) {
        this.abitrate = abitrate;
    }

    public String getSampling_rate() {
        return sampling_rate;
    }

    public void setSampling_rate(String sampling_rate) {
        this.sampling_rate = sampling_rate;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }
}
