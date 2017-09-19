import org.junit.jupiter.api.Test

internal class NaiveBayesTest {
    @Test
    fun probOfClass() {
        val bayes = NaiveBayes(VotesFileParser())
        println(bayes.aPrioriProbOfClass(true))
        println(bayes.aPrioriProbOfClass(false))
    }

    @Test
    fun dropTheBayes() {
        val bayes = NaiveBayes(VotesFileParser())
        var vote = Vote("brasil democracia familia", true)
        vote.buildAttributes(BagOfWords(mutableListOf("Brasil", "Democracia", "familia", "namibia")))

        var prediction = bayes.dropTheBayes(vote, logarithms = false)
        println(prediction)
    }

    @Test
    fun dropTheBayesLogarithm() {
        val bayes = NaiveBayes(VotesFileParser())
        var vote = Vote("brasil democracia familia", true)
        vote.buildAttributes(BagOfWords(mutableListOf("Brasil", "Democracia", "familia", "namibia")))

        var prediction = bayes.dropTheBayes(vote, logarithms = true)
        println(prediction)
    }

    @Test
    fun accuracyMeasures() {
        val numberOfBatteries = 50
        val batteriesOfTestsResults = mutableListOf<List<Prediction>>()
        var totalOfPredictions = 0
        var totalOfCorrectPredictions = 0
        var numberOfUncertainPredictions = 0

        val logarithms_batteriesOfTestsResults = mutableListOf<List<Prediction>>()
        var logarithms_totalOfPredictions = 0
        var logarithms_totalOfCorrectPredictions = 0
        var logarithms_numberOfUncertainPredictions = 0



        (1..numberOfBatteries).forEach {
            val bayes = NaiveBayes(VotesFileParser())

            var predictions = bayes.measureAccuracy(logarithms = false)
            totalOfPredictions += predictions.size
            totalOfCorrectPredictions += predictions.filter{ it.isSuccessful()}.size
            numberOfUncertainPredictions += predictions.filter { it.certainty == 0.0 }.size
            batteriesOfTestsResults.add(predictions)

            predictions = bayes.measureAccuracy(logarithms = true)
            logarithms_totalOfPredictions += predictions.size
            logarithms_totalOfCorrectPredictions += predictions.filter{ it.isSuccessful()}.size
            logarithms_numberOfUncertainPredictions += predictions.filter { it.certainty >= -999999 }.size
            logarithms_batteriesOfTestsResults.add(predictions)
        }


        println("Ran $numberOfBatteries batteries of tests for each mode - test votes selected at random (10% of all data)")

        println("Mode -> Logarithms = [false] | Laplace = [false]")
        println("///////////////////////////// RESULTS /////////////////////////////")
        println("Success ratio = ${totalOfCorrectPredictions.toDouble()} / ${totalOfPredictions.toDouble()} = " +
                "${totalOfCorrectPredictions.toDouble() / totalOfPredictions.toDouble()}")

        println("${numberOfUncertainPredictions.toDouble() / totalOfPredictions}" +
                " of the predictions had low certainty")
        println("${numberOfUncertainPredictions.toDouble() / totalOfCorrectPredictions}" +
                " of the correct predictions had low certainty")
        println("///////////////////////////////////////////////////////////////////")


        println("Mode -> Logarithms = [true] | Laplace = [false]")
        println("///////////////////////////// RESULTS /////////////////////////////")
        println("Success ratio = ${logarithms_totalOfCorrectPredictions.toDouble()} / ${logarithms_totalOfPredictions.toDouble()} = " +
                "${logarithms_totalOfCorrectPredictions.toDouble() / logarithms_totalOfPredictions.toDouble()}")

        println("${logarithms_numberOfUncertainPredictions.toDouble() / logarithms_totalOfPredictions}" +
                " of the predictions had low certainty")
        println("${logarithms_numberOfUncertainPredictions.toDouble() / logarithms_totalOfCorrectPredictions}" +
                " of the correct predictions had low certainty")
        println("///////////////////////////////////////////////////////////////////")
    }
}