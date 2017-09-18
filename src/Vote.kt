class Vote (text: String, val voteClass: Boolean) {

    val text : String

    /**
     * Each hashMap entry is a key-value pair representing word and word-count
     */
    val attributes = mutableListOf<Int>()

    init {
        val builder = StringBuilder()
        text.split(" ", "\n")
                .filter { it.isNotBlank() }
                .forEach { builder.append(it).append(" ") }

        this.text = builder.toString()
    }

    fun buildAttributes(bagOfWords: BagOfWords) {
        //val attributes = mutableListOf<Int>()

        val allWords = bagOfWords.words.map{ it -> it.toLowerCase() }
        val wordsInVote = text.split(" ", "\n").filter{ it.isNotBlank() }.map{ it -> it.toLowerCase() }

        //counts how many times each word appears in vote and sets attributes list item accordingly
        for (word in allWords.filter { it.isNotBlank() }) {
            attributes.add(allWords.indexOf(word), wordsInVote.count{ it == word })
        }
    }

    override fun toString(): String {
        return "[VOTE: TEXT-> $text]"
    }
}