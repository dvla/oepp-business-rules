package uk.gov.dvla.rules;

import org.kie.api.KieServices;
import org.kie.api.KieServices.Factory;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.StatelessKieSession;
import uk.gov.dvla.domain.OffenceCase;

public class BusinessRulesProcessor {

    private static final String SESSION_NAME = "OffenceCaseSession";

    private KieContainer container;

    public BusinessRulesProcessor() {
        container = Factory.get().getKieClasspathContainer();

        Results verificationResult = container.verify();
        if (verificationResult.hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Initialization errors: " + verificationResult.getMessages());
        }
    }

    public ProcessingContext process(OffenceCase offenceCase) {
        StatelessKieSession session = container.newStatelessKieSession(SESSION_NAME);
        KieRuntimeLogger logger = KieServices.Factory.get().getLoggers().newConsoleLogger((session));


        ProcessingContext context = new ProcessingContext(offenceCase);
        session.execute(context);
        return context;
    }

}
