package com.example.simpleviralgamestask

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.simpleviralgamestask.databinding.ActivityGenerateDogsScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GenerateDogsScreen : AppCompatActivity() {
    companion object {
        private const val RESPONSE_STATUS_SUCCESS = "success"
        private const val FETCH_ERROR = "An error occurred: "
        private const val FETCH_IMAGE_ERROR = "Failed to fetch the dog image."
    }

    private lateinit var binding: ActivityGenerateDogsScreenBinding
    private val dogApiService: DogApiService by lazy { DogApi.service }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateDogsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.generate_dogs)
        binding.btnGenerate.setOnClickListener {
            generateDogImage()
        }
    }

    private fun generateDogImage() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val dogImage = getRandomDogImage()
                if (dogImage != null) {
                    DogImageCache.cacheDogImage(dogImage)
                    Glide.with(this@GenerateDogsScreen).load(dogImage.message)
                        .into(binding.ivDogImage)
                } else {
                    Toast.makeText(
                        this@GenerateDogsScreen, FETCH_IMAGE_ERROR, Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@GenerateDogsScreen, FETCH_ERROR + e.message, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun getRandomDogImage(): DogImageResponse? = withContext(Dispatchers.IO) {
        try {
            val response = dogApiService.getRandomDogImage()
            if (response.status == RESPONSE_STATUS_SUCCESS) {
                return@withContext response
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }
}