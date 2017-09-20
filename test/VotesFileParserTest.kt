import org.junit.jupiter.api.Test

internal class VotesFileParserTest {
    @Test
    fun testVotesAndBagOfWordsParsing() {
        val fileParser = VotesFileParser()

        assert(fileParser.trainingVotes.filter { it.voteClass == true }.size
                + fileParser.testingVotes.filter { it.voteClass == true }.size == 364)

        assert(fileParser.trainingVotes.filter { it.voteClass == false }.size
                + fileParser.testingVotes.filter { it.voteClass == false }.size == 133)

        fileParser.bagOfWordsYes.pairsWordCount.keys.forEach {
            val isPresentInVote = fileParser.trainingVotes
                    .filter { it.voteClass }
                    .any { vote -> vote.text.contains(it) }
            assert(isPresentInVote)
        }

        fileParser.bagOfWordsNo.pairsWordCount.keys.forEach {
            val isPresentInVote = fileParser.trainingVotes
                    .filter { !it.voteClass }
                    .any { vote -> vote.text.contains(it) }
            assert(isPresentInVote)
        }

        fileParser.trainingVotes.forEach { vote ->
            when (vote.voteClass) {
                true ->
                    for (word in vote.text.split(" ", "\n").filter { it.isNotBlank() })
                        assert(fileParser.bagOfWordsYes.pairsWordCount.contains(word))

                false ->
                    for (word in vote.text.split(" ", "\n").filter { it.isNotBlank() })
                        assert(fileParser.bagOfWordsNo.pairsWordCount.contains(word))
            }
        }

        var checkSumIsCorrect = false
        fileParser.trainingVotes.forEach {
            val numberOfWords = it.text.split(" ", "\n").filter { it.isNotBlank() }.size
            var checkSum = 0
            it.attributes.pairsWordCount.values.forEach { checkSum += it }

            checkSumIsCorrect = (checkSum == numberOfWords)
        }
        assert(checkSumIsCorrect)
    }

    private fun printFileParserPropertiesSizes() {
        val fileParser = VotesFileParser()

        print("Total de ${fileParser.bagOfWordsYes.size} palavras na classe SIM, " +
                "Total de ${fileParser.bagOfWordsNo.pairsWordCount.size} palavras na classe NAO, " +
                "${fileParser.trainingVotes.filter { it.voteClass == false }.size} votos negativos de treino, " +
                "${fileParser.trainingVotes.filter { it.voteClass == true }.size} votos positivos de treino, " +
                "${fileParser.testingVotes.filter { it.voteClass == false }.size} votos negativos de teste, " +
                "${fileParser.testingVotes.filter { it.voteClass == true }.size} votos positivos de teste.]\n")
    }
}