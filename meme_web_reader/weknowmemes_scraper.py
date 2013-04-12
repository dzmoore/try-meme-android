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

def getWeKnowMemesPageText(page_num):
	wkmText = get_page_text('http://weknowmemes.com/generator/ajax/get_meme.php?page=' + str(page_num))
	return wkmText

def main():
	for i in range(1, 2):
		wkmText = getWeKnowMemesPageText(i)
		print(wkmText)

		wkmBSoup = BeautifulSoup(wkmText)

		for eaImgTag in wkmBSoup.find_all('img'):
			imgUrl = eaImgTag.attrs['src']
			imgName = eaImgTag.attrs['data-title']
			
			opener = urllib2.build_opener()
			page = opener.open(imgUrl)
			img = page.read()

			imgFilenameRegex = re.compile(
				'(?<=http://weknowmemes.com/generator/uploads/generated/).+'
			)

			regexGroup = imgFilenameRegex.findall(imgUrl)
		
			if (len(regexGroup) > 0):
				imgFilename = regexGroup[0]
				imgFilenameSansSuffixRegex = re.compile(
					'.+(?=\\.)'
				);
				
				imgFilenameGroup = imgFilenameSansSuffixRegex.findall(
					imgFilename		
				)
				
				if (len(imgFilenameGroup) > 0):
					imgFileSansSuffix = imgFilenameGroup[0]
				
					print(
						'img file name sans suffix: '
						+ str(imgFileSansSuffix)
					)
					
					filename = imgFileSansSuffix + 'png'
					fout = open(filename, 'wb')
					fout.write(img)
					fout.close()

					print('stored: ' + filename)	
					
					#ohh wait, need to go into the actual 'generator' page using the href links
					
				else:
					print('no img filename: ' + str(imgFilename))

			else:
				print('no filename: ' + str(regexGroup))

main()


