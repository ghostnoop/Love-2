package com.testtask.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun inflate(
    context: Context,
    viewId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(context).inflate(viewId, parent, attachToRoot)
}

