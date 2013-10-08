from memegens3 import *
import memecrunch_scraper

url='http://dylan-mint:8080/memebackgrounds/create/json'
bucket_url='https://s3.amazonaws.com/mgs_dev_bucket/meme_imgs/'
max_width = 100
max_height = 100

for ea in get_meme_backgrounds('meme_imgs/'):
    file_name = ea['filePath']
    ea_url = bucket_url + file_name
    memecrunch_scraper.resize_and_store_in_s3(
        img_url = ea_url,
        img_file = file_name,
        max_width = max_width,
        max_height = max_height,
        key_prefix = '/meme_imgs/thumbs2/'
    )

