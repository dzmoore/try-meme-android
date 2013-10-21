#!/bin/bash
imgPath='/full/path/here/tmimitw.jpg';
memeType='most_int_man';
memeTypeDesc='The Most Interesting Man in the World';
topText="I don\'t always test my code, but when I do..."
bottomText="it\'s in production."

#######
MYSQL_CMD_OUT='mysql -u mgsdb_user -ppassword -B -N mgsdb';
MYSQL_CMD_OUT_WITHCOLS='mysql -u mgsdb_user -ppassword mgsdb';
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

# create new meme entry
memeInsertQry="insert into meme (meme_background_fk, lv_meme_type_fk) values "\
"($imgPathId, $memeTypeId);select last_insert_id()"
memeId=$(echo $memeInsertQry | $MYSQL_CMD_OUT)

topTxtIdQry="select id from lv_meme_text_type where type='TOP'"
topTxtId=$(echo $topTxtIdQry | $MYSQL_CMD_OUT)

btmTxtIdQry="select id from lv_meme_text_type where type='BOTTOM'"
btmTxtId=$(echo $btmTxtIdQry | $MYSQL_CMD_OUT)

topTextInsertQry="insert into meme_text (text, lv_meme_text_type_fk, meme_fk) values "\
"('$topText', $topTxtId, $memeId)"
$(echo $topTextInsertQry | $MYSQL_CMD_OUT)

btmTextInsertQry="insert into meme_text (text, lv_meme_text_type_fk, meme_fk) value "\
"('$bottomText', $btmTxtId, $memeId)"
$(echo $btmTextInsertQry | $MYSQL_CMD_OUT)

resultQry="select "\
"	m.id				as meme_id, "\
"	mtype.descr 		as meme_description, "\
"	mback.path			as background_path, "\
"	toptext.text		as top_text, "\
"	bottomtext.text		as bottom_text "\
"from "\
"	meme m join lv_meme_type mtype on "\
"	(m.lv_meme_type_fk = mtype.id) "\
"	join meme_text toptext on "\
"	(toptext.meme_fk = m.id) "\
"	join lv_meme_text_type toptexttype "\
"	on (toptexttype.id = toptext.lv_meme_text_type_fk and toptexttype.type = 'TOP') "\
"	join meme_text bottomtext on "\
"	(bottomtext.meme_fk = m.id) "\
"	join lv_meme_text_type bottomtexttype "\
"	on (bottomtexttype.id = bottomtext.lv_meme_text_type_fk and bottomtexttype.type = 'BOTTOM') "\
"	join meme_background mback on "\
"	(mback.id = m.meme_background_fk) "\
"where "\
"	m.id = $memeId \G";

result=$(echo $resultQry | $MYSQL_CMD_OUT_WITHCOLS)
printf "$result\n"
