package com.codebyedu.sample1.ui.fragments

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codebyedu.sample1.R

class WinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)
        val userName = intent.getStringExtra(USER_NAME) ?: ""
        supportFragmentManager.beginTransaction()
            .replace(R.id.frContainer, WinFragment.newInstance(userName = userName))
            .commitNow()
    }

    companion object {
        fun newIntent(context: Context, userName: String): Intent =
            Intent(context, WinActivity::class.java).apply {
                putExtra(USER_NAME, userName)
            }

        private const val USER_NAME = "com.codebyedu.sample1.ui.fragments.WinActivity.USER_NAME"
    }
}