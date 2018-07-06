package de.heiryn.nhentai.ui.adapter

import de.heiryn.nhentai.R
import de.heiryn.nhentai.databinding.ItemBookCardFixedHeightBinding
import de.heiryn.nhentai.model.Book
import de.heiryn.nhentai.ui.common.NHBindingItemViewBinder

class FixedHeightBookCardBinder
	: NHBindingItemViewBinder<Book, ItemBookCardFixedHeightBinding>(R.layout.item_book_card_fixed_height)