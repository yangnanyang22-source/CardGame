package cn.edu.sicnu.cs.yn.cardgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private var cards: List<Any>,
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnCard: Button = itemView as Button
    }

    // 更新数据的方法，不重新创建Adapter
    fun updateData(newCards: List<Any>) {
        cards = newCards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val btn = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false) as Button
        return CardViewHolder(btn)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position] as cn.edu.sicnu.cs.yn.cardgame.model.Card

        if (card.isChosen || card.isMatched) {
            holder.btnCard.text = card.toString()
            holder.btnCard.setBackgroundResource(R.drawable.card_front)
            if (card.suit == "♥" || card.suit == "♦") {
                holder.btnCard.setTextColor(0xFFD32F2F.toInt())
            } else {
                holder.btnCard.setTextColor(0xFF212121.toInt())
            }
        } else {
            holder.btnCard.text = ""
            holder.btnCard.setBackgroundResource(R.drawable.card_back)
        }

        holder.btnCard.setOnClickListener {
            onCardClick(position)
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}