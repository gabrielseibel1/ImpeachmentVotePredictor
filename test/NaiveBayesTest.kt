import org.junit.jupiter.api.Test

internal class NaiveBayesTest {
    @Test
    fun probOfClass() {
        val bayes = NaiveBayes()
        println(bayes.aPrioriProbOfClass(true))
        println(bayes.aPrioriProbOfClass(false))
    }

    @Test
    fun dropTheBayes() {
        val bayes = NaiveBayes()
        var vote = Vote("brasil democracia familia", true)
        vote.buildAttributes(BagOfWords(mutableListOf("Brasil", "Democracia", "familia", "namibia")))

        var prediction = bayes.dropTheBayes(vote)
        println(prediction)
    }

    @Test
    fun accuracyMeasure() {

        val numberOfBatteries = 50
        val batteriesOfTestsResults = mutableListOf<List<Prediction>>()
        var totalOfPredictions = 0
        var totalOfCorrectPredictions = 0
        var numberOfUncertainPredictions = 0

        (1..numberOfBatteries).forEach {

            val bayes = NaiveBayes()
            val predictions = bayes.measureAccuracy()
            totalOfPredictions += predictions.size
            totalOfCorrectPredictions += predictions.filter{ it.isSuccessful()}.size
            numberOfUncertainPredictions += predictions.filter { it.certainty == 0.0 }.size

            batteriesOfTestsResults.add(predictions)
        }


        println("Ran $numberOfBatteries batteries of tests")
        println("///////////////////////////// RESULTS /////////////////////////////")
        println("Success ratio = ${totalOfCorrectPredictions.toDouble()} / ${totalOfPredictions.toDouble()} = " +
                "${totalOfCorrectPredictions.toDouble() / totalOfPredictions.toDouble()}")

        println("${numberOfUncertainPredictions.toDouble() / totalOfPredictions}" +
                " of the predictions had 0.0 certainty")
        println("${numberOfUncertainPredictions.toDouble() / totalOfCorrectPredictions}" +
                " of the correct predictions had 0.0 certainty")
        println("///////////////////////////////////////////////////////////////////")
    }
}