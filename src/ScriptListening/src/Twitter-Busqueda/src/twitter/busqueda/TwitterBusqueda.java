package twitter.busqueda;

import Twitter.TwitterKeys;
import Twitter.TwitterMngr;
import Twitter.TwitterQuery;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class TwitterBusqueda {
    public static void main(String[] args) throws InterruptedException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        
        TwitterKeys oKeys = TwitterMngr.GetKeys(sProyecto);
        ArrayList<TwitterQuery> oQueries = TwitterMngr.GetQueries(sProyecto);
        String sPath = TwitterMngr.GetQueriesPath(sProyecto);
        
        Twitter twitter = null;

        ConfigurationBuilder cb = null;
        
        try
        {
            cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
              .setOAuthConsumerKey(oKeys.KEY)
              .setOAuthConsumerSecret(oKeys.SECRET)
              .setOAuthAccessToken(oKeys.ACCESSTOKEN)
              .setOAuthAccessTokenSecret(oKeys.ACCESSSECRET);
            cb.setJSONStoreEnabled(true);
        }
        catch (Exception te)
        {
            System.out.println("Error APITwitter.search.searchTweet: " + te.getMessage());
        }
        try{
            
            twitter = new TwitterFactory(cb.build()).getInstance();
        } catch (Exception te) {
            System.out.println("Error APITwitter.search.searchTweet: " + te.getMessage());
        }
        
        for (int iQuery=0;iQuery<oQueries.size();iQuery++) {
            TwitterQuery oQuery = oQueries.get(iQuery);
            Query querys = new Query(oQuery.Query);
            if (oQuery.Radius>0) {
                querys.setGeoCode(new GeoLocation(oQuery.Latitude, oQuery.Longitude), oQuery.Radius, "km");
            }
            querys.setCount(100);
//            if (oQuery.NumResults>0) {
//                querys.setCount(oQuery.NumResults);
//            }
            QueryResult result = null;

            int nTweets = 0;
            int iTweet = 0;
            while (nTweets<oQuery.NumResults) {
                try {
                    if (querys!=null) {
                        
                        try {
                            Map<String, RateLimitStatus> oRT = twitter.getRateLimitStatus();
                            RateLimitStatus rateLimit = oRT.get("/search/tweets");
                            int remaining = rateLimit.getRemaining();
                            if (remaining<=10) {
                               int remainingTime = rateLimit.getSecondsUntilReset();
                               Thread.sleep(remainingTime * 1000);
                            }
                        } catch (Exception te) {
                            System.out.println(te.toString());
                        }
                        
                        
                        result = twitter.search(querys);
                        querys = result.nextQuery();
                        List<Status> tweets = result.getTweets();

                        for (Status tweet : tweets ) {

                            String sStatusJSON = DataObjectFactory.getRawJSON(tweet);

                            if (tweet.getGeoLocation()!=null) {
                                String sStringCoordinates = ",\"custom-geo\": {" +
                                    "\"longitude\":" + tweet.getGeoLocation().getLongitude() + "," +
                                        "\"latitude\":" + tweet.getGeoLocation().getLatitude() + "}";
                                sStatusJSON = sStatusJSON.substring(0, sStatusJSON.length()-1) +
                                        sStringCoordinates + 
                                        "}";
                            } else if (tweet.getRetweetedStatus()!=null && tweet.getRetweetedStatus().getGeoLocation() != null) {
                                String sStringCoordinates = ",\"custom-geo\": {" +
                                    "\"longitude\":" + tweet.getRetweetedStatus().getGeoLocation().getLongitude() + "," +
                                        "\"latitude\":" + tweet.getRetweetedStatus().getGeoLocation().getLatitude() + "}";
                                sStatusJSON = sStatusJSON.substring(0, sStatusJSON.length()-1) +
                                        sStringCoordinates + 
                                        "}";
                            } 



                            String statusId = String.valueOf(tweet.getId());

                            Date oDate = tweet.getCreatedAt();
                            String sDateFolder = (oDate.getYear()+1900) + "" + String.format("%02d", (oDate.getMonth()+1)) + "" + String.format("%02d", oDate.getDate());

                            System.out.println(tweet.getCreatedAt() + " @" + tweet.getUser().getScreenName() + " - " + tweet.getText());

                            if (!new File(sPath + "/" + sDateFolder).exists()) {
                                Files.createDirectory(Paths.get(sPath + "/" + sDateFolder));
                            }
                            
                            FileWriter fw = new FileWriter(sPath + "/" + sDateFolder + "/" + statusId);
                            fw.append(sStatusJSON);
                            fw.close();


                            iTweet++;

                            if (iTweet>=oQuery.NumResults) {
                                break;
                            }
                        }

                        nTweets += tweets.size();
                    } else {
                        break;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Thread.sleep(5 * 1000);
                }
            }
            
//            System.out.println("Sleeping 5 minutes...");
//            Thread.sleep(5 * 60 * 1000);
        }
    }
}
