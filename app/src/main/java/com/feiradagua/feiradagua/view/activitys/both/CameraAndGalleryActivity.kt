package com.feiradagua.feiradagua.view.activitys.both

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityCameraAndGalleryBinding
import com.feiradagua.feiradagua.utils.Constants.CameraX.PHOTO_URI
import com.feiradagua.feiradagua.utils.Constants.CameraX.REQUEST_CODE_PERMISSIONS
import com.feiradagua.feiradagua.utils.Constants.CameraX.REQUIRED_PERMISSIONS
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.view.activitys.producer.ProducerUpdateAndAddProductActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerUpdateProfileActivity
import com.feiradagua.feiradagua.view.activitys.user.UserUpdateProfileActivity
import com.feiradagua.feiradagua.viewModel.CameraViewModel
import kotlinx.android.synthetic.main.activity_camera_and_gallery.*
import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraAndGalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraAndGalleryBinding
    private var viewModelCamera = CameraViewModel()
    private var position: Int = 0
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraAndGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelCamera = ViewModelProvider(this).get(CameraViewModel::class.java)
        position = intent.getIntExtra(POSITION, 0)


        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listener for take photo button
        binding.cameraCaptureButton.setOnClickListener { takePhoto() }
        binding.btGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 0)
        }
        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val targetUri = data?.data
        if (resultCode == Activity.RESULT_OK) {
            try {
                targetUri?.let {
                    addingPhotoToDB(it)
                }
            } catch (exception: FileNotFoundException) {
                exception.printStackTrace()
            }
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            "profilePhoto.jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
//                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    addingPhotoToDB(savedUri)
//                    Log.d(TAG, msg)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun addingPhotoToDB(uri: Uri) {
        val msg = "Foto salva com Sucesso!! Aguarde..."

        viewModelCamera.putFileToStorage(uri)
        viewModelCamera.getUri.observe(this@CameraAndGalleryActivity) {
//            when(position) {
////                1 -> { startExtraInfos(uri) }
//                2 -> { startUpdateUserProfile(uri) }
//                3 -> { startUpdateProduct(uri) }
//                4 -> { startUpdateProducerProfile(uri) }
//            }
            finish()
        }
        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun startUpdateUserProfile(uri: Uri) {
        val intent = Intent(this@CameraAndGalleryActivity, UserUpdateProfileActivity::class.java)
        intent.putExtra(PHOTO_URI, uri.toString())
        intent.putExtra(POSITION, 1)
        startActivity(intent)
        finish()
    }

    private fun startUpdateProducerProfile(uri: Uri) {
        val intent = Intent(this@CameraAndGalleryActivity, ProducerUpdateProfileActivity::class.java)
        intent.putExtra(PHOTO_URI, uri.toString())
        intent.putExtra(POSITION, 1)
        startActivity(intent)
        finish()
    }

    private fun startUpdateProduct(uri: Uri) {
        val intent = Intent(this@CameraAndGalleryActivity, ProducerUpdateAndAddProductActivity::class.java)
        intent.putExtra(PHOTO_URI, uri.toString())
        intent.putExtra(POSITION, 1)
        startActivity(intent)
        finish()
    }

    private fun startExtraInfos(uri: Uri) {
        val intent = Intent(this@CameraAndGalleryActivity, ExtraInfosActivity::class.java)
        intent.putExtra(PHOTO_URI, uri.toString())
        intent.putExtra(POSITION, 1)
        startActivity(intent)
        finish()
    }
}