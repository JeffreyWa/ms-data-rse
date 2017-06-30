package rse

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.feature.MinHashLSH
import org.apache.spark.mllib.stat.Statistics 
import org.apache.spark.rdd.RDD 
import org.apache.spark.mllib.linalg._
import util.control.Breaks._
object rse{

	def main(args: Array[String]): Unit = {
          var (train_data, test_data,prod_list) = DataPrep.readandshape()
          val conf =  new SparkConf().setAppName("MS-DATA RSE").setMaster("local[*]")
          val sc = new SparkContext(conf)
          val realval :Array[Array[Double]] = train_data.map(row => row.drop(1))
          val realtest = test_data.map(row => row.drop(1)).filter(row => row.sum > 1).take(100)

          IBCF.train(realval, sc,"jaccard")
          IBCF.predict(realtest,prod_list)
          sc.stop()
     }
}