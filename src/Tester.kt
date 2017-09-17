fun main(args: Array<String>) {
    val fileParser = VotesFileParser("data/sim_com_preProcess.txt", "data/nao_com_preProcess.txt")
    print("-- VOTOS NAO --\n")
    print("[ Total de ${fileParser.bagOfWords.size()} palavras e ${fileParser.negativeVotes.size} votos ]\n")
    printVotes(fileParser.negativeVotes)

    print("-- VOTOS SIM --\n")
    print("[ Total de ${fileParser.bagOfWords.size()} palavras e ${fileParser.positiveVotes.size} votos ]\n")
    printVotes(fileParser.positiveVotes)

    print("-- BAG OF WORDS --\n")
    print(fileParser.bagOfWords)
}

fun printVotes(votes: List<Vote>) {
    print("VOTES: [\n")
    votes.forEach {
        println(it)
    }
    print("]\n")
}