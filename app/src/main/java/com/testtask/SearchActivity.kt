package com.testtask

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.iremember.storage.SharedPrefManager
import com.testtask.models.Avatars
import com.testtask.models.CameraPreview
import kotlinx.android.synthetic.main.activity_search.*

@Suppress("DEPRECATION", "PrivatePropertyName", "SpellCheckingInspection")
class SearchActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraPreview: CameraPreview? = null
    private var msurfaceHolder: SurfaceHolder? = null
    private var cameraCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val intent = getIntent().getSerializableExtra("avatar")
        if (intent != null)
            setUI(intent as Avatars)
        turn_camera_btn.setOnClickListener { setCamera() }

    }

    fun setUI(avatars: Avatars) {
        Glide.with(this).load(avatars.url).error(ColorDrawable(Color.RED)).circleCrop()
            .into(profile_image)

    }

    fun setCamera() {
        val camBoolean = SharedPrefManager.getInstance(this).getSettings("cam")
        if (checkAndRequestPermissions() && !cameraCreated && camBoolean) createCam()
    }

    fun createCam() {
        msurfaceHolder = surfaceView!!.holder
        mCamera = getCameraInstance()


        mCameraPreview = mCamera?.let {
            CameraPreview(this, it, surfaceView)
        }
        surfaceView.visibility = View.VISIBLE
        cameraCreated = true
        turn_camera_btn.visibility = View.GONE
        SharedPrefManager.getInstance(this).saveSettings(true,"cam")


    }

    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open(isFrontCameraExisted())
        } catch (e: Exception) {
            null
        }
    }


    private fun isFrontCameraExisted(): Int {
        var cameraId = -1
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i
                break
            }
        }
        return cameraId
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
                        setCamera()
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
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
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
