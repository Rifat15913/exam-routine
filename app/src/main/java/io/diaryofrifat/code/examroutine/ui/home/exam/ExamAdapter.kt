package io.diaryofrifat.code.examroutine.ui.home.exam

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.material.chip.Chip
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.ui.base.component.BaseAdapter
import io.diaryofrifat.code.examroutine.ui.base.component.BaseViewHolder
import io.diaryofrifat.code.examroutine.ui.base.getString
import io.diaryofrifat.code.examroutine.ui.base.makeItGone
import io.diaryofrifat.code.examroutine.ui.base.makeItInvisible
import io.diaryofrifat.code.examroutine.ui.base.makeItVisible
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.TimeUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import kotlinx.android.synthetic.main.item_exam.view.*
import kotlinx.android.synthetic.main.item_exam.view.constraint_layout_container
import kotlinx.android.synthetic.main.item_exam.view.text_view_date
import kotlinx.android.synthetic.main.item_exam.view.text_view_day
import kotlinx.android.synthetic.main.item_exam.view.text_view_month
import kotlinx.android.synthetic.main.item_exam.view.view_initial_separator
import kotlinx.android.synthetic.main.item_exam.view.view_separator
import kotlinx.android.synthetic.main.item_exam_native_ad.view.*
import timber.log.Timber
import java.util.*

