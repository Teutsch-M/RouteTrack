package com.example.routetrack.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

}