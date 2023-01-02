package com.lacolinares.digidraw.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileHelper {

    fun getVit4mnist(context: Context): String{
        return assetFilePath(context, "vit4mnist.ptl")
    }

    @Throws(IOException::class)
    private fun assetFilePath(context: Context, assetName: String): String {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }
        context.assets.open(assetName).use { `is` ->
            FileOutputStream(file).use { os ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (`is`.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                os.flush()
            }
            return file.absolutePath
        }
    }
}