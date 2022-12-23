package com.evg_ivanoff.roomirovanie.presenter

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.evg_ivanoff.roomirovanie.MainView
import com.evg_ivanoff.roomirovanie.R
import com.evg_ivanoff.roomirovanie.data.UserDataBase
import com.evg_ivanoff.roomirovanie.databinding.ActivityMainBinding
import com.evg_ivanoff.roomirovanie.domain.UserRoomDataBase
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val TAG = "TEST_TAG"
    private val scope = CoroutineScope(Dispatchers.Main)
    private val db by lazy {
        UserRoomDataBase.getDatabase(this)
    }
    private var user: UserDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mode = intent.getStringExtra("MODE")
        when (mode) {
            MainView.MODE_ADD -> {
                title = "Add new user"
            }
            MainView.MODE_EDIT -> {
                title = "Edit user"
                if (intent.hasExtra("User")) {
                    user = intent.getParcelableExtra<UserDataBase>("User")
                    binding.etName.setText(user?.name)
                    binding.etAge.setText(user?.age)
                    binding.tietEmail.setText(user?.email)
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            scope.launch {
                db.userDao().deleteAllUsers()
            }
        }

        binding.btnSave.setOnClickListener {
            if (checkAllCorrectData()) {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                if (mode == MainView.MODE_ADD) {
                    scope.launch {
                        db.userDao().addUser(getUserData(MainView.MODE_ADD))
                    }
                    finish()
                }
                if (mode == MainView.MODE_EDIT) {
                    scope.launch {
                        db.userDao().updateUser(getUserData(MainView.MODE_EDIT))
                    }
                    finish()
                }
            } else {
                Toast.makeText(this, "Please, check errors, my friend", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAllCorrectData(): Boolean {
        val term1 = checkCorrectData(binding.etName, "name")
        val term2 = checkCorrectData(binding.etAge, "age")
        val term3 = checkCorrectData(binding.tietEmail, "email")
        return term1 && term2 && term3
    }

    private fun getUserData(mode: String): UserDataBase {
        val userName = binding.etName.text.toString()
        val userAge = binding.etAge.text.toString()
        val userEMail = binding.tietEmail.text.toString()
        return if (mode == MainView.MODE_ADD)
            UserDataBase(null, userName, userAge, userEMail)
        else
            UserDataBase(user?.id, userName, userAge, userEMail)
    }

    private fun checkCorrectData(textView: EditText, checkType: String): Boolean {
        when (checkType) {
            "name" -> {
                val data = textView.text.toString()
                if (data.length in 0..2) {
                    showError(binding.tilName, getString(R.string.err_name_length))
                    return false
                } else {
                    hideError(binding.tilName)
                    return true
                }
            }
            "age" -> {
                val dataS = textView.text.toString()
                val data = if (dataS == "") 0 else dataS.toInt()
                if (dataS == "") {
                    showError(binding.tilAge, "error: empty value")
                    return false
                } else if (data == 0) {
                    showError(binding.tilAge, "error: incorrect value")
                    return false
                } else if (data !in 1..99) {
                    showError(binding.tilAge, "error: max limit is 99")
                    return false
                } else {
                    hideError(binding.tilAge)
                    return true
                }
            }
            "email" -> {
                val data = textView.text.toString()
                if (data.length in 0..5) {
                    showError(binding.tilEmail, "error: min 6 symbols")
                    return false
                } else if (!(data.contains('@'))) {
                    showError(binding.tilEmail, "error: must have '@'")
                    return false
                } else {
                    hideError(binding.tilEmail)
                    return true
                }
            }
            else -> return false
        }
    }

    private fun showError(textView: TextInputLayout, message: String) {
        textView.error = message
    }

    private fun hideError(textView: TextInputLayout) {
        textView.error = ""
    }

}