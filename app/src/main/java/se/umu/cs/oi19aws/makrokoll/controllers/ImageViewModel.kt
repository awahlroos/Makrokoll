package se.umu.cs.oi19aws.makrokoll.controllers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageViewModel : ViewModel() {
    /*private lateinit var context:Context
    fun setContext(context: Context){
        this.context=context
    }*/

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

    private fun updateBitmap() {
        if (width > 0 && height > 0) file?.let {
            Thread {
                var bm = BitmapFactory.decodeFile(it.absolutePath)
                val rotatedBitmap: Bitmap?
                val orientation = getExifRotation(it)
                val matrix = Matrix()
                if (orientation != 0 && bm != null) {
                    matrix.postRotate(orientation.toFloat())
                    rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
                    bm = null //för att spara minne
                } else {
                    rotatedBitmap = bm
                }


                val bitmap = Bitmap.createScaledBitmap(rotatedBitmap!!, width, height, true)
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()

                /*
                //TODO: Heeeea

                // Scale the bitmap to fit the ImageView
                val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap!!, width, height, true)

                // Generate a unique filename
                val fileName = generateImageName()

                // Create the output file
                val outputDir = context.cacheDir // Use cache directory for temporary files
                val outputFile = File(outputDir, fileName)

                // Save the bitmap to the file
                val outputStream = FileOutputStream(outputFile)
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()

                // Post the scaled bitmap to the LiveData
                v.postValue(scaledBitmap)

                //TODO: Heeeea
                */


                //Skala om bilden så att den passar i imageviewn. Observera att getWidth och  getHeight
                //ej kommer ge korrekta värden förrän från onResume
                v.postValue(bitmap)
                //v.postValue(Bitmap.createScaledBitmap(rotatedBitmap!!, width, height, true))
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