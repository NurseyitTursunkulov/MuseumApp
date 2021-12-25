package com.google.mlkit.md.barcodedetection

import android.media.Image
import androidx.annotation.IdRes

data class BarcodeInfoToDisplay(
    val title: String, val description : String,
    val image: String,
    val barcodeUrl:String
)
