package app.slyworks.navigator_demo_app

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.collection.SimpleArrayMap
import app.slyworks.navigator.Navigator
import app.slyworks.navigator.interfaces.FragmentContinuationStateful
import app.slyworks.navigator_demo_app.databinding.ActivityHomeBinding
import java.lang.reflect.Modifier.PRIVATE

class HomeActivity : BaseActivity() {
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var navigator:FragmentContinuationStateful

    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding: ActivityHomeBinding

    private var fragmentMap: SimpleArrayMap<String, Int> = SimpleArrayMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initViews()
    }

    private fun initData(){
        fragmentMap.also{
            it.put(HomeFragment::class.simpleName, R.id.action_home)
            it.put(SearchFragment::class.simpleName, R.id.action_search)
            it.put(FavoritesFragment::class.simpleName, R.id.action_favorites)
        }

        navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

        this.onBackPressedDispatcher
            .addCallback(this,
                object: OnBackPressedCallback(true){
                    override fun handleOnBackPressed() {
                        if(supportFragmentManager.backStackEntryCount == 1){
                            if(ActivityUtils.isLastActivity())
                                ExitDialog.newInstance()
                                    .show(supportFragmentManager, "exit dialog")
                            else{
                                isEnabled = false
                                onBackPressed()
                                return
                            }
                        }else{
                            navigator.popBackStack{ updateActiveItem(fragmentMap[it]!!)}
                        }
                    }
                })
    }

    private fun initViews(){
        navigator.into(binding.fragmentContainerHome.id)
            .show(HomeFragment.newInstance())
            .navigate()
    }

    private fun updateActiveItem(@IdRes id:Int):Unit =
        binding.bnvMain.setSelectedItemId(id)
}