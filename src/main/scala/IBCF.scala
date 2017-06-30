package rse

import breeze.linalg.{DenseMatrix => BDM}
import org.apache.spark.{SparkConf, SparkContext} 
import org.apache.spark.mllib.linalg._ 
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.rdd.RDD 

private[rse] object IBCF{
  var model:Matrix = null

  def train(train_data:Array[Array[Double]], sc:SparkContext, method:String) = {
  	val _method = method.toLowerCase
  	model = Models.train(train_data,sc,_method)
  }

  def predict(test_data:Array[Array[Double]],prod_list:List[Int]): Unit= {
  	if(model == null ) return ;
  	val simmatrix = model
    val pred = Array.fill(test_data.size)(-1)
    var cur_index = 0
    /////skip users who just visit one page////////////
    println("First train_data line " + test_data(0).map(_.toString).mkString(" "))
    for(row <- test_data){
    ////////get the first visited page as well as expected page////
         var expected = row.indexOf(1.0)
         row(expected) = 0.0
    ///////////////////caculate the distance////////////////////////
         var expected_score = 0.0
    ////////////skip pages have been visited////////////
         var est_index = 0
         for(i <- 0 until row.length){
              if(row(i) != 1.0){
                   var sum_sim = 0.0
                   var score = 0.0
                   var est = 0.0
                   var sim = 0.0
                   for(j <- 0 until row.length){
                        sim = simmatrix.apply(i,j)
                        sum_sim += sim
                        score += sim * row(j)                              
                   }
                   if(sum_sim > 0 ){
                        est = (score / sum_sim)
                   }
                   else
                        est = 0.0
                   if(est > expected_score){
                        expected_score = est
                        est_index = i
                   }
              }
         } 

         if(est_index == expected)
              pred(cur_index) = 1
         else
              pred(cur_index) = 0
         cur_index += 1
         println("expected position:" + expected+" Predict position:"+ est_index)
    }
    println("total:" + test_data.size )
    println("result:" + pred.filter(x => x == 1).sum)
  }
}