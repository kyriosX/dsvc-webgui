package views.formdata;

public class EncodeFailed {

    public String failed = "";
    public String command = "";

    public EncodeFailed(String failed, String command) {
        this.failed = failed;
        this.command = command;
    }

    public EncodeFailed() {
    }

    public String getFailed() {
        return failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
