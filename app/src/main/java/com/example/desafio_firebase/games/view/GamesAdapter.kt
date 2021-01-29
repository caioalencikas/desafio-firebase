package com.example.desafio_firebase.games.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafio_firebase.R
import com.example.desafio_firebase.games.model.GameModel

class GamesAdapter (private var games: MutableList<GameModel>, private val listener: (GameModel) -> Unit): RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = games[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { listener(item) }
    }

    override fun getItemCount() = games.size

    class GameViewHolder (view: View): RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.txtCardName)
        private val year = view.findViewById<TextView>(R.id.txtCardYear)
        private val image = view.findViewById<ImageView>(R.id.imgCardGame)

        fun bind (game: GameModel) {
            name.text = game.name
            year.text = game.year

            Glide.with(itemView).load(game.image_URI).into(image)
        }
    }
}