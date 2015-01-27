package views.formdata;

/**
 * Created by Ivan Kirilyuk on 27.01.15.
 *
 */
public class ProgressEvent {

    public double progress;

    public ProgressEvent() {

    }

    public ProgressEvent(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
