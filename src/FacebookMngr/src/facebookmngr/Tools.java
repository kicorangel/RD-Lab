/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookmngr;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author kico
 */
public class Tools {
    public static String GetHtml(String url, String charset)
    {
        if (url.endsWith(".pdf"))
            return "";
        else
            return GetHtml(GetUrl(url), charset);
    }
    
    public static String GetHtml(URL pageUrl, String charset)
    {
        return GetHtml(pageUrl, charset, true);
    }

    public static String GetHtml(URL pageUrl, String charset, boolean correct)
    {
        return GetHtml(pageUrl, charset, "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.17) Gecko/20110422 Ubuntu/10.04 (lucid) Firefox/3.6.17", "", correct);
    }
    
    public static String GetHtml(URL pageUrl, String charset, String userAgent, String cookie, boolean correct)
    {
        try
        {

            // Open connection to URL for reading.
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                            pageUrl.openStream(), "ISO-8859-1"));

            boolean bReCrawl = true;
            String sCharset = "iso-8859-1"; // ISO recomendado para los idiomas europeos
            if (!charset.isEmpty())
                sCharset = charset;
            
            String sToReturn = "";
            Boolean bOther = false;
            while (bReCrawl)
            {

                /*
                HttpURLConnection uc = (HttpURLConnection)pageUrl.openConnection();
                uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; InfoPath.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; MS-RTC LM 8)");
            uc.connect();
            if (uc.getContentType().contains("text/html"))
            {
                String sResp = uc.getResponseMessage();
                String sEncoding = uc.getContentEncoding();
//                OutputStream outputStream = uc.getOutputStream();
                InputStream inputStream = uc.getInputStream();
                int b = inputStream.read();
                inputStream.close();
//                outputStream.close();
            }
*/

                URLConnection urlConnection = pageUrl.openConnection();

		urlConnection.setAllowUserInteraction(false);
//                urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; InfoPath.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; MS-RTC LM 8)");
//                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/533.4 (KHTML, like Gecko) Chrome/5.0.375.99 Safari/533.4");
//                urlConnection.setRequestProperty("User-Agent", "Corex Navigator");
//                urlConnection.setRequestProperty("Accept-Charset", "utf-8");
                urlConnection.setRequestProperty("User-Agent", userAgent);
                if (!cookie.isEmpty())
                    urlConnection.setRequestProperty("cookie", cookie);
//                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.17) Gecko/20110422 Ubuntu/10.04 (lucid) Firefox/3.6.17");
//                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.17) Gecko/20110422 Ubuntu/10.04 (lucid) Firefox/3.6.17");
//                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.17) Gecko/20110422 Ubuntu/10.04 (lucid) Firefox/3.6.17");
//                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.17) Gecko/20110422 Ubuntu/10.04 (lucid) Firefox/3.6.17");
//		InputStream urlStream = pageUrl.openStream();
                InputStream urlStream = urlConnection.getInputStream();
//		String type = urlConnection.guessContentTypeFromStream(urlStream);
//		if (type == null)
//		    break;
//		if (type.compareTo("text/html") != 0)
//		    break;
                
		// search the input stream for links
		// first, read in the entire URL
		byte b[] = new byte[1000];
		int numRead = urlStream.read(b);
               
		String content = new String(b, 0, numRead, Charset.forName(sCharset));
		while (numRead != -1) {
		    numRead = urlStream.read(b);
		    if (numRead != -1) {
			String newContent = new String(b, 0, numRead, Charset.forName(sCharset));
			content += newContent;
		    }
		}
		urlStream.close();


                /*
                InputStream in = pageUrl.openStream();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, sCharset));


                // Read page into buffer.
                String line;
                StringBuffer pageBuffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                        pageBuffer.append(line);
                }

                sToReturn = pageBuffer.toString();
*/

                sToReturn = content;
                bReCrawl = false;
                
                if (correct)
                {
                    if (sToReturn.toLowerCase().indexOf("windows-1252")>0)
                    {
                        if ((sToReturn.indexOf("�")>0) && !bOther)
                        {
                            bOther = true;
                            bReCrawl = true;
                            sCharset = "windows-1252";
                            continue;
                        }
                    }
                    if (sToReturn.toLowerCase().indexOf("iso-8859-1")>0)
                    {
                        if ((sToReturn.indexOf("�")>0) && !bOther)
                        {
                            bOther = true;
                            bReCrawl = true;
                            sCharset = "ISO-8859-1" ;
                            continue;
                        }
                    }

                    if (sToReturn.toLowerCase().indexOf("utf-8")>0)
                    {
                        if (!sCharset.equalsIgnoreCase("utf-8") && !bOther)
                        {
                            bReCrawl = true;
                            sCharset = "UTF-8";
                            continue;
                        }
                    }
                }                 
            }
            return sToReturn;
        }
        catch (Exception e)
        {
            String s = e.toString();
            String k = s;
            k=s;
        }

        return null;
    }
    
    public static URL GetUrl(String url)
    {
        url = removeWwwFromUrl(url);
        URL myUrl = verifyUrl(url);
        return myUrl;
    }
    
    public static String GetHost(String url)
    {
        URI uri = URI.create(url);
                String sHost = uri.getHost() ;
                return sHost;
    }

    // Verify URL format.
    public static URL verifyUrl(String url)
    {
		// Only allow HTTP URLs.
		if (!url.toLowerCase().startsWith("http://"))
			return null;

		// Verify format of URL.
		URL verifiedUrl = null;
		try {
			verifiedUrl = new URL(url);
		} catch (Exception e) {
			return null;
		}

		return verifiedUrl;
    }

    // Remove leading "www" from a URL's host if present.
    public static String removeWwwFromUrl(String url)
    {
        /*
		int index = url.indexOf("://www.");
		if (index != -1) {
			return url.substring(0, index + 3) + url.substring(index + 7);
		}

		return (url);
         *
         */
        return url;
    }
    
    public static String GetCanonicalHost(String url)
    {
        String sHost = "";
        
        try
        {
            url = url.replace("feed://", "http://");
            url = url.replace("webcal://", "https://");
            
            if (!url.startsWith("http"))
                url = "http://" + url;
                
            
            URL oUrl = new URL(url);
            sHost = oUrl.getHost();
        }
        catch (Exception ex)
        {
            sHost = "ERROR GetCannonicalHost(" + url + "): " + ex.toString();
        }
        
        return sHost;
    }
    
    public static int GetInt32(String intValue)
    {
        return GetInt32(intValue, 0);
    }

    public static int GetInt32(String intValue, int defaultValue)
    {
        try
        {
            double oD = Double.parseDouble(intValue);
            double toParse = Math.round(oD);
            int toRet = (int)toParse;
            return toRet;
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }
    
    public static long GetLong(String longValue)
    {
        return GetLong(longValue, 0);
    }

    public static long GetLong(String longValue, long defaultValue)
    {
        try
        {
            double oD = Double.parseDouble(longValue);
            double toParse = Math.round(oD);
            long toRet = (long)toParse;
            return toRet;
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    public static double GetDouble(String doubleValue)
    {
        return GetDouble(doubleValue, 0);
    }

    public static double GetDouble(String doubleValue, double defaultValue)
    {
        try
        {
            return Double.parseDouble(doubleValue);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }
    
    public static long IP2Long(String IP) {
		long f1, f2, f3, f4;
		String tokens[] = IP.split("\\.");
		if (tokens.length < 4) return -1;

		try {
			f1 = Long.parseLong(tokens[0]) << 24;
			f2 = Long.parseLong(tokens[1]) << 16;
			f3 = Long.parseLong(tokens[2]) << 8;
			f4 = Long.parseLong(tokens[3]);
			return f1+f2+f3+f4;
		} catch (Exception e) {
			return -1;
		}

	}
    
    public static String GetTimestamp(Date pubDate)
    {
        String sDate = "0";
        try
        {
            SimpleDateFormat formato = new SimpleDateFormat( "yyyyMMddHHmmssS"); 
            sDate = formato.format(pubDate);
//            sDate = sDate.substring(0, sDate.length() - 2);
            if (sDate.length()<15)
                sDate = sDate + "000000";
            
            sDate = sDate.substring(0, 15);
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
        
        return sDate;
    }
    
    public static String GetTimestamp(String pubDate)
    {
        String sDate = "0";
        try
        {
            Date myDate = new Date(pubDate);

            sDate = GetTimestamp(myDate);
        }
        catch (Exception ex)
        {
            try
            {
                // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
                // http://www.coderanch.com/t/409709/java/java/date-format-conversion-MM-dd
                // 
    //            pubDate = pubDate.replace(":", "");
                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
                formatoFecha.setLenient(false);
                formatoFecha.parse(pubDate);
                Date oDt = formatoFecha.parse(pubDate);
                sDate = GetTimestamp(oDt);
            }
            catch (Exception ex2)
            {
                try
                {
                    // http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
                    // http://www.coderanch.com/t/409709/java/java/date-format-conversion-MM-dd
                    // 
        //            pubDate = pubDate.replace(":", "");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.getDefault());
                    formatoFecha.setLenient(false);
                    formatoFecha.parse(pubDate);
                    Date oDt = formatoFecha.parse(pubDate);
                    sDate = GetTimestamp(oDt);
                }
                catch (Exception ex3)
                {
                    try
                    {
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss Z", Locale.ROOT);
                    formatoFecha.setLenient(false);
                    formatoFecha.parse(pubDate);
                    Date oDt = formatoFecha.parse(pubDate);
                    sDate = GetTimestamp(oDt);
                    }
                    catch (Exception ex4)
                    {
                        try
                           {
//                              SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                              SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                                String s1 = "2008-03-30T15:30:00.000+02:00";
                             
                              String s1 = "";
                              if (pubDate.contains("+"))
                                s1 = pubDate.substring(0, pubDate.indexOf("+"));
                              else if (pubDate.indexOf("Z")==pubDate.length()-1)
                                  s1 = pubDate.substring(0, pubDate.indexOf("Z"));
                              else
                                s1 = pubDate.substring(0, pubDate.lastIndexOf("-"));
                                
                              Date oDt = sdf2.parse(s1);
                              sDate = GetTimestamp(oDt);
                           } 
                        catch (Exception ex5)
                            {
                                try
                                {
                                    // Sample = 30 Ago 2012 10:52:00:000
                                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                                    formatoFecha.setLenient(false);
//                                    formatoFecha.parse(pubDate);
                                    String s1 = pubDate.substring(0, pubDate.lastIndexOf(":"));
                                    Date oDt = formatoFecha.parse(s1);
                                    sDate = GetTimestamp(oDt);
                                }
                                catch (Exception ex6)
                                {
                                    try
                                    {
                                        // Sample = 30 Aug 2012 10:52:00:000
                                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
                                        formatoFecha.setLenient(false);
    //                                    formatoFecha.parse(pubDate);
                                        String s1 = pubDate.substring(0, pubDate.lastIndexOf(":"));
                                        Date oDt = formatoFecha.parse(s1);
                                        sDate = GetTimestamp(oDt);
                                    }
                                    catch (Exception ex7)
                                    {
                                        try
                                        {
                                            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                                            formatoFecha.setLenient(false);
                                            Date oDt = formatoFecha.parse(pubDate);
                                            sDate = GetTimestamp(oDt);
                                        }
                                        catch(Exception ex8)
                                        {
                                            
                                            
                                            try
                                                
                                            {
                                                SimpleDateFormat formatoFecha = new SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.ENGLISH);
                                                formatoFecha.setLenient(false);
                                                String s1 = pubDate.substring(0, pubDate.lastIndexOf(":"));
                                                Date oDt = formatoFecha.parse(s1);
                                                sDate = GetTimestamp(oDt);
                                            }
                                            catch (Exception ex9)
                                            {
                                             
                                                try
                                                {
                                                    // Sample = 30 Ago 2012
                                                    Locale spanishLocale = new Locale("es", "ES");
                                                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMM yyyy", spanishLocale);
                                                    formatoFecha.setLenient(false);
                        //                                    formatoFecha.parse(pubDate);
                                                    Date oDt = formatoFecha.parse(pubDate);
                                                    sDate = GetTimestamp(oDt);
                                                }
                                                catch (Exception ex10)
                                                {
                                                    
                                                    System.out.println(ex10.toString());
                                                }
                                            }
                                           
                                        }
                                    }
                                }
                            }
                        
                    }
                }
            }
            
//            System.out.println(ex.toString());
        }
        
        return sDate;
    }
}
