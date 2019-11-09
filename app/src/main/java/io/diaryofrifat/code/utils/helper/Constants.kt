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
            const val SLASH = "/"
            const val APP_COMMON_DATE_FORMAT: String = "EEEE, dd MMMM, yyyy"
            const val APP_COMMON_TIME_FORMAT: String = "hh:mm a"
            const val APP_COMMON_ONLY_DATE_FORMAT: String = "dd"
            const val APP_COMMON_ONLY_DAY_FORMAT: String = "E"
            const val APP_COMMON_ONLY_MONTH_FORMAT: String = "MMM"
            const val DATE_FORMAT = "dd MMM"
            const val YEAR_FORMAT = "yyyy"
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

    class IntentKey {
        companion object {
            const val MODEL = "MODEL"
            const val CATEGORY = "category"
            const val SUBCATEGORY = "subcategory"
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

    class SelectionIds {
        companion object {
            const val EXAM_TYPE = "exam_type"
        }
    }

    class Firebase {
        companion object {
            const val SUBJECT_CODE = "subject_code"
            const val SUBJECT_NAME = "subject_name"
            const val ENDING_TIME = "ending_time"
            const val STARTING_TIME = "starting_time"
            const val VISITING_URL = "visiting_url"
        }
    }
}