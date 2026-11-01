package cn.edu.sicnu.cs.yn.cardgame

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EditInfoActivity : AppCompatActivity() {

    private lateinit var ivAvatar: ImageView
    private lateinit var etName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var cbTravel: CheckBox
    private lateinit var cbReading: CheckBox
    private lateinit var cbGame: CheckBox
    private lateinit var cbShopping: CheckBox

    companion object {
        const val REQUEST_CAMERA = 300
        const val REQUEST_GALLERY = 400
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)

        // 初始化控件
        ivAvatar = findViewById(R.id.iv_avatar_edit)
        etName = findViewById(R.id.et_name)
        etStudentId = findViewById(R.id.et_student_id)
        etPhone = findViewById(R.id.et_phone)
        etEmail = findViewById(R.id.et_email)
        rgGender = findViewById(R.id.rg_gender)
        cbTravel = findViewById(R.id.cb_travel)
        cbReading = findViewById(R.id.cb_reading)
        cbGame = findViewById(R.id.cb_game)
        cbShopping = findViewById(R.id.cb_shopping)

        val btnSave: Button = findViewById(R.id.btn_save)
        val btnCancel: Button = findViewById(R.id.btn_cancel)

        // 接收从主界面传过来的旧数据，回显到输入框
        val oldName = intent.getStringExtra(MainActivity.EXTRA_NAME)
        val oldGender = intent.getStringExtra(MainActivity.EXTRA_GENDER)
        val oldStudentId = intent.getStringExtra(MainActivity.EXTRA_STUDENT_ID)
        val oldPhone = intent.getStringExtra(MainActivity.EXTRA_PHONE)
        val oldEmail = intent.getStringExtra(MainActivity.EXTRA_EMAIL)
        val oldHobby = intent.getStringExtra(MainActivity.EXTRA_HOBBY)

        etName.setText(oldName)
        etStudentId.setText(oldStudentId)
        etPhone.setText(oldPhone)
        etEmail.setText(oldEmail)

        // 回显性别
        if (oldGender == "女") {
            rgGender.check(R.id.rb_female)
        } else {
            rgGender.check(R.id.rb_male)
        }

        // 回显爱好
        oldHobby?.let {
            cbTravel.isChecked = it.contains("旅游")
            cbReading.isChecked = it.contains("阅读")
            cbGame.isChecked = it.contains("游戏")
            cbShopping.isChecked = it.contains("购物")
        }

        // 点击头像弹出选择：拍照 或 相册
        ivAvatar.setOnClickListener {
            showAvatarDialog()
        }

        // 保存按钮点击
        btnSave.setOnClickListener {
            // 收集所有输入
            val name = etName.text.toString().trim()
            val studentId = etStudentId.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val email = etEmail.text.toString().trim()

            // 获取性别
            val selectedGenderId = rgGender.checkedRadioButtonId
            val gender = if (selectedGenderId == R.id.rb_female) "女" else "男"

            // 收集爱好
            val hobbies = mutableListOf<String>()
            if (cbTravel.isChecked) hobbies.add("旅游")
            if (cbReading.isChecked) hobbies.add("阅读")
            if (cbGame.isChecked) hobbies.add("游戏")
            if (cbShopping.isChecked) hobbies.add("购物")
            val hobbyStr = hobbies.joinToString("、")

            // 把数据返回给主界面
            val resultIntent = Intent()
            resultIntent.putExtra(MainActivity.EXTRA_NAME, name)
            resultIntent.putExtra(MainActivity.EXTRA_GENDER, gender)
            resultIntent.putExtra(MainActivity.EXTRA_STUDENT_ID, studentId)
            resultIntent.putExtra(MainActivity.EXTRA_PHONE, phone)
            resultIntent.putExtra(MainActivity.EXTRA_EMAIL, email)
            resultIntent.putExtra(MainActivity.EXTRA_HOBBY, hobbyStr)
            setResult(RESULT_OK, resultIntent)
            finish() // 关闭当前页面，回到主界面
        }

        // 取消按钮点击
        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    // 弹出选择头像方式的对话框
    private fun showAvatarDialog() {
        val options = arrayOf("拍照", "从相册选择")
        AlertDialog.Builder(this)
            .setTitle("选择头像")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    // 打开相机拍照
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    // 打开相册选择
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    // 接收拍照和相册选择的结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    // 拍照返回的缩略图
                    val bitmap = data?.extras?.get("data") as Bitmap
                    ivAvatar.setImageBitmap(bitmap)
                    Toast.makeText(this, "拍照成功", Toast.LENGTH_SHORT).show()
                }
                REQUEST_GALLERY -> {
                    // 从相册选的图片
                    val uri = data?.data
                    ivAvatar.setImageURI(uri)
                    Toast.makeText(this, "相册选择成功", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
