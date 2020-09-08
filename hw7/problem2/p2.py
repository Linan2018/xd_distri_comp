from pyspark import SparkConf,SparkContext

conf = SparkConf().setMaster("local").setAppName("problem2")
sc = SparkContext(conf=conf)

def f1(line):
    lst = line.split(",")
    
    return [(lst[0], ['-' + lst[1]]), (lst[1], ['+' + lst[0]])]
    
def f2(t):
    grandparent_list = [i[1:] for i in t[1] if i[0] == '+']
    grandchild_list  = [i[1:] for i in t[1] if i[0] == '-']
    
    result = []
    for gp in grandparent_list:
        for gc in grandchild_list:
            result.append((gc, gp))
            
    return result

textData = sc.textFile("hdfs://localhost:9000/input2/input_file2.txt")

splitData = textData.flatMap(f1) 
combineData = splitData.reduceByKey(lambda x, y: x + y)
resultData = combineData.flatMap(f2)

resultData.saveAsTextFile("hdfs://localhost:9000/resultspark2")