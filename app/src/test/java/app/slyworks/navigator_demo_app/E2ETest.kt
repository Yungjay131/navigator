package app.slyworks.navigator_demo_app

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import app.slyworks.navigator.Navigator
import app.slyworks.navigator_demo_app.HomeActivity
import app.slyworks.navigator_demo_app.SplashActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.jvm.Throws

/**
 *Created by Joshua Sylvanus, 10:32 AM, 17/11/2022.
 */
@RunWith(RobolectricTestRunner::class)
class E2ETest {
    //region Vars
    private lateinit var splashActivityMock: SplashActivity
    private lateinit var splashActivityReal: SplashActivity
    private lateinit var splashActivitySpy: SplashActivity
    //endregion

    /*@get:Rule
    val splashActivityRule = ActivityScenarioRule(SplashActivity::class.java)*/

    @Before
    fun setup(){
        splashActivityMock = mock()
        splashActivitySpy = spy()
        ActivityScenario.launch(SplashActivity::class.java).onActivity {
            splashActivityReal = it
        }
    }

    @Test
    fun checking(){
        onView(withId(R.id.tv_splash))
            .check(matches(isDisplayed()))
    }

    @Test
    fun `does addExtra() actually pass value`(){}

    @Test
    fun `is extra passed to addExtra() extra received`(){}

    @Test(expected = IllegalArgumentException::class)
    fun `does wrong type throw IllegalArgumentException in addExtra()`(){
                Navigator.intentFor<HomeActivity>(splashActivityReal)
                    .addExtra("key", Any())
                    .navigate()
    }

    @Test
    fun `does clearTop() actually clear last Activity`(){}

    @Test
    fun `does newAndClearTask() create a new task backstack`(){}

    @Test
    fun `does previousIsTop() make the previous Activity top`(){}

    @Test
    fun `does singleTop() make sure there is only 1 instance of Activity`(){}

    @Test
    fun `does finishCaller() actually finish Activity`(){
        whenever(splashActivityMock.navigateToAppropriateActivity())
            .then {
                Navigator.intentFor<HomeActivity>(splashActivityReal)
                    .finishCaller()
                    .navigate()
            }

        splashActivityMock.navigateToAppropriateActivity()

        assertThat(splashActivityReal.isFinishing).isTrue()
    }

    @Test
    fun `does navigate() work`(){
        whenever(splashActivityMock.navigateToAppropriateActivity())
            .then {
                Navigator.intentFor<HomeActivity>(splashActivityReal)
                    .newAndClearTask()
                    .navigate()
            }

        splashActivityMock.navigateToAppropriateActivity()

         onView(withId(R.id.fragment_container_home))
             .check(matches(withText("Home Fragment")))
    }

    @Test
    fun `does replace() work`(){
        whenever(splashActivityMock.navigateToAppropriateActivity())
            .then {
                Navigator.intentFor<HomeActivity>(splashActivityReal)
                    .newAndClearTask()
                    .navigate()
            }

        splashActivityMock.navigateToAppropriateActivity()

        onView(withText("Search"))
            .perform(click())

        onView(withId(R.id.tv_search))
            .check(matches(isDisplayed()))

        assertThat(splashActivityReal.supportFragmentManager.backStackEntryCount).isEqualTo(0)
    }

    @Test
    fun `does hideCurrent() work`(){}

    @Test
    fun `does show() work`(){}

    @Test
    fun `does after() work`(){}

    @Test
    fun `does popBackStack() work`(){}

    @Test
    fun `does onDestroy() destroy FragmentManager instance`(){

    }

}