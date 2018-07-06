package de.heiryn.nhentai.ui.reader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.heiryn.nhentai.R
import de.heiryn.nhentai.databinding.ActivityBookReaderBinding
import de.heiryn.nhentai.model.Book
import de.heiryn.nhentai.ui.common.NHBindingActivity
import de.heiryn.nhentai.util.extension.objectAsJson

class BookReaderActivity: NHBindingActivity<ActivityBookReaderBinding>() {

	override val LAYOUT_RES_ID: Int = R.layout.activity_book_reader

	private val viewModel: BookReaderViewModel = BookReaderViewModel()

	override fun onViewCreated(savedInstanceState: Bundle?) {
		binding.vm = viewModel



		binding.init()
	}

	private fun ActivityBookReaderBinding.init() {

	}

	companion object {

		private val TAG = BookReaderActivity::class.java.simpleName

		private const val EXTRA_BOOK_JSON = "book_json"
		private const val EXTRA_PAGE = "page"

		@JvmStatic @JvmOverloads fun launch(context: Context, book: Book, page: Int = 1) {
			val intent = Intent(context, BookReaderActivity::class.java)
			intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
			intent.putExtra(EXTRA_BOOK_JSON, book.objectAsJson())
			intent.putExtra(EXTRA_PAGE, page)
			context.startActivity(intent)
		}

	}

}