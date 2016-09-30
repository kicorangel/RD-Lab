"""
file:   wiki_extractor.py
module: WikiCities
author: Raül Fabra Boluda <rfabra@dsic.upv.es>
date:   09/04/2015
description:
	This file implements the class parser. 
	It implements several methods for parsing,
	like regular expressions for text extraction
	and text normalization.
"""

import re
import math
import unicodedata

class WikiExtractor:
	def __init__(self):

		# Coordinates regular expression
		self._lat_re  = re.compile(r"""class=\"latitude\">(-*\d+\.\d*)""")
		self._lon_re  = re.compile(r"""class=\"longitude\">(-*\d+\.\d*)""")

	def title(self, title):
		""" Returns (city_name, disamb_info), the name of the
			city and the disambiguation information between 
			parentheses.
		"""

		city_name = title.split('(')[0]
		if city_name[-1] == '_':
			city_name = city_name[:-1]

		return city_name
	
	def extract_coords(self, text):
		""" Extract coordinates (longitude, latitude) from text. 
			Text is a wikipedia html article.
		"""
		lon = lat = None
		try:
			latitude = self._lat_re.findall(text)
			lat = latitude[0]

		except:
			pass
		
		try:
			longitude = self._lon_re.findall(text)
			lon = longitude[0]
		except:
			pass

		return (lat, lon)

#	def clean(self, text):
#		""" Replace punctuation marks by spaces. """
#		if text != None:
#			text = self._non_alpha.sub(' ', text)
#			text = self.collapse_whitespaces(text)
#			text = self.normalize(text)
#			text = self.trim(text)
#			return text
#
#	def trim(self, text):
#		""" Remove leading and trailing spaces. """
#		if text != None:
#			text = self._leading_ws.sub('', text)
#			text = self._trailing_ws.sub('', text)
#			return text
#		
#	def collapse_whitespaces(self, text):
#		""" Remove duplicate whitespaces. """
#		if text != None:
#			return self._ws.sub(' ', text)
#
#	def normalize(self, text):
#		""" Lowercase and normalize text (remove accents). """
#		if text != None:
#			text = text.lower()
#			return ''.join((c for c in unicodedata.normalize('NFD', text) if unicodedata.category(c) != 'Mn'))
