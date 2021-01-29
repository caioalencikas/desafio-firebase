package com.example.desafio_firebase.games.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.desafio_firebase.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GameDetailsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var userStorageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_details)

        val txtName = findViewById<TextView>(R.id.txtGameDetailsName)
        val txtName2 = findViewById<TextView>(R.id.txtGameDetailsName2)
        val txtYear = findViewById<TextView>(R.id.txtGameDetailsYear)
        val txtDescription = findViewById<TextView>(R.id.txtGameDetailsDescription)
        val imgImage = findViewById<ImageView>(R.id.imgGameDetails)

        val name = intent.getStringExtra("Name")
        val year = intent.getStringExtra("Year")
        val description = intent.getStringExtra("Description")
        val image = intent.getStringExtra("ImageURI")

        txtName.text = name
        txtName2.text = name
        txtYear.text = year
        txtDescription.text = description

        Glide.with(this).load(image).into(imgImage)

        findViewById<ImageView>(R.id.imgGameDetailsBack).setOnClickListener() {
            finish()
        }

        findViewById<FloatingActionButton>(R.id.btnGameEdit).setOnClickListener() {
            intent = Intent(this, GameCreationActivity::class.java)
            intent.putExtra("Name", name)
            intent.putExtra("Description", description)
            intent.putExtra("Year", year)
            intent.putExtra("ImageURI", image)
            startActivity(intent)
        }
    }
}