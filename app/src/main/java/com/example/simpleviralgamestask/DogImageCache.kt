package com.example.simpleviralgamestask

import android.app.Application
import android.content.Context
import com.google.gson.Gson

class DogImageCache(application: Application) {
    companion object {
        private const val PREFS_NAME = "dog_image_cache"
        private const val KEY_CACHED_DOG_IMAGES = "cached_dog_images"
        private const val MAX_CACHE_SIZE = 20
    }

    private val sharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun cacheDogImage(dogImage: DogImageResponse) {
        val cachedDogImages = getAllCachedDogImages().toMutableList()
        cachedDogImages.removeAll { it.message == dogImage.message }
        cachedDogImages.add(0, dogImage)
        if (cachedDogImages.size > MAX_CACHE_SIZE) {
            cachedDogImages.removeLast()
        }
        saveDogImagesToCache(cachedDogImages)
    }

    fun getAllCachedDogImages(): List<DogImageResponse> {
        val cachedDogImagesJson = sharedPreferences.getString(KEY_CACHED_DOG_IMAGES, null)
        return if (!cachedDogImagesJson.isNullOrEmpty()) {
            gson.fromJson(cachedDogImagesJson, Array<DogImageResponse>::class.java).toList()
        } else {
            emptyList()
        }
    }

    fun clearCache() {
        sharedPreferences.edit().remove(KEY_CACHED_DOG_IMAGES).apply()
    }

    private fun saveDogImagesToCache(dogImages: List<DogImageResponse>) {
        val cachedDogImagesJson = gson.toJson(dogImages)
        sharedPreferences.edit().putString(KEY_CACHED_DOG_IMAGES, cachedDogImagesJson).apply()
    }
}