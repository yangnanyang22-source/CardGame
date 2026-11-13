package cn.edu.sicnu.cs.yn.cardgame

import java.io.Serializable

data class ProfileData(
    var name: String = "杨楠",
    var gender: String = "男",
    var studentId: String = "2024110539",
    var phone: String = "13800138000",
    var email: String = "yangnanyang22@example.com",
    var hobby: String = "旅游、阅读"
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
