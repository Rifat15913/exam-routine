package io.diaryofrifat.code.utils.helper

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
            const val PREFIX_MAILTO = "mailto:"
            const val SLASH = "/"
            const val APP_COMMON_DATE_FORMAT: String = "EEEE, dd MMMM, yyyy"
            const val APP_COMMON_TIME_FORMAT: String = "hh:mm a"
            const val APP_COMMON_ONLY_DATE_FORMAT: String = "dd"
            const val APP_COMMON_ONLY_DAY_FORMAT: String = "E"
            const val APP_COMMON_ONLY_MONTH_FORMAT: String = "MMM"
        }
    }

    class IntentKey {
        companion object {
            const val CATEGORY = "category"
            const val SUBCATEGORY = "subcategory"
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