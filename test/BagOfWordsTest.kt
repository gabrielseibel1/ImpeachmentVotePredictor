import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BagOfWordsTest {
    @Test
    fun parseWords() {
        val bagOfWords = BagOfWords()
        bagOfWords.parseWords("Pela pela democracia e contra o golpe golpe")
        val correctExample = mutableMapOf(
                Pair("pela",2), Pair("democracia",1), Pair("e", 1), Pair("contra",1), Pair("o",1), Pair("golpe",2)
        )

        println(bagOfWords.pairsWordCount)

        for ((key, value) in bagOfWords.pairsWordCount) {
            println("1 - For key $key found value ${correctExample[key]}")
            assert(correctExample[key] == value)
        }

        for ((key, value) in correctExample.entries) {
            println("2 - For key $key found value ${bagOfWords.pairsWordCount[key]}")
            assert(bagOfWords.pairsWordCount[key] == value)
        }
    }

}