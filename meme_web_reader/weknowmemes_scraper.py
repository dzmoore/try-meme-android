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

def main():
	wkmText = get_page_text('http://weknowmemes.com/generator/ajax/get_meme.php?page=2')
	print wkmText


main()


