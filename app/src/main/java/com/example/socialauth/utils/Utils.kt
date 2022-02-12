package com.example.socialauth.utils

import android.content.Context
import android.widget.Toast

fun String.makeToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}
