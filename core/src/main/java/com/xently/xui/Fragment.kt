package com.xently.xui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.xently.xui.utils.ui.IModifyToolbar
import com.xently.xui.utils.ui.fragment.IFragment

open class Fragment : androidx.fragment.app.Fragment(), IFragment {

    private val backPressDispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private var backPressCallback: OnBackPressedCallback? = null

    private var iModifyToolbar: IModifyToolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = onBackPressed()
        }
        backPressDispatcher.addCallback(this, backPressCallback!!)

        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IModifyToolbar) iModifyToolbar = context
    }

    override fun onDetach() {
        iModifyToolbar = null
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        iModifyToolbar?.onModifyToolbar(
            toolbarTitle,
            toolbarSubTitle,
            toolbarUpIcon,
            showToolbar
        )
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item)

    override fun onBackPressed() {
        super.onBackPressed()
        backPressCallback?.isEnabled = false
        backPressDispatcher.onBackPressed()
    }
}