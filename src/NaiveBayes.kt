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

    fun dropTheBayes(vote: Vote, logarithms: Boolean, laplace: Boolean, boostedBayes: Boolean) : Prediction{
        return when {
            boostedBayes -> boostedBayesPredictClass(vote)
            !boostedBayes and logarithms -> predictClassLogarithms(vote, laplace)
            else -> predictClass(vote, laplace)
        }
    }

    private fun predictClass(vote: Vote, laplace: Boolean) : Prediction {
        val predictedProbabilities = mutableMapOf(Pair(true, 0.toDouble()), Pair(false, 0.toDouble()))

        for (ithClass in predictedProbabilities.keys) {
            var prod = 1.toDouble()

            for ((key) in vote.attributes.pairsWordCount) {

                val timesThatWordAppearsInClass = when (ithClass) {
                    true -> data.bagOfWordsYes.pairsWordCount.entries.filter { it.key == key }.size.toDouble()
                    false -> data.bagOfWordsNo.pairsWordCount.entries.filter { it.key == key }.size.toDouble()
                }

                var numerator = 0.0
                var denominator = 0.0
                when {
                    ithClass and !laplace   -> {
                        numerator = timesThatWordAppearsInClass
                        denominator = data.bagOfWordsYes.size.toDouble()
                    }
                    !ithClass and !laplace  -> {
                        numerator = timesThatWordAppearsInClass
                        denominator = data.bagOfWordsNo.size.toDouble()
                    }
                    ithClass and laplace    -> {
                        numerator = (timesThatWordAppearsInClass + 1)
                        denominator = (data.bagOfWordsYes.size + data.bagOfWordsYes.pairsWordCount.size).toDouble()
                    }
                    !ithClass and laplace   -> {
                        numerator = (timesThatWordAppearsInClass + 1)
                        denominator = (data.bagOfWordsNo.size + data.bagOfWordsNo.pairsWordCount.size).toDouble()
                    }
                }
                val pOfWordGivenClass = numerator / denominator

                prod *= pOfWordGivenClass
            }
            predictedProbabilities[ithClass] = aPrioriProbOfClass(ithClass) * prod
        }

        var maxProbabilityPair = predictedProbabilities.entries.elementAt(0)
        predictedProbabilities.entries
                .asSequence()
                .filter { it.value > maxProbabilityPair.value }
                .forEach { maxProbabilityPair = it }

        return Prediction(vote, maxProbabilityPair.key, maxProbabilityPair.value)
    }

    private fun predictClassLogarithms(vote: Vote, laplace: Boolean) : Prediction {
        val predictedProbabilities = mutableMapOf(Pair(true, 0.toDouble()), Pair(false, 0.toDouble()))

        for (ithClass in predictedProbabilities.keys) {
            var sum = 0.0

            for ((key) in vote.attributes.pairsWordCount) {

                val timesThatWordAppearsInClass = when (ithClass) {
                    true -> data.bagOfWordsYes.pairsWordCount.entries.filter { it.key == key }.size.toDouble()
                    false -> data.bagOfWordsNo.pairsWordCount.entries.filter { it.key == key }.size.toDouble()
                }

                var numerator = 0.0
                var denominator = 0.0
                when {
                    ithClass and !laplace   -> {
                        numerator = timesThatWordAppearsInClass
                        denominator = data.bagOfWordsYes.size.toDouble()
                    }
                    !ithClass and !laplace  -> {
                        numerator = timesThatWordAppearsInClass
                        denominator = data.bagOfWordsNo.size.toDouble()
                    }
                    ithClass and laplace    -> {
                        numerator = (timesThatWordAppearsInClass + 1)
                        denominator = (data.bagOfWordsYes.size + data.bagOfWordsYes.pairsWordCount.size).toDouble()
                    }
                    !ithClass and laplace   -> {
                        numerator = (timesThatWordAppearsInClass + 1)
                        denominator = (data.bagOfWordsNo.size + data.bagOfWordsNo.pairsWordCount.size).toDouble()
                    }
                }
                val pOfWordGivenClass = numerator / denominator

                sum += Math.log(pOfWordGivenClass)
            }
            predictedProbabilities[ithClass] = Math.log(aPrioriProbOfClass(ithClass)) + sum
        }

        var maxProbabilityPair = predictedProbabilities.entries.elementAt(0)
        predictedProbabilities.entries
                .asSequence()
                .filter { it.value > maxProbabilityPair.value }
                .forEach { maxProbabilityPair = it }

        return Prediction(vote, maxProbabilityPair.key, maxProbabilityPair.value)
    }

    private fun boostedBayesPredictClass(vote: Vote) : Prediction {
        val predictedProbabilities = mutableMapOf(Pair(true, 0.toDouble()), Pair(false, 0.toDouble()))

        for (ithClass in predictedProbabilities.keys) {
            var sum = 0.0

            for ((key) in vote.attributes.pairsWordCount) {

                val timesThatWordAppearsInClass = when (ithClass) {
                    true -> data.bagOfWordsYes.pairsWordCount.entries.filter { it.key == key }.size.toDouble()
                    false -> data.bagOfWordsNo.pairsWordCount.entries.filter { it.key == key }.size.toDouble()
                }

                val pOfWordGivenClass = (timesThatWordAppearsInClass + 1)
                val normalizer = when (ithClass) { true -> 1.0 false -> 1.04 }

                sum += Math.log(pOfWordGivenClass * normalizer)
            }
            predictedProbabilities[ithClass] = sum
        }

        var maxProbabilityPair = predictedProbabilities.entries.elementAt(0)
        predictedProbabilities.entries
                .asSequence()
                .filter { it.value > maxProbabilityPair.value }
                .forEach { maxProbabilityPair = it }

        return Prediction(vote, maxProbabilityPair.key, maxProbabilityPair.value)
    }

    fun measureAccuracy(logarithms: Boolean, laplace: Boolean, boostedBayes: Boolean) : List<Prediction> {
        val results = mutableListOf<Prediction>()
        data.testingVotes.forEach {
            results.add(dropTheBayes(it, logarithms, laplace, boostedBayes))
        }
        return results.toList()
    }
}