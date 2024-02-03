package com.codebyedu.sample1.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codebyedu.sample1.R

/**
 * A simple [Fragment] subclass.
 * Use the [WinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WinFragment : Fragment() {
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(USER_NAME_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_win, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleView = view.findViewById<TextView>(R.id.winTitle)
        val winBtnOk = view.findViewById<TextView>(R.id.winBtnOk)
        if (isWin()) {
            setWinText(titleView)
        } else {
            setLooseText(titleView)
        }
        winBtnOk.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setWinText(textView: TextView) {
        textView.text = getString(R.string.congratulations, userName)
        textView.setTextColor(Color.GREEN)
    }

    private fun setLooseText(textView: TextView) {
        textView.text = getString(R.string.game_over, userName)
        textView.setTextColor(Color.RED)
    }

    private fun isWin(): Boolean = userName == WIN_NAME

    companion object {
        private const val WIN_NAME = "T-Rex"

        private const val USER_NAME_PARAM =
            "com.codebyedu.sample1.ui.fragments.WinFragment.USER_NAME_PARAM"

        @JvmStatic
        fun newInstance(userName: String) =
            WinFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_NAME_PARAM, userName)
                }
            }
    }
}