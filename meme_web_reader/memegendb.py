import pymysql

def get_meme_background_names():
    conn = get_local_conn()
    cur = conn.cursor()
    cur.execute(
        "select distinct description "
        "from meme_background where active = 1"
    )
    
    results = cur.fetchall()
    
    bg_name_list = []
    for eaId in results:
        bg_name_list.append(eaId[0])

    return bg_name_list

def get_samples(name, limit):
    conn = get_crawler_conn()
    cur = conn.cursor()
    cur.execute(
        "select name, top_text, bottom_text "
        "from crawler_meme "
        "where lower(name) = lower(%s) limit %s",
        (name, limit)
    )
    results = cur.fetchall()
    cur.close()
    conn.close()
    return results

def create_meme(top_text, bottom_text, meme_background_id):
    font_size = 26.0
    created_by_user_id = 1

    if len(top_text) > 30:
        font_size = 15.0
    else:
        font_size = 26.0

    conn = get_local_conn()
    cur = conn.cursor()
    cur.execute(
        "insert into meme_text "
        "(font_size, text) "
        "values (%s, %s)",
        (font_size, top_text)
    )

    cur.execute("select last_insert_id()")
    top_text_id = cur.fetchall()[0][0]

    if len(bottom_text) > 30:
        font_size = 15.0
    else:
        font_size = 26.0

    cur.execute(
        "insert into meme_text "
        "(font_size, text) "
        "values (%s, %s)",
        (font_size, bottom_text)
    )
    
    cur.execute("select last_insert_id()")
    bottom_text_id = cur.fetchall()[0][0]

    cur.execute(
        "insert into meme "
        "(top_text, bottom_text, meme_background) "
        "values (%s,%s,%s)",
        (top_text_id, bottom_text_id, meme_background_id)
    )

    cur.execute("select last_insert_id()")
    meme_id = cur.fetchall()[0][0]
    
    conn.commit()
    cur.close()
    conn.close()

    return meme_id


def get_meme_background_id_for_name(background_name):
    conn = get_local_conn()
    cur = conn.cursor()
    cur.execute(
        "select id "
        "from meme_background "
        "where lower(description) = %s",
        (background_name)
    )
    
    results = cur.fetchall()

    id = -1L
    
    if len(results) > 0:
        id = results[0][0]

    cur.close()
    conn.close()
    
    return id
 
def store_samples(meme_background_id, meme_ids): 
    local_conn = get_local_conn()
    local_cur = local_conn.cursor()
    
    for ea_meme_id in meme_ids:
        local_cur.execute(
            "insert into sample_meme "
            "(background, sample_meme) values (%s, %s)",
            (meme_background_id, ea_meme_id)
        )
    local_conn.commit()
    local_cur.close()
    local_conn.close()   

def get_crawler_conn():
    return pymysql.connect(
        host='us-cdbr-east-03.cleardb.com', 
        unix_socket='/tmp/mysql.sock', 
        user='b0799366f0bdc2', 
        passwd='b21b5e8f', 
        db='heroku_b630f8d967bd52a'
    )

def get_local_conn():
    return pymysql.connect(
        host='127.0.0.1',
        user='root',
        passwd='password',
        db='mgsdbv1'
    )
