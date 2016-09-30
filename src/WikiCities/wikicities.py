"""
file:   Wikicities.py
module: Wikicites.Wikicites
author: Raül Fabra Boluda <rfabra@dsic.upv.es>
date:   09/04/2015
description:
	This file implements the class Wikicities, which
	parses file containing several URLs to Wikipedia
	cities articles.
	This class downloads and parses that information,
	extracting city name, disambiguation information,
	longitude, latitude, radius. 
"""


from html.parser import HTMLParser  
from urllib.request import urlopen  
import urllib
from urllib import parse as urlparse

import sys

#from tweet_data import country as ctry
from tweet_data import city as cty
from WikiCities import wiki_extractor as we

class WikiCities:
	def __init__(self):
		""" Initialize the parser for text normalization. """
		self._wiki_extractor = we.WikiExtractor()

	def process_url_file(self, fileIn, db, verbose=False):
		""" Process a file containg one URL per line,
			each one corresonding to a Wikipedia article
			to a city. 

			Parameters:
				- fileIn: file containing URLs
				- db: database file where store collected data
			
		"""
		f_in = open(fileIn,  'r')	

		urls = f_in.readlines()
		urls = [url for url in urls if len(url) > 1]
		self._country = fileIn.split('/')[-1].split('.')[0]
		self._pop_ranking = 1
		#self._country.write_db(db)

		for url in urls:
			# Remove last \n
			self._url = url[:-1]

			print('Processing URL:|{}|'.format(self._url))

			html = self.get_data_url(url)
			city = self.parse_html(html)
			
			if verbose:
				print('--Name: ' + str(city.name))
				print('--lat: ' + str(city.lat))
				print('--lon: ' + str(city.lon))
				print('--pop_ranking: ' + str(city.pop_ranking))
				print('--population: ' + str(city.population))
				print('--country: ' + str(city.country_name))

			city.write_db(db)
			self._pop_ranking = self._pop_ranking + 1

	def parse_html(self, html):
		""" Extract city information from the html document. """
		# Unquote
		parsed_url = urlparse.unquote(self._url)
		# Take the suffix
		suffix = parsed_url.split('/')[-1]
		
		# Extract the city name from the suffix
		city_name = self._wiki_extractor.title(suffix)
		
		# Extract city data
		(lat, lon) = self._wiki_extractor.extract_coords(html)
		
		# Returns a city object
		return cty.City(name = city_name,
						lat = lat,
						lon = lon,
						country_name = self._country,
						pop_ranking = self._pop_ranking)


	
	def get_data_url(self, url):
		""" Download and parse Wikipedia article. """
		print(str(url))
		response = urlopen(url)
		htmlBytes = response.read()
		return htmlBytes.decode("utf-8")
