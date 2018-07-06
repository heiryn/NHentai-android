package de.heiryn.nhentai.ui.main.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import me.drakeet.multitype.MultiTypeAdapter
import de.heiryn.nhentai.R
import de.heiryn.nhentai.databinding.FragmentNewHomeBinding
import de.heiryn.nhentai.ui.adapter.BookCardBinder
import de.heiryn.nhentai.ui.common.NHBindingFragment
import de.heiryn.nhentai.ui.common.setOnLoadMoreListener
import de.heiryn.nhentai.util.extension.*

class HomeFragment: NHBindingFragment<FragmentNewHomeBinding>() {

	override val LAYOUT_RES_ID: Int = R.layout.fragment_home

	private val adapter by lazy { MultiTypeAdapter().apply { registerOne(BookCardBinder()) } }

	private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
    }

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		binding?.vm = viewModel

		binding?.init()
	}

	private fun FragmentNewHomeBinding.init() {
		recyclerView.adapter = adapter
		recyclerView.setOnLoadMoreListener(viewModel::onNext)

		swipeRefreshLayout.setColorSchemeResources(R.color.deep_purple_500)
		swipeRefreshLayout.setOnRefreshListener(viewModel::onRefresh)
	}

}