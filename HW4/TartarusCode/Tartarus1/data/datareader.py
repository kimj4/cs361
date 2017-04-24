import sys

fnumber = 101
maxnum = 0
timenum = 0

for i in range(10):
    f = open('t'+str(sys.argv[1])+".stc","r")
    contentbyline = f.readlines()

    line = contentbyline[144]
    index=line.find("      ")
    maxstring=line[int(index+len("      ")):int(index+len("      ")+4)]
    maxnum+=float(maxstring)

    line = contentbyline[153]
    index = 9
    timenum+=float(line[index:index+5])

    fnumber+=1

print(maxnum/10.0)
print(timenum/10.0)
