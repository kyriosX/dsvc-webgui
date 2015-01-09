package views.formdata;

/**
 * Created by Ivan Kirilyuk on 09.01.15.
 * Reflects akka.cluster.Member. Used for
 * webgui encoders management.
 */
public class Member {

    public String hostPort = "";
    public String status = "";
    public String rechabilityStatus = "";
    public String roles = "";
    public String heapUsage = "";
    public String cpuUsage = "";
    public String processors = "";

    public Member() {
    }

    public Member(String hostPort, String status, String rechabilityStatus, String roles) {
        this.hostPort = hostPort;
        this.status = status;
        this.rechabilityStatus = rechabilityStatus;
        this.roles = roles;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRechabilityStatus() {
        return rechabilityStatus;
    }

    public void setRechabilityStatus(String rechabilityStatus) {
        this.rechabilityStatus = rechabilityStatus;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getHeapUsage() {
        return heapUsage;
    }

    public void setHeapUsage(String heapUsage) {
        this.heapUsage = heapUsage;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getProcessors() {
        return processors;
    }

    public void setProcessors(String processors) {
        this.processors = processors;
    }
}
