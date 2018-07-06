package de.heiryn.nhentai.api

import kotlinx.coroutines.experimental.*
import de.heiryn.nhentai.model.Book
import de.heiryn.nhentai.util.HttpUtils
import de.heiryn.nhentai.util.extension.readAsJsonObject

object BookApi {

	suspend fun getBook(bookId: String): Book? = ApiConstants.getBookDetailsUrl(bookId)
			.run(HttpUtils::requestUrl)
			?.readAsJsonObject()

	fun getBookAsync(bookId: String): Deferred<Book?> =
			async(CommonPool) { getBook(bookId) }

}