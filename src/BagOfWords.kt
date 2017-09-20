class BagOfWords {

    var pairsWordCount: MutableMap<String, Int> = mutableMapOf()

    var size = 0

    fun parseWords(string: String) {
        val parsedWords = string.split(" ", "\n").map { it -> it.toLowerCase() }
        parsedWords.filter { !it.isBlank() }.forEach { word ->
            pairsWordCount.putIfAbsent(word, parsedWords.count{ it == word })
            size += parsedWords.count{ it == word }
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        pairsWordCount.forEach{
            builder.append("[ $it ]")
            builder.append("\n")
        }

        return builder.toString()
    }

    fun clear() {
        pairsWordCount.clear()
    }
}