fun main(args: Array<String>) {
}

fun printVotes(votes: List<Vote>) {
    print("VOTES: [\n")
    votes.forEach {
        println(it)
    }
    print("]\n")
}