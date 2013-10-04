from bs4 import BeautifulSoup
import urllib2
import re
import json
import sys
import boto
import Image
import cStringIO
from httplib2 import Http

def get_page_text(url):
    pageResults = urllib2.urlopen(url)
    pageText = ""
    for eaLine in pageResults.readlines():
        pageText += eaLine
    pageResults.close()
    return pageText

def getMemeCrunchPageText(page_num):
	mcText = get_page_text('http://memecrunch.com/' + str(page_num))
	return mcText

def getMemeCrunchGenPageText():
	return get_page_text('http://memecrunch.com/generator/')

def main():
	mcText = getMemeCrunchGenPageText()
	#print(wkmText)

	mcBSoup = BeautifulSoup(mcText)
	imgSrcRegex = re.compile(
		'/image/.+jpg(\\?w=\\d+)*'
	)

	for eaImgTag in mcBSoup.find_all('img'):
		imgUrl = eaImgTag.attrs['src']
		if (len(imgSrcRegex.findall(imgUrl)) > 0 and 'alt' in eaImgTag.attrs.keys()):
			imgSrcContentRegex = re.compile(
				'/image/.+jpg'
			)
			
			imgUrl = 'http://memecrunch.com' + imgSrcContentRegex.findall(imgUrl)[0]
			print("imgUrl = " + imgUrl)
			imgName = str(eaImgTag.attrs['alt']).replace(' ', '_') + ".jpg"

			writeImgToS3(imgUrl, imgName)

			print('wrote: ' + imgName)

			postCrawlerBackgroundData(imgName, eaImgTag.attrs['alt'])


def writeImgToS3(imgUrl, imgFile):
	fp = urllib2.urlopen(imgUrl)

	# Load the URL data into the image
	img = Image.open(cStringIO.StringIO(fp.read()))

	# Resize the image
	#im2 = im.resize((500, 100), Image.NEAREST)  

	# saving the image into a cStringIO object to avoid writing to disk
	out_img = cStringIO.StringIO()

	# You MUST specify the file type because there is no file name to discern it from
	img.save(out_img, 'JPEG')

	# Now we connect to our s3 bucket and upload from memory
	# credentials stored in environment AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
	conn = boto.connect_s3()

	#Connect to bucket and create key
	b = conn.get_bucket('mgs_dev_bucket')
	imgKey = b.new_key('/meme_imgs/mc_imgs/'+imgFile)

	#  setting contents from the in-memory string provided by cStringIO
	imgKey.set_contents_from_string(out_img.getvalue())
	
	imgKey.set_acl('public-read')

		
		
			
def writeImgFromUrl(imgUrl, imgFilename):
	opener = urllib2.build_opener()
	page = opener.open(imgUrl)
	img = page.read()

	if (len(imgFilename) > 0):
		imgFileSansSuffix = imgFilename
	
		print(
			'img file name sans suffix: '
			+ str(imgFileSansSuffix)
		)
		
		filename = imgFileSansSuffix + '.jpg'
		fout = open(filename, 'wb')
		fout.write(img)
		fout.close()

		print('stored: ' + filename)	
		
	else:
		print('no img filename: ' + str(imgFilename))

def postCrawlerBackgroundData(imgFilename, imgDesc):
	bgDetailObj = {
		'crawlerImgFilename' : imgFilename,
		'crawlerImgDesc' : imgDesc,
		'sourceDesc' : 'crunchmeme'
	}

	bgList = []
	bgList.append(bgDetailObj)

	h = Http()
	resp, content = h.request(
		'http://polar-cliffs-6420.herokuapp.com/spring/crawler/store_backgrounds',
		'POST',
		body = json.dumps(bgList),
		headers = { 'Accept' : 'application/json', 'Content-type' : 'application/json' }
    )

	print resp
	print content


main()


