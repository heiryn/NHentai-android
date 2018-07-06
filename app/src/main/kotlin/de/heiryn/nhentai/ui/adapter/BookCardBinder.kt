package de.heiryn.nhentai.ui.adapter

import de.heiryn.nhentai.R
import de.heiryn.nhentai.databinding.ListItemBookCardBinding
import de.heiryn.nhentai.model.Book
import de.heiryn.nhentai.ui.common.NHBindingItemViewBinder

class BookCardBinder
	: NHBindingItemViewBinder<Book, ListItemBookCardBinding>(R.layout.list_item_book_card)