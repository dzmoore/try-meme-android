from memegens3 import *

url='http://dylan-mint:8080/memebackgrounds/create/json'
for ea in get_meme_backgrounds('meme_imgs/'):
    bg_id = post_meme_background(url, ea)

