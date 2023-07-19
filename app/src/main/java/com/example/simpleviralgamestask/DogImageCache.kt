package com.example.simpleviralgamestask

import android.util.LruCache
import java.util.LinkedList

object DogImageCache {

    private const val MAX_CACHE_SIZE = 20
    private val dogImageCache = LruCache<String, DogImageResponse>(MAX_CACHE_SIZE)
    private val cachedDogImageKeys = LinkedList<String>()

    fun cacheDogImage(dogImage: DogImageResponse) {
        dogImageCache.put(dogImage.message, dogImage)
        cachedDogImageKeys.remove(dogImage.message)
        cachedDogImageKeys.addFirst(dogImage.message)
    }

    fun getAllCachedDogImages(): List<DogImageResponse> {
        return cachedDogImageKeys.mapNotNull { key ->
            dogImageCache.get(key)
        }
    }

    fun clearCache() {
        dogImageCache.evictAll()
        cachedDogImageKeys.clear()
    }
}