/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.p2pmodels.messages;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;


public class MintMessage implements Message{
    private String text;
    private String dateStamp;
    
    private Id senderId;
    
    public MintMessage(String text,String date,Id senderId){
        this.text=text;
        this.dateStamp=date;
        this.senderId=senderId;
    }

    public String getText() {
        return text;
    }

    public String getDateStamp() {
        return dateStamp;
    }

   
     @Override
    public String toString() {
        return "MintMessage{" +text+" "+"sender=" + senderId + ", dateStamp=" + dateStamp +'}';
    }

    public Id getSenderId() {
        return senderId;
    }
    
    
    @Override
    public int getPriority() {
       return Message.LOW_PRIORITY;
    }
    
}
