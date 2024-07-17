package com.nightx.ingale.feature_local.main.data.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


fun encodeToBase64(
    image: Bitmap?,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int,
): String {
    return try {
        val byteArrayOS = ByteArrayOutputStream()
        image?.compress(compressFormat, quality, byteArrayOS)
        Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
    } catch (e: Exception) {
        ""
    }
}

fun decodeBase64(input: String?): Bitmap? {
    return if (!input.isNullOrBlank()) {
        val decodedBytes = Base64.decode(input, 0)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }else{
        null
    }
}