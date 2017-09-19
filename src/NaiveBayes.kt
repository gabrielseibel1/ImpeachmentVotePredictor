class NaiveBayes {

    /**
     * Parser to obtain training and testing data
     */
    val data = VotesFileParser()

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

    fun dropTheBayes(vote: Vote) = predictClass(vote)

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

                    prod *= timesThatWordAppearsInClass.toDouble() / data.trainingVotes.filter { it.voteClass == ithClass }.size.toDouble()
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

    fun measureAccuracy() : List<Prediction> {
        val results = mutableListOf<Prediction>()
        data.testingVotes.forEach {
            results.add(dropTheBayes(it))
        }
        return results.toList()
    }
}