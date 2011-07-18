package org.emonocot.checklist.controller;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.xml.XmlPath.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;

import com.jayway.restassured.RestAssured;

/**
 *
 * @author ben
 *
 */
public class ChecklistOaiPmhWebserviceFunctionalTest {

    /**
     *
     */
    private static final int TOTAL_NUMBER_OF_NODES = 6;

    /**
   *
   */
    private static final int NODES_IN_LOREACEAE = 5;

    /**
    *
    */
    private Properties properties;

    /**
     * @throws Exception
     *             if there is a problem instantiating the selenium server
     */
    @Before
    public final void setUp() throws Exception {
        Resource propertiesFile
        = new ClassPathResource("application.properties");
        properties = new Properties();
        properties.load(propertiesFile.getInputStream());
        RestAssured.baseURI = properties.getProperty(
                "functional.test.baseUri",
                "http://129.67.24.160");
        RestAssured.port = Integer.parseInt(
                properties.getProperty("functional.test.port",
                "80"));
        RestAssured.basePath = properties.getProperty(
                "functional.test.basePath",
                "/latest/checklist");

        if (properties.getProperty("http.proxyHost", null) != null
                && properties
                  .getProperty("http.proxyHost", null).length() > 0) {
            RestAssured.proxyHost = properties.getProperty("http.proxyHost",
                    null);
            RestAssured.proxyPort = Integer.parseInt(properties.getProperty(
                    "http.proxyPort", "8080"));
            RestAssured.proxyScheme = properties.getProperty(
                    "http.proxyScheme", "http");
        }
    }

    /**
     * Tests ListIdentifiers without any restrictions.
     */
    @Test
    public final void testListIdentifiers() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc").get("/oai").asString();
        assertEquals("There should be 6 identifiers returned",
                TOTAL_NUMBER_OF_NODES,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * Tests ListIdentifiers in a set.
     */
    @Test
    public final void testListIdentifiersInSet() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "set", "Loreaceae").get("/oai").asString();
        assertEquals("There should be 5 identifiers returned",
                NODES_IN_LOREACEAE,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * Tests ListIdentifiers where no nodes match.
     */
    @Test
    public final void testListIdentifiersInEmptySet() {
        expect().statusCode(HttpStatus.BAD_REQUEST.value()).given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "set", "Orchidaceae").get("/oai");
    }

    /**
     * Tests ListIdentifiers of object modified after a certain date.
     */
    @Test
    public final void testListIdentifiersFromDate() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "from", "2011-08-01T01:00:00Z")
                        .get("/oai").asString();
        assertEquals("There should be 1 identifier returned",
                1,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }

    /**
     * Tests ListIdentifiers of object modified before a certain date.
     */
    @Test
    public final void testListIdentifiersUntilDate() {

        String xml = given()
                .parameters("verb", "ListIdentifiers", "metadataPrefix",
                        "oai_dc", "until", "2011-08-01T01:00:00Z")
                        .get("/oai").asString();
        assertEquals("There should be 5 identifiers returned",
                NODES_IN_LOREACEAE,
                with(xml).get("OAI-PMH.ListIdentifiers.header.size()"));
    }
}