package rse 


import breeze.linalg.{DenseMatrix => BDM}
import org.apache.spark.{SparkConf, SparkContext} 
import org.apache.spark.mllib.linalg._ 
import org.apache.spark.mllib.stat.Statistics 
import org.apache.spark.rdd.RDD 

private[rse] object JaccardModel extends BaseModel{ 

  def JaccardDistance(v1: SparseVector, v2: SparseVector): Double = {
    val indices1 = v1.indices.toSet
    val indices2 = v2.indices.toSet
    val intersection = indices1.intersect(indices2)
    val union = indices1.union(indices2)
    1.0 - (intersection.size / union.size.toDouble)
  }
/**
   * Compute the jaccard distance matrix from the sparse vectors.
   */
  def ComputerMatrix(train_data:Array[Array[Double]],sc:SparkContext): Matrix = {
    val transMatrix =  train_data.transpose
    val n = transMatrix.size;
    val G = new BDM[Double](n, n)
    var row = 0
    var col = 0
    var value = 0.0
    while (row < n) {
      col = row+1
      while (col < n) {
        value = 1-JaccardDistance(Vectors.dense(transMatrix(row)).toSparse,Vectors.dense(transMatrix(col)).toSparse)
        G(row, col) = value
        G(col, row) = value
        col += 1
      }
      row +=1
    }

   // put 1.0 on the diagonals
    row = 0
    while (row < n) {
      G(row, row) = 1.0
      row +=1
    }
    Matrices.dense(n, n, G.data)
  }
} 
