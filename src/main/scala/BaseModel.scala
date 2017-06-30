package rse


import breeze.linalg.{DenseMatrix => BDM}
import org.apache.spark.{SparkConf, SparkContext} 
import org.apache.spark.mllib.linalg._ 
import org.apache.spark.rdd.RDD 

private[rse] trait BaseModel{
	def ComputerMatrix(train_data:Array[Array[Double]], sc:SparkContext) : Matrix
}

private[rse] object Models{
	def train (train_data:Array[Array[Double]], sc:SparkContext,
	    model:String = ModelNames.defaultModelName) :Matrix = {
      val Model :BaseModel = getModelFromName(model)
      Model.ComputerMatrix(train_data,sc)
    }

	def getModelFromName(method: String): BaseModel = {
	  try {
      	ModelNames.nameToObjectMap(method)
      } catch {
        case nse: NoSuchElementException =>
        throw new IllegalArgumentException("Unrecognized method name. Supported model: "
          + ModelNames.nameToObjectMap.keys.mkString(", "))
      }
	}
}
private[rse] object ModelNames {
  // Note: after new types of model are implemented, please update this map.
  val nameToObjectMap = Map(("cosin", CosineModel), ("jaccard", JaccardModel), ("pearson", PearsonModel))
  val defaultModelName: String = "jaccard"
}