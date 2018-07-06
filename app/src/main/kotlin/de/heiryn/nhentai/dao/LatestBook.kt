package de.heiryn.nhentai.dao

import de.heiryn.nhentai.model.Book
import de.heiryn.nhentai.util.extension.HawkInterface

object LatestBook: HawkInterface {

	override val STORE_NAME: String = LatestBook::class.java.toString()

	var list by nullableProperty<List<Book>>()
	var nowPage by property(defValue = 1)

}