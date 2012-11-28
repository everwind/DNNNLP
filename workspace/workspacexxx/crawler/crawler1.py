#!/usr/bin/env python


import sys 

for line in open('2.html'):
        items=line.split()
        for item in items:
                if item.find('href')>=0:
                        #print item
                        arr=item.split('>')
                        if len(arr)<2:
                                continue
                        a=arr[0][7:]
                        url=str(a[:len(a)-2])
                        b=arr[1]
                        key=str(b[:len(b)-4])
                        print url
        

