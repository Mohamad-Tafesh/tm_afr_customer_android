package com.africell.africell.features.accountInfo

import android.os.Bundle
import androidx.navigation.findNavController
import com.africell.africell.R
import com.africell.africell.app.BaseActivity
import com.africell.africell.util.navigation.goBack
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountInfoActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info, false, false,  0)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_account).goBack(this)


}
