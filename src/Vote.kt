class Vote (val voteText: String, bagOfWords: BagOfWords) {

    init {
        //TODO build attributes
    }
    val attributes: MutableList<Int> = mutableListOf()

    override fun toString(): String {
        return "[VOTE: $voteText]"
    }
}