package io.diaryofrifat.code.utils.helper

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import io.diaryofrifat.code.examroutine.databinding.ProgresssDialogLayoutBinding

object ProgressDialogUtils {
    private var mAlertDialog: AlertDialog? = null

    fun showProgressDialog(context: Context) {
        val builder = AlertDialog.Builder(context)

        val binding = ProgresssDialogLayoutBinding.inflate(LayoutInflater.from(context),
                null, false)

        binding.textViewMessage.setTypeface(null, Typeface.NORMAL)

        builder.setCancelable(false)
        builder.setView(binding.root)

        mAlertDialog = builder.create()
        mAlertDialog?.show()
    }

    fun hideProgressDialog() {
        if (mAlertDialog != null) {
            mAlertDialog?.dismiss()
            mAlertDialog = null
        }
    }
}