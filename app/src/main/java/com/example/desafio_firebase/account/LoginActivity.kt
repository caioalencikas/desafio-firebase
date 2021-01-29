package com.example.desafio_firebase.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio_firebase.games.view.MainActivity
import com.example.desafio_firebase.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var layoutEmail: TextInputLayout
    private lateinit var layoutPassword: TextInputLayout
    private lateinit var checkBox: CheckBox
    private lateinit var btnCreateAccountLogin: Button
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.etLoginEmail)
        password = findViewById(R.id.etLoginPassword)
        layoutEmail = findViewById(R.id.loginEmail)
        layoutPassword = findViewById(R.id.loginPassword)
        checkBox = findViewById(R.id.checkBoxRemember)
        btnCreateAccountLogin = findViewById(R.id.btnCreateAccountLogin)
        btnLogin = findViewById(R.id.btnLogin)

        btnCreateAccountLogin.setOnClickListener() {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener() {
            if (checkFields()) {
                login(email.text.toString(), password.text.toString())
            }
        }
    }

    private fun checkFields(): Boolean {
        var response = true

        if (email.text.isNullOrBlank()) {
            layoutEmail.error = getString(R.string.email_fill)
            response = false
        }

        if (password.text.isNullOrBlank()) {
            layoutPassword.error = getString(R.string.password_fill)
            response = false
        }

        return response
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    baseContext, "Email ou senha incorretos.",
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText(
                    baseContext, email,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}