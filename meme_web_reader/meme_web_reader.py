from bs4 import BeautifulSoup
import urllib2
import re
import json
import sys

def get_page_text(url):
	pageResults = urllib2.urlopen(url)
	pageText = ""
	for eaLine in pageResults.readlines():
		pageText += eaLine
	pageResults.close()
	return pageText

def get_caption_url(pageTextSoup, captionLinkId, baseUrl):
	captionLinksSoup = pageTextSoup.select('#' + captionLinkId)
	captionLink = captionLinksSoup[0]
	return baseUrl + captionLink.attrs['href']

def get_meme_id(captionPageUrl):
	regex = re.compile("(?<=id\=)\d+")
	return regex.findall(captionPageUrl)[0]

def get_caption_api_url(baseUrl, apiCaptionUrl, captionPageUrl):
	return baseUrl + apiCaptionUrl + get_meme_id(captionPageUrl)

def get_meme_name(fullPageUrl):
	regex = re.compile("(?<=name\=).+(?=\&)")
	return regex.findall(fullPageUrl)[0]

def traverse_reddit_links(pageSoup):
	regex = re.compile('http://www.quickmeme.com/meme/.+/')
	hrefStr = ""
	for ea in pageSoup.find_all("a"):
		if 'href' in ea.attrs.keys():
			hrefStr = ea.attrs['href']
			if len(hrefStr) > 0:
				matches = regex.findall(hrefStr)
				if len(matches) > 0:
					print hrefStr


def main():
		# set up some variables for this script
		# (these should change for different sites)
		baseUrl = 'http://www.quickmeme.com'
		captionLinkId = 'editurl'
		apiCaptionUrl = '/make/get_data/?id='

		# first argument should be meme page url
		args = sys.argv[1:]
		url = args[0]
		
		# get the text for the page at 'url'
		# and initialize the BeautifulSoup object for it
		pageOneText = get_page_text(url)
		pageOneSoup = BeautifulSoup(pageOneText)

		# retrieve the link for the 'add your own caption' page
		pageTwoLinkUrl 	= get_caption_url(pageOneSoup, captionLinkId, baseUrl)
		
		# create the url of the api call to retrieve the
		# captions (top/bottom text)
		captionApiUrl = get_caption_api_url(baseUrl, apiCaptionUrl, pageTwoLinkUrl)
		
		# call the api and process the response
		jsonCaptionResponse = json.loads(get_page_text(captionApiUrl))
		jsonCaps = jsonCaptionResponse['caps']
		jsonTopCap = jsonCaps[0]
		jsonBottomCap = jsonCaps[1]

		topTxt = jsonTopCap['txt']
		bottomTxt = jsonBottomCap['txt']

		# get the name from the caption page url
		memeName = get_meme_name(pageTwoLinkUrl)

		print 'URL:\t\t\t' + url
		print 'Caption URL:\t\t' + captionApiUrl 
		print 'Meme Name:\t\t' + memeName
		print 'Top Text:\t\t' + topTxt
		print 'Bottom Text:\t\t' + bottomTxt
		
		return 0

#main()
traverse_reddit_links(BeautifulSoup(get_page_text('http://www.reddit.com/r/AdviceAnimals/top/?sort=top&t=hour')))
