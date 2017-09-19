class Prediction(val vote: Vote, val guess: Boolean, val certainty: Double) {

    fun isSuccessful() = guess == vote.voteClass

    override fun toString(): String {
        return "For vote \"${vote.text.substring(0, Math.min(vote.text.length, 30))}...\":\n" +
                "Guessed [$guess] with certainty [$certainty] and success [${isSuccessful()}]"
    }
}