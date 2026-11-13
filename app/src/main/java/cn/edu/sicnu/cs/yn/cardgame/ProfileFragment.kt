package cn.edu.sicnu.cs.yn.cardgame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ProfileFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvStudentId: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvHobby: TextView

    private var profile = ProfileData()

    private val editInfoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            profile.name = data.getStringExtra(MainActivity.EXTRA_NAME) ?: profile.name
            profile.gender = data.getStringExtra(MainActivity.EXTRA_GENDER) ?: profile.gender
            profile.studentId =
                data.getStringExtra(MainActivity.EXTRA_STUDENT_ID) ?: profile.studentId
            profile.phone = data.getStringExtra(MainActivity.EXTRA_PHONE) ?: profile.phone
            profile.email = data.getStringExtra(MainActivity.EXTRA_EMAIL) ?: profile.email
            profile.hobby = data.getStringExtra(MainActivity.EXTRA_HOBBY) ?: profile.hobby

            renderProfile()
            saveProfile()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tv_name)
        tvGender = view.findViewById(R.id.tv_gender)
        tvStudentId = view.findViewById(R.id.tv_student_id)
        tvPhone = view.findViewById(R.id.tv_phone)
        tvEmail = view.findViewById(R.id.tv_email)
        tvHobby = view.findViewById(R.id.tv_hobby)

        profile = loadProfile() ?: ProfileData().also { saveProfile(it) }
        renderProfile()

        view.findViewById<Button>(R.id.btn_edit_info).setOnClickListener {
            openEditInfo()
        }
    }

    private fun renderProfile() {
        tvName.text = profile.name
        tvGender.text = profile.gender
        tvStudentId.text = profile.studentId
        tvPhone.text = profile.phone
        tvEmail.text = profile.email
        tvHobby.text = profile.hobby
    }

    private fun openEditInfo() {
        val intent = Intent(requireContext(), EditInfoActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_NAME, profile.name)
            putExtra(MainActivity.EXTRA_GENDER, profile.gender)
            putExtra(MainActivity.EXTRA_STUDENT_ID, profile.studentId)
            putExtra(MainActivity.EXTRA_PHONE, profile.phone)
            putExtra(MainActivity.EXTRA_EMAIL, profile.email)
            putExtra(MainActivity.EXTRA_HOBBY, profile.hobby)
        }
        editInfoLauncher.launch(intent)
    }

    private fun saveProfile(data: ProfileData = profile) {
        try {
            requireContext()
                .openFileOutput(PROFILE_FILE_NAME, Context.MODE_PRIVATE)
                .use { output ->
                    ObjectOutputStream(output).use { objectOutput ->
                        objectOutput.writeObject(data)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadProfile(): ProfileData? {
        return try {
            requireContext()
                .openFileInput(PROFILE_FILE_NAME)
                .use { input ->
                    ObjectInputStream(input).use { objectInput ->
                        objectInput.readObject() as? ProfileData
                    }
                }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val PROFILE_FILE_NAME = "profile_data.dat"
    }
}
