package cn.edu.sicnu.cs.yn.cardgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.yn.cardgame.model.CardMatchingGame

class CardGameActivity : AppCompatActivity() {

    private lateinit var game: CardMatchingGame
    private lateinit var buttons: MutableList<Button>
    private lateinit var tvScore: TextView
    private var highScore: Int = 0

    // 16张牌对应的Button ID
    private val cardIds = arrayOf(
        R.id.card0, R.id.card1, R.id.card2, R.id.card3,
        R.id.card4, R.id.card5, R.id.card6, R.id.card7,
        R.id.card8, R.id.card9, R.id.card10, R.id.card11,
        R.id.card12, R.id.card13, R.id.card14, R.id.card15
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_game)

        tvScore = findViewById(R.id.tv_score_value)
        highScore = intent.getIntExtra(MainActivity.EXTRA_HIGH_SCORE, 0)

        // 初始化16个按钮
        buttons = mutableListOf()
        for (id in cardIds) {
            buttons.add(findViewById(id))
        }

        // 开始新游戏
        startNewGame()

        // 给每张牌设置点击事件
        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                chooseCard(i)
            }
        }

        // 重置游戏按钮
        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            startNewGame()
        }

        // 退出按钮
        findViewById<Button>(R.id.btn_exit).setOnClickListener {
            // 把最高分返回给主界面
            val resultIntent = Intent()
            resultIntent.putExtra(MainActivity.EXTRA_HIGH_SCORE, highScore)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // 开始新游戏
    private fun startNewGame() {
        game = CardMatchingGame(16) // 16张牌
        updateUI()
    }

    // 点击某张牌
    private fun chooseCard(index: Int) {
        game.chooseCardAtIndex(index)
        updateUI()

        // 检查游戏是否结束（所有牌都配对了）
        checkGameEnd()
    }

    // 更新界面显示
    private fun updateUI() {
        for (i in buttons.indices) {
            val card = game.cardAtIndex(i)
            val button = buttons[i]

            if (card.isChosen || card.isMatched) {
                // 牌面朝上：白色牌面，显示扑克牌内容
                button.text = card.toString()
                button.setBackgroundResource(R.drawable.card_front)
                // 红桃和方块用红色字，黑桃和梅花用黑色字
                if (card.suit == "♥" || card.suit == "♦") {
                    button.setTextColor(0xFFD32F2F.toInt())
                } else {
                    button.setTextColor(0xFF212121.toInt())
                }
            } else {
                // 牌面朝下：深蓝色扑克牌背面
                button.text = ""
                button.setBackgroundResource(R.drawable.card_back)
            }
        }

        // 更新得分
        tvScore.text = game.score.toString()

        // 更新最高分
        if (game.score > highScore) {
            highScore = game.score
        }
    }

    // 检查游戏是否结束
    private fun checkGameEnd() {
        var allMatched = true
        for (i in 0 until 16) {
            if (!game.cardAtIndex(i).isMatched) {
                allMatched = false
                break
            }
        }
        if (allMatched) {
            Toast.makeText(this, "恭喜通关！得分：${game.score}", Toast.LENGTH_LONG).show()
        }
    }
}