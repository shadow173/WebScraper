package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Main {
    public static void main(String[] args) throws InterruptedException {
        int runTimes = 0;
        List<String> newURLs = Collections.synchronizedList(new ArrayList<>());
        List<String> allURLs = Collections.synchronizedList(new ArrayList<>());
        String initialUrl = "https://www.bing.com";

        ScrapeSite first = new ScrapeSite(initialUrl, newURLs, allURLs);
        Thread firstThread = new Thread(first);
        firstThread.start();
        try {
            firstThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ExecutorService service = Executors.newFixedThreadPool(10);

        while (runTimes < 5) {
            // Make a copy of newURLs to iterate over during this iteration
            List<String> currentURLs = new ArrayList<>(newURLs);

            synchronized (currentURLs) {
                for (String url : currentURLs) {
                    if(runTimes < 10) { // only run 10 times to demonstrate capibilities. otherwise it would literally go forever
                        service.execute(new ScrapeSite(url, currentURLs, allURLs));

                    } else{
                        break;
                    }
                    runTimes++;

                }
            }
            System.out.println("ITERATION " + runTimes + " + ALL visible links and URLs on the website after visiting 10 links: " + currentURLs);
            // Optionally wait for the current batch to finish before proceeding
        }


        service.shutdown();
      // NOTE MIGHT GET YOUR COMPUTER RATE LIMITED THIS IS ALOT OF REQUESTS AT ONCE SO I MADE SURE TO LIMIT IT TO ONLY 10 REQUESTS


    }
}