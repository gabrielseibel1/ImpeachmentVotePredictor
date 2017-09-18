import org.junit.jupiter.api.Test

internal class VotesFileParserTest {
    @Test
    fun testVotesAndBagOfWordsParsing() {
        val fileParser = VotesFileParser()

        assert(fileParser.trainingVotes.filter { it.voteClass == true }.size
                + fileParser.testingVotes.filter { it.voteClass == true }.size == 364)

        assert(fileParser.trainingVotes.filter { it.voteClass == false }.size
                + fileParser.testingVotes.filter { it.voteClass == false }.size == 133)

        fileParser.trainingWords.words
                .forEach {
                    val isPresentInVote = (fileParser.trainingVotes).any { vote -> vote.text.contains(it) }
                    assert(isPresentInVote)
                }

        fileParser.trainingVotes
                .forEach { vote ->
                    for (word in vote.text.split(" ", "\n").filter { it.isNotBlank() }) {
                        assert(fileParser.trainingWords.words.contains(word))
                }
        }

        var checkSumIsCorrect = false
        fileParser.trainingVotes
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
                "${fileParser.trainingVotes.filter { it.voteClass == false }.size} votos negativos de treino, " +
                "${fileParser.trainingVotes.filter { it.voteClass == true }.size} votos positivos de treino, " +
                "${fileParser.testingVotes.filter { it.voteClass == false }.size} votos negativos de teste, " +
                "${fileParser.testingVotes.filter { it.voteClass == true }.size} votos positivos de teste.]\n")
    }
}