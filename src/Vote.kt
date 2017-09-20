class Vote (text: String, val voteClass: Boolean) {

    val text : String

    /**
     * Each hashMap entry is a key-value pair representing word and word-count
     */
    val attributes = BagOfWords()

    init {
        val builder = StringBuilder()
        text.split(" ", "\n")
                .filter { it.isNotBlank() }
                .forEach { builder.append(it).append(" ") }

        this.text = builder.toString()
        buildAttributes()
    }

    private fun buildAttributes() {
        attributes.parseWords(text)
    }

    override fun toString(): String {
        return "[VOTE: TEXT-> $text]"
    }
}