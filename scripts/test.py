def addlist(alist):  
    for i in alist:  
        yield i + 1 
alist = [1, 2, 3, 4]  
for x in addlist(alist):  
    print x,