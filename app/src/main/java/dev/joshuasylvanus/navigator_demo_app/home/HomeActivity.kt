package dev.joshuasylvanus.navigator_demo_app.home

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.collection.SimpleArrayMap
import androidx.fragment.app.Fragment
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.*
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivityHomeBinding
import java.lang.reflect.Modifier.PRIVATE

class HomeActivity : BaseActivity() {
    @IdRes
    private var selectedItem:Int = -1

    lateinit var navigator: FragmentContinuationStateful

    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding: ActivityHomeBinding

    private var fragmentMap: SimpleArrayMap<String, Int> = SimpleArrayMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }


    private fun initViews(){
    fragmentMap.also{
        it.put(Home0Fragment::class.simpleName, R.id.action_home)
        it.put(SearchFragment::class.simpleName, R.id.action_search)
        it.put(FavoritesFragment::class.simpleName, R.id.action_favorites)
    }

    navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

    this.onBackPressedDispatcher
        .addCallback(this, MOnBackPressedCallback(this, navigator, { updateActiveItem(fragmentMap[it]!!) }))

    navigator
        .into(binding.fragmentContainerHome.id)
        .show(Home0Fragment.newInstance())
        .navigate()

        binding.bnvMain.setOnItemSelectedListener(::handleBnvMenuItemClick)
        binding.bnvMain.setOnItemReselectedListener(::handleBnvMenuItemClick)
    }

    private fun updateActiveItem(@IdRes id:Int):Unit =
        binding.bnvMain.setSelectedItemId(id)

    private fun handleBnvMenuItemClick(item:MenuItem):Boolean{
        if(item.itemId == selectedItem)
            return true

        selectedItem = item.itemId
        return when(item.itemId){
            R.id.action_home -> {
                inflateFragment(Home0Fragment.newInstance())
                true
            }
            R.id.action_search -> {
                inflateFragment(SearchFragment.newInstance())
                true
            }
            R.id.action_favorites -> {
                inflateFragment(FavoritesFragment.newInstance())
                true
            }
            else -> throw UnsupportedOperationException()
        }
    }

    private fun inflateFragment(f: Fragment){
        navigator
            .show(f)
            .navigate()
    }

}