import sys 
reload(sys) 
sys.setdefaultencoding('gb18030') 
from BeautifulSoup import BeautifulSoup
import re
import urllib2
import chardet

next_url='http://detail.zol.com.cn/kitchen_ventilator_index/subcate409_list_20.html'
writer=open('out3.txt','w')
# load url_list
#url_list=['/PDP_index/subcate399_list_1.html']
url_list=[]
for line in open('url'):
	code=chardet.detect(line)
	line=line.decode(code['encoding'],'ignore').encode('utf-8')
	print line
	url_list.append(line.strip())

url_dict={}
for url in url_list:
	if url.startswith('http'):
		next_url=url
	else:
		next_url='http://detail.zol.com.cn'+url
	print '...................start url:'+next_url


	while next_url!=None:
		url_dict[next_url]=1
		content = urllib2.urlopen(next_url).read()
		code=chardet.detect(content)
		print code
		code=code['encoding']
		content=content.decode(code,'ignore').encode('utf-8')
		soup1 = BeautifulSoup(content)
		a=soup1.findAll('h3')
		for b in a:
		        try:
		        	if len(b.contents[0].attrs)==2:
		        		outstr=b.contents[0].string
		        		#print outstr
		        		writer.write(outstr+'\n')
		        except Exception, e:
		        	continue
		try:
			next_url=soup1.findAll('a',{"class":"next"})
			suffix= next_url[0]['href']
			next_url='http://detail.zol.com.cn'+suffix
			print next_url
		except:
			next_url=None
		if next_url in url_dict:
			print 'revisit the url .................................'
			next_url=None
			break
writer.flush()
writer.close()
