package uk.gov.dvla.rules.preconditions;

import org.drools.core.xml.ExtensibleXmlParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.gov.dvla.domain.OffenceCase;
import uk.gov.dvla.rules.BusinessRulesProcessor;
import uk.gov.dvla.rules.ProcessingContext;
import uk.gov.dvla.rules.constants.Fixtures;
import uk.gov.dvla.rules.preconditions.data.Closure;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class AllowedCaseClosuresTest {

    private static BusinessRulesProcessor processor;

    @Parameterized.Parameter(0)
    public Closure closure;

    @BeforeClass
    public static void init() {
        processor = new BusinessRulesProcessor();
    }

    @Parameterized.Parameters(name = "closure = {0}")
    public static Collection testData() {
        return Arrays.asList(
                Closure.withReason("DC").withDebtCollectionIndicator(false),
                Closure.withReason("IC"),
                Closure.withReason("IE"),
                Closure.withReason("OT"),
                Closure.withReason("RR"),
                Closure.withReason("MP"),
                Closure.withReason("RS"),
                Closure.withReason("UE"),
                Closure.withReason("WO"),
                Closure.withReason("LO")

        );
    }

    @Test
    public void shouldRejectUnsupportedClosureReasonCode() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setClosureDate(closure.getDate())
                .setClosureReason(closure.getReason())
                .setInDebtCollection(closure.getDebtCollectionIndicator())
                .setInCourt(closure.getCourtIndicator())
                .create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getRejectionReason().isPresent(), is(false));
        assertThat(context.getPenaltyType().isPresent(), is(true));
    }

}