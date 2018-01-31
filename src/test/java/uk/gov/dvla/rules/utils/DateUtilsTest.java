package uk.gov.dvla.rules.utils;


import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DateUtilsTest {

    @Test
    public void isBeforeTodayShouldReturnTrueForADateInThePast() {

        DateTime beforeToday = DateTime.now().withTimeAtStartOfDay().minusDays(1);
        boolean result = DateUtils.isBeforeToday(beforeToday.toDate());

        assertThat(result, is(true));
    }

    @Test
    public void isBeforeTodayShouldReturnFalseForToday() {

        DateTime today = DateTime.now().withTimeAtStartOfDay();
        boolean result = DateUtils.isBeforeToday(today.toDate());

        assertThat(result, is(false));
    }

    @Test
    public void isBeforeTodayShouldReturnFalseForADateInTheFuture() {

        DateTime afterToday = DateTime.now().withTimeAtStartOfDay().plusDays(1);
        boolean result = DateUtils.isBeforeToday(afterToday.toDate());

        assertThat(result, is(false));
    }

    @Test
    public void isBeforeTodayShouldThrowExceptionIfDateIsNull() {
        try {
            DateUtils.isBeforeToday(null);
            fail("NullPointerException is expected as the date is null");
        } catch (NullPointerException npe) {
            assertThat(npe.getMessage(), is("Date is required"));
        }
    }
}