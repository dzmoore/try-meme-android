#!/bin/bash
imgPath='./tmimitw.jpg';
memeType='most_int_man';
memeTypeDesc='The Most Interesting Man in the World';

MYSQL_CMD_OUT='mysql -u mgsdb_user -ppassword -B -N mgsdb';
imgPathStoredQry="select path from meme_background where path = '$imgPath'"; 

# store/find img path
imgPathStored=$(echo $imgPathStoredQry | $MYSQL_CMD_OUT)

if [[ $imgPathStored == $imgPath ]]
then
	echo "found same path; skipping background insert.";
else
	echo "inserting background path";
	imgPathInsQry="insert into meme_background (path, active) values ('$imgPath', 1)"
	$(echo $imgPathInsQry | $MYSQL_CMD_OUT)
fi

imgPathIdQry="select id from meme_background where path = '$imgPath'";
imgPathId=$(echo $imgPathIdQry | $MYSQL_CMD_OUT)


# store/find meme type
memeTypeStoredQry="select type from lv_meme_type where type = '$memeType'"
memeTypeStored=$(echo $memeTypeStoredQry | $MYSQL_CMD_OUT)

if [[ $memeTypeStored == $memeType ]]
then
	echo "found same meme type; skipping type insert.";
else
	echo "inserting meme type";
	memeTypeInsertQry="insert into lv_meme_type (type, descr, active) values ('$memeType', '$memeTypeDesc', 1)"
	$(echo $memeTypeInsertQry | $MYSQL_CMD_OUT)
fi

memeTypeIdQry="select id from lv_meme_type where type = '$memeType'"
memeTypeId=$(echo $memeTypeIdQry | $MYSQL_CMD_OUT)

echo $memeTypeId

