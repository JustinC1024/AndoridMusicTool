import pymysql

# 查
def select_singer_name(name):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql="select * from singer where name='"+name+"'"
    cursor.execute(sql)
    data=cursor.fetchone()
    print(data)
    db.close()
    return data[2]
def select_singer():
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql="select * from singer"
    cursor.execute(sql)
    data=cursor.fetchall()
    db.close()
    return data
def select_singer_id(id):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql = "select * from singer where id=%s"
    cursor.execute(sql,id)
    data = cursor.fetchone()
    print(data)
    db.close()
    return data


# 增
def add_singer(name,code):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql="insert into singer(name,code)values(%s,%s)"
    cursor.execute(sql,(name,code))
    db.commit()
    db.close()
    return 'true'

# 改
def update_singer(id,name,code):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql = "update singer set name=%s,code=%s where id=%s"
    cursor.execute(sql, (name,code,id))
    db.commit()
    db.close()
    return 'true'

# 删
def delete_singer(id):
    db = pymysql.connect("localhost", "root", "123", "music_app")
    cursor = db.cursor()
    sql = "delete from singer where id=%s"
    cursor.execute(sql, id)
    db.commit()
    db.close()
    return 'true'