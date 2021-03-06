package com.example.csr83.watchaproject.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import android.support.annotation.ColorRes
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.csr83.watchaproject.R
import com.example.csr83.watchaproject.data.remote.MovieRemote
import com.example.csr83.watchaproject.data.remote.Movie
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.util.*


class Utils {

    companion object {
//        fun loadData(activity: Activity): ArrayList<Movie> {
        fun loadData(context: Context): ArrayList<Movie> {
            val listMovie = arrayListOf<Movie>()

            // assets 폴더의 data.txt 파일 데이터 읽기
            val assetManager: AssetManager = context.resources.assets
            val inputStream: InputStream = assetManager.open("data.txt")
            val inputString = inputStream.bufferedReader().use { it.readText() }

            // json 데이터 파싱 후 listMovie 에 추가
            try {
                val jsonData = JSONObject(inputString)
                val jsonArrayMovie = jsonData.getJSONArray("movie")
                for (i in 0 until jsonArrayMovie.length()) {
                    val jsonMovie = jsonArrayMovie.getJSONObject(i)
                    val movie = Movie(
                        null,
                        jsonMovie.getString("title"),
                        jsonMovie.getString("image_wide"),
                        jsonMovie.getString("image_tall"),
                        jsonMovie.getString("year")
                    )
                    listMovie.add(movie)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return listMovie
        }
//        fun loadData2(activity: Activity): ArrayList<MovieRemote> {
//            val listMovie = arrayListOf<MovieRemote>()
//
//            // assets 폴더의 data.txt 파일 데이터 읽기
//            val assetManager: AssetManager = activity.resources.assets
//            val inputStream: InputStream = assetManager.open("data.txt")
//            val inputString = inputStream.bufferedReader().use { it.readText() }
//
//            // json 데이터 파싱 후 listMovie 에 추가
//            try {
//                val jsonData = JSONObject(inputString)
//                val jsonArrayMovie = jsonData.getJSONArray("movie")
//                for (i in 0 until jsonArrayMovie.length()) {
//                    val jsonMovie = jsonArrayMovie.getJSONObject(i)
//                    val movie = MovieRemote(
//                        null,
//                        jsonMovie.getString("title"),
//                        jsonMovie.getString("image_wide"),
//                        jsonMovie.getString("image_tall"),
//                        jsonMovie.getString("year")
//                    )
//                    listMovie.add(movie)
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//            return listMovie
//        }

        fun getStatusBarHeight(context: Context?): Int {
            if (context != null) {
                val statusBarHeightResourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (statusBarHeightResourceId > 0) {
                    return context.resources.getDimensionPixelSize(statusBarHeightResourceId)
                }
            }
            return 0
        }

        fun setTranslucentStatusBar(window: Window) {
            val sdkInt = Build.VERSION.SDK_INT
            if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
                setTranslucentStatusBarLollipop(window)
            } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatusBarKiKat(window)
            }
        }

        fun setNoTranslucentStatusBar(window: Window, @ColorRes id: Int = R.color.status_bar_color) {
            val sdkInt = Build.VERSION.SDK_INT
            if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
                setNoTranslucentStatusBarLollipop(window, id)
            } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
                setNoTranslucentStatusBarKiKat(window)
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun setTranslucentStatusBarLollipop(window: Window) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = window.context.resources.getColor(android.R.color.transparent)
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private fun setTranslucentStatusBarKiKat(window: Window) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun setNoTranslucentStatusBarLollipop(window: Window, @ColorRes id: Int) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = window.context.resources.getColor(id)
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private fun setNoTranslucentStatusBarKiKat(window: Window) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        fun convertTimeFormat(ms: Long?): String {
            if (ms == null)
                return ""

            val second = ms / 1000 % 60
            val minute = ms / (1000 * 60) % 60
            val hour = ms / (1000 * 60 * 60) % 24

            return if (hour == 0L)
                String.format("%d:%02d", minute, second)
            else
                String.format("%d:%02d:%02d", hour, minute, second)
        }

        fun convertTimeFormat2(sec: Int?, type: Int): String {
            if (sec == null)
                return ""

            val second = sec % 60
            val minute = sec / 60 % 60
            val hour = sec / (60 * 60) % 24

            when (type) {
                1 -> {
                    return if (minute == 0)
                        String.format("%02d초", second)
                    else if (hour == 0)
                        String.format("%d분 %02d초", minute, second)
                    else
                        String.format("%d시간 %02d분 %02d", hour, minute, second)
                }
                else -> return ""
            }
        }

        fun convertPxToDp(px: Int, context: Context)
                = (px / (context.resources.displayMetrics.densityDpi / 160f)).toInt()

        fun convertDpToPx(dp: Int, context: Context)
                = (dp * (context.resources.displayMetrics.densityDpi / 160f)).toInt()

        fun showSoftInput(context: Context, edt: EditText) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(edt, 0)
        }

        fun hideSoftInput(context: Context, edt: EditText) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(edt.windowToken, 0)
        }

    }
}