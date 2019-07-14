from flask import Flask,render_template
import json
from flask import request
import os

app = Flask(__name__)
basedir=os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN']=True
app.config['SQLALCHEMY_TRACK_MODIFICATIONS']=True

getSinger=__import__("singer")
getSong = __import__("song")

@app.route('/')
def hello_world():
    print('已经连上')
    return "hello world"

#找图片
@app.route('/pic',methods=['POST'])
def find_pic():
    request.encoding = 'utf-8'
    request.form['picName']
    getCatchPic=__import__("catchPic")
    singer=getSinger.select_singer_name(request.form['picName'])
    temp='%d'%singer
    url= 'https://www.kugou.com/yy/singer/home/'+temp+'.html'
    piclist=getCatchPic.singer_pic(url)
    for item in piclist:
        print(item)
    print(piclist[0])
    data={
        'img': piclist[0],
    }
    print(data)
    return json.dumps(data)

#找歌曲
@app.route('/new')
def new_song():
    getCatchList=__import__("catchList")
    url='http://m.kugou.com/'
    songlist=getCatchList.song_list(url)
    list=[]
    for item in songlist:
        songlocal = getSong.select_song_name(item)
        if(songlocal==None):
            surl='';
            img='';
        else:
            surl=songlocal[1]
            img=songlocal[3]
        data={
            'artist':getCatchList.singer_list(url,item),
            'title':item,
            'url':surl,
            'img':img,
        }
        list.append(data)
    return json.dumps(list,ensure_ascii=False)

# 网页后台
@app.route('/web/index')
def singer_index():
    singer = getSinger.select_singer()
    return render_template('index.html', singer=singer)

@app.route('/web/singer_update_prepare',methods=['GET'])
def singer_update_prepare():
    singer = getSinger.select_singer_id(request.values['singerId'])
    return json.dumps(singer,ensure_ascii=False)

@app.route('/web/singer_add',methods=['POST'])
def singer_add():
    flag=getSinger.add_singer(request.form['name'],request.form['code'])
    return flag

@app.route('/web/singer_update',methods=['POST'])
def singer_update():
    flag=getSinger.update_singer(request.form['singerId'],request.form['name'],request.form['code'])
    return flag

@app.route('/web/singer_delete',methods=['GET'])
def singer_delete():
    flag = getSinger.delete_singer(request.values['singerId'])
    return flag

@app.route('/web/song_index')
def song_index():
    song=getSong.select_song()
    return render_template('song_index.html', song=song)

@app.route('/web/song_update_prepare',methods=['GET'])
def song_update_prepare():
    singer = getSong.select_song_id(request.values['songId'])
    return json.dumps(singer,ensure_ascii=False)

@app.route('/web/song_add',methods=['POST'])
def song_add():
    flag=getSong.add_song(request.form['name'],request.form['sname'],request.form['img'])
    return flag

@app.route('/web/song_update',methods=['POST'])
def song_update():
    flag=getSong.update_song(request.form['songId'],request.form['name'],request.form['sname'],request.form['img'])
    return flag

@app.route('/web/song_delete',methods=['GET'])
def song_delete():
    flag = getSong.delete_song(request.values['songId'])
    return flag

if __name__ == '__main__':
    app.run(host='0.0.0.0')
