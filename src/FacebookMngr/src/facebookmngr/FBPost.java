/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookmngr;

import java.util.ArrayList;

/**
 *
 * @author @kicorangel
 */
public class FBPost {
    public String PageId = "";
    public String PostId = "";
    public String CTimestamp = "0";
    public String RTimestamp = "0";
    public String ATimestamp = "0";
    public String STimestamp = "0";
    public String UTimestamp = "0";
    public String Type = "";
    public String From_Id = "";
    public String From_Name = "";
    public String From_Category = "";
    public String Name = "";
    public String Message = "";
    public String Story = "";
    public String Caption = "";
    public String Description = "";
    public String Lang = "";
    public String Link = "";
    public String Picture = "";
    public String Icon = "";
    public String Source = "";
    public String Application = "";
    public int NShares = 0;
    public int NLikes = 0;
    public int NComments = 0;
    
    public ArrayList<FBComment> Comments = new ArrayList<FBComment>();
    
    public Boolean Valid = false;
    public String Exception = "Initialized";
}
