#!/bin/bash
ant release && scp -P 1337 bin/meme_gen_android-release.apk dylan@spacepocalypse.com:/var/www/spacepocalypse/apk
