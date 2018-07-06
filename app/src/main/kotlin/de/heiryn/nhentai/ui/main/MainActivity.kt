package de.heiryn.nhentai.ui.main

import android.os.Bundle
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import de.heiryn.nhentai.R
import de.heiryn.nhentai.databinding.ActivityMainBinding
import de.heiryn.nhentai.ui.common.NHBindingActivity
import de.heiryn.nhentai.ui.main.fragment.HomeFragment

class MainActivity: NHBindingActivity<ActivityMainBinding>(), BottomNavigation.OnMenuChangedListener {

	override val LAYOUT_RES_ID: Int = R.layout.activity_main

	private val homeFragment = HomeFragment()

	override fun onViewCreated(savedInstanceState: Bundle?) {
		binding.init()

		supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
	}

	private fun ActivityMainBinding.init() {
		bottomNavigation.setOnMenuChangedListener(this@MainActivity)
	}

	override fun onMenuChanged(item: BottomNavigation) {
		if (!supportFragmentManager.isStateSaved) {
			supportFragmentManager.beginTransaction().apply {
				when (item.id) {
					R.id.item_home -> replace(R.id.container, homeFragment)
					R.id.item_downloaded ->  replace(R.id.container, homeFragment)
					R.id.item_fav ->  replace(R.id.container, homeFragment)
					R.id.item_history ->  replace(R.id.container, homeFragment)
				}
			}.commit()
		}
	}

}