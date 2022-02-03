package com.anna.mindhealth.ui.patient.therapist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anna.mindhealth.data.model.Therapist

class TherapistListRecyclerAdapter : RecyclerView.Adapter<TherapistListViewHolder>() {
    private var therapistList: List<Therapist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TherapistListViewHolder {
        return TherapistListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TherapistListViewHolder, position: Int) {
        holder.apply {
            bind(therapistList[position])
        }
    }

    override fun getItemCount(): Int = therapistList.size

    fun setTherapistList(therapists: List<Therapist>){
        therapistList = therapists
        notifyDataSetChanged()
    }
}