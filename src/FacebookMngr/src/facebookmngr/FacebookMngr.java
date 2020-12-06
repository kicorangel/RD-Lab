/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookmngr;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class FacebookMngr {

    private static String APIKEY = "";
    
    public static void main(String[] args) {
        
    }
    
    
    public static FBPage GetPageInfo(String pageName, boolean silentMode)
    {
        if (!silentMode)
            System.out.println("Processing page: " + pageName);
        
        FBPage oPage = new FBPage();
        try
        {
            String sUrl = "https://graph.facebook.com/" + pageName;
            URL url = new URL(sUrl);
            String sJSON = Tools.GetHtml(url, "UTF-8");

            JSONObject oJSON = new JSONObject(sJSON);
            
            oPage.PageId = oJSON.getString("id");
            oPage.Name = oJSON.getString("name"); if (oPage.Name==null) oPage.Name="";
            oPage.WebSite = oJSON.optString("website", ""); if (oPage.WebSite==null) oPage.WebSite = "";
            oPage.WebSites = oPage.WebSite.split("\n"); 
            oPage.UserName = oJSON.optString("username", ""); if (oPage.UserName==null) oPage.UserName = "";
            oPage.Description = oJSON.optString("description", ""); if (oPage.Description == null) oPage.Description = "";
            oPage.About = oJSON.optString("about", ""); if (oPage.About == null) oPage.About = "";
            String sTalkingAboutCount = oJSON.optString("talking_about_count", "0");
            if (sTalkingAboutCount == null || sTalkingAboutCount.isEmpty())
                oPage.TalkingAboutCount =  0;
            else
                oPage.TalkingAboutCount = Integer.valueOf(sTalkingAboutCount);
            oPage.Category = oJSON.optString("category", ""); if (oPage.Category == null) oPage.Category = "";
            oPage.Link = oJSON.optString("link", ""); if (oPage.Link == null) oPage.Link = "";
            String sLikes = oJSON.optString("likes", "0");
            if (sLikes == null || sLikes.isEmpty())
                oPage.Likes = 0;
            else
                oPage.Likes = Integer.valueOf(sLikes);
            
            oPage.RTimestamp = Tools.GetTimestamp(new Date());
            
            JSONObject oCover = oJSON.optJSONObject("cover");
            if (oCover != null)
                oPage.Photo = oCover.optString("source", "");
            
            oPage.Valid = true; oPage.Exception = "";
            
            if (!silentMode)
                System.out.println(oPage.Name + " (" + oPage.Category + "): " + oPage.Description);
        }
        catch (Exception ex)
        {
            oPage.Valid = false;
            oPage.Exception = "ERROR UpdatePage(" + pageName + "): " + ex.toString();
            
            if (!silentMode)
                System.out.println(oPage.Exception);
        }
        
        return oPage;
    }
    
    public static ArrayList<FBPost> GetPagePosts(String pageName, int limit, boolean silentMode)
    {
        return GetPagePosts(pageName, limit, silentMode, true);
    }
    public static ArrayList<FBPost> GetPagePosts(String pageName, int limit, boolean silentMode, boolean getComments)
    {
        if (!silentMode)
            System.out.println("Processing posts from " + pageName + " with limit: " + limit);
        
        ArrayList<FBPost> oPosts = new ArrayList<FBPost>();
        
        try
        {
            String sUrl = "https://graph.facebook.com/" + pageName + "/feed?limit=100&access_token=" + APIKEY + "&format=json";

            int iResults = 0;
            int iTotalResults = 0;
            do
            {
                try
                {
                    if (!sUrl.isEmpty())
                    {
                        URL url = new URL(sUrl);
                        String sJSON = Tools.GetHtml(url, "UTF-8", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31", "", false);
                        int iTrials = 0;
                        while (sJSON==null && iTrials<10) {
                            if (!silentMode) {
                                System.out.println("Waiting...");
                            }
                            Thread.sleep(1000 * 60);    // Waiting for 1 minute
                            sJSON = Tools.GetHtml(url, "UTF-8", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31", "", false);
                            iTrials++;
                        }
                        
                        JSONObject oJSON = new JSONObject(sJSON);
                        JSONArray oData = oJSON.optJSONArray("data");
                        JSONObject oPaging = oJSON.optJSONObject("paging");
                        sUrl = (oPaging!=null)?oPaging.getString("next"):"";

                        iResults = oData.length();

                        for (int i=0;i<iResults;i++)
                        {
                            FBPost oPost = new FBPost();

                            JSONObject oJSPost = (JSONObject)oData.get(i);

                            oPost.PostId = oJSPost.getString("id");
                            String []ids = oPost.PostId.split("_");
                            oPost.PageId = ids[0];
                            oPost.Story = oJSPost.optString("story", "");
                            oPost.Application = oJSPost.optString("application", "");
                            oPost.Caption = oJSPost.optString("caption", "");

                            oPost.Description = oJSPost.optString("description", "");
                            oPost.Icon = oJSPost.optString("icon", "");

                            oPost.Link = oJSPost.optString("link", "");
                            oPost.Message = oJSPost.optString("message", "");
                            oPost.Name = oJSPost.optString("name", "");
                            oPost.Picture = oJSPost.optString("picture", "");

                            oPost.Source = oJSPost.optString("source", "");
                            oPost.Story = oJSPost.optString("story", "");
                            oPost.Type = oJSPost.optString("type", "");

                            JSONObject oFrom = oJSPost.optJSONObject("from");
                            if (oFrom != null)
                            {
                                oPost.From_Category = oFrom.optString("category","");
                                oPost.From_Id = oFrom.optString("id", "");
                                oPost.From_Name = oFrom.optString("name", "");
                            }

                            oPost.RTimestamp = Tools.GetTimestamp(new Date());
                            String sCTimestamp = oJSPost.getString("created_time");
                            String sUTimestamp = oJSPost.getString("updated_time");
                            oPost.CTimestamp = Tools.GetTimestamp(sCTimestamp); oPost.CTimestamp = String.format("%1$-15s", oPost.CTimestamp); oPost.CTimestamp = oPost.CTimestamp.replaceAll(" ", "0");
                            oPost.UTimestamp = Tools.GetTimestamp(sUTimestamp); oPost.UTimestamp = String.format("%1$-15s", oPost.UTimestamp); oPost.UTimestamp = oPost.UTimestamp.replaceAll(" ", "0");

                            oPost.NLikes = 0;
                            JSONObject oLikes = oJSPost.optJSONObject("likes");
                            if (oLikes!=null) {
                                JSONArray aLikes = oLikes.getJSONArray("data");
//                                oPost.NLikes = oLikes.optInt("count", 0);
                                oPost.NLikes = aLikes.length();
                                
                                if (oPost.NLikes==25) {
                                    JSONObject oLikesPaging = oLikes.getJSONObject("paging");
                                    if (oLikesPaging!=null) {
                                        String sNext = oLikesPaging.optString("next", "");
                                        if (!sNext.isEmpty()) {
                                            if (sNext.contains("limit")) {
                                                sNext = sNext.replace("limit=25", "limit=100000");
                                            } else {
                                                sNext += "limit=100000";
                                            }
                                            URL urlNext = new URL(sNext);
                                            String sJSONLikes = Tools.GetHtml(urlNext, "UTF-8", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31", "", false);
                                            if (sJSONLikes!=null && !sJSONLikes.isEmpty()) {
                                                JSONObject oJSONLikes = new JSONObject(sJSONLikes);
                                                if (oJSONLikes!=null) {
                                                    JSONArray aNextLikes = oJSONLikes.optJSONArray("data");
                                                    if (aNextLikes!=null) {
                                                        oPost.NLikes += aNextLikes.length();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                                

                            JSONObject oShares = oJSPost.optJSONObject("shares");
                            if (oShares != null)
                                oPost.NShares = oShares.optInt("count", 0);

                            oPost.NComments = 0;
                            JSONObject oComments = oJSPost.optJSONObject("comments");
                            if (oComments != null) {
                                JSONArray aComments = oComments.getJSONArray("data");
                                oPost.NComments = aComments.length();
//                                oPost.NComments = oComments.optInt("count", 0);
                                
                                if (oPost.NComments==25) {
                                    JSONObject oCommentsPaging = oComments.getJSONObject("paging");
                                    if (oCommentsPaging!=null) {
                                        String sNext = oCommentsPaging.optString("next", "");
                                        if (!sNext.isEmpty()) {
                                            if (sNext.contains("limit")) {
                                                sNext = sNext.replace("limit=25", "limit=100000");
                                            } else {
                                                sNext += "limit=100000";
                                            }
                                            URL urlNext = new URL(sNext);
                                            String sJSONComments = Tools.GetHtml(urlNext, "UTF-8", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31", "", false);
                                            if (sJSONComments!=null && !sJSONComments.isEmpty()) {
                                                JSONObject oJSONComments = new JSONObject(sJSONComments);
                                                if (oJSONComments!=null) {
                                                    JSONArray aNextComments = oJSONComments.optJSONArray("data");
                                                    if (aNextComments!=null) {
                                                        oPost.NComments += aNextComments.length();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                
                            }

                            if ((oPost.NComments>0) && (getComments))
                                oPost.Comments = GetPostComments(oPost.PostId, silentMode);

                            oPost.Valid = true; oPost.Exception = "";

                            oPosts.add(oPost);

                            iTotalResults++;
                        }
                    
                        if (!silentMode)
                            System.out.println(iTotalResults + " posts");
                    }
                }
                catch (Exception ex)
                {
                    FBPost oPost = new FBPost();
                    oPost.Valid = false;
                    oPost.Exception = "ERROR GetPagePosts(" + pageName + "): " + ex.toString();
                    oPosts.add(oPost);
                    
                    if (!silentMode)
                        System.out.println(oPost.Exception);
                }
            } while ((iResults==100) && (iTotalResults<limit));
        }
        catch (Exception ex)
        {
            FBPost oPost = new FBPost();
            oPost.Valid = false;
            oPost.Exception = "ERROR GetPagePosts(" + pageName + "): " + ex.toString();
            oPosts.add(oPost);
            
            if (!silentMode)
                System.out.println(oPost.Exception);
        }
        
        return oPosts;
        
    }
    
    public static ArrayList<FBComment> GetPostComments(String postId, boolean silentMode)
    {
        if (!silentMode)
            System.out.println("Processing comments for post " + postId);
        
        ArrayList<FBComment> oComments = new ArrayList<FBComment>();
        
        try
        {
            String sUrl = "https://graph.facebook.com/" + postId + "?access_token=" + APIKEY;
            URL url = new URL(sUrl);
            String sJSON = Tools.GetHtml(url, "UTF-8", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31", "", false);
            
            int iTrials = 0;
            while (sJSON==null && iTrials<10) {
                if (!silentMode) {
                    System.out.println("Waiting...");
                }
                Thread.sleep(1000 * 60);    // Waiting for 1 minute
                sJSON = Tools.GetHtml(url, "UTF-8", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31", "", false);
                iTrials++;
            }

            JSONObject oJSON = new JSONObject(sJSON);
            JSONObject oJSComments = oJSON.optJSONObject("comments");
            if (oJSComments != null)
            {
                JSONArray oData = oJSComments.optJSONArray("data");
                for (int i=0;i<oData.length();i++)
                {
                    JSONObject oJSComment = oData.getJSONObject(i);
                    FBComment oComment = new FBComment();
                    oComment.CommentId = oJSComment.getString("id");
                    String []ids = oComment.CommentId.split("_");
                    oComment.PageId = ids[0];
                    oComment.PostId = ids[1];
                    oComment.Message = oJSComment.optString("message", "");
                    oComment.RTimestamp = Tools.GetTimestamp(new Date());
                    String sCTimestamp = oJSComment.getString("created_time");
                    oComment.CTimestamp = Tools.GetTimestamp(sCTimestamp); 
                    oComment.Likes = oJSComment.optInt("likes", 0);
                    JSONObject oFrom = oJSComment.optJSONObject("from");
                    if (oFrom != null)
                    {
                        oComment.From_Id = oFrom.getString("id");
                        oComment.From_Name = oFrom.getString("name");
                    }
                    
                    oComment.Valid = true; oComment.Exception = "";
                    
                    oComments.add(oComment);
                }
                
                if (!silentMode)
                    System.out.println(oData.length() + " comments");
            }
            else
            {
                FBComment oComment = new FBComment();
                oComment.Valid = false;
                oComment.Exception = "ERROR GetPostComments(" + postId + "): No comments";
                oComments.add(oComment);
                
                if (!silentMode)
                    System.out.println(oComment.Exception);
            }
            
            
        }
        catch (Exception ex)
        {
            FBComment oComment = new FBComment();
            oComment.Valid = false;
            oComment.Exception = "ERROR GetPostComments(" + postId + "): " + ex.toString();
            oComments.add(oComment);
            
            if (!silentMode)
                System.out.println(oComment.Exception);
        }
        
        return oComments;
    }
}
