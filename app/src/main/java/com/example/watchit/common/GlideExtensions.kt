package com.example.watchit.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.example.watchit.R

fun ImageView.loadImage(path: String?) {
    Glide.with(this.context).load(Const.IMAGE_BASE_URL + path)
        .apply(centerCropTransform().error(R.drawable.error_img)).into(this)
}

