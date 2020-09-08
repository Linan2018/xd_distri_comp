from pyspark import SparkConf,SparkContext

conf = SparkConf().setMaster("local").setAppName("problem1_1")
sc = SparkContext(conf=conf)

textData = sc.textFile("hdfs://localhost:9000/input1/input_file_1.txt")

splitData = textData.map(lambda line:line.split(","))
filtData = splitData.filter(lambda lst: lst[3] == "必修")
flagData = filtData.map(lambda lst: (lst[1], [float(lst[4]), 1]))
countData = flagData.reduceByKey(lambda x, y: [x[0] + y[0], x[1] + y[1]])
meanData = countData.map(lambda x: (x[0], x[1][0] / x[1][1]))

meanData.saveAsTextFile("hdfs://localhost:9000/resultspark1_1")