package com.example.desafio_firebase.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.desafio_firebase.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var name: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var repeatPassword: TextInputEditText
    private lateinit var layoutName: TextInputLayout
    private lateinit var layoutEmail: TextInputLayout
    private lateinit var layoutPassword: TextInputLayout
    private lateinit var layoutRepeatPassword: TextInputLayout
    private lateinit var btnCreateAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        name = findViewById(R.id.etSignUpName)
        email = findViewById(R.id.etSignUpEmail)
        password = findViewById(R.id.etSignUpPassword)
        repeatPassword = findViewById(R.id.etSignUpRepeatPassword)
        layoutName = findViewById(R.id.signUpName)
        layoutEmail = findViewById(R.id.signUpEmail)
        layoutPassword = findViewById(R.id.signUpPassword)
        layoutRepeatPassword = findViewById(R.id.signUpRepeatPassword)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)


        btnCreateAccount.setOnClickListener() {
            if (checkFields()) {
                createAccount(email.text.toString(),password.text.toString())
            }
        }

    }

    private fun checkFields(): Boolean {
        var response = true

        if (email.text.isNullOrBlank()) {
            layoutEmail.error = "Please type your e-mail"
            response = false
        }

        if (name.text.isNullOrBlank()) {
            layoutName.error = "Please type your Name"
            response = false
        }

        if (password.text.isNullOrBlank()) {
            layoutPassword.error = "Password Required"
            response = false
        } else if (password.text!!.length < 8) {
            layoutPassword.error = "Password must be at least 8 characters long"
            response = false
        }

        if (repeatPassword.text.isNullOrBlank()) {
            layoutRepeatPassword.error = "Please repeat your password"
            response = false
        } else if (password.text.toString() != repeatPassword.text.toString()) {
            layoutRepeatPassword.error = "Passwords do not match"
            response = false
        }

        return response
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "User created successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }

    }
}