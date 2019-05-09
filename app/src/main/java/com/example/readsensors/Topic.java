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
        String pathS = p.substring(p.indexOf('/') + 1);
        path = pathS.split("/");
        this.name = path[path.length - 1];
    }

    public Topic(String name, int ct) {
        this.name = name;
        this.ct = ct;
        this.path = null;
    }

    /* Takes name, path and ct and makes them into a universal URI to send easily as just a String*/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("</").append(getPathString()).append(">;ct=").append(getCt());
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

    public String[] getPath() {
        return path;
    }

    public void setPath(Topic parent) {
        String[] array = new String[parent.getPath().length + 1];
        int i;
        for (i = 0; i < parent.getPath().length; i++) {
            array[i] = parent.getPath()[i];
        }
        array[i] = this.name;
        this.path = array;
    }

    public void setPath(){
        String[] p = new String[2];
        p[0] = "ps";
        p[1] = this.name;
    }

    public String getPathString() {
        try {
            StringBuilder sb = new StringBuilder();
            for (String p : path) {
                sb.append('/');
                sb.append(p);
            }
            sb.deleteCharAt(0);
            return sb.toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public int getCt() {
        return ct;
    }
}