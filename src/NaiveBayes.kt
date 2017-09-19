class NaiveBayes (val data: VotesFileParser) {

    /**
     * "P(yes)" : Frequency of class yes divided by total number of training votes
     */
    private val probYes = data.trainingVotes.filter { it.voteClass == true }.size.toDouble() /
            (data.trainingVotes.size).toDouble()

    /**
     * "P(no)" : Frequency of class no divided by total number of training votes
     */
    private val probNo = data.trainingVotes.filter { it.voteClass == false }.size.toDouble() /
            (data.trainingVotes.size).toDouble()

    fun aPrioriProbOfClass(theClass: Boolean): Double = when (theClass) {
        true -> probYes
        false -> probNo
    }

    fun dropTheBayes(vote: Vote, logarithms: Boolean) : Prediction{
        return when {
            logarithms -> predictClassLogarithms(vote)
            else -> predictClass(vote)
        }
    }

    private fun predictClass(vote: Vote) : Prediction {
        val predictedProbabilities = mutableMapOf(Pair(true, 0.toDouble()), Pair(false, 0.toDouble()))

        for (ithClass in predictedProbabilities.keys) {
            var prod = 1.toDouble()

            for ((index, attribute) in vote.attributes.withIndex()) {

                if (attribute > 0) {
                    var timesThatWordAppearsInClass = 0
                    data.trainingVotes.filter { it.voteClass == ithClass }.forEach {
                        timesThatWordAppearsInClass += it.attributes[index]
                    }

                    val pOfWordGivenClass = timesThatWordAppearsInClass.toDouble() / data.trainingVotes.filter { it.voteClass == ithClass }.size.toDouble()
                    prod *= pOfWordGivenClass
                }
            }
            prod *= aPrioriProbOfClass(ithClass)
            predictedProbabilities[ithClass] = prod
        }

        var maxProbabilityPair = predictedProbabilities.entries.elementAt(0)
        predictedProbabilities.entries
                .asSequence()
                .filter { it.value > maxProbabilityPair.value }
                .forEach { maxProbabilityPair = it }

        return Prediction(vote, maxProbabilityPair.key, maxProbabilityPair.value)
    }

    private fun predictClassLogarithms(vote: Vote) : Prediction {
        val predictedProbabilities = mutableMapOf(Pair(true, 0.toDouble()), Pair(false, 0.toDouble()))

        for (ithClass in predictedProbabilities.keys) {
            var sum = 0.toDouble()

            for ((index, attribute) in vote.attributes.withIndex()) {

                if (attribute > 0) {
                    var timesThatWordAppearsInClass = 0
                    data.trainingVotes.filter { it.voteClass == ithClass }.forEach {
                        timesThatWordAppearsInClass += it.attributes[index]
                    }

                    val pOfWordGivenClass = timesThatWordAppearsInClass.toDouble() / data.trainingVotes.filter { it.voteClass == ithClass }.size.toDouble()

                    sum += if (pOfWordGivenClass != 0.0) Math.log(pOfWordGivenClass) else (-999999).toDouble()
                }
            }
            sum += Math.log(aPrioriProbOfClass(ithClass))
            predictedProbabilities[ithClass] = sum
        }

        var maxProbabilityPair = predictedProbabilities.entries.elementAt(0)
        predictedProbabilities.entries
                .asSequence()
                .filter { it.value > maxProbabilityPair.value }
                .forEach { maxProbabilityPair = it }

        return Prediction(vote, maxProbabilityPair.key, maxProbabilityPair.value)
    }


    fun measureAccuracy(logarithms: Boolean) : List<Prediction> {
        val results = mutableListOf<Prediction>()
        data.testingVotes.forEach {
            results.add(dropTheBayes(it, logarithms))
        }
        return results.toList()
    }
}