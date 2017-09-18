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
    val trainingWords = BagOfWords()

    /**
     * Votes of class YES used for training
     */
    val trainingPositiveVotes = mutableListOf<Vote>()

    /**
     * Votes of class NO used for training
     */
    val trainingNegativeVotes = mutableListOf<Vote>()

    /**
     * Votes of class YES used for testing
     */
    val testingPositiveVotes = mutableListOf<Vote>()

    /**
     * Votes of class NO used for testing
     */
    val testingNegativeVotes = mutableListOf<Vote>()

    init  {
        val allPositiveVotes = votesTextsFromFile(yesVotesFileName)
        val allNegativeVotes = votesTextsFromFile(noVotesFileName)

        segregateTestingAndTrainingVotes(allPositiveVotes, trainingPositiveVotes, testingPositiveVotes)
        segregateTestingAndTrainingVotes(allNegativeVotes, trainingNegativeVotes, testingNegativeVotes)

        buildBagOfWords()

        trainingPositiveVotes.forEach { it.buildAttributes(trainingWords) }
        trainingNegativeVotes.forEach { it.buildAttributes(trainingWords) }
        testingPositiveVotes.forEach { it.buildAttributes(trainingWords) }
        testingNegativeVotes.forEach { it.buildAttributes(trainingWords) }
    }

    private fun segregateTestingAndTrainingVotes(allVotes: MutableList<Vote>, trainingVotes: MutableList<Vote>, testingVotes: MutableList<Vote>) {
        allVotes.forEach {
            if ((1..10).random() == 1)
                testingVotes.add(it)
            else
                trainingVotes.add(it)
        }
    }

    private fun buildBagOfWords() {
        trainingPositiveVotes.forEach { trainingWords.parseWords(it.text.toLowerCase()) }
        trainingNegativeVotes.forEach { trainingWords.parseWords(it.text.toLowerCase()) }
    }

    private fun votesTextsFromFile(fileName: String) : MutableList<Vote> {
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
                votes.add(Vote(stringBuilder.toString().toLowerCase()))

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