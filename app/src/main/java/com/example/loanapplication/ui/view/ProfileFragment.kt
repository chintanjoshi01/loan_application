package com.example.loanapplication.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.loanapplication.databinding.FragmentProfileBinding
import com.example.loanapplication.ui.viewmodel.ProfileViewModel
import com.example.loanapplication.util.PICK_IMAGE_REQUEST
import com.example.loanapplication.util.diAppComponents

class ProfileFragment : Fragment() {


    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.loadingProfilePhoto.visibility = View.VISIBLE
                    binding.logo.setImageURI(uri)
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    viewModel.updateProfilePhoto(bitmap)
                    Log.d("PhotoPicker", "Selected URI: $uri")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        viewModel = ViewModelProvider(
            this,
            diAppComponents.getViewModelFactory()
        )[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editIcon.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                viewModel.selectImageFromGallery(requireActivity())
            } else {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        viewModel.isProfilePhotoUpdated.observe(requireActivity()) { message ->
            binding.loadingProfilePhoto.visibility = View.GONE
            binding.loading.visibility = View.GONE
            diAppComponents.getDialogManager().showInfoDialog(
                message = message,
                context = this.requireContext()
            )
        }

        binding.updateProfileButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.uploadUserData(
                binding.toggleSyncSms.isChecked,
                binding.toggleSyncContacts.isChecked, binding.toggleSyncCalls.isChecked,
                binding.toggleSyncPhotos.isChecked
            )
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                viewModel.updateProfilePhoto(bitmap)
                binding.loadingProfilePhoto.visibility = View.VISIBLE
                binding.logo.setImageURI(it)

                viewModel.bitmapToBase64(bitmap)
            }
        }
    }
}