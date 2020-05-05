/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.p2pmodels.messages;

import rice.p2p.commonapi.Message;


public class MintMessage implements Message{
    private String text;
    private String dateStamp;
    private String senderAlias;
    
    public MintMessage(String text,String date,String senderAlias){
        this.text=text;
        this.dateStamp=date;
        this.senderAlias=senderAlias;
    }

    public String getText() {
        return text;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public String getSenderAlias() {
        return senderAlias;
    }
     @Override
    public String toString() {
        return "MintMessage{" +text+" "+"sender=" + senderAlias + ", dateStamp=" + dateStamp +'}';
    }
    
    
    @Override
    public int getPriority() {
       return rice.p2p.commonapi.Message.LOW_PRIORITY;
    }
    
}
