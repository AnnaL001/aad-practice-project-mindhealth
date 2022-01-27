package com.anna.mindhealth.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.anna.mindhealth.data.`interface`.OnImageReceivedListener
import com.anna.mindhealth.databinding.DialogSetAvatarBinding

class SetAvatarDialog: DialogFragment() {
    private var _binding: DialogSetAvatarBinding ?= null
    private lateinit var imageReceived: OnImageReceivedListener

    private val binding get() = _binding!!


    private val openGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if (uri != null){
            imageReceived.getImageUri(uri)
        }
    }

    private val openCameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){ bitmap ->
        Log.d(TAG, "onActivityResult: done taking a photo.")
        if (bitmap != null){
            imageReceived.getImageBitmap(bitmap)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogSetAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeOptions()
    }

    private fun initializeOptions(){
        binding.txvOpenGallery.setOnClickListener {
            Log.d(TAG, "onClick: accessing phones memory.")
            openGalleryLauncher.launch("image/*")
        }

        binding.txvOpenCamera.setOnClickListener {
            Log.d(TAG, "onClick: starting camera...")
            openCameraLauncher.launch(null)
        }
    }

    override fun onAttach(context: Context) {
        try{
            imageReceived = parentFragment as OnImageReceivedListener
        } catch (e: ClassCastException){
            Log.e(TAG, "onAttach: ClassCastException", e.cause)
        }
        super.onAttach(context)
    }

    companion object{
        val TAG = SetAvatarDialog::class.simpleName
    }
}