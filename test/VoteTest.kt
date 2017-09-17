import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VoteTest {

    @Test
    fun getVoteText() {

    }

    @Test
    fun buildAttributes() {
        val vote = Vote("I love kotlin and unit-testing Yes please",
                BagOfWords(mutableListOf("Love", "kotlin")))

        print(vote.attributes)
        //TODO assertion
    }
}