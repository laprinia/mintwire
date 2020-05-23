
package mintwire.p2pmodels.apps;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import mintwire.classes.MintFile;
import mintwire.p2pmodels.MintNode;
import mintwire.p2pmodels.messages.PeerInfo;
import mintwire.p2pmodels.messages.SharedResource;
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
public class SendSharedResourceApp implements Application {
    private ArrayList<MintFile> mintFiles=new ArrayList<MintFile>();
    private MintNode mintNode;
    private PastryNode pastryNode;
    private Endpoint endpoint;
    public SendSharedResourceApp(PastryNode pastryNode){
        
        this.pastryNode = pastryNode;
        this.endpoint = pastryNode.buildEndpoint(this, "peerinfoinstance");
        this.endpoint.register();
        
    }
    public void setMintNode(MintNode mn){
       mintNode=mn;
    }
    public void sendResources(NodeHandle nh,SharedResource resource){
         endpoint.route(null, resource, nh);
    }
    public void requestResources(){
        List<rice.pastry.NodeHandle> handles = mintNode.getNode().getLeafSet().asList();
        HashSet<Id> uniqueHandles = new HashSet<>();
        handles.removeIf(e -> !uniqueHandles.add(e.getId()));
        
        for (rice.pastry.NodeHandle h : handles) {
            SharedResource sh=new SharedResource(null,pastryNode.getLocalHandle(), false);
            endpoint.route(h.getId(),sh,null);
        
        }
        
    }
    @Override
    public boolean forward(RouteMessage rm) {
        return true;
    }

    @Override
    public void deliver(Id id, Message msg) {
        SharedResource sh=(SharedResource) msg;
        if(sh.isItResponse()){
            System.err.println("received response for res from: "+sh.getLocalhandle());
            ArrayList<MintFile> mfs=sh.getMintfiles();
            for(MintFile mf:mfs){
            if(!mintFiles.contains(mf)){
                mintFiles.add(mf);
            }
        }
            
        }else{
            System.err.println("received request for res from:"+sh.getLocalhandle());
            File shared=new File(mintNode.getSharedPath());
            ArrayList<MintFile> delivery=new ArrayList<>();
            File[] list = shared.listFiles();
            for (File f: list){
               delivery.add(new MintFile(f.getAbsolutePath(),f.getName(),f.length(),pastryNode.alias,pastryNode.getLocalHandle()));
            }
           SharedResource resource=new SharedResource(delivery, mintNode.getNode().getLocalHandle(), true);
           sendResources(sh.getLocalhandle(), resource);
        }
    }

    @Override
    public void update(NodeHandle nh, boolean bln) {
        
    }

    public ArrayList<MintFile> getMintFiles() {
        return mintFiles;
    }
    
    
}
