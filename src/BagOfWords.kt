class BagOfWords {
    private var words: MutableList<String> = mutableListOf()
        get() = field

    fun parseWords(string: String) {
        val parsedWords = string.split(" ")
        words.addAll(parsedWords)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        words.forEach{
            builder.append("[ $it ]")
            builder.append("\n")
        }

        return builder.toString()
    }

    fun clear() {
        words.clear()
    }

    fun size() = words.size
}