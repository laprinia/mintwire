package mintwire.p2pmodels.apps;

import java.util.ArrayList;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.CodeStitch;
import mintwire.p2pmodels.messages.PeerInfo;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.pastry.PastryNode;

/**
 *
 * @author Lavinia
 */
public class SendPeerInfoApp implements Application {

    private PastryNode pastryNode;
    private Endpoint endpoint;
    private ArrayList<PeerInfo> peerList = new ArrayList<>();

    public SendPeerInfoApp(PastryNode pastryNode) {
        this.pastryNode = pastryNode;
        this.endpoint = pastryNode.buildEndpoint(this, "peerinfoinstance");
        this.endpoint.register();
    }

    public void requestPeerInfo(Id id, PeerInfo info) {
        endpoint.route(id, info, null);
    }

    public ArrayList<PeerInfo> getPeerList() {
        return peerList;
    }

    @Override
    public boolean forward(RouteMessage rm) {
        return true;
    }

    @Override
    public void deliver(Id id, Message msg) {
        PeerInfo peerInfo = (PeerInfo) msg;

        if (peerInfo.isItResponse()) {
            System.err.println("am primit raspuns de la: " + peerInfo.getAlias());
            if (!peerList.contains(peerInfo)) {
                System.err.println("In peerInfo added"+peerInfo.getAlias());
                peerList.add(peerInfo);
                System.err.println(peerList.toString());
            }

        } else {
            System.out.println("received request for info from " + peerInfo.getAlias());
            requestPeerInfo(peerInfo.getNodeHandle().getId(), new PeerInfo(pastryNode.getLocalHandle(), pastryNode.alias, pastryNode.status, true));
            //send back info using nodehandle
            //impacheteaza frumusetea aia de poza de profil si pune o si pe aia
        }
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {

    }

}
