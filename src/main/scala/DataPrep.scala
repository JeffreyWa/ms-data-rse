package rse

/***
  *read and shape data from file
  */
  
import scala.io.Source._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.LinkedHashSet

object DataPrep{

  def readandshape():(Array[Array[Double]],Array[Array[Double]],List[Int]) = {
        val dataPath = "data/"        
        val dataFile = s"$dataPath/anonymous-msweb.data"
        
        val testFile = s"$dataPath/anonymous-msweb.test"

        def file_proc(datafile:String) :(Array[Array[Double]],List[Int]) = {
          val listOfLines = scala.io.Source.fromFile(dataFile).getLines() 
                                                              .map(_.split(","))
                                                              .toArray
          val attrlist = listOfLines.filter(_(0) == "A")
          val visitlist = listOfLines.filter(a => a(0) == "C" || a(0) == "V")
          var currentuser = 0
          var truelist = ArrayBuffer[Array[Int]]()
          var userset = LinkedHashSet[Int]()
          var prodset =  LinkedHashSet[Int]()
          for(row <- visitlist){
            if(row(0) == "C"){
              currentuser = row(2).toInt
              userset += currentuser
            } else{
              truelist += Array(currentuser,row(1).toInt)
              prodset += row(1).toInt
            } 
          }

          val userlist = userset.toList
          val prodlist = prodset.toList

          val visitmatrix = Array.fill(userlist.size,prodlist.size+1)(0.0)

          var rownum = 0 
          for(user <- userlist){
            visitmatrix(rownum)(0) = user;
            rownum += 1;
          }
          for(row <- truelist){
            visitmatrix(userlist.indexOf(row(0)))(prodlist.indexOf(row(1))+1) = 1.0
          }
          (visitmatrix,prodlist)
        }
        val (train_data,prodlist1) = file_proc(dataFile)
        val (test_data,prodlist2) = file_proc(testFile)
        (train_data,test_data,prodlist1)

     }
}





/*
 *shuffle and split data: train data : test data = 0.8 :02
***/
 ////////////Actually, after shuffle, there exist many identical columns, furter effort required////
/*        var indlist = (0 to userlist.size-1).toList
        var shufflelist =  scala.util.Random.shuffle(indlist)
 
        val train_num = (userlist.size * 0.8).toInt
        val test_num = userlist.size - train_num
        
        val train_data = Array.ofDim[Double](train_num,prodlist.size)
        val test_data = Array.ofDim[Double](test_num ,prodlist.size)
        for(i <- 0 until userlist.size){
          if (i < train_num){
            train_data(i) = visitmatrix(shufflelist(i))
          } else {
            test_data(i-train_num) = visitmatrix(shufflelist(i))
          }
        }

        (train_data,test_data,prodlist)
*/


