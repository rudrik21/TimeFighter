package com.rudrik.timefighter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun String.toast(c: Context) {
    Toast.makeText(c, this, Toast.LENGTH_SHORT).show()
}

fun String.snack(v: View) {
    Snackbar.make(v, this, Snackbar.LENGTH_SHORT).show()
}

fun String.log(c: Context) {
    Log.d(c.javaClass.simpleName, this)
}