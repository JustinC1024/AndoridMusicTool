import pymysql

# 查
def select_song_name(name):
    db=pymysql.connect("localhost","root","123","music_app")
    cursor = db.cursor()
    sql = "select * from song where sname='"+name+"'"
    cursor.execute(sql)
    data = cursor.fetchone()
    print(data)
    return data
def select_song():
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql="select * from song"
    cursor.execute(sql)
    data=cursor.fetchall()
    db.close()
    return data
def select_song_id(id):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql = "select * from song where id=%s"
    cursor.execute(sql,id)
    data = cursor.fetchone()
    print(data)
    db.close()
    return data
# 增
def add_song(name,sname,img):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql = "insert into song(name,sname,img)values(%s,%s,%s)"
    cursor.execute(sql, (name,sname,img))
    db.commit()
    db.close()
    return 'true'

# 改
def update_song(id,name,sname,img):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql = "update song set name=%s,sname=%s,img=%s where id=%s"
    cursor.execute(sql, (name,sname,img,id))
    db.commit()
    db.close()
    return 'true'

# 删
def delete_song(id):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql = "delete from song where id=%s"
    cursor.execute(sql, id)
    db.commit()
    db.close()
    return 'true'