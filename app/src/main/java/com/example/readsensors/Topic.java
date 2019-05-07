package com.example.readsensors;

public class Topic {

    private String name;
    private String path;
    private int ct;

    /* Takes a URI and extracts the name, path and ct from it */
    public Topic(String format) {
        String[] small = format.split(";");
        this.ct = Integer.parseInt(small[1].substring(small[1].indexOf('=') + 1));
        String p = small[0].replace('<', ' ').replace('>', ' ').trim();
        this.path = p.substring(p.indexOf('/') + 1);
        this.name = path.substring(this.path.lastIndexOf('/') + 1);
    }

    public Topic(String name, String path, int ct) {
        this.name = name;
        this.path = path;
        this.ct = ct;
    }

    public Topic(String name, int ct) {
        this.name = name;
        this.ct = ct;
    }

    /* Takes name, path and ct and makes them into a universal URI */
    public String makeURI(){
        StringBuilder sb = new StringBuilder().append("<").append(getPath()).append(">;ct=").append(getCt());
        return sb.toString();
    }

    /* Takes name and ct and makes them intp a URI for the CREATE command*/
    public String makeCreate() {
        StringBuilder sb = new StringBuilder().append("<").append(getName()).append(">;ct=").append(getCt());
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getCt() {
        return ct;
    }
}