from pyspark import SparkConf,SparkContext

conf = SparkConf().setMaster("local").setAppName("problem1_2")
sc = SparkContext(conf=conf)

textData = sc.textFile("hdfs://localhost:9000/input1/input_file_1.txt")

splitData = textData.map(lambda line:line.split(","))
flagData = splitData.map(lambda lst: (lst[0] + '-' + lst[2], [float(lst[4]), 1]))
countData = flagData.reduceByKey(lambda x, y: [x[0] + y[0], x[1] + y[1]])
meanData = countData.map(lambda x: (x[0], x[1][0] / x[1][1]))

meanData.saveAsTextFile("hdfs://localhost:9000/resultspark1_2")