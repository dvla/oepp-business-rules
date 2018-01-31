package uk.gov.dvla.rules.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility class for date calculations
 */
public class DateUtils {

    private DateUtils() {
        throw new AssertionError("This class should not be instantiated");
    }

    /**
     * Calculates the number of whole days between the specified date and today.
     * <p>
     * Method truncates the time of both the start date and today.
     *
     * @param date the start date, must not be null
     * @return number of days in the period
     */
    public static int daysUntilToday(Date date) {
        checkNotNull(date, "Date is required");
        return Days.daysBetween(new DateTime(date).withTimeAtStartOfDay(), DateTime.now().withTimeAtStartOfDay()).getDays();
    }

    /**
     * Returns true if the date is before today.
     *
     * @param date the date to check, must not be null.
     * @return true if the date is before today, otherwise false.
     */
    public static boolean isBeforeToday(Date date) {
        checkNotNull(date, "Date is required");
        DateTime today = DateTime.now().withTimeAtStartOfDay();
        return new DateTime(date).withTimeAtStartOfDay().isBefore(today);
    }

}
