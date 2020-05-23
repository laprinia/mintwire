/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.p2pmodels.messages;

import mintwire.classes.MintFile;
import java.util.ArrayList;
import rice.p2p.commonapi.Message;
import rice.pastry.NodeHandle;
/**
 *
 * @author Lavinia
 */
public class SharedResource implements Message{
   ArrayList<MintFile> mintfiles;
   NodeHandle localhandle;
   boolean isResponse;

    public SharedResource(ArrayList<MintFile> mintfiles, NodeHandle localhandle,boolean isResponse) {
        this.mintfiles = mintfiles;
        this.localhandle = localhandle;
        this.isResponse=isResponse;
    }

    public boolean isItResponse() {
        return isResponse;
    }
    
    
    public ArrayList<MintFile> getMintfiles() {
        return mintfiles;
    }

    public void setMintfiles(ArrayList<MintFile> mintfiles) {
        this.mintfiles = mintfiles;
    }

    public NodeHandle getLocalhandle() {
        return localhandle;
    }

    public void setLocalhandle(NodeHandle localhandle) {
        this.localhandle = localhandle;
    }
    
   
    @Override
    public int getPriority() {
        return Message.LOW_PRIORITY;
    }
    
   
}
