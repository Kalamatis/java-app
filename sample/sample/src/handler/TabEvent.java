package handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

public class TabEvent {
    private int id;
    private String name;
    private String url;
    private String time;

    // === Constructors ===
    public TabEvent(int id, String name,  String url, String time) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    	this.time = time;
    }

    // Empty constructor (useful if setting data later)
    public TabEvent() {}

    // === Getters ===
    public int getId() {return id;}

    public String getEvent() {return name;}


    public String getUrl() {
    	
    	return url;}

    public String getTime() {return time;}

    // === Setters ===
    public void setId(int id) {this.id = id;}

    public void setName(String name) {this.name = name;}


    public void setUrl(String url) {this.url = sanitizeUrl(url);}

    public void setTime(String time) { this.time = time;}

    // === Utility ===
    private String sanitizeUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) return "unknown";
        try {
            URI uri = new URI(rawUrl);
            String host = uri.getHost();
            return (host != null) ? host.replace("www.", "") : rawUrl;
        } catch (URISyntaxException e) {
            // fallback if URL is malformed
            return rawUrl;
        }
    }

    // === Converts to vector (for UI table models, etc.) ===
    public Vector<Object> getEventData() {
        Vector<Object> vec = new Vector<>();
        vec.add(id);
        vec.add(name);
        vec.add(sanitizeUrl(url));
        vec.add(time);
        return vec;
    }

    // === For debugging / logs ===
    @Override
    public String toString() {
    	
        return String.format(
            "TabEvent{id='%s', name='%s', url='%s', time='%s'}",
            id, name, url, time
        );
    }
}
