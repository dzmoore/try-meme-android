import json
from httplib2 import Http

def get_bg():
    h = Http()
    resp, content = h.request('http://arcane-chamber-4976.herokuapp.com/memebackgrounds/list/json/1/1', 'GET')
    print(content)

get_bg()
