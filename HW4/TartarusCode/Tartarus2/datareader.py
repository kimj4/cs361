import sys

avgMax = 0
avgTime = 0

for i in range(10):
    f = open('data/t' + str(int(sys.argv[1]) + i) + ".stc","r")
    lines = f.readlines()
    fitnessData = ""
    timeData = ""
    for line in lines:
        if "|" in line:
            fitnessData += line;
        if "seconds/generation" in line:
            timeData += line

    fitnessDataArray = fitnessData.strip().split("\n")
    fitnessLastLine = fitnessDataArray[-1]
    bestFitness = fitnessLastLine.split("|")[1].strip().split(" ")[0]
    avgMax += float(bestFitness)

    bestTime = timeData.split(" ")[4]
    avgTime += float(bestTime)

print("from t%s to t%s:") % (str(sys.argv[1]), str(int(sys.argv[1]) + 9))
print(avgMax / 10.0)
print(avgTime / 10.0)
