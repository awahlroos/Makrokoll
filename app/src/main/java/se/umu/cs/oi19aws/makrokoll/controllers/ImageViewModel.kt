package se.umu.cs.oi19aws.makrokoll.controllers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// View model to handle the image user can take for a recipe
class ImageViewModel : ViewModel() {

    private val v = MutableLiveData<Bitmap>()

    val bitmap: LiveData<Bitmap>
        get() = v

    private var width = 0
    private var height = 0

    var file: File? = null
        set(f) {
            if (f !== field) {
                field = f
                updateBitmap()
            }
        }

    fun setSize(height: Int, width: Int) {
        if (height != this.height || width != this.width) {
            this.height = height
            this.width = width
            updateBitmap()
        }
    }

    //Update bitmap on a new thread to not lock main thread
    private fun updateBitmap() {
        if (width > 0 && height > 0) file?.let {
            Thread {
                //Correct the rotation of the image given its exif information
                var bm = BitmapFactory.decodeFile(it.absolutePath)
                val rotatedBitmap: Bitmap?
                val orientation = getExifRotation(it)
                val matrix = Matrix()
                if (orientation != 0 && bm != null) {
                    matrix.postRotate(orientation.toFloat())
                    rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
                    bm = null
                } else {
                    rotatedBitmap = bm
                }

                val bitmap = Bitmap.createScaledBitmap(rotatedBitmap!!, width, height, true)
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                v.postValue(bitmap)
            }.start()
        }
    }


    private fun getExifRotation(imageFile: File): Int {
        return try {
            val exif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ExifInterface(imageFile)
            } else {
                return 0
            }
            when (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
            0
        }
    }
}