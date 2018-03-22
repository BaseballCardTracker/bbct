/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common.activity.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import bbct.android.common.R;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.rule.DataTestRule;
import bbct.data.BaseballCard;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static bbct.android.common.test.matcher.BaseballCardMatchers.withYear;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

abstract public class BaseballCardListWithDataTest <T extends MainActivity> {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();
    @Rule
    public ActivityTestRule<T> activityTestRule;

    private static final String TAG = BaseballCardListWithDataTest.class.getName();

    private UiDevice device;
    private List<BaseballCard> expectedCards;
    private Instrumentation inst;
    private Activity activity = null;
    private List<BaseballCard> allCards;
    private BaseballCard newCard = null;
    private DatabaseUtil dbUtil;

    public BaseballCardListWithDataTest(Class<T> activityClass) {
        activityTestRule = new ActivityTestRule<>(activityClass);
    }

    @Before
    public void setUp() throws Exception {
        inst = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(inst);
        activity = activityTestRule.getActivity();
        allCards = dataTestRule.getAllCards();
        newCard = new BaseballCard(true, "Mint", "Code Guru Apps", 1993,
                1, 50000, 1, "Code Guru", "Code Guru Devs", "Catcher");

        dbUtil = new DatabaseUtil(inst.getTargetContext());
    }

