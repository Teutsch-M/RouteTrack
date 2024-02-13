package com.example.routetrack.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.routetrack.R
import com.example.routetrack.utility.Constants
import com.example.routetrack.utility.TrackingUtility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddRefuelFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var imgView: ImageView
    private lateinit var addImage: Button
    private lateinit var addLiter: EditText
    private lateinit var addPrice: EditText
    private lateinit var addRefuel: Button
    private var capturedImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_refuel, container, false)

        view.apply {
            initializeView(this)
            registerListeners(this)
        }

        return view
    }


    private fun initializeView(view: View) {
        imgView = view.findViewById(R.id.addRefuelImage)
        addImage = view.findViewById(R.id.addRefuelImageButton)
        addLiter = view.findViewById(R.id.addLiter)
        addPrice = view.findViewById(R.id.addPrice)
        addRefuel = view.findViewById(R.id.addRefuelButton)
        capturedImageUri = createImageUri()
    }

    private fun registerListeners(view: View) {
        addImage.setOnClickListener {
            requestPermissions()
            takePictureLauncher.launch(capturedImageUri)
        }
    }


    private fun requestPermissions(){
        if (TrackingUtility.hasCameraPermissions(requireContext()))
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            EasyPermissions.requestPermissions(
                this,
                "Accept camera permission to upload image.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.CAMERA
            )
        }
        else {
            EasyPermissions.requestPermissions(
                this,
                "Accept camera permissions to upload image.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }


    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            imgView.setImageURI(null)
            imgView.setImageURI(capturedImageUri)
        }

    }

    private fun createImageUri(): Uri {
        val image = File(context?.filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.routetrack.fileprovider",
            image
        )
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}