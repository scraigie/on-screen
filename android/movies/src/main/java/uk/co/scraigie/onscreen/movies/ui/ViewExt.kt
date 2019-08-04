package uk.co.scraigie.onscreen.movies.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import uk.co.scraigie.onscreen.movies.R

fun ImageView.load(url: String) {

    val requestOptions = RequestOptions()
        .placeholder(R.drawable.ic_placeholder_24dp)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(CenterCrop(), RoundedCorners(20))

    Glide.with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(url)
        .into(this)
}