package com.hajarslamah.erthquickapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ErthQuick : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quick_erth)
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer,ErthQuickFragment.newInstance())                .commit()        }
    }
}