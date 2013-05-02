from bs4 import BeautifulSoup
import urllib2
import re
import json
import sys
import boto
import Image
import cStringIO

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
		#print(wkmText)

		wkmBSoup = BeautifulSoup(wkmText)
		for eaImgTag in wkmBSoup.find_all('a'):
			#print eaImgTag
			genLinkUrl = ''
			memeId = -1
			if ('href' in eaImgTag.attrs):
				genLinkUrl = eaImgTag.attrs['href'].replace(' ', '%20')
				memeId = getIdFromUrl(genLinkUrl)
				print('id=' + memeId)

			previewImgName = ''
			if ('data-title' in eaImgTag.img.attrs):	
				previewImgName = eaImgTag.img.attrs['data-title']
			
			print('href=' + genLinkUrl)
			print('data-tile=' + previewImgName)
			
			if (memeId == -1):
				return

			urlMemeData = 'http://weknowmemes.com/generator/ajax/get_data.php?g_id=' + memeId
			jsonMemeData = json.loads(get_page_text(urlMemeData))
			imgFileName = jsonMemeData['img']
			bgImgUrl = 'http://weknowmemes.com/generator/uploads/background/' + imgFileName
	
			writeImgFromUrl(bgImgUrl, imgFileName)
			


def getIdFromUrl(url):
	idResult = -1

	if (len(url) > 0):
		urlIdRegex = re.compile(
			'(?<=http://weknowmemes.com/generator/caption/#id=)\d+'
		);

		urlIdRegexGroup = urlIdRegex.findall(url)
		if (len(urlIdRegexGroup) > 0):
			idResult = urlIdRegexGroup[0]
	
	return idResult
	
	
def writeImgToS3(imgUrl, imgFile):
	fp = urllib.urlopen(imgUrl)

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
	imgKey = b.new_key(imgFile)

	#  setting contents from the in-memory string provided by cStringIO
	imgKey.set_contents_from_string(out_img.getvalue())

		
		
			
def writeImgFromUrl(imgUrl, imgFilename):
	opener = urllib2.build_opener()
	page = opener.open(imgUrl)
	img = page.read()

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
		
		filename = imgFileSansSuffix + '.jpg'
		fout = open(filename, 'wb')
		fout.write(img)
		fout.close()

		print('stored: ' + filename)	
		
	else:
		print('no img filename: ' + str(imgFilename))


main()


