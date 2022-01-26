package com.anna.mindhealth.ui.therapist.profile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anna.mindhealth.R
import com.anna.mindhealth.base.Utility.THERAPIST_ROLE
import com.anna.mindhealth.data.`interface`.OnImageReceivedListener
import com.anna.mindhealth.data.model.Therapist
import com.anna.mindhealth.databinding.FragmentPersonalInfoTherapistBinding
import com.anna.mindhealth.dialog.SetAvatarDialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class TherapistInfoFragment: Fragment(), OnImageReceivedListener {
    private var _binding: FragmentPersonalInfoTherapistBinding ?= null
    private val binding get() = _binding!!

    private lateinit var therapyProfileViewModel: TherapyProfileViewModel
    private var storagePermission: Boolean = false

    private val requestStoragePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {  permissions ->
        Log.d(TAG, "RequestStoragePermission")
        permissions.entries.forEach { entry ->
            if (entry.value == true) {
                Log.d(TAG, "User has allowed permission to access ${entry.key}")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoTherapistBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun verifyStoragePermission() {
        Log.d(TAG, "verifyPermissions: asking user for permissions.")
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        if (ContextCompat.checkSelfPermission(binding.root.context, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(binding.root.context, permissions[1]) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(binding.root.context, permissions[2]) == PackageManager.PERMISSION_GRANTED
        ) {
            storagePermission = true
        } else {
            // Request for permissions
            requestStoragePermission.launch(permissions)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        therapyProfileViewModel = ViewModelProvider(this).get(TherapyProfileViewModel::class.java)

        setTherapistInfo()
        initializeAvatarDisplay()
        initializeButton()
    }

    private fun setTherapistInfo(){
        therapyProfileViewModel.therapistReference?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                val therapist = task.result.toObject<Therapist>()!!
                val therapistName = therapist.name.split(" ").toList()

                if (therapistName.size < 2){
                    binding.edtInputFirstName.editText?.setText(therapistName[0])
                } else {
                    binding.edtInputFirstName.editText?.setText(therapistName[0])
                    binding.edtInputLastName.editText?.setText(therapistName[1])
                }

                Glide.with(requireContext())
                    .asBitmap().load(therapist.avatar)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.imvAvatar)

                binding.edtInputEmail.editText?.setText(therapist.email)
                binding.edtInputPhoneNo.editText?.setText(therapist.phone_no)
                binding.edtInputRate.editText?.setText(therapist.rate.toString())
            } else {
                Log.d(TAG, "Unable to fetch data", task.exception)
            }
        }
    }

    private fun initializeButton(){
        binding.btnUpdatePersonalInfo.setOnClickListener {
            val name = "${binding.edtInputFirstName.editText?.text} ${binding.edtInputLastName.editText?.text}"
            val phoneNo = "${binding.edtInputPhoneNo.editText?.text}"
            val rate = "${binding.edtInputRate.editText?.text}".toDouble()

            val avatar = if (binding.imvAvatar.drawable is VectorDrawable){
                binding.imvAvatar.drawToBitmap()
            } else {
                (binding.imvAvatar.drawable as BitmapDrawable).bitmap
            }


            val therapist = Therapist(id = Firebase.auth.currentUser!!.uid ,name = name, phone_no = phoneNo, rate = rate)

            therapyProfileViewModel.updatePersonalInfo(therapist, THERAPIST_ROLE, avatar)

        }
    }

    private fun initializeAvatarDisplay(){
        binding.txvUploadPhoto.setOnClickListener {
            when(storagePermission){
                true -> {
                    SetAvatarDialogFragment().show(childFragmentManager, SetAvatarDialogFragment.TAG)
                }
                else -> verifyStoragePermission()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        val TAG = TherapistInfoFragment::class.simpleName
    }

    override fun getImageUri(uri: Uri) {
        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        }

        binding.imvAvatar.setImageBitmap(bitmap)
    }

    override fun getImageBitmap(bitmap: Bitmap) {
        binding.imvAvatar.setImageBitmap(bitmap)
    }
}