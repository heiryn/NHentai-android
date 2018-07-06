package de.heiryn.nhentai.ui.category

import android.databinding.ObservableField
import de.heiryn.nhentai.model.Tag
import de.heiryn.nhentai.ui.common.NHViewModel

class CategoryViewModel: NHViewModel() {

	val tag: ObservableField<Tag> = ObservableField()

}