package com.example.desafio_firebase.games.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio_firebase.R
import com.example.desafio_firebase.games.model.GameModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference
    private lateinit var userDatabaseRef: DatabaseReference
    private lateinit var userStorageRef: StorageReference
    private lateinit var viewManager: GridLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: GamesAdapter
    private var gameList = mutableListOf<GameModel>()

    data class GameModelMain(
        val name: String = "",
        val description: String = "",
        val year: String = "",
        val image_URI: String = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        storageRef = storage.getReference("uploads")
        databaseRef = database.getReference("users")
        userDatabaseRef = databaseRef.child(user.uid)
        userStorageRef = storageRef.child(user.uid)
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAdd)

        viewManager = GridLayoutManager(this, 2)
        recyclerView = findViewById(R.id.rcyVwGames)
        viewAdapter = GamesAdapter(gameList) {
            val intent = Intent(this, GameDetailsActivity::class.java)
            intent.putExtra("Name", it.name)
            intent.putExtra("Year", it.year)
            intent.putExtra("Description", it.description)
            intent.putExtra("ImageURI", it.image_URI)
            startActivity(intent)
        }

        recyclerView.apply {
            setHasFixedSize(true)

            layoutManager = viewManager
            adapter = viewAdapter
        }

        val gameListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach() {
                    val data = it.getValue(GameModelMain::class.java)!!
                    gameList.add(GameModel(data.name, data.description, data.year, data.image_URI))
                }
                viewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", error.toException())
            }
        }

        userDatabaseRef.addValueEventListener(gameListener)

        btnAdd.setOnClickListener() {
            val intent = Intent(this, GameCreationActivity::class.java)
            startActivity(intent)
        }
    }
}