package com.sm.borutoapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import okhttp3.internal.toHexString

object PaletteGenerator {

    suspend fun convertImageUrlToBitmap(imageUrl:String, context:Context) : Bitmap? {
        val loader = ImageLoader(context = context)
        val request = ImageRequest.Builder(context = context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        val imageResult = loader.execute(request = request)

        return if (imageResult is SuccessResult){
            (imageResult.drawable as BitmapDrawable).bitmap
        }else{
            null
        }
    }

    private fun parseColorSwatch(color : Palette.Swatch?) : String {
        return if (color != null){
            val parsedColor = color.rgb.toHexString()
            return "#$parsedColor"
        }else{ "#000000" } // return black color.
    }

    private fun parseBodyColor(color:Int?) : String {
        return if (color != null){
            val parsedColor = color.toHexString()
            return "#$parsedColor"
        }else{ "#FFFFFF" } // return white color.
    }

    fun extractColorsFromBitmap(bitmap: Bitmap) : Map<String,String> {
        return mapOf(
            "vibrant" to parseColorSwatch(color = Palette.from(bitmap).generate().vibrantSwatch) ,
            "darkVibrant" to parseColorSwatch(color = Palette.from(bitmap).generate().darkVibrantSwatch) ,
            "onDarkVibrant" to parseBodyColor(color = Palette.from(bitmap).generate().darkVibrantSwatch?.bodyTextColor)
        )
    }

}