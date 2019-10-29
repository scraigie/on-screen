package uk.co.scraigie.onscreen.movies.ui

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import uk.co.scraigie.onscreen.movies.R


fun ImageView.load(url: String, onBitmapReady: (Bitmap) -> Unit = {}) {

    val requestOptions = RequestOptions()
        .placeholder(AppCompatResources.getDrawable(this.context, R.drawable.ic_placeholder_24dp))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(RoundedCorners(20))

    Glide.with(this)
        .asBitmap()
        .apply(requestOptions)
        .load(url)
        .listener(object: RequestListener<Bitmap>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let(onBitmapReady)
                return false
            }
        })
        .into(this)

}