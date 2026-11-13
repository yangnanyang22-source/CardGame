package cn.edu.sicnu.cs.yn.cardgame.model

import java.io.Serializable

class CardMatchingGame(count: Int) : Serializable {
    var score = 0
        private set

    private val cards: MutableList<Card>

    init {
        val deck = Deck()
        cards = mutableListOf()
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    fun cardAtIndex(index: Int): Card {
        return cards.get(index)
    }

    val MISMATCH_PENALTY = 2
    val MATCH_BONUS = 4
    val COST_TO_CHOOSE = 1

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (!card.isMatched) {
            if (card.isChosen) {
                // 再点一下取消选中
                card.isChosen = false
            } else {
                // 找出当前已经选中但还没配对的牌
                val chosenCards = cards.filter { it.isChosen && !it.isMatched }
                val requiredOtherCards = if (cards.size == 2) 1 else 2

                if (chosenCards.size < requiredOtherCards) {
                    // 还没有选够需要参与配对的牌，直接翻开
                    card.isChosen = true
                    score -= COST_TO_CHOOSE
                } else if (chosenCards.size == requiredOtherCards) {
                    // 已选牌数量足够，开始判断配对
                    val matchScore = card.match(chosenCards.toTypedArray())
                    if (matchScore > 0) {
                        // 配对成功
                        score += matchScore * MATCH_BONUS
                        card.isMatched = true
                        chosenCards.forEach { it.isMatched = true }
                    } else {
                        // 配对失败，扣分，把之前翻开的牌翻回去
                        score -= MISMATCH_PENALTY
                        chosenCards.forEach { it.isChosen = false }
                        card.isChosen = true
                    }
                }
            }
        }
    }

    // 重置游戏，重新发牌
    fun reset(count: Int) {
        score = 0
        cards.clear()
        val deck = Deck()
        for (i in 1..count) {
            val card: Card? = deck.drawRandomCard()
            if (card != null) {
                cards.add(card)
            }
        }
    }

    fun dealMatchingCards(count: Int) {
        score = 0
        cards.clear()

        val rank = Card.rankStrings.random()
        val suits = Card.validSuits.toList().shuffled().take(count)
        for (suit in suits) {
            cards.add(Card(suit, rank))
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
