package com.anna.mindhealth.ui.patient.therapist

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.setImageViewResource
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.databinding.ItemTherapistListBinding
import com.anna.mindhealth.ui.therapist.profile.ProfileViewFragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class TherapistListViewHolder(private val binding: ItemTherapistListBinding): RecyclerView.ViewHolder(binding.root) {
    init {
//        binding.root.setOnLongClickListener {}
        binding.root.setOnClickListener {}
    }

    fun bind(therapist: Therapist){
        therapist.apply {
            binding.txvTherapistName.text = this.name
            binding.txvTherapistShortDesc.text = this.profile.short_desc

            Glide.with(binding.root.context)
                .asBitmap().load(Uri.parse(therapist.avatar))
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(binding.imvTherapistAvatar)

            setCountryImageView(binding.imvTherapistCountry, therapist.profile.country)
        }
    }

    private fun setCountryImageView(imageView: ShapeableImageView, textValue: String){
        when(textValue){
            ProfileViewFragment.COUNTRY_KENYA -> setImageViewResource(imageView, R.drawable.kenya)
            ProfileViewFragment.COUNTRY_UGANDA -> setImageViewResource(imageView, R.drawable.uganda)
            ProfileViewFragment.COUNTRY_TANZANIA -> setImageViewResource(imageView, R.drawable.tanzania)
        }
    }

    companion object{
        fun create(parent: ViewGroup): TherapistListViewHolder{
            val binding = ItemTherapistListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TherapistListViewHolder(binding)
        }
    }
}