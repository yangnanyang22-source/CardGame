package cn.edu.sicnu.cs.yn.cardgame

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.edu.sicnu.cs.yn.cardgame.model.CardMatchingGame
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ThreeCardGameFragment : Fragment() {

    private lateinit var game: CardMatchingGame
    private lateinit var cardButtons: List<Button>
    private lateinit var tvScore: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_three_card_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvScore = view.findViewById(R.id.tv_score_value)
        cardButtons = listOf(
            view.findViewById(R.id.btn_card_1),
            view.findViewById(R.id.btn_card_2),
            view.findViewById(R.id.btn_card_3)
        )

        game = loadGame() ?: CardMatchingGame(CARD_COUNT).also {
            it.dealMatchingCards(CARD_COUNT)
        }

        cardButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                chooseCard(index)
            }
        }

        view.findViewById<Button>(R.id.btn_reset).setOnClickListener {
            game.dealMatchingCards(CARD_COUNT)
            updateUi()
            saveGame()
        }

        updateUi()
        saveGame()
    }

    override fun onStop() {
        if (::game.isInitialized) {
            saveGame()
        }
        super.onStop()
    }

    private fun chooseCard(index: Int) {
        game.chooseCardAtIndex(index)
        updateUi()
        saveGame()
        checkAllMatched()
    }

    private fun updateUi() {
        tvScore.text = game.score.toString()

        cardButtons.forEachIndexed { index, button ->
            val card = game.cardAtIndex(index)
            if (card.isChosen || card.isMatched) {
                button.text = card.toString()
                button.setBackgroundResource(R.drawable.card_front)
                button.setTextColor(
                    if (card.suit == "♥" || card.suit == "♦") {
                        0xFFD32F2F.toInt()
                    } else {
                        Color.BLACK
                    }
                )
            } else {
                button.text = ""
                button.setBackgroundResource(R.drawable.card_back)
                button.setTextColor(Color.WHITE)
            }
        }
    }

    private fun checkAllMatched() {
        val allMatched = cardButtons.indices.all { index ->
            game.cardAtIndex(index).isMatched
        }
        if (allMatched) {
            Toast.makeText(requireContext(), R.string.game_all_matched, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveGame() {
        try {
            requireContext()
                .openFileOutput(GAME_FILE_NAME, Context.MODE_PRIVATE)
                .use { output ->
                    ObjectOutputStream(output).use { objectOutput ->
                        objectOutput.writeObject(game)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadGame(): CardMatchingGame? {
        return try {
            requireContext()
                .openFileInput(GAME_FILE_NAME)
                .use { input ->
                    ObjectInputStream(input).use { objectInput ->
                        objectInput.readObject() as? CardMatchingGame
                    }
                }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val CARD_COUNT = 3
        private const val GAME_FILE_NAME = "three_card_game.dat"
    }
}
