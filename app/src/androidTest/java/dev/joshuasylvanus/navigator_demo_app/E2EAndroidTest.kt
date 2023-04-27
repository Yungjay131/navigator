package dev.joshuasylvanus.navigator_demo_app

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.junit.Test

import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.Navigator.Companion.getExtra
import dev.joshuasylvanus.navigator.interfaces.ActivityContinuation
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import dev.joshuasylvanus.navigator_demo_app.home.HomeActivity
import dev.joshuasylvanus.navigator_demo_app.login.LoginActivity
import dev.joshuasylvanus.navigator_demo_app.onboarding.OnboardingActivity
import dev.joshuasylvanus.navigator_demo_app.registration.RegistrationActivity
import dev.joshuasylvanus.navigator_demo_app.registration.RegistrationGeneral1Fragment
import dev.joshuasylvanus.navigator_demo_app.registration.RegistrationGeneral2Fragment
import dev.joshuasylvanus.navigator_demo_app.registration.RegistrationOTP1Fragment
import dev.joshuasylvanus.navigator_demo_app.splash.SplashActivity
import org.junit.Before

/**
 * Created by Joshua Sylvanus, 10:32 AM, 17/11/2022.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class E2EAndroidTest {

    private lateinit var splashActivityMock: SplashActivity
    private lateinit var splashActivityReal: SplashActivity


    @Before
    fun setup(){
        splashActivityMock = mock()
        ActivityScenario.launch(SplashActivity::class.java).onActivity {
            splashActivityReal = it
        }
    }

    @Test
    fun checking(){
        onView(withId(R.id.tvText))
            .check(matches(withText("SplashActivity")))
    }

    @Test
    fun doesAddExtra_actuallyPassValue_toNewActivity(){
        Navigator.intentFor<HomeActivity>(splashActivityReal)
            .addExtra("key", Any())
            .navigate()

        val receivedString:String =
            getCurrentActivity().intent.getExtra("key","no_value")!!

        assertThat(receivedString).isEqualTo("no_value")
    }

    @Test(expected = IllegalArgumentException::class)
    fun doesWrongType_throwIllegalArgumentException_inAddExtraFunc(){
         Navigator.intentFor<HomeActivity>(splashActivityReal)
             .addExtra("key", Any())
             .navigate()
    }

    @Test
    fun doesClearTopFunc_actuallyClearLastActivity(){
        Navigator.intentFor<HomeActivity>(splashActivityReal)
            .clearTop()
            .navigate()

        assertThat(splashActivityReal.isFinishing).isTrue()
    }

    @Test
    fun doesNewAndClearTaskFunc_createANewTask(){
        Navigator.intentFor<HomeActivity>(splashActivityReal)
            .newAndClearTask()
            .navigate()

        /* activity count should be 1 */
        assertThat(ActivityUtils.getActivityCount()).isEqualTo(1)
    }

    @Test
    fun doesPreviousIsTopFunc_makeThePreviousActivityTop(){
        Navigator.intentFor<LoginActivity>(splashActivityReal)
            .previousIsTop()
            .navigate()

        Navigator.intentFor<HomeActivity>(getCurrentActivity())
            .navigate()

        pressBack()

        onView(withId(R.id.tvText)).check(matches(withText("SplashActivity_")))
    }

    @Test
    fun doesSingleTopFunc_makeSureThere_isOnly1InstanceOfActivity(){}

    @Test
    fun doesFinishCallerFunc_actuallyFinishActivity(){
        Navigator.intentFor<HomeActivity>(splashActivityReal)
            .finishCaller()
            .navigate()

        assertThat(splashActivityReal.isFinishing).isTrue()
    }

    @Test
    fun doesNavigateFuncWork(){
        Navigator.intentFor<RegistrationActivity>(splashActivityReal)
            .navigate()

        onView(withId(R.id.tvText))
            .check(matches(withText("RegistrationGeneral0Fragment")))
    }

    @Test
    fun doesShowFuncWork(){
        Navigator.intentFor<RegistrationActivity>(splashActivityReal)
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .show(RegistrationGeneral1Fragment.newInstance())
            .navigate()

        onView(withId(R.id.tvText)).check(matches(withText("RegistrationGeneral2Fragment_t")))
    }

    @Test
    fun doesReplaceFuncWork(){
        Navigator.intentFor<RegistrationActivity>(splashActivityReal)
            .navigate()

        /* starting from RegistrationGeneral0Fragment */
        onView(withId(R.id.tvText)).check(matches(withText("RegistrationGeneral0Fragment")))

        /* replacing the fragment */
        (getCurrentActivity() as RegistrationActivity).navigator
            .replace(RegistrationGeneral1Fragment.newInstance())
            .navigate()

        onView(withId(R.id.tvText)).check(matches(withText("RegistrationGeneral1Fragment")))

        assertThat(splashActivityReal.supportFragmentManager.fragments.size).isEqualTo(1)
    }

    @Test
    fun doesAfterFuncWork(){
        var wasFunctionCalled:Boolean = false
        val func:() -> Unit = { wasFunctionCalled = true }

        Navigator.intentFor<RegistrationActivity>(splashActivityReal)
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .after(func)
            .navigate()

        assertThat(wasFunctionCalled).isFalse()
    }

    @Test
    fun doesPopBackStackFuncWork(){
        Navigator.intentFor<RegistrationActivity>(splashActivityReal)
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .show(RegistrationGeneral2Fragment.newInstance())
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .popBackStack()

        assertThat(
            getCurrentActivity()
                .supportFragmentManager
                .fragments
                .size).isEqualTo(10)
    }

    @Test
    fun doesPopBackStack_toSpecificFragmentFunc_work(){
        Navigator.intentFor<RegistrationActivity>(splashActivityReal)
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .show(RegistrationGeneral1Fragment.newInstance())
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .show(RegistrationGeneral2Fragment.newInstance())
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .popBackStack(RegistrationGeneral1Fragment::class.simpleName!!)

        assertThat(
            getCurrentActivity()
                .supportFragmentManager
                .fragments
                .size).isEqualTo(10)
    }

    @Test
    fun doesOnDestroyFunc_destroyFragmentManagerInstance(){
        var wasFuncCalled:Boolean = false
        val func:() -> Unit = { wasFuncCalled = true }

        Navigator.intentFor<RegistrationActivity>(splashActivityReal)
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .show(RegistrationGeneral1Fragment.newInstance())
            .navigate()

        (getCurrentActivity() as RegistrationActivity).navigator
            .onDestroy(func)

        assertThat(wasFuncCalled).isFalse()
    }

    @Test
    fun whenNavigatingToOnboardingActivity_isSplashActivityCleared(){
        onView(withId(R.id.btnNext))
            .perform(click())

        onView(withId(R.id.tvText))
            .check(matches(withText("OnboardingActivity")))

        assertThat(splashActivityReal.isDestroyed).isTrue()
    }

    @Test
    fun whenNavigatingToOnboardingActivity_doesOnboardingActivity_becomeTaskRoot(){
        navigateToOnboardingActivity()

        onView(withId(R.id.tvText))
            .check(matches(withText("OnboardingActivity")))

        val a:Activity = getCurrentActivity()
        assertThat(a::class).isEqualTo(OnboardingActivity::class)
        assertThat(a.isTaskRoot()).isTrue()
    }

    @Test
    fun whenNavigatingToOnboardingActivity_doesOnboardingActivity_displayExitDialog_onBackPressed(){
        navigateToOnboardingActivity()

        onView(withId(R.id.tvText))
            .check(matches(withText("OnboardingActivity")))

        val a:Activity = getCurrentActivity()
        assertThat(a::class).isEqualTo(OnboardingActivity::class)
        assertThat(a.isTaskRoot()).isTrue()
    }


    @Test
    fun doesNavigatingToRegistrationActivity_withExtras_goToOTP1Fragment(){
        navigateToRegistrationActivity_withExtras()

        onView(withId(R.id.tvText))
            .check(matches(withText("RegistrationOTP1Fragment")))
    }

    @Test
    fun doesNavigatingTo_RegistrationGeneral1Fragment_work(){
        navigateToRegistrationActivity_RegistrationGeneral1Fragment()

        onView(withId(R.id.tvText))
            .check(matches(withText("RegistrationGeneral1Fragment")))
    }

     @Test
    fun doesNavigatingTo_RegistrationGeneral2Fragment_work(){
        navigateToRegistrationActivity_RegistrationGeneral2Fragment()

        onView(withId(R.id.tvText))
            .check(matches(withText("RegistrationGeneral2Fragment")))
    }

    @Test
    fun doesNavigatingTo_RegistrationOTP1Fragment_work(){
        navigateToRegistrationActivity_RegistrationOTP1Fragment()

        onView(withId(R.id.tvText))
            .check(matches(withText("RegistrationOTP1Fragment")))
    }

    @Test
    fun doesNavigatingTo_RegistrationOTP2Fragment_work(){
        navigateToRegistrationActivity_RegistrationOTP2Fragment()

        onView(withId(R.id.tvText))
            .check(matches(withText("RegistrationOTP2Fragment")))
    }

    @Test
    fun doesNavigatingTo_VerificationActivityFromRegOTP2Frag_work(){
        navigateToVerificationActivity_fromRegOTP2()

        onView(withId(R.id.tvText))
            .check(matches(withText("VerificationActivity")))
    }

    @Test
    fun whenNavigatingToVerificationActivity_fromRegOTP2Frag_doesPreviousIsTop_work(){
         navigateToVerificationActivity_fromRegOTP2()

        pressBack()

        onView(withId(R.id.tvText))
            .check(matches(withText("OnboardingActivity")))
    }

    @Test
    fun doesNavigatingToLoginActivity_work(){
        navigateToLoginActivity()

        onView(withId(R.id.tvText))
            .check(matches(withText("LoginActivity")))
    }

    @Test
    fun doesNavigatingTo_HomeActivityFromVerificationActivity_work(){
        navigateToHomeActivity_fromVerificationActivity()

        onView(withId(R.id.tvText))
            .check(matches(withText("HomeFragment")))
    }

    private fun navigateToOnboardingActivity(){
        onView(withId(R.id.btnNext))
            .perform(click())
    }

    private fun navigateToLoginActivity(){
        navigateToOnboardingActivity()

        onView(withId(R.id.btn_login))
            .perform(click())
    }

    private fun navigateToRegistrationActivity_withoutExtras(){
        navigateToOnboardingActivity()

        onView(withId(R.id.btn_get_started))
            .perform(click())
    }

    private fun navigateToRegistrationActivity_withExtras(){
        navigateToOnboardingActivity()

        onView(withId(R.id.btn_get_started))
            .perform(longClick())
    }

    private fun navigateToRegistrationActivity_RegistrationGeneral1Fragment(){
        navigateToRegistrationActivity_withoutExtras()

        onView(withId(R.id.btnNext))
            .perform(click())
    }

    private fun navigateToRegistrationActivity_RegistrationGeneral2Fragment(){
        navigateToRegistrationActivity_RegistrationGeneral1Fragment()

        onView(withId(R.id.btnNext))
            .perform(click())
    }

    private fun navigateToRegistrationActivity_RegistrationOTP1Fragment(){
        navigateToRegistrationActivity_RegistrationGeneral2Fragment()

        onView(withId(R.id.btnNext))
            .perform(click())
    }

    private fun navigateToRegistrationActivity_RegistrationOTP2Fragment(){
        navigateToRegistrationActivity_RegistrationOTP1Fragment()

        onView(withId(R.id.btnNext))
            .perform(click())
    }

    private fun navigateToVerificationActivity_fromRegOTP2(){
        navigateToRegistrationActivity_RegistrationOTP2Fragment()

        onView(withId(R.id.btnNext))
            .perform(click())
    }

    private fun navigateToHomeActivity_fromVerificationActivity(){
        navigateToLoginActivity()

        onView(withId(R.id.btnNext))
            .perform(click())
    }


    private fun getCurrentActivity(): AppCompatActivity {
        var currentActivity:Activity? = null

        getInstrumentation().waitForIdleSync()

        getInstrumentation().runOnMainSync {
            val activities:Collection<Activity> =
                ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            if(activities.iterator().hasNext())
                currentActivity = activities.iterator().next()
        }

        return currentActivity as AppCompatActivity
    }
}