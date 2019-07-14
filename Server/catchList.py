import re
import urllib.request

stock_CodeUrl='http://m.kugou.com/'

def song_list(url):
    list = []
    html = urllib.request.urlopen(url).read()
    html = html.decode('utf-8')
    s = r'<span>.* - (.*?)</span>'
    pat = re.compile(s)
    code = pat.findall(html)
    for item in code:
        list.append(item)
    return list

def singer_list(url,song):
    html = urllib.request.urlopen(url).read()
    html = html.decode('utf-8')
    s = r'<span>(.*?) - '+song+'</span>'
    pat = re.compile(s)
    code = pat.findall(html)
    if code==[]:
        return "empty"
    return code[0]

if __name__=='__main__':
    songlist = song_list(stock_CodeUrl)
    for item in songlist:
        print(item)
        singer=singer_list(stock_CodeUrl,item)
        print(singer)