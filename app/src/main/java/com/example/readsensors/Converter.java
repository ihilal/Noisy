package com.example.readsensors;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class Converter {

    /*helper functions for  CoapResponse */

    public static Set<WebLink> getWebLinks(CoapResponse response){
        return LinkFormat.parse(response.getResponseText());
    }

    public static Set<WebLink> getAllWebLinks(CoapResponse response) {
        return extractAllWebLinks(getWebLinks(response));
    }

    /*helper functions for Set<WebLink>*/

    public static WebLink[] getArray(Set<WebLink> webLinks) {
        return webLinks.toArray(new WebLink[webLinks.size()]);
    }

    public static Set<WebLink> extractAllWebLinks(Set<WebLink> webLinks) {
        Set<WebLink> topics = new ConcurrentSkipListSet();
        WebLink p;
        for(WebLink w: webLinks) {
            p = w;
            while (!cleanUri(p.getURI()).equals("ps")) {
                if (!topics.contains(p))
                    topics.add(p);
                p = getParent(p);
            }
        }
        return topics;
    }

    /*helper functions for WebLink*/

    public static WebLink makeWebLink(String uri, int ct) {
        WebLink webLink = new WebLink('/' + cleanUri(uri));
        setCT(webLink, ct);
        return webLink;
    }

    public static String getUri(WebLink webLink) {
        return cleanUri(webLink.getURI());
    }

    public static int getCT(WebLink webLink) {
        String cts = webLink.getAttributes().getContentTypes().get(0);
        int ct;
        try {
            ct = Integer.parseInt(cts.substring(0,1));
        } catch (NumberFormatException e) {
            ct = Integer.parseInt(cts.substring(0,0));
        }
        return ct;
    }

    public static void setCT(WebLink webLink, int ct) {
        webLink.getAttributes().clearContentType();
        webLink.getAttributes().addContentType(ct);
    }

    public static String getCTString (int ct) {
        return MediaTypeRegistry.toString(ct);
    }

    public static String getName (WebLink webLink) {
        return getName(webLink.getURI());
    }

    public static String getParentUri (WebLink webLink) {
        return getParentUri(webLink.getURI());
    }

    public static WebLink getParent (WebLink webLink) {
        return makeWebLink(getParentUri(webLink), 40);
    }

    public static Set<WebLink> getSubTopics(WebLink webLink, Set<WebLink> webLinks) {
        return getSubTopics(webLink.getURI(), webLinks);
    }

    /*helper functions for uri*/

    public static String getName (String uri) {
        return cleanUri(uri).substring(uri.lastIndexOf('/')+1);
    }

    public static String getParentUri (String uri) {
        return cleanUri(uri).substring(0, uri.lastIndexOf('/'));
    }

    public static String cleanUri (String uri) {
        if(uri.startsWith("/"))
            uri = uri.substring(1);
        if (uri.endsWith("/"))
            uri = uri.substring(0, uri.length()-1);
        return uri;
    }

    public static Set<WebLink> getSubTopics(String uri, Set<WebLink> webLinks) {
        uri = cleanUri(uri);
        Set<WebLink> subtopics = new ConcurrentSkipListSet();
        for (WebLink w: webLinks) {
            if(cleanUri(w.getURI()).contains(uri) && !cleanUri(w.getURI()).equals(uri)) {
                subtopics.add(w);
            }
        }
        return subtopics;
    }

}
