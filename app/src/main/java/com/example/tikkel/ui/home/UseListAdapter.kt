package com.example.tikkel.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tikkel.databinding.PointItemBinding

class UseListAdapter(val score: MutableList<UseScore>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        class ScoreViewHolder(val binding: PointItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var files = score
    private  var isEmpty = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScoreViewHolder(PointItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ScoreViewHolder).binding
        val current= files?.get(position)
        binding.pointDate.text = current?.ScoreDate
        binding.pointContent.text = current?.ScoreContent
        binding.pointValue.text = current?.ScoreValue
    }


    override fun getItemCount(): Int {
        return files.size
    }
}