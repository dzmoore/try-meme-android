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
	url = ""
	if len(captionLinksSoup) > 0:
		captionLink = captionLinksSoup[0]
		url = baseUrl + captionLink.attrs['href']
	return url

def get_meme_id(captionPageUrl):
	regex = re.compile("(?<=id\=)\d+")
	return regex.findall(captionPageUrl)[0]

def get_caption_api_url(baseUrl, apiCaptionUrl, captionPageUrl):
	return baseUrl + apiCaptionUrl + get_meme_id(captionPageUrl)

def get_meme_name(fullPageUrl):
	regex = re.compile("(?<=name\=).+")
	memeName = ""
	matches = regex.findall(fullPageUrl)
	if len(matches) > 0:
		memeName = matches[0]
		ampRegex = re.compile(".*&.*")
		if len(ampRegex.findall(memeName)) > 0:
			reextractRegex = re.compile(".+(?=\&)")
			reextracts = reextractRegex.findall(memeName)
			if len(reextracts) > 0:
				memeName = reextracts[0]
	else:
		print 'Could not find meme name for:[' + fullPageUrl + ']'
	return memeName

def traverse_reddit_links(pageSoup, urlRegexList):
	links = set([])
	for eaRegex in urlRegexList:
		regex = re.compile(eaRegex)
		hrefStr = ""
		for ea in pageSoup.find_all("a"):
			if 'href' in ea.attrs.keys():
				hrefStr = ea.attrs['href']
				if len(hrefStr) > 0:
					matches = regex.findall(hrefStr)
					if len(matches) > 0:
						links.add(hrefStr)
	return links

def get_quickmeme_meme_detail(url):
	memeDetailObj = { 
		'name' : '',
		'topText' : '',
		'bottomText' : ''
	}

	# set up some variables for this script
    # (these should change for different sites)
	baseUrl = 'http://www.quickmeme.com'
	captionLinkId = 'editurl'
	apiCaptionUrl = '/make/get_data/?id='


    # get the text for the page at 'url'
    # and initialize the BeautifulSoup object for it
	pageOneText = get_page_text(url)
	pageOneSoup = BeautifulSoup(pageOneText)

    # retrieve the link for the 'add your own caption' page
	pageTwoLinkUrl  = get_caption_url(pageOneSoup, captionLinkId, baseUrl)
	if len(pageTwoLinkUrl) == 0:
		return memeDetailObj


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

	memeDetailObj = {
		'name' : memeName,
		'topText' : topTxt,
		'bottomText' : bottomTxt
	}

	return memeDetailObj


def print_meme_detail(url):
	# set up some variables for this script
	# (these should change for different sites)
	baseUrl = 'http://www.quickmeme.com'
	captionLinkId = 'editurl'
	apiCaptionUrl = '/make/get_data/?id='

	
	# get the text for the page at 'url'
	# and initialize the BeautifulSoup object for it
	pageOneText = get_page_text(url)
	pageOneSoup = BeautifulSoup(pageOneText)

	# retrieve the link for the 'add your own caption' page
	pageTwoLinkUrl 	= get_caption_url(pageOneSoup, captionLinkId, baseUrl)
	if len(pageTwoLinkUrl) == 0:
		return 1
	
	
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

	if len(topTxt) > 0 and len(bottomTxt) > 0:
		print 'URL:\t\t\t' + url
		print 'Caption URL:\t\t' + captionApiUrl 
		print 'Meme Name:\t\t' + memeName
		print 'Top Text:\t\t' + topTxt
		print 'Bottom Text:\t\t' + bottomTxt
	
	return 0

def main():
	# first argument should be meme page url
	args = sys.argv[1:]
	#urlArg = args[0]
	
	urlRegexList = ['http://www.quickmeme.com/meme/.+/', 'http://qkme.me/.+']

	url = 'http://www.reddit.com/r/AdviceAnimals/top/?sort=top&t=hour'
	if len(args) > 0:
		url = args[0]
	adviceAnimalsSoup = BeautifulSoup(get_page_text(url))

	linksFromReddit = traverse_reddit_links(adviceAnimalsSoup, urlRegexList)

	for ea in linksFromReddit:
		print_meme_detail(ea)
		print("\n")

	print "TOTAL FOUND:\t\t" + "{0}".format(len(linksFromReddit))

	for ea in linksFromReddit:
		print get_quickmeme_meme_detail(ea)
		

main()
					


