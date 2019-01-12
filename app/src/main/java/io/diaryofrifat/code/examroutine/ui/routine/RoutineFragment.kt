package io.diaryofrifat.code.examroutine.ui.routine

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.databinding.FragmentExamRoutineBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.base.helper.LinearMarginItemDecoration
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.SharedPrefUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import io.diaryofrifat.code.utils.libs.firebase.FirebaseUtils

class RoutineFragment : BaseFragment<RoutineMvpView, RoutinePresenter>() {

    private lateinit var mBinding: FragmentExamRoutineBinding
    private lateinit var mMarginItemDecoration: LinearMarginItemDecoration
    private var mFirebaseDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null

    override val layoutId: Int
        get() = R.layout.fragment_exam_routine

    override fun getFragmentPresenter(): RoutinePresenter {
        return RoutinePresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as FragmentExamRoutineBinding

        val examType: String = SharedPrefUtils.get(Constants.PreferenceKey.EXAM_TYPE,
                Constants.Default.DEFAULT_STRING)!!

        val databaseExamPath: String?

        when (examType) {
            getString(R.string.psc) -> {
                databaseExamPath = getString(R.string.path_psc)
            }

            getString(R.string.jsc) -> {
                databaseExamPath = getString(R.string.path_jsc)
            }

            getString(R.string.ssc) -> {
                databaseExamPath = getString(R.string.path_ssc)
            }

            getString(R.string.hsc) -> {
                databaseExamPath = getString(R.string.path_hsc)
            }

            else -> {
                ToastUtils.error(getString(R.string.something_went_wrong))
                return
            }
        }

        if (mFirebaseDatabaseReference == null) {
            mFirebaseDatabaseReference = FirebaseUtils.getDatabaseReference(databaseExamPath)
        }

        if (mContext != null) {
            mMarginItemDecoration = LinearMarginItemDecoration(
                    ViewUtils.getPixel(R.dimen.margin_16),
                    ViewUtils.getPixel(R.dimen.margin_16),
                    ViewUtils.getPixel(R.dimen.margin_8))

            ViewUtils.initializeRecyclerView(mBinding.recyclerViewExams, RoutineAdapter(),
                    null, null, LinearLayoutManager(mContext),
                    mMarginItemDecoration, null, DefaultItemAnimator())
        }

        if (mChildEventListener == null) {
            mChildEventListener = object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onChildMoved(data: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(data: DataSnapshot, p1: String?) {
                    getAdapter().addItem(data.getValue(Exam::class.java)!!)
                }

                override fun onChildAdded(data: DataSnapshot, p1: String?) {
                    getAdapter().addItem(data.getValue(Exam::class.java)!!)
                }

                override fun onChildRemoved(data: DataSnapshot) {

                }

            }
        }

        mFirebaseDatabaseReference?.addChildEventListener(mChildEventListener!!)
    }

    override fun stopUI() {
        if (mChildEventListener != null) {
            mFirebaseDatabaseReference?.removeEventListener(mChildEventListener!!)
        }
    }

    override fun onStop() {
        super.onStop()
        mBinding.recyclerViewExams.removeItemDecoration(mMarginItemDecoration)
    }

    private fun getAdapter(): RoutineAdapter {
        return mBinding.recyclerViewExams.adapter as RoutineAdapter
    }
}