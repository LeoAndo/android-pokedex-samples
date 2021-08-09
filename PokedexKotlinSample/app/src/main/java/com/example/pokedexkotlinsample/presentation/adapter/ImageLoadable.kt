package com.example.pokedexkotlinsample.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.example.pokedexkotlinsample.R

interface ImageLoadable {
    @SuppressLint("CheckResult")
            /*
        @BindingAdapter(
            value = ["imageUrl", "centerCrop", "fitCenter", "circleCrop", "roundedCorners", "diskCacheStrategy", "signatureKey"],
            requireAll = false
        )
             */
    fun ImageView.loadImage(
        imageUrl: String? = null,
        centerCrop: Boolean = false,
        fitCenter: Boolean = false,
        circleCrop: Boolean = false,
        roundedCorners: Boolean? = false,
        diskCacheStrategy: DiskCacheStrategy? = null,
        signatureKey: String? = null
    ) {
        val requestBuilder: RequestBuilder<Drawable> = createGlideRequestBuilder(
            imageUrl,
            centerCrop,
            fitCenter,
            circleCrop,
            roundedCorners,
            diskCacheStrategy,
            signatureKey
        )
        requestBuilder.into(object : DrawableImageViewTarget(this) {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                super.onResourceReady(resource, transition)
                if (resource is GifDrawable) { // For gif animation, set a loop.
                    resource.setLoopCount(3)
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    fun ImageView.createGlideRequestBuilder(
        imageUrl: String? = null,
        centerCrop: Boolean = false,
        fitCenter: Boolean = false,
        circleCrop: Boolean = false,
        roundedCorners: Boolean? = false,
        diskCacheStrategy: DiskCacheStrategy? = null,
        signatureKey: String? = null
    ): RequestBuilder<Drawable> {
        return Glide.with(context).load(imageUrl).apply {
            transition(DrawableTransitionOptions.withCrossFade(800)) // enable animation.
            // placeholder(R.drawable.ic_place_holder)
            error(R.drawable.ic_error)
            apply(
                createRequestOptions(
                    centerCrop,
                    fitCenter,
                    circleCrop,
                    roundedCorners,
                    diskCacheStrategy,
                    signatureKey
                )
            )
        }
    }

    @SuppressLint("CheckResult")
    private fun createRequestOptions(
        centerCrop: Boolean = false,
        fitCenter: Boolean = false,
        circleCrop: Boolean = false,
        roundedCorners: Boolean? = false,
        diskCacheStrategy: DiskCacheStrategy? = null,
        signatureKey: String? = null
    ): RequestOptions {
        return RequestOptions().apply {
            // cache type: https://futurestud.io/tutorials/glide-how-to-choose-the-best-caching-preference
            diskCacheStrategy(diskCacheStrategy ?: DiskCacheStrategy.AUTOMATIC)
            // crop type
            if (roundedCorners != false) transform(CenterCrop(), RoundedCorners(12))
            if (centerCrop) centerCrop()
            if (fitCenter) fitCenter()
            if (circleCrop) circleCrop()
            if (!signatureKey.isNullOrBlank()) {
                signature(ObjectKey(signatureKey))
            }
        }
    }
}
