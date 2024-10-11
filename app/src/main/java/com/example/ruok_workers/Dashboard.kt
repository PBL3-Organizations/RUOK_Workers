package com.example.ruok_workers

import android.graphics.Bitmap

class Dashboard(hNum: Int, hPhotoResId: Int?, hPhotoBitmap: Bitmap?, hName:String, hBirth:String) {
    var hNum: Int = hNum
        private set
    var hPhotoResId: Int? = hPhotoResId
        private set
    var hPhotoBitmap: Bitmap? = hPhotoBitmap
        private set
    var hName: String = hName
        private set
    var hBirth: String = hBirth
        private set
}