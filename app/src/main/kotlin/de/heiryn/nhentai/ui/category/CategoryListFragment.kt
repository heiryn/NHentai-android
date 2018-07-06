package de.heiryn.nhentai.ui.category

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import me.drakeet.multitype.MultiTypeAdapter
import de.heiryn.nhentai.R
import de.heiryn.nhentai.api.ApiConstants
import de.heiryn.nhentai.api.PageApi
import de.heiryn.nhentai.databinding.FragmentCategoryListBinding
import de.heiryn.nhentai.model.Book
import de.heiryn.nhentai.model.Tag
import de.heiryn.nhentai.ui.adapter.BookCardBinder
import de.heiryn.nhentai.ui.common.NHBindingFragment
import de.heiryn.nhentai.ui.common.NHViewModel
import de.heiryn.nhentai.ui.common.setOnLoadMoreListener
import de.heiryn.nhentai.ui.widget.SwipeBackCoordinatorLayout
import de.heiryn.nhentai.util.extension.jsonAsObject
import de.heiryn.nhentai.util.extension.objectAsJson
import de.heiryn.nhentai.util.extension.registerOne

class CategoryListFragment: NHBindingFragment<FragmentCategoryListBinding>() {

	override val LAYOUT_RES_ID: Int = R.layout.fragment_category_list

	private lateinit var viewModel: CategoryListViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)[CategoryListViewModel::class.java]

		arguments?.let {
			viewModel.tag = it.getString(ARG_TAG_DATA).jsonAsObject()
			viewModel.isLatestType = it.getBoolean(ARG_IS_LATEST_TYPE)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding?.vm = viewModel

		binding?.init()

		if (savedInstanceState == null) {
			viewModel.onRefresh()
		}
	}

	private fun FragmentCategoryListBinding.init() {
		recyclerView.setOnLoadMoreListener(viewModel::onNext)
		recyclerView.layoutManager = StaggeredGridLayoutManager(
				2, StaggeredGridLayoutManager.VERTICAL)
		recyclerView.adapter = MultiTypeAdapter().apply { registerOne(BookCardBinder()) }
	}

	fun canSwipeBack(dir: Int): Boolean =
			SwipeBackCoordinatorLayout.canSwipeBack(binding?.recyclerView, dir)

	class CategoryListViewModel: NHViewModel() {

		lateinit var tag: Tag
		var isLatestType = true

		val items = ObservableField<MutableList<Book>>(mutableListOf())
		val isRefreshing = ObservableField<Boolean>(false)
		val currentPage = ObservableField(1)

		@Synchronized fun onRefresh() = ui {
			if (!isRefreshing.get()) {
				isRefreshing.set(true)

				val result = PageApi.getPageListAsync(
						ApiConstants.getTagUrl(tag, !isLatestType, 1)).await()
				if (result?.result?.isNotEmpty() == true) {
					items.set(result.result!!.toMutableList())
					currentPage.set(1)
				} else {
					// TODO Show error
					error("Error")
				}

				isRefreshing.set(false)
			}
		}

		@Synchronized fun onNext() = ui {
			if (!isRefreshing.get()) {
				isRefreshing.set(true)

				val result = PageApi.getPageListAsync(
						ApiConstants.getTagUrl(tag, !isLatestType, currentPage.get() + 1)).await()
				if (result?.result?.isNotEmpty() == true) {
					items.set((items.get() + result.result!!).toMutableList())
					currentPage.set(currentPage.get() + 1)
				} else {
					// TODO Show error
					error("Error")
				}

				isRefreshing.set(false)
			}
		}

	}

	companion object {

		private const val ARG_TAG_DATA = "tag_data"
		private const val ARG_IS_LATEST_TYPE = "latest_type"

		fun newInstance(tag: Tag, latestType: Boolean): CategoryListFragment =
				CategoryListFragment().apply {
					arguments = Bundle().apply {
						putString(ARG_TAG_DATA, tag.objectAsJson())
						putBoolean(ARG_IS_LATEST_TYPE, latestType)
					}
				}

	}

}