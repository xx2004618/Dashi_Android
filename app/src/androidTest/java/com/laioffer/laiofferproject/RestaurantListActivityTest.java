package com.laioffer.laiofferproject;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.laioffer.laiofferproject.R;
import com.laioffer.laiofferproject.RestaurantListActivity;

import org.junit.After;
import org.junit.Before;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;


public class RestaurantListActivityTest extends ActivityInstrumentationTestCase2<RestaurantListActivity> {

    private RestaurantListActivity restaurantListActivity;

    public RestaurantListActivityTest() {
        super(RestaurantListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        restaurantListActivity = getActivity();
    }

    // Tests.
    public void testListEntryCount() {
        onData(anything())
                .inAdapterView(withId(R.id.restaurant_list))
                .atPosition(19)
                .check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
