package io.diaryofrifat.code.utils.helper

import io.diaryofrifat.code.examroutine.R

class Constants {
    class Default {
        companion object {
            const val DEFAULT_STRING: String = ""
            const val DEFAULT_INTEGER: Int = 0
            const val DEFAULT_LONG: Long = 0
            const val DEFAULT_BOOLEAN: Boolean = false
        }
    }

    class Common {
        companion object {
            const val APP_COMMON_DATE_FORMAT: String = "EEEE, dd MMMM, yyyy"
            const val APP_COMMON_TIME_FORMAT: String = "hh:mm a"
        }
    }

    class TableNames {
        companion object {
            const val USER = "USER"
        }
    }

    class ColumnNames {
        companion object {
            const val ID = "ID"
            const val USER_ID = "USER_ID"
            const val USER_NAME = "USER_NAME"
        }
    }

    class PreferenceKey {
        companion object {
            const val EXAM_TYPE = "EXAM_TYPE"
        }
    }

    class File {
        companion object {
            val DIRECTORY_ROOT = DataUtils.getString(R.string.app_name)
            val PREFIX_IMAGE = "IMG_"
            val PREFIX_CROPPED_IMAGE = "IMG_CROPPED_"
            val SUFFIX_IMAGE = ".jpg"
        }
    }
}