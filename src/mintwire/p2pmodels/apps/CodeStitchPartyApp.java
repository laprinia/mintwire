
package mintwire.p2pmodels.apps;

import java.util.Collection;
import rice.environment.Environment;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.scribe.Scribe;
import rice.p2p.scribe.ScribeClient;
import rice.p2p.scribe.ScribeContent;
import rice.p2p.scribe.ScribeMultiClient;
import rice.p2p.scribe.ScribePolicy;
import rice.p2p.scribe.Topic;
import rice.p2p.scribe.rawserialization.RawScribeContent;
import rice.p2p.scribe.rawserialization.ScribeContentDeserializer;

/**
 *
 * @author Lavinia
 */
public class CodeStitchPartyApp implements Application,Scribe{

    @Override
    public boolean forward(RouteMessage rm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deliver(Id id, Message msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Topic topic, ScribeClient sc, RawScribeContent rsc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Topic topic, ScribeClient sc, RawScribeContent rsc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Topic topic, ScribeMultiClient smc, RawScribeContent rsc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Collection<Topic> clctn, ScribeClient sc, RawScribeContent rsc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Collection<Topic> clctn, ScribeMultiClient smc, RawScribeContent rsc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void publish(Topic topic, RawScribeContent rsc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void anycast(Topic topic, RawScribeContent rsc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void anycast(Topic topic, RawScribeContent rsc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setContentDeserializer(ScribeContentDeserializer scd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ScribeContentDeserializer getContentDeserializer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Topic topic, ScribeClient sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsubscribe(Topic topic, ScribeClient sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsubscribe(Topic topic, ScribeMultiClient smc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unsubscribe(Collection<Topic> clctn, ScribeMultiClient smc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addChild(Topic topic, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeChild(Topic topic, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRoot(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NodeHandle getRoot(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NodeHandle[] getChildren(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<NodeHandle> getChildrenOfTopic(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NodeHandle getParent(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Topic[] getTopics(ScribeClient sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Topic> getTopicsByClient(ScribeClient sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Topic> getTopicsByClient(ScribeMultiClient smc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int numChildren(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<ScribeClient> getClients(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<ScribeMultiClient> getClientsByTopic(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsTopic(Topic topic) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsChild(Topic topic, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ScribePolicy getPolicy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPolicy(ScribePolicy sp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Environment getEnvironment() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Topic topic, ScribeClient sc, ScribeContent sc1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Topic topic, ScribeClient sc, ScribeContent sc1, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Topic topic, ScribeMultiClient smc, ScribeContent sc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Collection<Topic> clctn, ScribeClient sc, ScribeContent sc1, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subscribe(Collection<Topic> clctn, ScribeMultiClient smc, ScribeContent sc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void publish(Topic topic, ScribeContent sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void anycast(Topic topic, ScribeContent sc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void anycast(Topic topic, ScribeContent sc, NodeHandle nh) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
