import re
import urllib.request

stock_CodeUrl='https://www.kugou.com/singer/83882.html'

def singer_pic(url):
    picList = []
    html = urllib.request.urlopen(url).read()
    html = html.decode('utf-8')
    s = r'src="https://www.kugou.com/yy/static/images/blank.gif" _src="http://singerimg.kugou.com/uploadpic/softhead/240/20(.*?)" _def="https://www.kugou.com/yy/static/images/default1.jpg" height="142" width="142"/>'
    pat = re.compile(s)
    code = pat.findall(html)
    for item in code:
        picList.append(item)
    return picList

if __name__=='__main__':
    piclist = singer_pic(stock_CodeUrl)
    for item in piclist:
        print(item)