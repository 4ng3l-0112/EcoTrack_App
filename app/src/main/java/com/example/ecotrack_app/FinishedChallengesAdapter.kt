package com.example.ecotrack_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FinishedChallengesAdapter(private var challenges: List<FinishedChallenge>) :
    RecyclerView.Adapter<FinishedChallengesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val challengeName: TextView = view.findViewById(R.id.challengeNameTextView)
        val completionDate: TextView = view.findViewById(R.id.completionDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_finished_challenge, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.challengeName.text = challenge.name
        holder.completionDate.text = "Completed on ${challenge.completionDate}"
    }

    override fun getItemCount() = challenges.size

    fun updateChallenges(newChallenges: List<FinishedChallenge>) {
        challenges = newChallenges
        notifyDataSetChanged()
    }
}
