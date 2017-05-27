package twitter.timeline;

import Twitter.TwitterKeys;
import Twitter.TwitterMngr;
import Twitter.TwitterTimeline;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class TwitterTimelineRetrieval {

    public static void main(String[] args) {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        
        TwitterKeys oKeys = TwitterMngr.GetKeys(sProyecto);
        ArrayList<TwitterTimeline> oTimelines = TwitterMngr.GetTimelines(sProyecto);
        String sPath = TwitterMngr.GetTimelinesPath(sProyecto);
        
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
        
        for (int iTimeline=0;iTimeline<oTimelines.size();iTimeline++) {
            TwitterTimeline oTimeline = oTimelines.get(iTimeline);
            
            List<Status> statuses;
            try {
                int iPage = 1;
                int iTweets = 0;
                do {

                    try {
                        Map<String, RateLimitStatus> oRT = twitter.getRateLimitStatus();
                        RateLimitStatus rateLimit = oRT.get("/statuses/user_timeline");
                        int remaining = rateLimit.getRemaining();
                        if (remaining<=10) {
                           int remainingTime = rateLimit.getSecondsUntilReset();
                           Thread.sleep(remainingTime * 1000);
                        }
                    } catch (Exception te) {
                        System.out.println(te.toString());
                    }
                    
                    
                    int iPageSize = 0;
                    if (iTweets+200<oTimeline.NumResults) {
                        iPageSize = 200;
                    } else {
                        iPageSize = oTimeline.NumResults - iTweets;
                    }
                    statuses = twitter.getUserTimeline(oTimeline.UserName,new Paging(iPage, iPageSize));

                    for (Status tweet : statuses) {
                        String sStatusId = "-1";
                        try
                        {
//                            String sUserId = String.valueOf(tweet.getUser().getId());
//                            String sRetweetedUser = "";
//                            if ((status.getRetweetedStatus()!=null) && (status.getRetweetedStatus().getUser()!=null))
//                                sRetweetedUser = String.valueOf(status.getRetweetedStatus().getUser().getId());
//                            sStatusId = String.valueOf(status.getId());
    
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

                        }
                        catch (Exception ex)
                        {
                            System.out.println("ERROR in status id " + sStatusId);
                        }

                        iTweets++;
                    }
                    iPage++;

    //                break;
                } while (statuses.size()>0 && iTweets<oTimeline.NumResults);
            } catch (Exception ex ) {
                
            }
        }
    }
}
