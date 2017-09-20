import org.junit.jupiter.api.Test

internal class NaiveBayesTest {

    private val normal = Triple(false, false, false)
    private val logarithms = Triple(true, false, false)
    private val logarithmsLaplace = Triple(true, true, false)
    private val normalLaplace = Triple(false, true, false)
    private val boostedBayes = Triple(true,true, true)

    @Test
    fun probOfClass() {
        val bayes = NaiveBayes(VotesFileParser())
        println(bayes.aPrioriProbOfClass(true))
        println(bayes.aPrioriProbOfClass(false))
    }

    @Test
    fun dropTheBayes() {
        val bayes = NaiveBayes(VotesFileParser())
        val vote = Vote("brasil democracia familia", true)

        val prediction = bayes.dropTheBayes(vote, logarithms = false, laplace = false, boostedBayes = false)
        println(prediction)
    }

    @Test
    fun dropTheBayesLogarithm() {
        val bayes = NaiveBayes(VotesFileParser())
        val vote = Vote("brasil democracia familia", true)

        val prediction = bayes.dropTheBayes(vote, logarithms = true, laplace = false, boostedBayes = false)
        println(prediction)
    }

    @Test
    fun accuracyMeasures() {
        val numberOfBatteries = 1000
        var totalOfPredictions = 0

        val batteriesOfTestsResults = mutableMapOf<Triple<Boolean, Boolean, Boolean>, MutableList<List<Prediction>>>(
                Pair(normal, mutableListOf()),
                Pair(logarithms, mutableListOf()),
                Pair(logarithmsLaplace, mutableListOf()),
                Pair(normalLaplace, mutableListOf()),
                Pair(boostedBayes, mutableListOf())
        )
        val totalOfCorrectPredictions = mutableMapOf<Triple<Boolean, Boolean, Boolean>, Int?> (
                Pair(normal, 0),
                Pair(logarithms, 0),
                Pair(logarithmsLaplace, 0),
                Pair(normalLaplace, 0),
                Pair(boostedBayes, 0)
        )

        (1..numberOfBatteries).forEach {
            val bayes = NaiveBayes(VotesFileParser())
            totalOfPredictions += bayes.data.testingVotes.size

            batteriesOfTestsResults.keys.forEach {
                val predictions = bayes.measureAccuracy(it.first, it.second, it.third)
                totalOfCorrectPredictions[it] = totalOfCorrectPredictions[it]?.plus(predictions.filter{ it.isSuccessful() }.size)
                batteriesOfTestsResults[it]?.add(predictions)
            }
        }

        println("Ran $numberOfBatteries batteries of tests for each mode - test votes selected at random (10% of all data)")
        batteriesOfTestsResults.keys.forEach {
            println("Mode -> Logarithms = [${it.first}] | Laplace = [${it.second}] | BoostedBayes [${it.third}]")
            println("///////////////////////////// RESULTS /////////////////////////////")
            println("Success ratio = ${totalOfCorrectPredictions[it]} / ${totalOfPredictions.toDouble()} = " +
                    "${totalOfCorrectPredictions[it]?.div(totalOfPredictions.toDouble())}")

            var correctPredictionsOfClassNo = 0
            batteriesOfTestsResults[it]?.forEach {
                it.filter { it.isSuccessful() and !it.vote.voteClass }.forEach { correctPredictionsOfClassNo++ }
            }
            var correctPredictionsOfClassYes = 0
            batteriesOfTestsResults[it]?.forEach {
                it.filter { it.isSuccessful() and it.vote.voteClass }.forEach { correctPredictionsOfClassYes++ }
            }
            var totalPredictionsOfClassNo = 0
            batteriesOfTestsResults[it]?.forEach {
                it.filter { !it.vote.voteClass }.forEach { totalPredictionsOfClassNo++ }
            }
            var totalPredictionsOfClassYes = 0
            batteriesOfTestsResults[it]?.forEach {
                it.filter { it.vote.voteClass }.forEach { totalPredictionsOfClassYes++ }
            }

            println("Success ratio for class yes = $correctPredictionsOfClassYes / $totalPredictionsOfClassYes = " +
                    "${correctPredictionsOfClassYes.div(totalPredictionsOfClassYes.toDouble())}")

            println("Success ratio for class no = $correctPredictionsOfClassNo / $totalPredictionsOfClassNo = " +
                    "${correctPredictionsOfClassNo.div(totalPredictionsOfClassNo.toDouble())}")

            println("///////////////////////////////////////////////////////////////////\n\n")
        }
    }
}