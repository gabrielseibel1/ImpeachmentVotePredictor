class BagOfWords {
    private var words: MutableList<String> = mutableListOf()
        get() = field

    fun parseWords(string: String) {
        val parsedWords = string.split(" ", "\n")
        parsedWords.filter { !it.isBlank() }.forEach {
            if (!words.contains(it)) words.add(it)
        }
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