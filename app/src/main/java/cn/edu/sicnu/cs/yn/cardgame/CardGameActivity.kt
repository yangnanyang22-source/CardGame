package cn.edu.sicnu.cs.yn.cardgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cs.yn.cardgame.model.Card
import cn.edu.sicnu.cs.yn.cardgame.model.CardMatchingGame

class CardGameActivity : AppCompatActivity() {

    private lateinit var game: CardMatchingGame
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    private lateinit var tvScore: TextView
    private var highScore: Int = 0
    private var cardCount: Int = 24  // 默认24张牌

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_game)

        tvScore = findViewById(R.id.tv_score_value)
        recyclerView = findViewById(R.id.recycler_cards)
        highScore = intent.getIntExtra(MainActivity.EXTRA_HIGH_SCORE, 0)

        // 判断横屏还是竖屏，设置不同的列数和牌数
        val orientation = resources.configuration.orientation
        val spanCount: Int
        if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏：4列，24张牌
            spanCount = 4
            cardCount = 24
        } else {
            // 竖屏：3列，24张牌（多一点才能滚动）
            spanCount = 3
            cardCount = 24
        }

        // 设置RecyclerView的布局管理器
        recyclerView.layoutManager = GridLayoutManager(this, spanCount)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true

        // 先创建一个空的Adapter，后面再更新数据
        adapter = CardAdapter(emptyList()) { position ->
            game.chooseCardAtIndex(position)
            updateUI()
            checkGameEnd()
        }
        recyclerView.adapter = adapter

        // 开始新游戏
        startNewGame()

        // 重置游戏按钮
        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            startNewGame()
        }

        // 退出按钮
        findViewById<Button>(R.id.btn_exit).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(MainActivity.EXTRA_HIGH_SCORE, highScore)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // 开始新游戏
    private fun startNewGame() {
        game = CardMatchingGame(cardCount)
        updateUI()
    }

    // 更新界面显示
    private fun updateUI() {
        // 获取所有牌的列表
        val cards = mutableListOf<Card>()
        for (i in 0 until cardCount) {
            cards.add(game.cardAtIndex(i))
        }

        // 更新适配器数据，不重新创建Adapter，保留滚动位置
        adapter.updateData(cards)

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
        for (i in 0 until cardCount) {
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