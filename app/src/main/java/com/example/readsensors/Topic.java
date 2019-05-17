package com.example.readsensors;

import org.eclipse.californium.core.WebLink;

public class Topic {

    private String name;
    private String path;
    private int ct;

    /* Takes a URI and extracts the name, path and ct from it */
    public Topic(WebLink link) {


        String a = link.getURI();
        this.path = a;
        String[] b = a.split("/");
        int s = b.length;
        this.name = b[s - 1];

        int j = link.toString().indexOf('[');

        if (link.toString().charAt(j + 2) == ']') {

            this.ct = Character.getNumericValue(link.toString().charAt(j + 1));

        } else {
            try {
                this.ct = Integer.parseInt(link.toString().substring(j + 1, j + 3));
            } catch (NumberFormatException | StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {

            }
        }
    }

    public Topic(int ct, String path) {
        this.path = path;
        this.ct = ct;
        this.name = this.path.substring(this.path.lastIndexOf('/') + 1);
    }

    @Override
    public String toString() {

        String res = "Topic Name : " + this.name + "\nTOPIC PATH : " + this.path + "\nCONTENT FORMAT : " + this.ct + "\n";

        return res;
    }

    public int getCt() {
        return ct;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Topic(String name, int ct) {
        this.name = name;
        this.ct = ct;
        this.path = null;
    }


}
