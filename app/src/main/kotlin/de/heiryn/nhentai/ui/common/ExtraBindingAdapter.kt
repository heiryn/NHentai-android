package de.heiryn.nhentai.ui.common

import android.databinding.BindingAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import me.drakeet.multitype.MultiTypeAdapter
import de.heiryn.nhentai.model.Book
import de.heiryn.nhentai.util.ColorGenerator
import de.heiryn.nhentai.util.TextDrawable
import de.heiryn.nhentai.util.extension.*
import org.jetbrains.anko.*

object ExtraBindingAdapter: AnkoLogger {

	@BindingAdapter("bind:items")
	@JvmStatic fun setMultiTypeAdapterItems(view: RecyclerView, array: Array<*>?) {
		setMultiTypeAdapterItems(view, array?.toList())
	}

	@BindingAdapter("bind:items")
	@JvmStatic fun setMultiTypeAdapterItems(view: RecyclerView, list: List<*>?) {
		(view.adapter as? MultiTypeAdapter)?.items = list ?: mutableListOf<Any>()
		view.adapter?.notifyDataSetChanged()
	}

	@BindingAdapter("bind:isRefreshing")
	@JvmStatic fun setIsRefreshing(view: SwipeRefreshLayout, boolean: Boolean) {
		view.isRefreshing = boolean
	}

	@BindingAdapter("android:src")
	@JvmStatic fun setImageViewResource(imageView: ImageView, resource: Int) {
		imageView.setImageResource(resource)
	}

	@BindingAdapter("bind:imageUrl")
	@JvmStatic fun setImageViewUrl(imageView: ImageView, url: String?) {
		imageView.setImageDrawable(null)
		if (url != null) {
			async(UI) {
				Picasso.with(imageView.context)
						.load(url)
						.into(imageView, object: Callback {
							override fun onSuccess() { imageView.animateMaterial() }
							override fun onError() {}
						})
			}
		}
	}

	@BindingAdapter("bind:bookCover")
	@JvmStatic fun setImageViewBookCover(imageView: ImageView, book: Book?) {
		imageView.setImageDrawable(null)
		if (book != null) {
			imageView.background = TextDrawable.builder().buildRect(
					book.title.firstWord()?: "", ColorGenerator.MATERIAL.getColor(book))
			imageView.viewTreeObserver.addOnGlobalLayoutListener(
					object : ViewTreeObserver.OnGlobalLayoutListener {
						override fun onGlobalLayout() {
							book.images.thumbnail?.let {
								if (it.height > 0 && it.width > 0) {
									val width = imageView.measuredWidth
									val height = Math.round(
											width * (it.height.toDouble() / it.width)).toInt()
									imageView.layoutParams.height = height
									imageView.minimumHeight = height
								}
							}
							imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
						}
					}
			)
			async(UI) {
				try {
					Picasso.with(imageView.context)
							.load(book.thumbUrl)
							.into(imageView, object: Callback {
								override fun onSuccess() { imageView.animateMaterial() }
								override fun onError() {}
							})
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
		}
	}

}