"""memegens3 - s3 functionality for use with meme gen server"""

import boto
import re
from httplib2 import Http
import json
def get_names(dir):
    conn = boto.connect_s3()
    bucket = conn.get_bucket('mgs_dev_bucket')
    names = []
    for ea in bucket.list(prefix=dir):
        names.append(ea.name)
    return names


def get_file_names(dir):
    names = get_names(dir)
    name_re = re.compile(r'(?<=' + dir + r')[a-zA-Z0-9\.\_\-\ ]+$')
    top_lvl_names = []
    for ea in names:
        names = name_re.findall(ea)
        if len(names) > 0:
            top_lvl_names.append(names[0])
    return top_lvl_names

def get_meme_backgrounds(dir):
    file_names = get_file_names(dir)
    bgs = []
    for ea_file_name in file_names:
        bg = {
            'filePath' : ea_file_name,
            'description' : ea_file_name.replace('_', ' ').replace('.jpg', ''),
            'active' : True
        }
        bgs.append(bg)
    return bgs

def post_meme_background(url, bg):
    h = Http()
    json_body = json.dumps(bg)
    print('json: ' + json_body)
    resp, content = h.request(
        url,
        'POST',
        body = json_body,
        headers = { 
            'Accept' : 'application/json', 
            'Content-type' : 'application/json'
        }
    )

    print resp
    print content
    return content
