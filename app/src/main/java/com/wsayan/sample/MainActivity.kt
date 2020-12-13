package com.wsayan.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.wsayan.inctax.IncTaxCore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.taxText).text =
        IncTaxCore().calculateTaxByDefaultGross(
            IncTaxCore.TYPE_FEMALE,55000,60000,0).toString()
    }
}