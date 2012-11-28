import sys
import chardet
cnt=0
codeList=['TIS-620','ascii','GB2312','Big5']
for line in open('out1.txt'):
	line=line.strip()
	cnt=cnt+1	
	code=chardet.detect(line)['encoding']
	if code not in codeList and code!=None:
		codeList.append(code)
	#print code
print codeList
writer=open('out2.txt','w')
for line in open('out1.txt'):
	line=line.strip()
	code=chardet.detect(line)['encoding']
	for i in range(len(codeList)):
		try:
			line=line.decode(codeList[i]).encode('utf-8')
			writer.write(line+'\n')
			break
		except Exception, e:
			if i+1==len(codeList):
				print 'error'
				writer.write('error:'+line+'\n')
			continue
print 'done'