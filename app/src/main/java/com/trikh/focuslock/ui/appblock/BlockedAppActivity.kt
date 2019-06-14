package com.trikh.focuslock.ui.appblock

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.trikh.focuslock.R
import com.trikh.focuslock.data.source.local.QuotesLocalRepository
import com.trikh.focuslock.utils.Constants.Companion.PACKAGE_NAME
import kotlinx.android.synthetic.main.activity_blocked_app.*

class BlockedAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_app)

        val packageName = intent.getStringExtra(PACKAGE_NAME)
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        appIconIv.setImageDrawable(appInfo.loadIcon(packageManager))
        appNameTv.text = packageManager.getApplicationLabel(appInfo)

        val quote = QuotesLocalRepository.getInstance().getQuote()
        motivationMessageTv.text =  quote.quote
        motivationAuthorTv.text = quote.author
    }

    fun closeDialog(v: View){
        finish()
    }
}
