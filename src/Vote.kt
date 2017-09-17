class Vote (val voteText: String, bagOfWords: BagOfWords) {

    /**
     * Each hashMap entry is a key-value pair representing word and word-count
     */
    val attributes = mutableListOf<Int>()

    init {
        buildAttributes(bagOfWords)
    }

    private fun buildAttributes(bagOfWords: BagOfWords) {
        //val attributes = mutableListOf<Int>()

        val allWords = bagOfWords.words.map{ it -> it.toLowerCase() }
        val wordsInVote = voteText.split(" ").map{ it -> it.toLowerCase() }

        //counts how many times each word appears in vote and sets attributes list item accordingly
        for (word in allWords.filter { it.isNotBlank() && it.isNotEmpty() }) {
            attributes.add(allWords.indexOf(word), wordsInVote.count{ it == word })
        }
    }

    override fun toString(): String {
        return "[VOTE: TEXT-> $voteText ATTRS-> $attributes]"
    }
}