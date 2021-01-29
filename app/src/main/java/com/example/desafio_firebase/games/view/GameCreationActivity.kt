package com.example.desafio_firebase.games.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.desafio_firebase.R
import com.example.desafio_firebase.games.model.GameModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GameCreationActivity : AppCompatActivity() {

    private lateinit var txtName: TextInputEditText
    private lateinit var txtYear: TextInputEditText
    private lateinit var txtDescription: TextInputEditText
    private lateinit var gameNameField: TextInputLayout
    private lateinit var gameDateField: TextInputLayout
    private lateinit var gameDescriptionField: TextInputLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var btnSaveGame: MaterialButton
    private var imageFileReference: String = ""
    private var imageURI: Uri? = null
    private lateinit var imgAddPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_creation)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("uploads")
        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("users")

        txtName = findViewById(R.id.etGameCreateName)
        txtYear = findViewById(R.id.etGameCreateDate)
        txtDescription = findViewById(R.id.etGameCreateDescription)
        gameNameField = findViewById(R.id.layoutGameCreateName)
        gameDateField = findViewById(R.id.layoutGameCreateDate)
        gameDescriptionField = findViewById(R.id.layoutGameCreateDescription)
        btnSaveGame = findViewById(R.id.btnSaveGame)
        imgAddPhoto = findViewById(R.id.imgAddPhoto)

        val name = intent.getStringExtra("Name").toString()
        val year = intent.getStringExtra("Year").toString()
        val description = intent.getStringExtra("Description").toString()
        val image = intent.getStringExtra("ImageURI").toString()


        imgAddPhoto.setOnClickListener() {
            searchFile()
        }

        btnSaveGame.setOnClickListener() {
            if (checkFields()) {
                sendFile(storageRef)
            }
        }

        if (name != "null") {
            txtName.setText(name)
            txtYear.setText(year)
            txtDescription.setText(description)
            Glide.with(this).load(image).into(imgAddPhoto)
            imageURI = image.toUri()
        }
    }

    private fun checkFields(): Boolean {
        var response = true
        if (txtName.text.isNullOrBlank()) {
            gameNameField.error = getString(R.string.name_fill)
            response = false
        } else if (txtDescription.text.isNullOrBlank()) {
            gameDescriptionField.error = getString(R.string.description_fill)
            response = false
        } else if (txtYear.text.isNullOrBlank()) {
            gameDateField.error = getString(R.string.year_fill)
            response = false
        } else if (imgAddPhoto.drawable == null) {
            Toast.makeText(this, getString(R.string.image_fill), Toast.LENGTH_SHORT).show()
            response = false
        } else {
            response = true
        }
        return response
    }

    private fun sendFile(storageReference: StorageReference) {
        imageURI?.run {

            val extension =
                MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(this))
            val fileReference =
                storageReference.child(user.uid).child("${System.currentTimeMillis()}.${extension}")

            fileReference.putFile(this).addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener {
                    imageFileReference = it.toString()
                    saveGame(
                        databaseRef,
                        txtName.text.toString(),
                        txtYear.text.toString(),
                        txtDescription.text.toString(),
                        imageFileReference
                    )
                }
            }
                .addOnFailureListener {
                    Toast.makeText(
                        this@GameCreationActivity,
                        getString(R.string.image_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun searchFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CONTENT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTENT_REQUEST_CODE && resultCode == RESULT_OK) {
            imageURI = data?.data
            imgAddPhoto.setImageURI(imageURI)
        }
    }

    private fun saveGame(
        databaseRef: DatabaseReference,
        name: String,
        year: String,
        description: String,
        imageRef: String
    ) {
        val newGame = GameModel(name, year, description, imageRef)
        databaseRef.child(user.uid).child(name).setValue(newGame)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val CONTENT_REQUEST_CODE = 1
    }
}
