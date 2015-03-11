/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.autoritas.idi;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

/**
 *
 * @author kicorangel
 */
public class TwitterDownloader {

    public static String KEY = "";              // You should put here your Twitter KEY
    public static String SECRET = "";           // You should put here your Twitter SECRET
    public static String ACCESSTOKEN = "";      // You should put here your Twitter ACCESS TOKEN
    public static String ACCESSSECRET = "";     // You should put here your Twitter ACCESS SECRET
    
    private static int NUM_TWEETS = 50000;       // You should put here the number of tweets you want to collect
    private static String QUERY = "";           // You should put here the query to search for
    
    private static double LON = -3.569205;      // You should put here the longitude
    private static double LAT = 40.467927;      // You should put here the latitude
    private static int RADIUS = 500;            // You should put here the radius in kilometers
    
    private static String LANG = "es";          // You should put here the language you want to filter by
    
    private static String LANG_MODELS = "../lib/Language Detector/profiles";
    
    public static void main(String[] args) throws TwitterException, LangDetectException {
        
        DetectorFactory.loadProfile(LANG_MODELS);
        
        Twitter twitter = null;

        ConfigurationBuilder cb = null;
        
        try {
            cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
              .setOAuthConsumerKey(KEY)
              .setOAuthConsumerSecret(SECRET)
              .setOAuthAccessToken(ACCESSTOKEN)
              .setOAuthAccessTokenSecret(ACCESSSECRET);
            cb.setJSONStoreEnabled(true);
        }
        catch (Exception te) {
            System.out.println("Error APITwitter.search.searchTweet: " + te.getMessage());
        }
        
        try{
            
            twitter = new TwitterFactory(cb.build()).getInstance();
        } catch (Exception te) {
            System.out.println("Error APITwitter.search.searchTweet: " + te.getMessage());
        }
        
        Query querys = new Query(QUERY);
        querys.setGeoCode(new GeoLocation(LAT, LON), RADIUS, "km");
        querys.setCount(NUM_TWEETS);
        QueryResult result = null;
        
        int nTweets = 0;
        int iTweet = 0;
        while (nTweets<NUM_TWEETS && querys!=null) {
            
            result = twitter.search(querys);
            querys = result.nextQuery();
            List<Status> tweets = result.getTweets();

            for (Status tweet : tweets ) {

                if (tweet.getUser().getLang().equalsIgnoreCase(LANG)) {
                    Detector detector = DetectorFactory.create();
                    try {
                        detector.append(tweet.getText());
                        if (detector.detect().equalsIgnoreCase(LANG)) {
                            String sStatusJSON = DataObjectFactory.getRawJSON(tweet);

    //                        System.out.println(sStatusJSON);
                            iTweet++;
                        }
                    } catch (Exception langEx) {
                        
                    }
                }
                if (iTweet>=NUM_TWEETS) {
                    break;
                }
            }

            nTweets += tweets.size();


            boolean bWait = true;
            while (bWait) {
                try {
                    Map<String, RateLimitStatus> oRT = twitter.getRateLimitStatus();
                    RateLimitStatus rateLimit = oRT.get("/search/tweets");
                    int remaining = rateLimit.getRemaining();
                    System.out.println("Remaining API calls: " + remaining);
                    int remainingTime = rateLimit.getSecondsUntilReset();

                    if (remaining<=1) {
//                        System.out.println("Waiting " + remainingTime + " seconds");
                        Thread.sleep(remainingTime * 1000);
                    } else {
                        bWait = false;
                    }
                } catch (Exception te) {
//                    System.out.println("Waiting 60s");
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException ex) {

                    }
                }
            }
        }
    }
}
