package cn.edu.sicnu.cs.yn.cardgame.model

class CardMatchingGame(count: Int) {
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

                if (chosenCards.size < 2) {
                    // 已经选了0或1张，直接翻开
                    card.isChosen = true
                    score -= COST_TO_CHOOSE
                } else if (chosenCards.size == 2) {
                    // 已经选了2张，现在选第3张，开始判断配对
                    val matchScore = card.match(chosenCards.toTypedArray())
                    if (matchScore > 0) {
                        // 配对成功
                        score += matchScore * MATCH_BONUS
                        card.isMatched = true
                        chosenCards[0].isMatched = true
                        chosenCards[1].isMatched = true
                    } else {
                        // 配对失败，扣分，把前两张翻回去
                        score -= MISMATCH_PENALTY
                        chosenCards[0].isChosen = false
                        chosenCards[1].isChosen = false
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
}