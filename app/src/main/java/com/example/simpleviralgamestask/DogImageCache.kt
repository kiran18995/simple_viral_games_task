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

object DogImageCache {

    private const val MAX_CACHE_SIZE = 20
    private val dogImageCache = LruCache<String, DogImageResponse>(MAX_CACHE_SIZE)

    fun cacheDogImage(dogImage: DogImageResponse) {
        dogImageCache.put(dogImage.message, dogImage)
    }

    fun getAllCachedDogImages(): List<DogImageResponse> {
        val snapshot = dogImageCache.snapshot()
        return snapshot.values.toList()
    }

    fun clearCache() {
        dogImageCache.evictAll()
    }
}