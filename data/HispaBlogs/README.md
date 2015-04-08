# HispaBlogs

HispaBlogs is a new collection of Spanish blogs from five different countries: Argentina, Chile, Mexico, Peru and Spain. There are 450 training and 200 testing blogs respectively for each lan- guage variety, with a total of 2,250 and 1,000 blogs. Each user blog is represented by a set of user posts, with a minimum of 10 posts per user/blog.

## Collection

The Autoritas team has censored blogs from different Spanish spoken countries. Posts from the census were retrieved in January 2014. The process was the following:

- The RSS content was retrieved
- The permalink was scrapped, the contents were cleaned and the html removed
- The language was automatically identified and contents in different language than Spanish were removed
- Posts with less than 10 words were removed
- The number of posts per blog were limited between 1 and 10
- Blogs with less than 100 words were removed

## Distribution

The dataset is distributed with the following format:

- A folder per country, coded with ISO 3166-2
- A file per blog. The name of the file is a MD5 code plus the country. The MD5 code was calculated with the url + the country code 

E.g. /en/0b376c2d7e70824e2bee6fdc474663ab_es.xml


### File Format

`<author id=”MD5” url=”URL” feed=”FEED” country=”ISO 3166-2”>
``	<documents count=”N”> 
		<document id=”MD5” url=”URL”>
			<content>CONTENT</content> 
		</document>
		....
	</documents>
</author>

### Legend

- MD5 (author): Hash calculated with the blog url plus the country code
- FEED: RSS/atom of the blog
- ISO 3166-2: Country code in ISO format
- N: Number of posts of the blog
- MD5 (document): Hash calculated with the post url
- URL: Post permalink
- CONTENT: Retrieved content