package com.laioffer.laiofferproject;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

/**
 * Created by xiangxiao on 2016/9/8.
 */
public class RestaurantMapActivityTest extends ActivityInstrumentationTestCase2<RestaurantMapActivity> {
    private RestaurantMapActivity restaurantMapActivityTest;

    public RestaurantMapActivityTest() {
        super(RestaurantMapActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        restaurantMapActivityTest = getActivity();
    }

    // Tests.
    public void testListEntryCount() {
        onData(anything())
                .inAdapterView(withId(R.id.restaurant_map))
                .atPosition(19)
                .check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
