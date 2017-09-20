import org.junit.jupiter.api.Test

internal class VoteTest {

    @Test
    fun getVoteText() {

    }

    @Test
    fun buildAttributes() {
        val vote = Vote("love kotlin unit-testing Please", true)

        val numberOfWords = vote.text.split(" ").filter { it.isNotBlank() }.size
        var checkSum = 0
        vote.attributes.pairsWordCount.values.forEach { checkSum += it }
        val checkSumIsCorrect = (checkSum == numberOfWords)
        println("partial checkSum is $checkSumIsCorrect with value $checkSum and $numberOfWords words")

        assert(checkSumIsCorrect)
    }
}