package com.ehubstar.marketplace.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.ExifInterface
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Patterns
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.core.app.NotificationCompat
import java.io.*


class ImageUtils {
    companion object {

        public fun decodeImage(base64: String): Bitmap? {
            var bitmap: Bitmap? = null
            if(!base64.isNullOrEmpty()){
                //decode base64 string to image
                val imageBytes = Base64.decode(base64, Base64.DEFAULT)
                bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            }
            return bitmap
        }

        public fun encodeImage(imageUri: Uri, context: Context): String? {
            var encodedImage: String? = null
            val imageStream: InputStream? = context.getContentResolver().openInputStream(imageUri)
            if (imageStream != null) {
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                encodedImage = encodeImage(selectedImage)
            }
            return encodedImage
        }

        private fun encodeImage(bm: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        private fun encodeImage(path: String): String? {
            val imagefile = File(path)
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(imagefile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val bm = BitmapFactory.decodeStream(fis)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            //Base64.de
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        fun isNotEmpty(vararg txts: String): Boolean {
            var valid = true
            for (s in txts) {
                if (s.isEmpty()) {
                    valid = false
                    break
                }
            }
            return valid
        }

        private fun sendNotification(messageBody: String, context: Context, intent: Intent, icon: Int, app_name:String) {
//        val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            var notificationBuilder: NotificationCompat.Builder? = null
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    context.packageName,
                    context.packageName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = context.packageName
                notificationManager.createNotificationChannel(channel)
                if (notificationBuilder == null) {
                    notificationBuilder = NotificationCompat.Builder(context, context.packageName)
                }
            } else {
                if (notificationBuilder == null) {
                    notificationBuilder = NotificationCompat.Builder(context, context.packageName)
                }
            }
            notificationBuilder.setSmallIcon(icon)
                .setContentTitle(app_name)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        }

        fun Bitmap.isSquare(): Boolean =
            (height == width) ||
                    (height > width && height <= width * 1.15) ||
                    (height < width && height * 1.15 >= width)

        fun Bitmap.isVertical(): Boolean = height > width
        fun Bitmap.isHorizontal(): Boolean = height < width

        fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
            LayoutInflater.from(context).inflate(layoutId, this, false)

        fun <T> List<T>.second(): T {
            return when {
                size < 2 -> throw NoSuchElementException("List not contains two images")
                else -> this[1]
            }
        }

        fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

        fun getScreenWidth(context: Context): Int {
            var widthScreen = 0
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            widthScreen = if (Build.VERSION.SDK_INT > 12) {
                val size = Point()
                display.getSize(size)
                size.x
            } else {
                display.width // Deprecated
            }
            return widthScreen
        }

        fun getScreenHeight(context: Context): Int {
            var height = 0
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            height = if (Build.VERSION.SDK_INT > 12) {
                val size = Point()
                display.getSize(size)
                size.y
            } else {
                display.height // Deprecated
            }
            return height
        }


        fun decodeFile(path: String?): Point? {
            try {
                //decode image size
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeFile(path, o)
                //Find the correct scale value. It should be the power of 2.
                val width_tmp = o.outWidth
                val height_tmp = o.outHeight
                return Point(width_tmp, height_tmp)
            } catch (e: FileNotFoundException) {
            }
            return null
        }

        /*fun isImageFile(path: String?): Boolean {
            val mimeType: String = URLConnection.guessContentTypeFromName(path)
            return mimeType != null && mimeType.startsWith("image")
        }

        fun isVideoFile(path: String?): Boolean {
            val mimeType = URLConnection.guessContentTypeFromName(path)
            return mimeType != null && mimeType.startsWith("video")
        }*/

        fun getImageOrientation(imageLocalPath: String): Int {
            return try {
                val exifInterface = ExifInterface(imageLocalPath)
                exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } catch (e: IOException) {
                e.printStackTrace()
                ExifInterface.ORIENTATION_NORMAL
            }
        }

        fun getImageSize(imageLocalPath: String): Size {
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            /*val bitmap: Bitmap =*/ BitmapFactory.decodeFile(imageLocalPath, options)
            val width: Int = options.outWidth
            val height: Int = options.outHeight

            //kiem tra them orientation vi samsung co khi nhan dang nguoc lai opposite width and height
            val orientation = getImageOrientation(imageLocalPath)
            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_ROTATE_270 -> {
                    Size(height, width)
                }
                else -> {
                    Size(width, height)
                }
            }
        }
    }
}