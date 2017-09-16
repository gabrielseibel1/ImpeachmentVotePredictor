class Vote (val voteText: String) {

    init {
        //TODO
    }
    val atributes: MutableList<Int> = mutableListOf()

    override fun toString(): String {
        return "[VOTE: $voteText]"
    }
}