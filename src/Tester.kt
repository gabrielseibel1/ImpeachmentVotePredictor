fun main(args: Array<String>) {
    var preProcessor = VotesFileParser("data/nao_com_preProcess.txt")
    print("-- VOTOS NAO --\n")
    print("[ Total de ${preProcessor.dictionary.size()} palavras e ${preProcessor.votes.size} votos ]\n")
    //printVotes(preProcessor.votes)

    preProcessor = VotesFileParser("data/sim_com_preProcess.txt")
    print("-- VOTOS SIM --\n")
    print("[ Total de ${preProcessor.dictionary.size()} palavras e ${preProcessor.votes.size} votos ]\n")
    //printVotes(preProcessor.votes)
}

fun printVotes(votes: List<Vote>) {
    print("VOTES: [\n")
    votes.forEach {
        println(it)
    }
    print("]\n")
}