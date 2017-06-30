package rse

import breeze.linalg.{DenseMatrix => BDM}
import org.apache.spark.{SparkConf, SparkContext} 
import org.apache.spark.mllib.linalg._ 
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD 


private[rse] object PearsonModel extends BaseModel { 
  def ComputerMatrix(train_data:Array[Array[Double]],sc:SparkContext): Matrix = {
    val rdd = sc.parallelize(train_data.map(row => Vectors.dense(row)))
    val simmatrix:Matrix = Statistics.corr(rdd,"pearson")  
    simmatrix
  }
}
