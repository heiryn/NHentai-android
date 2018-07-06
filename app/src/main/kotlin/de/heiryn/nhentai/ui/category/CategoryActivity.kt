package de.heiryn.nhentai.ui.category

import android.app.ActivityManager
import android.app.ActivityOptions
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import de.heiryn.nhentai.R
import de.heiryn.nhentai.databinding.ActivityCategoryBinding
import de.heiryn.nhentai.model.Tag
import de.heiryn.nhentai.ui.common.NHBindingActivity
import de.heiryn.nhentai.ui.common.observers
import de.heiryn.nhentai.ui.widget.SwipeBackCoordinatorLayout
import de.heiryn.nhentai.util.extension.jsonAsObject
import de.heiryn.nhentai.util.extension.objectAsJson

class CategoryActivity: NHBindingActivity<ActivityCategoryBinding>(),
		SwipeBackCoordinatorLayout.OnSwipeListener {

	override val LAYOUT_RES_ID: Int = R.layout.activity_category

	private val pagerAdapter by lazy { CategoryPagerAdapter(supportFragmentManager) }

	private lateinit var viewModel: CategoryViewModel

	override fun onViewCreated(savedInstanceState: Bundle?) {
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			ActivityOptions.makeTaskLaunchBehind()
		}

        viewModel = ViewModelProviders.of(this)[CategoryViewModel::class.java]

		binding.vm = viewModel

		viewModel.tag.observers += { _, _ ->
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				setTaskDescription(ActivityManager.TaskDescription(
						viewModel.tag.get().type + ":" + viewModel.tag.get().name
				))
			}
		}
		viewModel.tag.set(intent.getStringExtra(EXTRA_DATA).jsonAsObject())

		binding.init()
	}

	private fun ActivityCategoryBinding.init() {
		swipeBackLayout.setOnSwipeListener(this@CategoryActivity)
		tabLayout.setupWithViewPager(pager)
		pager.adapter = pagerAdapter
	}

	override fun canSwipeBack(dir: Int): Boolean =
			(dir == SwipeBackCoordinatorLayout.UP_DIR
					|| (dir == SwipeBackCoordinatorLayout.DOWN_DIR && binding.appBarLayout.y >= 0))
					&& (pagerAdapter.getItem(binding.pager.currentItem) as CategoryListFragment)
					.canSwipeBack(dir)

	override fun onSwipeProcess(percent: Float) {
		binding.root.setBackgroundColor(SwipeBackCoordinatorLayout.getBackgroundColor(percent))
	}

	override fun onSwipeFinish(dir: Int) {
		finish()
	}

	inner class CategoryPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

		private val latestListFragment by lazy {
			CategoryListFragment.newInstance(viewModel.tag.get(), true)
		}
		private val popularListFragment by lazy {
			CategoryListFragment.newInstance(viewModel.tag.get(), false)
		}

		override fun getItem(position: Int): Fragment = when (position) {
			0 -> latestListFragment
			1 -> popularListFragment
			else -> throw IllegalArgumentException("No more pages")
		}

		override fun getPageTitle(position: Int): CharSequence? =
				resources.getStringArray(R.array.category_tabs_title)[position]

		override fun getCount(): Int = 2

	}

	companion object {

		private val TAG = CategoryActivity::class.java.simpleName

		private const val EXTRA_DATA = "extra_data"

		@JvmStatic fun launch(context: Context, tag: Tag) {
			val intent = Intent(context, CategoryActivity::class.java)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_NEW_TASK)
			} else {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			}
			intent.putExtra(EXTRA_DATA, tag.objectAsJson())
			context.startActivity(intent)
		}

	}

}