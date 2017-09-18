import org.junit.jupiter.api.Test

internal class VotesFileParserTest {
    @Test
    fun testVotesAndBagOfWordsParsing() {
        val fileParser = VotesFileParser()

        assert(fileParser.trainingNegativeVotes.size + fileParser.testingNegativeVotes.size == 133)
        assert(fileParser.trainingPositiveVotes.size + fileParser.testingPositiveVotes.size == 364)

        fileParser.trainingWords.words
                .forEach {
                    val isPresentInVote = (fileParser.trainingPositiveVotes + fileParser.trainingNegativeVotes).any { vote -> vote.text.contains(it) }
                    assert(isPresentInVote)
                }

        (fileParser.trainingPositiveVotes + fileParser.trainingNegativeVotes)
                .forEach { vote ->
                    for (word in vote.text.split(" ", "\n").filter { it.isNotBlank() }) {
                        assert(fileParser.trainingWords.words.contains(word))
                }
        }

        var checkSumIsCorrect = false
        (fileParser.trainingPositiveVotes + fileParser.trainingPositiveVotes)
                .forEach {
                    val numberOfWords = it.text.split(" ", "\n").filter { it.isNotBlank() }.size
                    var checkSum = 0
                    it.attributes.forEach { checkSum += it }

                    checkSumIsCorrect = (checkSum == numberOfWords)
                }
        assert(checkSumIsCorrect)
    }

    private fun printFileParserPRopertiesSizes() {
        val fileParser = VotesFileParser()

        print("[ Total de ${fileParser.trainingWords.size()} palavras, " +
                "${fileParser.trainingNegativeVotes.size} votos negativos de treino, " +
                "${fileParser.trainingPositiveVotes.size} votos positivos de treino, " +
                "${fileParser.testingNegativeVotes.size} votos negativos de teste, " +
                "${fileParser.testingPositiveVotes.size} votos positivos de teste.]\n")
    }
}