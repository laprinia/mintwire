
package mintwire.p2pmodels.messages;

import rice.p2p.commonapi.Message;
import rice.p2p.scribe.Topic;


public class HostTopic implements Message{
     private String passphrase;
     private Topic topic;
     
     private PeerInfo peerInfo;

    public HostTopic(String passphrase, Topic topic, PeerInfo peerInfo) {
        this.passphrase = passphrase;
        this.topic = topic;
        
        this.peerInfo = peerInfo;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public Topic getTopic() {
        return topic;
    }

    

    public PeerInfo getPeerInfo() {
        return peerInfo;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

   

    public void setPeerInfo(PeerInfo peerInfo) {
        this.peerInfo = peerInfo;
    }

    @Override
    public String toString() {
        return "HostTopic{" + "passphrase=" + passphrase + ", topic=" + topic + ", peerInfo=" + peerInfo + '}';
    }

     
     

    @Override
    public int getPriority() {
       return Message.LOW_PRIORITY;
    }



     
    
}
