package com.example.readsensors;

public class Topic {

    private String name;
    private String[] path;
    private int ct;

    /* Takes a URI and extracts the name, path and ct from it */
    public Topic(String format) {
        String[] small = format.split(";");
        this.ct = Integer.parseInt(small[1].substring(small[1].indexOf('=') + 1));
        String p = small[0].replace('<', ' ').replace('>', ' ').trim();
        this.path = p.substring(p.indexOf('/') + 1).split("/");
        this.name = path[path.length-1];
    }

    public Topic(String name, int ct) {
        this.name = name;
        this.ct = ct;
    }

    /* Takes name, path and ct and makes them into a universal URI */
    public String toString(){
        return "</" + getPathAsString() + ">;ct=" + getCt();
    }

    /* Takes name and ct and makes them intp a URI for the CREATE command*/
    public String makeCreate() {
        return "<" + getName() + ">;ct=" + getCt();
    }

    public String getName() {
        return name;
    }

    public String[] getPath() {
        String[] copy = new String[this.path.length];
        System.arraycopy(this.path, 0, copy, 0, copy.length);
        return copy;
    }

    public String getPathAsString() {
        StringBuilder sb = new StringBuilder();
        for (String p : path)
            sb.append('/').append(p);
        sb.deleteCharAt(0);
        return sb.toString();
    }

    public int getCt() {
        return ct;
    }

    public void setPath(String[] path) {
        this.path = new String[path.length];
        System.arraycopy(path, 0, this.path, 0, path.length);
    }

    public void setPath(String path) {
        if (path.startsWith("/"))
            throw new IllegalArgumentException();
        this.path = path.split("/");
    }
}