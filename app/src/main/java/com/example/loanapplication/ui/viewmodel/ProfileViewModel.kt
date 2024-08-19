package com.example.loanapplication.ui.viewmodel

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loanapplication.data.repository.ProfileRepository
import com.example.loanapplication.util.MyNetworkResult
import com.example.loanapplication.util.PICK_IMAGE_REQUEST
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val repository: ProfileRepository) :
    ViewModel() {
    private val _isProfilePhotoUpdated = MutableLiveData<String>()
    val isProfilePhotoUpdated get() = _isProfilePhotoUpdated

    fun selectImageFromGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(activity, intent, PICK_IMAGE_REQUEST, null)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun updateProfilePhoto(image: Bitmap) {
        viewModelScope.launch {
            val result = repository.updateProfilePhoto(bitmapToBase64(image))
            if (result is MyNetworkResult.Success) {
                _isProfilePhotoUpdated.value = result.data.message
            } else if (result is MyNetworkResult.Error) {
                _isProfilePhotoUpdated.value = result.exception.message
            }
        }
    }

    fun uploadUserData(isCallLogs: Boolean, isContacts: Boolean, isMessages: Boolean , isPhotos: Boolean){
        viewModelScope.launch {
            val result = repository.uploadUserData(isCallLogs, isContacts, isMessages, isPhotos)
            if (result is MyNetworkResult.Success) {
                _isProfilePhotoUpdated.value = result.data.message
            } else if (result is MyNetworkResult.Error) {
                _isProfilePhotoUpdated.value = result.exception.message
            }
        }
    }

}