package cn.edu.sicnu.cs.yn.cardgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_STUDENT_ID = "extra_student_id"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_HOBBY = "extra_hobby"
        const val EXTRA_HIGH_SCORE = "extra_high_score"

        const val REQUEST_EDIT = 100
        const val REQUEST_GAME = 200
    }

    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvHobby: TextView
    private lateinit var tvHighScore: TextView

    private var highScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化控件
        tvName = findViewById(R.id.tv_name)
        tvGender = findViewById(R.id.tv_gender)
        tvStudentId = findViewById(R.id.tv_student_id)
        tvPhone = findViewById(R.id.tv_phone)
        tvEmail = findViewById(R.id.tv_email)
        tvHobby = findViewById(R.id.tv_hobby)
        tvHighScore = findViewById(R.id.tv_high_score)

        val btnEditInfo: Button = findViewById(R.id.btn_edit_info)
        val btnStartGame: Button = findViewById(R.id.btn_start_game)

        // 点击"修改个人信息"按钮
        btnEditInfo.setOnClickListener {
            val intent = Intent(this, EditInfoActivity::class.java)
            // 把当前信息传过去，方便修改时回显
            intent.putExtra(EXTRA_NAME, tvName.text.toString())
            intent.putExtra(EXTRA_GENDER, tvGender.text.toString())
            intent.putExtra(EXTRA_STUDENT_ID, tvStudentId.text.toString())
            intent.putExtra(EXTRA_PHONE, tvPhone.text.toString())
            intent.putExtra(EXTRA_EMAIL, tvEmail.text.toString())
            intent.putExtra(EXTRA_HOBBY, tvHobby.text.toString())
            startActivityForResult(intent, REQUEST_EDIT)
        }

        // 点击"翻牌游戏"按钮
        btnStartGame.setOnClickListener {
            val intent = Intent(this, CardGameActivity::class.java)
            intent.putExtra(EXTRA_HIGH_SCORE, highScore)
            startActivityForResult(intent, REQUEST_GAME)
        }
    }


    // 创建菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // 菜单点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about -> {
                // 弹出关于对话框
                AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_about_title)
                    .setMessage(R.string.dialog_about_message)
                    .setPositiveButton(R.string.btn_ok, null)
                    .show()
                return true
            }
            R.id.menu_exit -> {
                // 退出APP
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 接收从其他页面返回的数据
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_EDIT -> {
                    // 从信息修改页返回，更新个人信息
                    tvName.text = data.getStringExtra(EXTRA_NAME)
                    tvGender.text = data.getStringExtra(EXTRA_GENDER)
                    tvStudentId.text = data.getStringExtra(EXTRA_STUDENT_ID)
                    tvPhone.text = data.getStringExtra(EXTRA_PHONE)
                    tvEmail.text = data.getStringExtra(EXTRA_EMAIL)
                    tvHobby.text = data.getStringExtra(EXTRA_HOBBY)
                }
                REQUEST_GAME -> {
                    // 从游戏页返回，更新最高分
                    val score = data.getIntExtra(EXTRA_HIGH_SCORE, 0)
                    if (score > highScore) {
                        highScore = score
                        tvHighScore.text = highScore.toString()
                    }
                }
            }
        }
    }
}