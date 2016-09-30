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
    private static String QUERY = "tecnologia";           // You should put here the query to search for
    
    private static double LON = -3.569205;      // You should put here the longitude
    private static double LAT = 40.467927;      // You should put here the latitude
    private static double RADIUS = 500;            // You should put here the radius in kilometers
    
    private static String LANG = "es";          // You should put here the language you want to filter by
    
    private static String LANG_MODELS = "/home/rfabra/Documents/tfm/src/profiles";
    
    public static void main(String[] args) throws TwitterException, LangDetectException {
        NUM_TWEETS = 500000;       // You should put here the number of tweets you want to collect
        QUERY = "";           // You should put here the query to search for
    
        LON = 0.0;      // You should put here the longitude
        LAT = 0.0;      // You should put here the latitude
        RADIUS = 0;            // You should put here the radius in kilometers
        LANG_MODELS = "/home/rfabra/Documents/tfm/src/profiles";
        
        
        System.err.println("Twitter Downloader");
        System.err.println("------------------");
        
        System.err.println("Command line input:");
        for (int i = 0; i < args.length; ++i)
        {
            if (args[i].equals("-nt"))
            {
                NUM_TWEETS = Integer.parseInt(args[i+1]);
                System.err.println("\tNumber of tweets : " + NUM_TWEETS);
            }
            else if (args[i].equals("-lon"))
            {
                LON = Double.parseDouble(args[i+1]);
                System.err.println("\tLongitude : " + LON);
            }
            else if (args[i].equals("-lat"))
            {
                LAT = Double.parseDouble(args[i+1]);
                System.err.println("\tLatitude : " + LAT);
            }
            else if (args[i].equals("-rad"))
            {
                RADIUS = Integer.parseInt(args[i+1]);
                if (RADIUS == 0) RADIUS = 3;
                System.err.println("\tRadius : " + RADIUS);
            }
            else if (args[i].equals("-lm"))
            {
                LANG_MODELS = args[i+1];
                System.err.println("\tLang models : " + LANG_MODELS);
            }
            else if (args[i].equals("-q"))
            {
                QUERY = "";
                for (int j = i+1; j < args.length; j++)
                {
                    QUERY += args[j] + " ";
                }
                System.err.println("\tQuery : " + QUERY);
            }          
            else if (args[i].equals("-t_key"))
            {
                KEY = args[i+1];
                
                System.err.println("\tKey : " + KEY);
            }
            else if (args[i].equals("-t_secret"))
            {
                SECRET = args[i+1];
                
                System.err.println("\tSecret : " + SECRET);
            }
            else if (args[i].equals("-ta_token"))
            {
                ACCESSTOKEN = args[i+1];
                
                System.err.println("\tAccess Token : " + ACCESSTOKEN);
            }
            else if (args[i].equals("-ta_secret"))
            {
                ACCESSSECRET = args[i+1];
                
                System.err.println("\tAccess Secret : " + ACCESSSECRET);
            }
            else if (args[i].equals("-l"))
            {
                LANG = args[i+1];
                
                System.err.println("\tLanguage : " + LANG);
            }
        }
        
        System.exit(0);
        
        
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
            System.err.println("Error APITwitter.search.searchTweet: " + te.getMessage());
        }
        
        try{
            
            twitter = new TwitterFactory(cb.build()).getInstance();
        } catch (Exception te) {
            System.err.println("Error APITwitter.search.searchTweet: " + te.getMessage());
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
 
                            System.out.println(sStatusJSON);
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
                    System.err.println("Remaining API calls: " + remaining);
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
