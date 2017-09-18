import org.junit.jupiter.api.Test

internal class VoteTest {

    @Test
    fun getVoteText() {

    }

    @Test
    fun buildAttributes() {
        val vote = Vote("love kotlin unit-testing Please")
        vote.buildAttributes(BagOfWords(mutableListOf("I", "love", "kotlin", "unit-testing", "please", "sugar", "extra")))

        var checkSumIsCorrect = false
        val numberOfWords = vote.text.split(" ").filter { it.isNotBlank() }.size
        var checkSum = 0
        vote.attributes.forEach { checkSum += it }
        checkSumIsCorrect = (checkSum == numberOfWords)
        println("partial checkSum is $checkSumIsCorrect with value $checkSum and $numberOfWords words")

        assert(checkSumIsCorrect)
    }
}