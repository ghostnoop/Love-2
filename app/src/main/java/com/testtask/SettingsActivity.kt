package com.testtask

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iremember.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    var switcher1: Boolean = true
    var switcher2: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setUI()
        controlSwitchs()
    }


    fun setUI() {
        with(SharedPrefManager.getInstance(this)) {
            cam_sw.isChecked = getSettings("cam")
            hd_sw.isChecked = getSettings("hd")
            men_sw.isChecked = getLooks("men")
            switcher1 = men_sw.isChecked
            women_sw.isChecked = getLooks("women")
            switcher2 = women_sw.isChecked
        }
    }

    fun controlSwitchs() {
        with(SharedPrefManager.getInstance(this)) {
            cam_sw.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    if (checkAndRequestPermissions()) {
                        saveSettings(isChecked, "cam")
                    }
                } else saveSettings(isChecked, "cam")
            }
            hd_sw.setOnCheckedChangeListener { _, isChecked ->
                saveSettings(isChecked, "hd")
            }
            men_sw.setOnClickListener {
                if (switcher1) {
                    men_sw.isChecked = false;switcher1 = false
                } else {
                    men_sw.isChecked = true;switcher1 = true
                }
                saveSettings(switcher1, "men")
            }
            women_sw.setOnClickListener {
                if (switcher2) {
                    women_sw.isChecked = false;switcher2 = false
                } else {
                    women_sw.isChecked = true;switcher2 = true

                }
                saveSettings(women_sw.isChecked, "women")

            }
        }
    }


    private fun checkAndRequestPermissions(): Boolean {
        val permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                if (grantResults.size > 0) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]
                    if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED) {
                        cam_sw.isChecked = true
                    } else {
                        Log.d("per", "Some permissions are not granted ask again ")
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            )
                        ) {
                            showDialogOK("Service Permissions are required for this app",
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            finish()
                                    }
                                })
                        } else {

                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                        }

                    }
                }
            }
        }

    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }

    private fun explain(msg: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(msg)
            .setPositiveButton("Yes") { paramDialogInterface, paramInt ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:com.example.parsaniahardik.kotlin_marshmallowpermission")
                    )
                )
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> finish() }
        dialog.show()
    }

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }
}
