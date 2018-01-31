package uk.gov.dvla.rules.constants;

import uk.gov.dvla.domain.OffenceCase;

import java.math.BigDecimal;
import java.util.Date;

import static uk.gov.dvla.rules.constants.TestConstants.SUPPORTED_CASE_TYPE;
import static uk.gov.dvla.rules.constants.TestConstants.SUPPORTED_S144A_CASE_TYPE;
import static uk.gov.dvla.rules.constants.TestConstants.SUPPORTED_S29_CASE_TYPE;

public class Fixtures {

    public static OffenceCase.Builder offenceCaseBuilder() {
        return new OffenceCase.Builder()
                .setCaseType(SUPPORTED_CASE_TYPE)
                .setPenaltyLetterSentDate(new Date())
                .setPayableArrearsAmount(BigDecimal.valueOf(20.00))
                .setPaidEnforcementArrearsAmount(BigDecimal.valueOf(10.00));
    }

    public static OffenceCase.Builder offenceCaseS29Builder()
    {
        return new OffenceCase.Builder()
                .setCaseType(SUPPORTED_S29_CASE_TYPE)
                .setPayableArrearsAmount(BigDecimal.valueOf(55.00));
    }

    public static OffenceCase.Builder offenceCaseS29HGVLevyBuilder() {
        return new OffenceCase.Builder()
                .setCaseType(SUPPORTED_S29_CASE_TYPE)
                .setPayableArrearsAmount(BigDecimal.valueOf(55.00))
                .sets11Case("Y");
    }

    public static OffenceCase.Builder offenceCaseS144ABuilder() {
        return new OffenceCase.Builder()
                .setCaseType(SUPPORTED_S144A_CASE_TYPE)
                .setPenaltyLetterSentDate(new Date());
    }

}
