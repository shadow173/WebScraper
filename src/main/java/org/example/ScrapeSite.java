package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
// check if newlinks is the same thing after every iteration and make sure its only getting new links and iterating through those
// take in arraylist of websites, iterate over them find links to pages and return
// a new list to the main method which will call another class to make new threads per new website and scrape them

// new thought
public class ScrapeSite implements Runnable {
    private List<String> allLinks = new ArrayList<String>();

    String url;

    private List<String> newLinks = new ArrayList<String>();

    public ScrapeSite(String url, List<String> newLinks, List<String> allLinks) {
        this.url = url;
        this.newLinks = newLinks;
        this.allLinks = allLinks;
    }

    @Override
    public void run() {
        try {
            System.out.println("Scraping " + url);
            Document document = Jsoup.connect(url).get();
            Elements links = document.getElementsByTag("a");
            for (Element link : links) {
                String href = link.attr("href");
                if (href != null && !href.isEmpty()) {
                    if (!href.startsWith("http://") && !href.startsWith("https://")) {
                        href = combine(url, href);
                    }
                    newLinks.add(href);
                }
            }
        } catch (IOException e) {
            // mabye  add more robust error handling here
        }

        // Remove duplicates
//        Set<String> concurrentSet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
//        concurrentSet.addAll(newLinks);
//        newLinks.addAll(concurrentSet);
//        allLinks.addAll(concurrentSet);

    }

    private String combine(String baseUrl, String relativeUrl) {
        if (baseUrl.endsWith("/") && relativeUrl.startsWith("/")) {
            // Both end and start with slash, remove one
            return baseUrl + relativeUrl.substring(1);
        } else if (!baseUrl.endsWith("/") && !relativeUrl.startsWith("/")) {
            // No slash at the join, add one
            return baseUrl + "/" + relativeUrl;
        } else {
            // Either baseUrl ends with "/" or relativeUrl starts with "/", just concatenate
            return baseUrl + relativeUrl;
        }
    }
}
