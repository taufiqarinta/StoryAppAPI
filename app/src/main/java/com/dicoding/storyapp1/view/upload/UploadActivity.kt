package com.dicoding.storyapp1.view.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp1.R
import com.dicoding.storyapp1.data.Constants.TOKEN
import com.dicoding.storyapp1.data.Preference
import com.dicoding.storyapp1.data.Result
import com.dicoding.storyapp1.databinding.ActivityUploadBinding
import com.dicoding.storyapp1.view.ViewModelFactory
import com.dicoding.storyapp1.view.main.MainActivity
import getImgUri
import reduceFileImg
import uriToFile

class UploadActivity : AppCompatActivity() {

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance()
    }

    private lateinit var binding: ActivityUploadBinding

    private var currentImgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImgUri = uri
            showImg()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImg() {
        currentImgUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.prevImg.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImgUri = getImgUri(this)
        launcherIntentCamera.launch(currentImgUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImg()
        }
    }

    private fun uploadImage() {
        currentImgUri?.let { uri ->
            val sharedPref = Preference.init(this, "session")
            val token = sharedPref.getString(TOKEN, "")
            val imgFile = uriToFile(uri, this).reduceFileImg()
            Log.d("Image File", "showImage: ${imgFile.path}")
            val desc = binding.etDesc.text.toString()

            viewModel.uploadImage(token!!, imgFile, desc).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            showToast(result.data)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        is Result.Error -> {
                            showToast(result.error)
                        }
                    }
                }
            }

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}