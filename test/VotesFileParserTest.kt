import org.junit.jupiter.api.Test

internal class VotesFileParserTest {
    @Test
    fun testVotesAndBagOfWordsParsing() {
        val fileParser = VotesFileParser("data/sim_com_preProcess.txt", "data/nao_com_preProcess.txt")

        print("[ Total de ${fileParser.trainingWords.size()} palavras, " +
                "${fileParser.trainingNegativeVotes.size} votos negativos de treino, " +
                "${fileParser.trainingPositiveVotes.size} votos positivos de treino, " +
                "${fileParser.testingNegativeVotes.size} votos negativos de teste, " +
                "${fileParser.testingPositiveVotes.size} votos positivos de teste.]\n")

        fileParser.trainingWords.words
                .forEach {
                    val isPresentInVote = (fileParser.trainingPositiveVotes + fileParser.trainingNegativeVotes).any { vote -> vote.text.contains(it) }
                    assert(isPresentInVote)
                }

        for (vote in fileParser.trainingPositiveVotes + fileParser.trainingNegativeVotes) {
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

}