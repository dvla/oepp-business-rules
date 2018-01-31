package uk.gov.dvla.rules.calculations;

import org.drools.compiler.compiler.DecisionTableFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import uk.gov.dvla.domain.OffenceCase;
import uk.gov.dvla.rules.HGVLevy;
import uk.gov.dvla.rules.PenaltyType;
import uk.gov.dvla.rules.BusinessRulesProcessor;
import uk.gov.dvla.rules.ProcessingContext;
import uk.gov.dvla.rules.constants.Fixtures;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.joda.time.LocalDate.now;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class PaymentAmountCalculationTest {

    private static BusinessRulesProcessor processor;


    @BeforeClass
    public static void setUpOnce() throws Exception {
        KieContainer kc;

        kc = KieServices.Factory.get().getKieClasspathContainer();

        //  LOG.debug("messages are " + kc.verify().getMessages().toString());
        //    assertThat(kc.verify().hasMessages(Message.Level.ERROR), is(false));
        /* display the generated rules */
        DecisionTableConfiguration dtableconfiguration
                = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dtableconfiguration.setInputType(DecisionTableInputType.XLS);

        String drlString = DecisionTableFactory
                .loadFromInputStream(ResourceFactory
                        .newClassPathResource("uk/gov/dvla/rules/offence-case-rules.xls")
                        .getInputStream(), dtableconfiguration);
        //  LOG.debug(drlString);

        //    post2017 = date("2017-04-01");
        //   pre2017 = date("2016-05-31");
        processor = new BusinessRulesProcessor();

        //    LOG.debug("version scheme-manager-rule-version sys="+CONTROL_VERSION);
    }

    //  @BeforeClass
    //  public static void init() {
    //       processor = new BusinessRulesProcessor();
    //  }

    @Test
    public void shouldUseStandardPenaltyAmountWhenLetterWasSentLessThan35DaysAgo() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setPenaltyLetterSentDate(now().minusDays(34).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.STANDARD));
    }

    @Test
    public void shouldUseElevatedPenaltyAmountWhenLetterWasSentMoreThan34DaysAgo() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setPenaltyLetterSentDate(now().minusDays(35).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.ELEVATED));
    }

    @Test
    public void shouldUseStandardPenaltyAmountWhenManualFirstResponseDateHasNotPassed() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setManualFirstResponseDate(now().plusDays(1).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.STANDARD));
    }

    @Test
    public void shouldUseStandardPenaltyAmountWhenOnManualFirstResponseDate() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setManualFirstResponseDate(now().toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.STANDARD));
    }

    @Test
    public void shouldUseElevatedPenaltyAmountWhenManualFirstResponseDateHasPassed() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setManualFirstResponseDate(now().minusDays(1).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.ELEVATED));
    }

    @Test
    public void shouldIgnorePenaltyLetterSentDateIfManualFirstResponseDateIsPresent() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setManualFirstResponseDate(now().minusDays(1).toDate())
                .setPenaltyLetterSentDate(now().minusDays(34).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.ELEVATED));
    }

    @Test
    public void shouldNotSetPenaltyTypeIfPenaltyLetterSentDateIsNull() {
        OffenceCase offenceCase = Fixtures.offenceCaseBuilder()
                .setPenaltyLetterSentDate(null).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(false));
    }

    @Test
    public void shouldUseNilPenaltyAmountForSection29()
    {
        OffenceCase offenceCase = Fixtures.offenceCaseS29Builder()
                .setPenaltyLetterSentDate(null).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.NIL));
    }


    @Test
    public void shouldUseNilPenaltyAmountForSection29withoutHGVLevy()
    {
        OffenceCase offenceCase = Fixtures.offenceCaseS29Builder()
                .setPenaltyLetterSentDate(null).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getHgvLevy().get(), is(HGVLevy.NOLEVY));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.NIL));
    }

    @Test
    public void shouldUseNilPenaltyAmountForSection29withHGVLevy()
    {
        OffenceCase offenceCase = Fixtures.offenceCaseS29HGVLevyBuilder()
                .setPenaltyLetterSentDate(null).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getHgvLevy().get(), is(HGVLevy.HGVLEVY));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.NIL));
    }

    @Test
    public void shouldUseStandardPenalty144aAmountWhenLetterWasSentLessThan34DaysAgo() {
        OffenceCase offenceCase = Fixtures.offenceCaseS144ABuilder()
                .setPenaltyLetterSentDate(now().minusDays(33).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.STANDARD144A));
    }

    @Test
    public void shouldUseElevatedPenalty144aAmountWhenLetterWasSentMoreThan33DaysAgo() {
        OffenceCase offenceCase = Fixtures.offenceCaseS144ABuilder()
                .setPenaltyLetterSentDate(now().minusDays(34).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.ELEVATED144A));
    }

    @Test
    public void shouldUseStandardPenalty144aAmountWhenManualFirstResponseDateHasNotPassed() {
        OffenceCase offenceCase = Fixtures.offenceCaseS144ABuilder()
                .setManualFirstResponseDate(now().plusDays(1).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.STANDARD144A));
    }

    @Test
    public void shouldUseStandardPenalty144aAmountWhenOnManualFirstResponseDate() {
        OffenceCase offenceCase = Fixtures.offenceCaseS144ABuilder()
                .setManualFirstResponseDate(now().toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.STANDARD144A));
    }

    @Test
    public void shouldUseElevatedPenalty144aAmountWhenManualFirstResponseDateHasPassed() {
        OffenceCase offenceCase = Fixtures.offenceCaseS144ABuilder()
                .setManualFirstResponseDate(now().minusDays(1).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.ELEVATED144A));
    }

    @Test
    public void shouldIgnorePenalty144aLetterSentDateIfManualFirstResponseDateIsPresent() {
        OffenceCase offenceCase = Fixtures.offenceCaseS144ABuilder()
                .setManualFirstResponseDate(now().minusDays(1).toDate())
                .setPenaltyLetterSentDate(now().minusDays(34).toDate()).create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getPenaltyType().isPresent(), is(true));
        assertThat(context.getPenaltyType().get(), is(PenaltyType.ELEVATED144A));
    }

}