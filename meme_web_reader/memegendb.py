import pymysql

def get_samples(name, limit):
    conn = get_conn()
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
    

def get_conn():
    return pymysql.connect(
        host='us-cdbr-east-03.cleardb.com', 
        unix_socket='/tmp/mysql.sock', 
        user='b0799366f0bdc2', 
        passwd='b21b5e8f', 
        db='heroku_b630f8d967bd52a'
    )
