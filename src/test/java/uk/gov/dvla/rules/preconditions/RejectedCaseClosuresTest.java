package uk.gov.dvla.rules.preconditions;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.gov.dvla.domain.OffenceCase;
import uk.gov.dvla.rules.BusinessRulesProcessor;
import uk.gov.dvla.rules.ProcessingContext;
import uk.gov.dvla.rules.RejectionReason;
import uk.gov.dvla.rules.constants.Fixtures;
import uk.gov.dvla.rules.preconditions.data.Closure;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dvla.rules.RejectionReason.*;

@RunWith(Parameterized.class)
public class RejectedCaseClosuresTest {

    private static BusinessRulesProcessor processor;

    @Parameterized.Parameter(0)
    public Closure closure;
    @Parameterized.Parameter(1)
    public RejectionReason expectedRejectionReason;

    @BeforeClass
    public static void init() {
        processor = new BusinessRulesProcessor();
    }

    @Parameterized.Parameters(name = "closure = {0}")
    public static Collection testData() {
        return Arrays.asList(new Object[][]{
                {Closure.withReason("OC"), PENALTY_ALREADY_PAID},
                {Closure.withReason("DD"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("NO"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("LL"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("CG"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("TU"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("DC").withDebtCollectionIndicator(true), PASSED_TO_DEBT_COLLECTORS},
                {Closure.withReason("SI"), PASSED_TO_DEBT_COLLECTORS},
                {Closure.withReason("IS"), PASSED_TO_DEBT_COLLECTORS},
                {Closure.withReason("MR"), PASSED_TO_DEBT_COLLECTORS},
                {Closure.withReason(null), INVALID_STATE},
                {Closure.withReason(""), INVALID_STATE},
                {Closure.withReason("  "), INVALID_STATE},
                {Closure.withReason("AC"), INVALID_STATE},
                {Closure.withReason("AP"), PENALTY_ALREADY_PAID},
                {Closure.withReason("CA"), INVALID_STATE},
                {Closure.withReason("CB"), INVALID_STATE},
                {Closure.withReason("CT"), INVALID_STATE},
                {Closure.withReason("DA"), INVALID_STATE},
                {Closure.withReason("IP"), INVALID_STATE},
                {Closure.withReason("JR"), INVALID_STATE},
                {Closure.withReason("LA"), INVALID_STATE},
                {Closure.withReason("LR"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("LV"), INVALID_STATE},
                {Closure.withReason("MV"), INVALID_STATE},
                {Closure.withReason("NA"), INVALID_STATE},
                {Closure.withReason("NC"), INVALID_STATE},
                {Closure.withReason("ND"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("NK"), INVALID_STATE},
                {Closure.withReason("NN"), INVALID_STATE},
                {Closure.withReason("NS"), INVALID_STATE},
                {Closure.withReason("PA"), INVALID_STATE},
                {Closure.withReason("PC"), INVALID_STATE},
                {Closure.withReason("PD"), INVALID_STATE},
                {Closure.withReason("PJ"), INVALID_STATE},
                {Closure.withReason("PL"), INVALID_STATE},
                {Closure.withReason("PM"), INVALID_STATE},
                {Closure.withReason("PO"), INVALID_STATE},
                {Closure.withReason("PS"), INVALID_STATE},
                {Closure.withReason("PV"), INVALID_STATE},
                {Closure.withReason("PW"), INVALID_STATE},
                {Closure.withReason("PX"), INVALID_STATE},
                {Closure.withReason("PY"), INVALID_STATE},
                {Closure.withReason("PZ"), INVALID_STATE},
                {Closure.withReason("RO"), INVALID_STATE},
                {Closure.withReason("RP"), INVALID_STATE},
                {Closure.withReason("RU"), INVALID_STATE},
                {Closure.withReason("SD"), INVALID_STATE},
                {Closure.withReason("SN"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("SP"), INVALID_STATE},
                {Closure.withReason("SR"), INVALID_STATE},
                {Closure.withReason("TC"), INVALID_STATE},
                {Closure.withReason("TP"), PENALTY_ALREADY_PAID},
                {Closure.withReason("TR"), INVALID_STATE},
                {Closure.withReason("UP"), NO_PAYMENT_REQUIRED},
                {Closure.withReason("WR"), INVALID_STATE},
                {Closure.withReason("Z1"), INVALID_STATE},
                {Closure.withReason("ZZ"), INVALID_STATE}
        });
    }

    @Test
    public void shouldRejectUnsupportedClosureReasonCode() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setClosureDate(closure.getDate())
                .setClosureReason(closure.getReason())
                .setInDebtCollection(closure.getDebtCollectionIndicator())
                .create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getRejectionReason().isPresent(), is(true));
        assertThat(context.getRejectionReason().get(), is(expectedRejectionReason));
    }

}
