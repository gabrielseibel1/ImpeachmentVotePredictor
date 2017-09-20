fun main(args: Array<String>) {
    val bayes = NaiveBayes(VotesFileParser())

    var oneMoreTime = true
    while (oneMoreTime) {

        var theClass: Boolean?
        do {
            println("Você vota a favor (1) ou contra (2) o impeachment de Dilma Roussef? ")
            theClass = when (readLine()) { "1" -> true "2" -> false else -> null}

        } while (theClass == null)

        println("Digite sua justificativa de voto:")
        val text = readLine()
        if (text != null) {
            val vote = Vote(text, theClass)
            println(bayes.dropTheBayes(vote, boostedBayes = true, laplace = true, logarithms = true))
        }

        println("Mais uma vez? Digite 1 (sim) ou outra tecla (nao)")
        oneMoreTime = when (readLine()) { "1" -> true  else -> false}
    }

}