package views.formdata;

public class VideoConfig {

    public String videoPath = "";
    public String format = "";
    public String acodec = "";
    public String vcodec = "";

    /**
     * Required for form instantiation.
     */
    public VideoConfig() {
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getAcodec() {
        return acodec;
    }

    public void setAcodec(String acodec) {
        this.acodec = acodec;
    }

    public String getVcodec() {
        return vcodec;
    }

    public void setVcodec(String vcodec) {
        this.vcodec = vcodec;
    }
}
