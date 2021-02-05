package com.tedmob.africell.features.addNewAccount

import android.os.Bundle
import androidx.navigation.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseActivity
import com.tedmob.africell.util.navigation.goBack


class AddAccountActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account, false, false,  0)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_add_account).goBack(this)


}
