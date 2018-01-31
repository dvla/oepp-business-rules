package uk.gov.dvla.rules.preconditions;

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
import uk.gov.dvla.rules.*;
import uk.gov.dvla.rules.constants.Fixtures;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.joda.time.LocalDate.now;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CourtCaseTest {

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
    public void shouldSendToCourt()
    {
        OffenceCase offenceCase = Fixtures.offenceCaseS29HGVLevyBuilder()
                .setPenaltyLetterSentDate(null).setInCourt("Y").create();

        ProcessingContext context = processor.process(offenceCase);
        assertThat(context.getRejectionReason().isPresent(), is(true));
        assertThat(context.getRejectionReason().get(), is(RejectionReason.PASSED_TO_COURT));
    }




}
