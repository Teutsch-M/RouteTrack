package com.example.routetrack.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

object Converter {

    fun fromBitmap(bmp: Bitmap?): String {
        val output = ByteArrayOutputStream()
        bmp?.compress(Bitmap.CompressFormat.PNG,100,output)
        val byteArray = output.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun toBitmap(base64String: String): Bitmap? {
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

    fun fromUri(context: Context, uri: Uri?): String? {
        val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val compressedBitmap = compressBitmap(bitmap, 20)
        val outputStream = ByteArrayOutputStream()
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)
        val imageByte: ByteArray = outputStream.toByteArray()
        inputStream?.close()
        return Base64.encodeToString(imageByte, Base64.DEFAULT)
    }

    fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}