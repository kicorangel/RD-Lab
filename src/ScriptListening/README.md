Script Listening
----------------

Script Listening allows to easily retrieve information from Twitter. It allows three ways of retrieving information, that may be combined in several ways:

* Retrieve timelines (Twitter-Timeline): It retrieves the defined number of tweets from a series of Twitter accounts (by username).
* Retrieve from geography (Twitter-Search): It retrieves the defined number of tweets from a series of geographical places (defined by longitude, latitude and ratio).
* Retrieve from search query (Twitter-Search): It retrieves the defined number of tweets from a series of keywords.
* Retrieve from search query and geography (Twitter-Search): It retrieves the defined number of tweets from a series of keywords restricted by geographical places.

#Project Structure#

The project contains the following folders:

* src: The source code in Java Netbeans projects.
* scripts: The .jar files.
* proyectos: It contains xml files defining different retrieval projects. Three documented examples are provided.


#Working instructions

* Copy compiled files (.jar) in the scripts folder.
* Define the corresponding retrieval project in proyectos folder.
* In the scripts folder, execute the corresponding .jar file with the following syntax:

java -jar file.jar project

Where:

* file.jar should be Twitter-Timeline.jar or Twitter-Search.jar
* project shuold be the name of a xml file defined in the folder proyectos

