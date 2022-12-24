package dev.joshuasylvanus.navigator_demo_app

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.joshuasylvanus.navigation_feature.Navigator
import org.junit.Test

import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

/**
 * Created by Joshua Sylvanus, 10:32 AM, 17/11/2022.
 */
@RunWith(AndroidJUnit4::class)
class E2EAndroidTest {
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
    fun doesAddExtra_actuallyPassValue(){}

    @Test
    fun isExtraPassedToAddExtra_received(){}

    @Test(expected = IllegalArgumentException::class)
    fun doesWrongType_throwIllegalArgumentException_inAddExtraFunc(){
                Navigator.intentFor<HomeActivity>(splashActivityReal)
                    .addExtra("key", Any())
                    .navigate()
    }

    @Test
    fun doesClearTopFunc_actuallyClearLastActivity(){}

    @Test
    fun doesNewAndClearTaskFunc_createANewTask(){}

    @Test
    fun doesPreviousIsTopFunc_makeThePreviousActivityTop(){}

    @Test
    fun doesSingleTopFunc_makeSureThere_isOnly1InstanceOfActivity(){}

    @Test
    fun doesFinishCallerFunc_actuallyFinishActivity(){
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
    fun doesNavigateFuncWork(){
         onView(withId(R.id.btn_splash))
             .perform(click())

         onView(withText("Home Fragment"))
             .check(matches(withText("Home Fragment")))
    }

    @Test
    fun doesReplaceFuncWork(){
        onView(withText("Search"))
            .perform(click())

        onView(withId(R.id.tv_search))
            .check(matches(isDisplayed()))

        assertThat(splashActivityReal.supportFragmentManager.backStackEntryCount).isEqualTo(0)
    }

    @Test
    fun doesHideCurrentFuncWork(){}

    @Test
    fun doesShowFuncWork(){}

    @Test
    fun doesAfterFuncWork(){}

    @Test
    fun doesPopBackStackFuncWork(){}

    @Test
    fun doesOnDestroyFunc_destroyFragmentManagerInstance(){}

}