    @After
    public void tearDown() throws RemoteException {
        device.setOrientationNatural();
    }

    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.containsAllBaseballCards(this.allCards));
        BBCTTestUtil.assertListViewContainsItems(this.allCards);
    }

    @Test
    public void testAddCardsMenuItem() {
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
    }

    @Test
    public void testFilterCardsMenuItem() {
        BBCTTestUtil.testMenuItem(R.id.filter_menu, FragmentTags.FILTER_CARDS);
    }

    @Test
    public void testHeader() {
        onView(withText(R.string.brand)).check(matches(isDisplayed()));
        onView(withText(R.string.year)).check(matches(isDisplayed()));
        onView(withText(R.string.number_col)).check(matches(isDisplayed()));
        onView(withText(R.string.player_name)).check(matches(isDisplayed()));
    }

    @Test
    public void testStateDestroyWithoutFilter() throws RemoteException {
        device.setOrientationLeft();
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    @Test
    public void testStateDestroyWithFilter() throws RemoteException {
        this.testYearFilter();
        device.setOrientationLeft();
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    @Test
    public void testStateDestroyClearFilter() throws RemoteException {
        this.testClearFilter();
        device.setOrientationLeft();
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    @Test
    public void testOnListItemClick() throws Throwable {
        Log.d(TAG, "testOnListItemClick()");
        int cardIndex = 3;
        BaseballCard expectedCard = allCards.get(cardIndex);
        onData(allOf(instanceOf(BaseballCard.class), is(expectedCard))).perform(click());
        BBCTTestUtil.assertAllEditTextContents(expectedCard);
    }

    @Test
    public void testAddDuplicateCard() throws Throwable {
        BaseballCard card = dataTestRule.getCard(0);
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(card);
        onView(withText(R.string.duplicate_card_title))
                .check(matches(isDisplayed()));
        onView(withText(R.string.duplicate_card_error))
                .check(matches(isDisplayed()));
        onView(withText(android.R.string.ok))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(R.string.duplicate_card_title))
                .check(doesNotExist());
        onView(withText(R.string.duplicate_card_error))
                .check(doesNotExist());
    }

    @Test
    public void testAddCardToPopulatedDatabase() throws Throwable {
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        allCards.add(newCard);
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    @Test
    public void testAddCardMatchingCurrentFilter() throws Throwable {
        testYearFilter();
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        expectedCards.add(newCard);
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    @Test
    public void testAddCardNotMatchingCurrentFilter() throws Throwable {
        testYearFilter();
        newCard = new BaseballCard(false, "Excellent", "Codeguru Apps",
                1976, 1, 50000, 1, "Codeguru", "Codeguru Devs", "Catcher");
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    @Test
    public void testAddCardAfterClearFilter() throws Throwable {
        testClearFilter();
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        allCards.add(newCard);
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    @Test
    public void testMarkAll() {
        this.markAll();
        this.assertAllCheckboxesChecked();
    }

    private void assertAllCheckboxesChecked() {
        onView(withId(R.id.select_all))
                .check(matches(isChecked()));

        for (int i = 0; i < allCards.size(); i++) {
            onData(instanceOf(BaseballCard.class))
                    .atPosition(i)
                    .check(matches(isChecked()));
        }
    }

    private void markAll() {
        onView(withId(R.id.select_all))
                .perform(click())
                .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteAll() throws Throwable {
        this.markAll();
        deleteCards();
        onView(withId(android.R.id.empty)).check(matches(isDisplayed()));
    }

    @Test
    public void testUnmarkAll() throws Throwable {
        this.markAll();
        onView(withId(R.id.select_all)).perform(click());
        assertNoCheckboxesChecked();
    }

    private void assertNoCheckboxesChecked() {
        onView(withId(R.id.select_all))
                .check(matches(isNotChecked()));

        for (int i = 0; i < allCards.size(); i++) {
            onData(instanceOf(BaseballCard.class))
                    .atPosition(i)
                    .check(matches(isNotChecked()));
        }
    }

    @Test
    public void testDeleteCardUsingFilter() throws Throwable {
        testYearFilter();

        int cardIndex = 0;
        final int year = 1993;
        expectedCards = BBCTTestUtil.filterList(allCards, withYear(year));
        expectedCards.remove(cardIndex);

        onData(instanceOf(BaseballCard.class))
                .atPosition(cardIndex)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
        deleteCards();
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    private void deleteCards() {
        onView(withId(R.id.delete_menu)).perform(click());
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.DELETE_MESSAGE);
        onView(withId(R.id.add_menu)).check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteCardNoFilter() throws Throwable {
        int cardIndex = 0;

        expectedCards = new ArrayList<>(allCards);
        expectedCards.remove(cardIndex);

        onData(instanceOf(BaseballCard.class))
                .atPosition(cardIndex)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
        deleteCards();
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    @Test
    public void testSelectionAfterSaveInstanceState() throws Throwable {
        Log.d(TAG, "testSelectionAfterSaveInstanceState()");

        int index = 1;
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));

        Log.d(TAG, "change orientation");
        device.setOrientationLeft();

        Log.d(TAG, "assertions");
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .check(matches(isChecked()));
    }

    @Test
    public void testYearFilter() {
        final int year = 1993;
        testSingleFilter(R.id.year_check, R.id.year_input, year + "", withYear(year));
    }

    @Test
    public void testClearFilter() {
        this.testYearFilter();
        BBCTTestUtil.testMenuItem(R.id.clear_filter_menu, FragmentTags.CARD_LIST);
        onView(withId(R.id.add_menu))
                .check(matches(isDisplayed()));
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    private void testSingleFilter(int checkId, int editId, String input,
                                  Matcher<BaseballCard> cardMatcher) {
        BBCTTestUtil.testMenuItem(R.id.filter_menu, FragmentTags.FILTER_CARDS);

        BBCTTestUtil.sendKeysToCurrFieldFilterCards(checkId, editId, input);
        onView(withId(R.id.save_menu)).perform(click());
        expectedCards = BBCTTestUtil.filterList(allCards, cardMatcher);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards);
        onView(withId(R.id.clear_filter_menu)).check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickCheckboxStartActionMode() {
        int index = 4;
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickCheckboxStopActionMode() {
        int index = 4;
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.add_menu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickCheckboxAll() {
        for(int i = 0; i < allCards.size(); ++i) {
            onData(instanceOf(BaseballCard.class))
                    .atPosition(i)
                    .onChildView(withId(R.id.checkmark))
                    .perform(click());
        }

        onView(withId(R.id.select_all))
                .check(matches(isChecked()));
    }

    @Test
    public void testOnCheckAllAndOnClickCheckbox() {
        onView(withId(R.id.select_all))
                .perform(click());
        onData(instanceOf(BaseballCard.class))
                .atPosition(1)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.select_all))
                .check(matches(isNotChecked()));
    }

    @Test
    public void testOnClickCheckboxAndOnCheckAll() {
        onData(instanceOf(BaseballCard.class))
                .atPosition(1)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.select_all))
                .perform(click());
        this.assertAllCheckboxesChecked();
    }

    @Test
    public void testOnItemLongClickStartActionMode() {
        int index = 4;
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .perform(longClick());
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickDoneButton() {
        this.testMarkAll();
        onView(withId(R.id.action_mode_close_button)).perform(click());
        onView(withId(R.id.add_menu)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all)).check(matches(isNotChecked()));
    }

}
