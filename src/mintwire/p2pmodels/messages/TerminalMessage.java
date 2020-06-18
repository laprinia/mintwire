/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.p2pmodels.messages;

import rice.p2p.commonapi.Message;

/**
 *
 * @author Lavinia
 */
public class TerminalMessage implements Message{
 private String alias;

    public TerminalMessage(String alias) {
        this.alias=alias;
    }
 
    @Override
    public int getPriority() {
       return Message.LOW_PRIORITY;
    }

    public String getAlias() {
        return alias;
    }
    
}
