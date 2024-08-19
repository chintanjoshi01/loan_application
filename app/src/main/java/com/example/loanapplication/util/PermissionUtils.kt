package com.example.loanapplication.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import javax.inject.Inject

class PermissionUtils @Inject constructor(context: Context) {

    private var permission: List<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            listOf(
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    private val permissionsName = listOf("Call Log", "SMS", "Contacts", "Storage")

    fun grantPermission(context: Context) {

        diAppComponents.getDialogManager().showInfoDialog(
            title = "Grant Permission",
            message = "Please grant the following permissions for optimal app functionality",
            context = context,
            positiveButtonText = "Grant",
            setCancelable = false,
            onPositiveClick = {
                grantPermissions(context)

            }
        )
    }

    private fun grantPermissions(context: Context) {
        Dexter.withContext(context)
            .withPermissions(
                permission
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(
                            context,
                            "All Permissions Granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        report.deniedPermissionResponses

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(
                    context,
                    "Something Wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }.onSameThread().check()
    }

}