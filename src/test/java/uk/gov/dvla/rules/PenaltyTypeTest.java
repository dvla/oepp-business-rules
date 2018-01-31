package uk.gov.dvla.rules;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PenaltyTypeTest extends TestCase {

    @Test
    public void testGetAmount() throws Exception {
        assertThat(PenaltyType.STANDARD.getAmount(), is(BigDecimal.valueOf(40.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(PenaltyType.ELEVATED.getAmount(), is(BigDecimal.valueOf(80.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(PenaltyType.NIL.getAmount(), is (BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(PenaltyType.STANDARD144A.getAmount(), is(BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(PenaltyType.ELEVATED144A.getAmount(), is(BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP)));
    }


}