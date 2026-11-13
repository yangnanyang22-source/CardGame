package cn.edu.sicnu.cs.yn.cardgame.model

import java.io.Serializable

class Card(
    val suit: String,
    val rank: String,
    var isChosen: Boolean = false,
    var isMatched: Boolean = false
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
        val rankStrings = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    fun match(otherCards: Array<Card>): Int {
        var score = 0
        if (otherCards.size == 1) {
            // 两张牌配对
            val otherCard = otherCards[0]
            if (otherCard.rank == rank) {
                score = 4
            } else if (otherCard.suit == suit) {
                score = 1
            }
        } else if (otherCards.size == 2) {
            // 三张牌配对
            val card1 = otherCards[0]
            val card2 = otherCards[1]
            // 三张同点数
            if (card1.rank == rank && card2.rank == rank) {
                score = 20
            }
            // 三张同花色
            else if (card1.suit == suit && card2.suit == suit) {
                score = 10
            }
            // 两张同点数 + 一张同花色
            else if ((card1.rank == rank && card2.suit == suit) ||
                (card2.rank == rank && card1.suit == suit)) {
                score = 5
            }
        }
        return score
    }
}
