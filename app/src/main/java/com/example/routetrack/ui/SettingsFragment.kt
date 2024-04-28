package com.example.routetrack.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.example.routetrack.LoginActivity
import com.example.routetrack.R
import com.example.routetrack.database.Converter
import com.example.routetrack.ui.viewmodels.UserViewModel
import com.example.routetrack.utility.Constants
import com.example.routetrack.utility.TrackingUtility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SettingsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "SettingsFragment"
    private val viewModel: UserViewModel by viewModels()
    private lateinit var identifierText: TextView
    private lateinit var logoutButton: Button
    private lateinit var imgView: ImageView
    private lateinit var pictureButton: Button
    private var capturedImageUri: Uri? = null
    private val userId = FirebaseAuth.getInstance().currentUser!!.email!!
    private val db = FirebaseFirestore.getInstance().collection("routes").document(userId)

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
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.apply {
            initializeView(this)
            registerListeners(this)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        identifierText.text = FirebaseAuth.getInstance().currentUser!!.email


        db.get().addOnSuccessListener {
            val img = it.getString("profileImage")
            if (img != null) {
                imgView.setImageBitmap(Converter.toBitmap(img))
                imgView.rotation = imgView.rotation - 90F
            }
        }



    }

    private fun initializeView(view: View) {
        identifierText = view.findViewById(R.id.identifierText)
        logoutButton = view.findViewById(R.id.signOutButton)
        imgView = view.findViewById(R.id.img_profile)
        pictureButton = view.findViewById(R.id.pictureButton)
        capturedImageUri = createImageUri()
    }

    private fun registerListeners(view: View) {
        logoutButton.setOnClickListener {
            viewModel.logout { success ->
                if (success){
                    val intent = Intent(this@SettingsFragment.requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                else {
                    Toast.makeText(activity, "Logout failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        pictureButton.setOnClickListener {
            requestPermissions()
            takePictureLauncher.launch(capturedImageUri)
            if (imgView.drawable != null) {
                val img = Converter.fromUri(requireContext(), capturedImageUri)
                val profileImage: Map<String, String?> = hashMapOf(
                    "profileImage" to img
                )
                db.set(profileImage, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(TAG, "Profile picture updated!")
                    }
                    .addOnFailureListener {  ex ->
                        Log.e(TAG, ex.message, ex)
                    }
            }
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


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
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


    private fun createImageUri(): Uri {
        val image = File(context?.filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.routetrack.fileprovider",
            image
        )
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            imgView.setImageURI(null)
            imgView.setImageURI(capturedImageUri)
        }
    }

}