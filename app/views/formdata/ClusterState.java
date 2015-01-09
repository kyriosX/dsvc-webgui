package views.formdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan Kirilyuk on 09.01.15.
 *
 */
public class ClusterState {

    public List<Member> members = new ArrayList<>();

    public ClusterState() {
    }

    public ClusterState(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
