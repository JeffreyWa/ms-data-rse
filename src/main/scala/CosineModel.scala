package rse

import breeze.linalg.{DenseMatrix => BDM}
import org.apache.spark.{SparkConf, SparkContext} 
import org.apache.spark.mllib.linalg._ 
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD 

private[rse] object CosineModel extends BaseModel{ 
  def ComputerMatrix(train_data:Array[Array[Double]], sc:SparkContext): Matrix = {
  	val rdd = sc.parallelize(train_data.map(row => Vectors.dense(row)))
	val rowMatrix = new distributed.RowMatrix(rdd)
	var cos = rowMatrix.columnSimilarities.toBlockMatrix.toLocalMatrix
    val n = cos.numRows
    val G = new BDM[Double](n, n)
  
	var row = 0
    var col = 0
    while (row < n) {
      col = row
      while (col < n) {
        G(row,col) = 1.0 - cos(row,col)
        col += 1
      }
      row +=1
    }

	row = 0
    col = 0
    while (row < n) {
      col = 0
      while (col < row) {
        G(row,col) = G(col,row)
        col += 1
      }
      row +=1
    }
    Matrices.dense(n, n, G.data)
  }
}