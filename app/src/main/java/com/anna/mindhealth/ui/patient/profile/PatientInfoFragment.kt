package com.anna.mindhealth.ui.patient.profile

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
import com.anna.mindhealth.base.Utility
import com.anna.mindhealth.base.Utility.PATIENT_ROLE
import com.anna.mindhealth.base.Utility.setEditTextValues
import com.anna.mindhealth.base.Utility.setImageViewResource
import com.anna.mindhealth.data.`interface`.OnImageReceivedListener
import com.anna.mindhealth.data.model.Patient
import com.anna.mindhealth.databinding.FragmentPersonalInfoBinding
import com.anna.mindhealth.dialog.SetAvatarDialog
import com.anna.mindhealth.ui.therapist.profile.TherapistInfoFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PatientInfoFragment: Fragment(), OnImageReceivedListener {
    private var _binding: FragmentPersonalInfoBinding ?= null
    private lateinit var profileViewModel: ProfileViewModel
    private var storagePermission: Boolean = false
    private val setAvatarDialogFragment by lazy {
        SetAvatarDialog()
    }

    private val binding get() = _binding!!

    private val requestStoragePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        Log.d(TherapistInfoFragment.TAG, "RequestStoragePermission")
        permissions.entries.forEach { entry ->
            if (entry.value == true) {
                Log.d(TherapistInfoFragment.TAG, "User has allowed permission to access ${entry.key}")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setPatientInfo()
        initializeDialog()
        initializeButton()
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

    private fun initializeButton(){
        binding.btnUpdatePersonalInfo.setOnClickListener {
            val name = "${binding.edtInputFirstName.editText?.text} ${binding.edtInputLastName.editText?.text}"
            val phoneNo = "${binding.edtInputPhoneNo.editText?.text}"

            val avatar = if (binding.imvAvatar.drawable is VectorDrawable){
                binding.imvAvatar.drawToBitmap()
            } else {
                (binding.imvAvatar.drawable as BitmapDrawable).bitmap
            }

            val patient = Patient(id = Firebase.auth.currentUser!!.uid ,name = name, phone_no = phoneNo)

            profileViewModel.updatePersonalInfo(patient, PATIENT_ROLE, avatar)
        }
    }

    private fun initializeDialog(){
        binding.txvUploadPhoto.setOnClickListener {
            when(storagePermission){
                true -> {
                    setAvatarDialogFragment.show(childFragmentManager, SetAvatarDialog.TAG)
                }
                else -> verifyStoragePermission()
            }
        }
    }

    private fun setPatientInfo(){
        profileViewModel.patientReference?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                val patient = task.result.toObject<Patient>()!!
                val patientName = patient.name.split(" ").toList()

                if (patientName.size < 2){
                    binding.edtInputFirstName.editText?.let { setEditTextValues(it, patientName[0]) }
                } else {
                    binding.edtInputFirstName.editText?.let { setEditTextValues(it, patientName[0]) }
                    binding.edtInputLastName.editText?.let { setEditTextValues(it, patientName[1]) }
                }

                Glide.with(requireContext())
                    .asBitmap().load(Uri.parse(patient.avatar))
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.imvAvatar)

                binding.edtInputEmail.editText?.let { setEditTextValues(it, patient.email) }
                binding.edtInputPhoneNo.editText?.let { setEditTextValues(it, patient.phone_no) }
            } else {
                Log.d(TAG, "Unable to fetch data", task.exception)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getImageUri(uri: Uri) {
        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        }

        setImageViewResource(binding.imvAvatar, bitmap)
        setAvatarDialogFragment.dismiss()
    }

    override fun getImageBitmap(bitmap: Bitmap) {
        setImageViewResource(binding.imvAvatar, bitmap)
        setAvatarDialogFragment.dismiss()
    }

    companion object{
        val TAG = PatientInfoFragment::class.simpleName
    }
}