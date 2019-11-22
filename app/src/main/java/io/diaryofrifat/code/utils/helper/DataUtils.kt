package io.diaryofrifat.code.utils.helper

import android.content.ContentResolver
import android.net.Uri
import io.diaryofrifat.code.BaseApplication
import java.util.*

class DataUtils private constructor() {
    companion object {
        /**
         * This method provides an unique id using UUID
         *
         * @return [String] unique string
         * */
        fun getUniqueId(): String {
            return UUID.randomUUID().toString()
        }

        /**
         * This method provides a random number
         *
         * @param min minimum limit
         * @param max maximum limit
         * @return [Int] random number
         * */
        fun randomInt(min: Int, max: Int): Int {
            return Random().nextInt(max - min + 1) + min
        }

        /**
         * This method returns a local string
         *
         * @param resourceId desired resource id
         * @return desired string
         * */
        fun getString(resourceId: Int): String {
            return BaseApplication.getBaseApplicationContext().getString(resourceId)
        }

        /**
         * This method returns a local integer
         *
         * @param resourceId desired resource id
         * @return desired integer
         * */
        fun getInteger(resourceId: Int): Int {
            return ViewUtils.getResources().getInteger(resourceId)
        }

        /**
         * This method returns a local resource [Uri]
         *
         * @param resourceId desired resource id
         * @return desired [Uri]
         * */
        fun getUriFromResource(resourceId: Int): Uri {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + AndroidUtils.getApplicationId() + "/" + resourceId)
        }

        /**
         * This method returns a converted title case string
         *
         * @param given given string
         * @return desired [String]
         * */
        fun toTitleCase(given: String, isChangeable: Boolean = true): String {
            var isSpace = true
            val builder = StringBuilder(given)
            val len = builder.length

            for (i in 0 until len) {
                val char = builder[i]
                if (isSpace) {
                    if (!Character.isWhitespace(char)) {
                        // Convert to title case and switch out of whitespace mode.
                        builder.setCharAt(i, Character.toTitleCase(char))
                        isSpace = false
                    }
                } else if (Character.isWhitespace(char)) {
                    isSpace = true
                } else {
                    builder.setCharAt(i,
                            if (isChangeable)
                                Character.toLowerCase(char)
                            else
                                char
                    )
                }
            }

            return builder.toString()
        }
    }
}