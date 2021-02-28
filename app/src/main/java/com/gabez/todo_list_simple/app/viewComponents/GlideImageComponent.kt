package com.gabez.todo_list_simple.app.viewComponents

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gabez.todo_list_simple.R

class GlideImageComponent(
    private val imageView: ImageView,
    private val progressBar: ProgressBar,
    private val context: Context
) {

    fun setUrl(newUrl: String?) {
        try {
            if (newUrl == null  || newUrl.replace("\\s".toRegex(), "") == "") {
                setPlaceholder()
                return
            }

            Glide.with(context)
                .load(newUrl)
                .apply(RequestOptions().circleCrop())
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(imageView)


        } catch (e: Exception) {
            imageView.setImageResource(R.drawable.ic_error)
            progressBar.visibility = View.GONE
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPlaceholder() {
        imageView.setImageResource(R.drawable.ic_placeholder)
        progressBar.visibility = View.GONE
    }

}