package com.example.simpleviralgamestask

import android.content.Context
import android.content.SharedPreferences
import android.util.LruCache
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.LinkedList

class DogImageCache private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "dog_image_cache"
        private const val KEY_CACHED_DOG_IMAGES = "cached_dog_images"
        private const val MAX_CACHE_SIZE = 20

        @Volatile
        private var instance: DogImageCache? = null

        fun getInstance(context: Context): DogImageCache {
            return instance ?: synchronized(this) {
                instance ?: DogImageCache(context.applicationContext).also { instance = it }
            }
        }
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val gson = Gson()
    private val lruCache = LruCache<String, DogImageResponse>(MAX_CACHE_SIZE)
    private val cachedDogImageKeys = LinkedList<String>()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        // Load cached dog images from SharedPreferences during initialization
        loadCacheFromSharedPreferences()
    }

    fun cacheDogImage(dogImage: DogImageResponse) {
        // Update LruCache
        ioScope.launch {
            lruCache.put(dogImage.message, dogImage)

            // Update the list of cached keys
            cachedDogImageKeys.addFirst(dogImage.message)

            // Trim the list to keep only the latest MAX_CACHE_SIZE keys
            while (cachedDogImageKeys.size > MAX_CACHE_SIZE) {
                cachedDogImageKeys.removeLast()
            }

            // Save the updated cache and key list to SharedPreferences
            saveCacheToSharedPreferences()
        }
    }

    suspend fun getAllCachedDogImages(): List<DogImageResponse> {
        // Get all cached dog images in reverse order to show the most recent images first
        return withContext(Dispatchers.IO) {
            // Get all cached dog images in reverse order to show the most recent images first
            val cachedDogImages = mutableListOf<DogImageResponse>()
            for (key in cachedDogImageKeys) {
                lruCache.get(key)?.let {
                    cachedDogImages.add(it)
                }
            }
            cachedDogImages
        }
    }

    fun clearCache() {
        // Clear LruCache
        ioScope.launch {
            // Clear LruCache
            lruCache.evictAll()

            // Clear the list of cached keys
            cachedDogImageKeys.clear()

            // Save the updated cache and empty key list to SharedPreferences
            saveCacheToSharedPreferences()
        }
    }

    private fun loadCacheFromSharedPreferences() {
        ioScope.launch {
            val cachedDogImagesJson = sharedPreferences.getString(KEY_CACHED_DOG_IMAGES, null)
            if (!cachedDogImagesJson.isNullOrEmpty()) {
                val cachedDogImages =
                    gson.fromJson(cachedDogImagesJson, Array<DogImageResponse>::class.java)
                cachedDogImages?.forEach { dogImage ->
                    // Add dog images to LruCache
                    lruCache.put(dogImage.message, dogImage)
                    // Add the keys to the list in the correct order
                    cachedDogImageKeys.addFirst(dogImage.message)
                }
            }
        }
    }

    private fun saveCacheToSharedPreferences() {
        ioScope.launch {
            val cachedDogImagesJson = gson.toJson(getAllCachedDogImages())
            sharedPreferences.edit().putString(KEY_CACHED_DOG_IMAGES, cachedDogImagesJson).apply()
        }
    }
}