package io.diaryofrifat.code.utils.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.IntRange
import io.diaryofrifat.code.BaseApplication

/**
 * This is a class that contains utils to work with network
 * @author Mohd. Asfaq-E-Azam Rifat
 * */
object NetworkUtils {
    const val NO_INTERNET_CONNECTION = 0
    const val MOBILE_DATA = 1
    const val WIFI = 2

    /**
     * This method returns the state of an internet connection
     *
     * @return [Int]
     * 0 = No Internet connection
     * 1 = Mobile Data
     * 2 = WiFi
     * */
    @Suppress("DEPRECATION", "LocalVariableName")
    @IntRange(from = 0, to = 2)
    fun getConnectionType(): Int {
        val context = BaseApplication.getBaseApplicationContext()

        var result = NO_INTERNET_CONNECTION
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = WIFI
                    } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = MOBILE_DATA
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = WIFI
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = MOBILE_DATA
                    }
                }
            }
        }

        return result
    }

    /**
     * This method returns the state of an internet connection
     *
     * @return true if the connection is online else false
     * */
    @Synchronized
    fun isOnline(): Boolean {
        return when (getConnectionType()) {
            NO_INTERNET_CONNECTION -> false
            else -> true
        }
    }
}