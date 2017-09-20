import java.io.File
import java.util.*

/**
 * Parses a formatted votes-files and generates
 * a BagOfWords containing all words of the files
 * and two collections of Votes
 */
class VotesFileParser {

    private val yesVotesFileName = "data/sim_com_preProcess.txt"
    private val noVotesFileName = "data/nao_com_preProcess.txt"

    /**
     * All the words in the training set
     */
    val bagOfWordsYes = BagOfWords()
    val bagOfWordsNo = BagOfWords()

    /**
     * Votes of class YES used for training
     */
    val trainingVotes = mutableListOf<Vote>()

    /**
     * Votes of class YES used for testing
     */
    val testingVotes = mutableListOf<Vote>()

    init  {
        val allVotes = votesFromFile(yesVotesFileName) + votesFromFile(noVotesFileName)

        segregateTestingAndTrainingVotes(allVotes)

        buildBagsOfWords()
    }

    private fun segregateTestingAndTrainingVotes(allVotes: List<Vote>) {
        allVotes.forEach {
            if ((1..10).random() == 1)
                testingVotes.add(it)
            else
                trainingVotes.add(it)
        }
    }

    private fun buildBagsOfWords() {
        trainingVotes.filter { it.voteClass == true }.forEach {
            bagOfWordsYes.parseWords(it.text.toLowerCase())
        }
        trainingVotes.filter{ it.voteClass == false }.forEach {
            bagOfWordsNo.parseWords(it.text.toLowerCase())
        }
    }

    private fun votesFromFile(fileName: String) : MutableList<Vote> {
        val votes = mutableListOf<Vote>()
        val votesClass = when (fileName) {
            yesVotesFileName -> true
            noVotesFileName -> false
            else -> true
        }

        val inputStream = File(fileName).inputStream()
        val listOfLines = mutableListOf<String>()

        //parse each line of the file to a list of lines
        inputStream.bufferedReader().useLines { lines -> lines.forEach { listOfLines.add(it) } }

        //create votes based on parsed lines
        var stringBuilder = StringBuilder()
        for (lineIndex in listOfLines.indices) {
            //found a blank line -> end of a vote
            if (listOfLines[lineIndex].isBlank()) {
                votes.add(Vote(stringBuilder.toString().toLowerCase(), votesClass))

                //reset builder and parse next vote
                stringBuilder = StringBuilder()
                continue
            }

            //parse part of vote (can be multiline)
            stringBuilder.append(listOfLines[lineIndex])
        }
        return votes
    }

    /**
     * Generates random number in a closed range
     */
    private fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start
}