class ExamAdapter : BaseAdapter<Exam>() {

    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_AD = 1
    }

    override fun isEqual(left: Exam, right: Exam): Boolean {
        return left.nativeAd == null && right.nativeAd == null && left.id == right.id
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.let {
            if (it.nativeAd == null) {
                TYPE_CONTENT
            } else {
                TYPE_AD
            }
        } ?: super.getItemViewType(position)
    }

    override fun newViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Exam> {
        return if (viewType == TYPE_CONTENT) {
            ExamViewHolder(inflate(parent, R.layout.item_exam))
        } else {
            AdViewHolder(inflate(parent, R.layout.item_exam_native_ad))
        }
    }

    inner class ExamViewHolder(view: View) : BaseViewHolder<Exam>(view) {
        override fun bind(item: Exam) {
            itemView.text_view_subject_name?.text = item.subjectName

            if (!TextUtils.isEmpty(item.subjectCode)) {
                itemView.text_view_subject_code?.makeItVisible()
                itemView.text_view_subject_code?.text =
                        String.format(
                                Locale.getDefault(),
                                getString(R.string.exam_placeholder_subject_code),
                                item.subjectCode
                        )
            } else {
                itemView.text_view_subject_code?.makeItInvisible()
            }

            itemView.chip_subcategory?.text =
                    if (item.subcategory == null) {
                        getString(R.string.exam_na_subcategory)
                    } else {
                        item.subcategory?.examTypeTitle
                    }

            itemView.text_view_time?.text =
                    String.format(Locale.ENGLISH,
                            getString(R.string.placeholder_exam_time),
                            TimeUtils.getFormattedDateTimeString(item.startingTime,
                                    Constants.Common.APP_COMMON_TIME_FORMAT),
                            TimeUtils.getFormattedDateTimeString(item.endingTime,
                                    Constants.Common.APP_COMMON_TIME_FORMAT))

            itemView.text_view_day?.text = TimeUtils.getFormattedDayNameString(item.startingTime)
            itemView.text_view_date?.text = TimeUtils.getFormattedOnlyDateString(item.startingTime)
            itemView.text_view_month?.text = TimeUtils.getFormattedMonthString(item.startingTime)

            val startingColor: Int =
                    if (item.startingTime < TimeUtils.currentTime()) {
                        if (DateUtils.isToday(item.startingTime)) {
                            ViewUtils.getColor(R.color.colorAlternativeGreen10)
                        } else {
                            ViewUtils.getColor(R.color.textLight10)
                        }
                    } else {
                        if (DateUtils.isToday(item.startingTime)) {
                            ViewUtils.getColor(R.color.colorAlternativeGreen10)
                        } else {
                            ViewUtils.getColor(R.color.colorPrimary10)
                        }
                    }

            val endingColor: Int = ViewUtils.getColor(R.color.colorWhite)
            val backgroundDrawable = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(startingColor, endingColor)
            )

            val desiredColor: Int =
                    if (item.startingTime < TimeUtils.currentTime()) {
                        if (DateUtils.isToday(item.startingTime)) {
                            ViewUtils.getColor(R.color.colorAlternativeGreen)
                        } else {
                            ViewUtils.getColor(R.color.textLight)
                        }
                    } else {
                        if (DateUtils.isToday(item.startingTime)) {
                            ViewUtils.getColor(R.color.colorAlternativeGreen)
                        } else {
                            ViewUtils.getColor(R.color.colorPrimary)
                        }
                    }

            itemView.constraint_layout_container?.background = backgroundDrawable
            itemView.view_initial_separator?.setBackgroundColor(desiredColor)
            itemView.text_view_day?.setTextColor(desiredColor)
            itemView.text_view_date?.setTextColor(desiredColor)
            itemView.text_view_month?.setTextColor(desiredColor)

            itemView.text_view_subject_name?.setTextColor(desiredColor)
            itemView.text_view_time?.setTextColor(desiredColor)

            itemView.text_view_subject_code?.setTextColor(desiredColor)
            itemView.chip_subcategory?.chipBackgroundColor = ColorStateList.valueOf(desiredColor)
            itemView.view_separator?.setBackgroundColor(desiredColor)
        }
    }

    inner class AdViewHolder(view: View) : BaseViewHolder<Exam>(view) {
        override fun bind(item: Exam) {
            val nativeAd: UnifiedNativeAd = item.nativeAd!!
            var previousItem: Exam? = null
            val adView = itemView.native_ad_view

            try {
                previousItem = getItem(adapterPosition - 1)
            } catch (e: Exception) {
                Timber.e(e)
            }

            previousItem?.let {
                itemView.text_view_day?.text = TimeUtils.getFormattedDayNameString(it.startingTime)
                itemView.text_view_date?.text = TimeUtils.getFormattedOnlyDateString(it.startingTime)
                itemView.text_view_month?.text = TimeUtils.getFormattedMonthString(it.startingTime)

                val startingColor: Int =
                        if (it.startingTime < TimeUtils.currentTime()) {
                            if (DateUtils.isToday(it.startingTime)) {
                                ViewUtils.getColor(R.color.colorAlternativeGreen10)
                            } else {
                                ViewUtils.getColor(R.color.textLight10)
                            }
                        } else {
                            if (DateUtils.isToday(it.startingTime)) {
                                ViewUtils.getColor(R.color.colorAlternativeGreen10)
                            } else {
                                ViewUtils.getColor(R.color.colorPrimary10)
                            }
                        }

                val endingColor: Int = ViewUtils.getColor(R.color.colorWhite)
                val backgroundDrawable = GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        intArrayOf(startingColor, endingColor)
                )

                val desiredColor: Int =
                        if (it.startingTime < TimeUtils.currentTime()) {
                            if (DateUtils.isToday(it.startingTime)) {
                                ViewUtils.getColor(R.color.colorAlternativeGreen)
                            } else {
                                ViewUtils.getColor(R.color.textLight)
                            }
                        } else {
                            if (DateUtils.isToday(it.startingTime)) {
                                ViewUtils.getColor(R.color.colorAlternativeGreen)
                            } else {
                                ViewUtils.getColor(R.color.colorPrimary)
                            }
                        }

                itemView.constraint_layout_container?.background = backgroundDrawable
                itemView.view_initial_separator?.setBackgroundColor(desiredColor)
                itemView.text_view_day?.setTextColor(desiredColor)
                itemView.text_view_date?.setTextColor(desiredColor)
                itemView.text_view_month?.setTextColor(desiredColor)

                itemView.text_view_headline?.setTextColor(desiredColor)
                itemView.text_view_body?.setTextColor(desiredColor)
                itemView.text_view_price?.setTextColor(desiredColor)
                itemView.text_view_store?.setTextColor(desiredColor)
                itemView.text_view_ad_advertiser?.setTextColor(desiredColor)

                itemView.chip_ad_call_to_action?.chipBackgroundColor =
                        ColorStateList.valueOf(desiredColor)
                itemView.view_separator?.setBackgroundColor(desiredColor)
            }

            adView?.headlineView = itemView.text_view_headline
            adView?.bodyView = itemView.text_view_body
            adView?.callToActionView = itemView.chip_ad_call_to_action
            adView?.iconView = itemView.image_view_ad_app_icon
            adView?.priceView = itemView.text_view_price
            adView?.starRatingView = itemView.rating_bar_stars
            adView?.storeView = itemView.text_view_store
            adView?.advertiserView = itemView.text_view_ad_advertiser

            (itemView.native_ad_view?.headlineView as AppCompatTextView).text = nativeAd.headline

            if (nativeAd.body == null) {
                adView.bodyView.visibility = View.GONE
            } else {
                adView.bodyView.visibility = View.VISIBLE
                (adView.bodyView as AppCompatTextView).text = nativeAd.body
            }

            if (nativeAd.callToAction == null) {
                adView.callToActionView.visibility = View.INVISIBLE
            } else {
                adView.callToActionView.visibility = View.VISIBLE
                (adView.callToActionView as Chip).text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                adView.iconView.visibility = View.GONE
                itemView.text_view_day?.makeItVisible()
                itemView.text_view_date?.makeItVisible()
                itemView.text_view_month?.makeItVisible()
            } else {
                (adView.iconView as AppCompatImageView).setImageDrawable(
                        nativeAd.icon.drawable)
                adView.iconView.visibility = View.VISIBLE

                itemView.text_view_day?.makeItGone()
                itemView.text_view_date?.makeItGone()
                itemView.text_view_month?.makeItGone()
            }

            if (nativeAd.price == null) {
                adView.priceView.visibility = View.INVISIBLE
            } else {
                adView.priceView.visibility = View.VISIBLE
                (adView.priceView as AppCompatTextView).text = nativeAd.price
            }

            if (nativeAd.store == null) {
                adView.storeView.visibility = View.GONE
            } else {
                adView.storeView.visibility = View.VISIBLE
                (adView.storeView as AppCompatTextView).text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                adView.starRatingView.visibility = View.GONE
            } else {
                (adView.starRatingView as AppCompatRatingBar).rating =
                        nativeAd.starRating!!.toFloat()
                adView.starRatingView.visibility = View.VISIBLE
            }

            if (nativeAd.advertiser == null) {
                adView.advertiserView.visibility = View.GONE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView.visibility = View.VISIBLE
            }

            adView.setNativeAd(nativeAd)
        }
    }
}