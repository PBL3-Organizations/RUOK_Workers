package com.example.ruok_workers

import android.graphics.Bitmap

class UnknownCard(meetPhotoResId: Int?, meetPhotoBitmap: Bitmap?, meetLog:String, meetPlace:String) {
    var meetPhotoResId: Int? = meetPhotoResId
        private set
    var meetPhotoBitmap: Bitmap? = meetPhotoBitmap
        private set
    var meetPlace:String = meetPlace
        private set
    var meeetLog:String = meetLog
        private set
}