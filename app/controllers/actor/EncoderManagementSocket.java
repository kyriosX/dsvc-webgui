package controllers.actor;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.*;
import com.kyrioslab.dsvc.node.messages.LocalMessage;
import play.libs.Json;
import scala.concurrent.duration.Duration;
import views.formdata.ClusterState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kirilyuk on 04.01.15.
 *
 */
public class EncoderManagementSocket extends UntypedActor {

    private static final int CLUSTER_STATUS_REFRESH_SECONDS = 4;
    private static final String RECHABILITY_STATUS_UNREACHABLE = "UNREACHABLE";
    private static final String RECHABILITY_STATUS_REACHABLE = "REACHABLE";
    private static final String ROLE_CLIENT = "client";


    public static Props props(ActorRef out, ActorRef client) {
        return Props.create(EncoderManagementSocket.class, out, client);
    }

    private final ActorRef out;

    private final ActorRef client;


    @Override
    public void preStart() {
        getContext().system().scheduler().schedule(
                Duration.apply(CLUSTER_STATUS_REFRESH_SECONDS, TimeUnit.SECONDS),
                Duration.apply(CLUSTER_STATUS_REFRESH_SECONDS, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        client.tell(new LocalMessage.ClusterStatusRequestMessage(), getSelf());
                    }
                },
                getContext().dispatcher());
    }

    public EncoderManagementSocket(ActorRef out, ActorRef client) {
        this.out = out;
        this.client = client;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof LocalMessage.ClusterStatusResponceMessage) {
            LocalMessage.ClusterStatusResponceMessage state = (LocalMessage.ClusterStatusResponceMessage) message;

            ClusterEvent.CurrentClusterState clusterState = state.getClusterState();
            Iterable<NodeMetrics> metrics = state.getMetrics().getNodeMetrics();

            List<views.formdata.Member> memberList = new ArrayList<>();
            Set<Member> unreachableList = clusterState.getUnreachable();
            for (Member m : clusterState.getMembers()) {

                //do not include client members
                if (m.getRoles().contains(ROLE_CLIENT)) {
                    continue;
                }

                String rstatus = RECHABILITY_STATUS_REACHABLE;
                if (unreachableList.contains(m)) {
                    rstatus = RECHABILITY_STATUS_UNREACHABLE;
                }

                String hostPort = m.address().hostPort();

                views.formdata.Member member = new views.formdata.Member(
                        hostPort.substring(hostPort.indexOf("@") + 1),
                        m.status().toString(),rstatus, m.getRoles().toString());

                NodeMetrics nodeMetrics = getNodeMetrics(m.address(), metrics);
                if (nodeMetrics != null) {
                    StandardMetrics.Cpu cpu = StandardMetrics.extractCpu(nodeMetrics);
                    if (cpu != null && cpu.systemLoadAverage().isDefined()) {
                        member.setCpuUsage(cpu.systemLoadAverage().get().toString());
                        member.setProcessors(String.valueOf(cpu.processors()));
                    }
                    StandardMetrics.HeapMemory heap = StandardMetrics.extractHeapMemory(nodeMetrics);
                    if (heap != null) {
                        member.setHeapUsage (String.format("%.1f",(double) heap.used() / 1024 / 1024));
                    }
                }
                memberList.add(member);
            }
            ClusterState currentState = new ClusterState(memberList);
            out.tell(Json.toJson(currentState).toString(), getSelf());
        }
    }

    private NodeMetrics getNodeMetrics(Address nodeAddress, Iterable<NodeMetrics> metrics) {
        for (NodeMetrics m : metrics) {
            if (m.address().equals(nodeAddress)) {
                return m;
            }
        }
        return null;
    }
}
