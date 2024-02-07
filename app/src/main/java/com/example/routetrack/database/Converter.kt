package com.example.routetrack.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object Converter {

    fun fromBitmap(bmp: Bitmap?): ByteArray{
        val output = ByteArrayOutputStream()
        bmp?.compress(Bitmap.CompressFormat.PNG,100,output)
        return output.toByteArray()
    }

    fun toBitmap(byteArray: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

}