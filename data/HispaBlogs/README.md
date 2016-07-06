# HispaBlogs

HispaBlogs is a new collection of Spanish blogs from five different countries: Argentina, Chile, Mexico, Peru and Spain. There are 450 training and 200 testing blogs respectively for each lan- guage variety, with a total of 2,250 and 1,000 blogs. Each user blog is represented by a set of user posts, with a minimum of 10 posts per user/blog.

## Collection

The Autoritas team has censused blogs from different Spanish speaking countries. Posts from the census were retrieved from January 2014. The process was the following:

- The RSS content was retrieved
- The permalink was scraped, the contents were cleaned and the html removed
- The language was automatically identified and contents in different language than Spanish were removed
- Posts with less than 10 words were removed
- The number of posts per blog were limited between 1 and 10
- Blogs with less than 100 words were removed

## Distribution

The dataset is distributed with the following format:

- A folder per country, coded with ISO 3166-2
- A file per blog. The name of the file is a MD5 code plus the country. The MD5 code was calculated with the url + the country code 

E.g. 

/en/0b376c2d7e70824e2bee6fdc474663ab_es.xml


### File Format

```
<author id=”MD5” url=”URL” feed=”FEED” country=”ISO 3166-2”>
	<documents count=”N”> 
		<document id=”MD5” url=”URL”>
			<content>CONTENT</content> 
		</document>
		....
	</documents>
</author>
```

### Legend

- MD5 (author): Hash calculated with the blog url plus the country code
- FEED: RSS/atom of the blog
- ISO 3166-2: Country code in ISO format
- N: Number of posts of the blog
- MD5 (document): Hash calculated with the post url
- URL: Post permalink
- CONTENT: Retrieved content

### References

To cite Hispablogs in your academic work, please use the following references:

```
@inproceedings{rangel:cicling:lvi:2016,
	title={A low dimensionality representation for language variety identification},
	author={Rangel, Francisco and Rosso, Paolo and Franco-Salvador, Marc},
	booktitle={17th International Conference on Intelligent Text Processing and Computational Linguistics,  CICLing},
	pages={},
	publisher={Springer-Verlag, LNCS()},
	year={2016}
}
```

```
@incollection{franco:clef:lvi:2015,
  title={Language variety identification using distributed representations of words and documents},
  author={Franco-Salvador, Marc and Rangel, Francisco and Rosso, Paolo and Taul{\'e}, Mariona and Mart{\'\i}, M Ant{\`o}nia},
  booktitle={Experimental IR meets multilinguality, multimodality, and interaction},
  pages={28--40},
  year={2015},
  publisher={Springer}
}
```
