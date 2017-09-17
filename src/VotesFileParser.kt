import java.io.File

/**
 * Parses a formatted votes-file and generates
 * a BagOfWords containing all words of the file
 * and a collection of Votes base on the BagOfWords
 */
class VotesFileParser (private val yesVotesFileName: String, private val noVotesFileName: String) {

    val bagOfWords = BagOfWords()
    var positiveVotes : MutableList<Vote>
    val negativeVotes : MutableList<Vote>

    init  {
        buildBagOfWords()
        positiveVotes = votesFromFile(yesVotesFileName)
        negativeVotes = votesFromFile(noVotesFileName)
    }

    private fun buildBagOfWords() {
        bagOfWords.clear()

        //parse positive votes' words
        var inputStream = File(yesVotesFileName).inputStream()
        var fileContent = inputStream.bufferedReader().use { it.readText() }
        bagOfWords.parseWords(fileContent)

        //parse negative votes' words
        inputStream = File(noVotesFileName).inputStream()
        fileContent = inputStream.bufferedReader().use { it.readText() }
        bagOfWords.parseWords(fileContent)
    }

    private fun votesFromFile(fileName: String) : MutableList<Vote> {
        val votes = mutableListOf<Vote>()

        val inputStream = File(fileName).inputStream()
        val listOfLines = mutableListOf<String>()

        //parse each line of the file to a list of lines
        inputStream.bufferedReader().useLines { lines -> lines.forEach { listOfLines.add(it) } }

        //create votes based on parsed lines
        var stringBuilder = StringBuilder()
        for (lineIndex in listOfLines.indices) {
            //found a blank line -> end of a vote
            if (listOfLines[lineIndex].isBlank()) {
                votes.add(Vote(stringBuilder.toString(), bagOfWords))

                //reset builder and parse next vote
                stringBuilder = StringBuilder()
                continue
            }

            //parse part of vote (can be multiline)
            stringBuilder.append(listOfLines[lineIndex])
        }

        return votes
    }
}