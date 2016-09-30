GeoSearchDownloader
--------------------

GeoSearchDownloader allows to retrieve geolocated tweets.

**Configuration**

Step 1: Edit TwitterDownloader.java to configure your Twitter API keys:

    public static String KEY = "";              // You should put here your Twitter KEY
    public static String SECRET = "";           // You should put here your Twitter SECRET
    public static String ACCESSTOKEN = "";      // You should put here your Twitter ACCESS TOKEN
    public static String ACCESSSECRET = "";     // You should put here your Twitter ACCESS SECRET

Step 2: Build TwitterDownloader.jar with Netbeans or whatever tool you may use.

Step 3: Copy TwitterDownloader.jar to the same level than geo_search.py, profiles and lib.

Step 4: Execute geo_search.py
