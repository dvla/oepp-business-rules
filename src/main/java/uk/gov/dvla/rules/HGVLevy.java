package uk.gov.dvla.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

public enum HGVLevy {
    HGVLEVY("hgvlevy.amount"),
    NOLEVY("nolevy.amount");

    private static final Logger LOGGER = LoggerFactory.getLogger(HGVLevy.class);

    private static final String PROPERTIES_FILE = "/business-rules.properties";

    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        try (InputStream inputStream = HGVLevy.class.getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inputStream);
            LOGGER.debug("Properties file : " + PROPERTIES_FILE + " successfully loaded.");
        } catch (IOException ex) {
            LOGGER.error("There has been a problem loading the properties file : " + PROPERTIES_FILE);
            throw new UncheckedIOException("Cannot load properties file : " + PROPERTIES_FILE, ex);
        }
    }

    private final String propertyKey;

    HGVLevy(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(PROPERTIES.getProperty(propertyKey)).setScale(2, RoundingMode.HALF_UP);
    }
}
