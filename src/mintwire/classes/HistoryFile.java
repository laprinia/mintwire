/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintwire.classes;

/**
 *
 * @author Lavinia
 */
public class HistoryFile {
    private String fileName;
    private String size;
    private String alias;
    private String date;

    public HistoryFile(String fileName, String size, String alias, String date) {
        this.fileName = fileName;
        this.size = size;
        this.alias = alias;
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSize() {
        return size;
    }

    public String getAlias() {
        return alias;
    }

    public String getDate() {
        return date;
    }
    
}
