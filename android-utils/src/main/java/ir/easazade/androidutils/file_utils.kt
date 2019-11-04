@file:Suppress("FunctionName")

package ir.easazade.androidutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.sql.Timestamp

/**
 * resizes large images based on a max number of pixels given.
 * so that image won't have more pixels than the limitation
 */
fun _resizeLargeImage(context: Context, uri: Uri, maxPixels: Long = 1000000): Bitmap? {

  var inputStream: InputStream?
  try {
    val IMAGE_MAX_SIZE = maxPixels // 1.2MP
    inputStream = context.contentResolver.openInputStream(uri)

    // Decode image size
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeStream(inputStream, null, options)
    inputStream.close()

    var scale = 1
    while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale.toDouble(), 2.0)) > IMAGE_MAX_SIZE) {
      scale++
    }
    Timber.d("%s%s", "scale = " + scale + ", orig-width: " + options.outWidth + "orig -height: ", options.outHeight)

    var resultBitmap: Bitmap? = null
    inputStream = context.contentResolver.openInputStream(uri)
    if (scale > 1) {
      scale--
      // scale to max possible inSampleSize that still yields an image
      // larger than target
      options = BitmapFactory.Options()
      options.inSampleSize = scale
      resultBitmap = BitmapFactory.decodeStream(inputStream, null, options)

      // resize to desired dimensions
      val height = resultBitmap.height
      val width = resultBitmap.width
      Timber.d("%s%s", "1th scale operation dimenions - width: " + width + ",height : ", height)

      val y = Math.sqrt(IMAGE_MAX_SIZE / ((width.toDouble()) / height))
      val x = (y / height) * width

      val scaledBitmap = Bitmap.createScaledBitmap(
        resultBitmap, x.toInt(),
        y.toInt(), true
      )
      resultBitmap.recycle()
      resultBitmap = scaledBitmap
    } else {
      resultBitmap = BitmapFactory.decodeStream(inputStream)
    }
    inputStream.close()

    Timber.d("bitmap size - width: " + resultBitmap.width + ", height: " + resultBitmap.height)
    return resultBitmap
  } catch (ioe: IOException) {
    Timber.e(ioe)
    return null
  } catch (e: Exception) {
    Timber.e(e)
    return null
  }
}

/**
 * compresses and resizes image without loss of quality
 */
fun _resizeLargeImageCompressAndReturnAsFile(
  context: Context,
  uri: Uri,
  maxPixels: Long = 1000000,
  compressionPercentage: Int = 100
): File? {
  try {
    //create a file to write bitmap data
    val file = File(context.cacheDir, "${Timestamp(System.currentTimeMillis()).toString()}.jpg")
    file.createNewFile()

    //resize bitmap
    val bitmap = _resizeLargeImage(context, uri, maxPixels) ?: return null
    //convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    bitmap.compress(CompressFormat.JPEG, compressionPercentage /*ignored for PNG images*/, bos)
    val bitmapData = bos.toByteArray()

    //write the bytes in file
    val fos = FileOutputStream(file)
    fos.write(bitmapData)
    fos.flush()
    fos.close()
    return file
  } catch (e: Exception) {
    Timber.e(e)
    return null
  }
}

/**
 * reads file from asset directory and copies them to cache directory
 * @return copied file
 */
fun _readFileFromAssetsAndCopyToCache(path: String, fileExtension: String, context: Context): File {
  val outputCacheFile = File(context.cacheDir, _generateRandomName() + fileExtension)
  var fos: FileOutputStream? = null
  try {
    //read file
    val inputStream = context.assets.open(path)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()

    //now create file in cache directory
    fos = FileOutputStream(outputCacheFile)
    fos.write(buffer)
    fos.close()
  } catch (e: java.lang.Exception) {
    Timber.e(e)
    fos?.close()
  } catch (ioe: IOException) {
    Timber.e(ioe)
    fos?.close()
  }
  return outputCacheFile
}

