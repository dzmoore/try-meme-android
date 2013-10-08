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
        img_url = eaImgTag.attrs['src']
        if (len(imgSrcRegex.findall(img_url)) > 0 and 'alt' in eaImgTag.attrs.keys()):
            imgSrcContentRegex = re.compile(
                '/image/.+jpg'
            )
            
            img_url = 'http://memecrunch.com' + imgSrcContentRegex.findall(img_url)[0]
            print("img_url = " + img_url)
            imgName = str(eaImgTag.attrs['alt']).replace(' ', '_') + ".jpg"

            writeImgToS3(img_url, imgName)

            print('wrote: ' + imgName)

            postCrawlerBackgroundData(imgName, eaImgTag.attrs['alt'])


def resize_and_store_in_s3(img_url, img_file, max_width, max_height, key_prefix):
    print("Opening: " + img_url)
    fp = urllib2.urlopen(img_url)

    # Load the URL data into the image
    img = Image.open(cStringIO.StringIO(fp.read()))

    max_area = max_width * max_height

    img_width, img_height = img.size
    print("Original img size: " + str(img_width) + ", " + str(img_height))
    
    # first scale height
    new_img_width = max_width
    new_img_height = int(float(new_img_width) * (float(img_height) / float(img_width)))
    print("First scale: " + str(new_img_width) + ", " + str(new_img_height))

    new_img_area = new_img_width * new_img_height

    # if the img needs to be scaled on width
    if new_img_area > max_area:
        new_img_height = max_height
        new_img_width = int(float(new_img_height) * (float(img_width) / float(img_height)))
        print("Second scale: " + str(new_img_width) + ", " + str(new_img_height))

    print("Resizing image to: " + str(new_img_width) + ", " + str(new_img_height))
    # Resize the image
    img = img.resize((new_img_width, new_img_height), Image.NEAREST)

    # saving the image into a cStringIO object to avoid writing to disk
    out_img = cStringIO.StringIO()

    # You MUST specify the file type because there is no file name to discern it from
    img.save(out_img, 'JPEG')

    # Now we connect to our s3 bucket and upload from memory
    # credentials stored in environment AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
    conn = boto.connect_s3()

    #Connect to bucket and create key
    b = conn.get_bucket('mgs_dev_bucket')

    print("Storing image in " + key_prefix + img_file)
    imgKey = b.new_key(key_prefix+img_file)

    #  setting contents from the in-memory string provided by cStringIO
    imgKey.set_contents_from_string(out_img.getvalue())
    
    imgKey.set_acl('public-read')

    print("Done with " + img_url)

def writeImgToS3(img_url, img_file):
    fp = urllib2.urlopen(img_url)

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
    imgKey = b.new_key('/meme_imgs/mc_imgs/'+img_file)

    #  setting contents from the in-memory string provided by cStringIO
    imgKey.set_contents_from_string(out_img.getvalue())
    
    imgKey.set_acl('public-read')

        
        
            
def writeImgFromUrl(img_url, img_filename):
    opener = urllib2.build_opener()
    page = opener.open(img_url)
    img = page.read()

    if (len(img_filename) > 0):
        img_fileSansSuffix = img_filename
    
        print(
            'img file name sans suffix: '
            + str(img_fileSansSuffix)
        )
        
        filename = img_fileSansSuffix + '.jpg'
        fout = open(filename, 'wb')
        fout.write(img)
        fout.close()

        print('stored: ' + filename)    
        
    else:
        print('no img filename: ' + str(img_filename))

def postCrawlerBackgroundData(img_filename, imgDesc):
    bgDetailObj = {
        'crawlerImgFilename' : img_filename,
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




