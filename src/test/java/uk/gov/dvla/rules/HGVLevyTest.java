package uk.gov.dvla.rules;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HGVLevyTest extends TestCase {

    @Test
    public void testGetAmount() throws Exception {
        assertThat(HGVLevy.HGVLEVY.getAmount(), is (BigDecimal.valueOf(300.00).setScale(2, RoundingMode.HALF_UP)));
        assertThat(HGVLevy.NOLEVY.getAmount(), is (BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP)));
    }


}