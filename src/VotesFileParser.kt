import java.io.File

/**
 * Parses a formatted votes-file and generates
 * a BagOfWords containing all words of the file
 * and a collection of Votes base on the BagOfWords
 */
class VotesFileParser (fileName: String) {

    val dictionary = BagOfWords()
    val votes = mutableListOf<Vote>()

    init  {
        dictionary.clear()
        votes.clear()

        val inputStream = File(fileName).inputStream()
        val listOfLines = mutableListOf<String>()

        //parse each line of the file to a list of lines
        inputStream.bufferedReader().useLines { lines -> lines.forEach { listOfLines.add(it) } }

        //create votes based on parsed lines
        var stringBuilder = StringBuilder()
        for (lineIndex in listOfLines.indices) {
            //found a blank line -> end of a vote
            if (listOfLines[lineIndex].isBlank()) {
                votes.add(Vote(stringBuilder.toString()))
                dictionary.parseWords(stringBuilder.toString())

                //reset builder and parse next vote
                stringBuilder = StringBuilder()
                continue
            }

            //parse part of vote (can be multiline)
            stringBuilder.append(listOfLines[lineIndex])
        }
    }

}