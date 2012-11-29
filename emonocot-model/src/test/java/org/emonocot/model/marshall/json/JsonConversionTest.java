package org.emonocot.model.marshall.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.easymock.EasyMock;
import org.emonocot.api.GroupService;
import org.emonocot.api.ImageService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.model.Annotation;
import org.emonocot.model.Distribution;
import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.RecordType;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.Place;
import org.emonocot.portal.model.AceDto;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.security.acls.domain.BasePermission;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.DefaultCoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 *
 * @author ben
 *
 */
public class JsonConversionTest {

    /**
     *
     */
    private ObjectMapper objectMapper;

    /**
     *
     */
    private ReferenceService referenceService;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private ImageService imageService;

    /**
     *
     */
    private UserService userService;

    /**
     *
     */
    private GroupService groupService;

	/**
	 * 
	 */
	private Place place;

    /**
     *
     */
    @Before
    public final void setUp() {
        referenceService = EasyMock.createMock(ReferenceService.class);
        taxonService = EasyMock.createMock(TaxonService.class);
        imageService = EasyMock.createMock(ImageService.class);
        userService = EasyMock.createMock(UserService.class);
        groupService = EasyMock.createMock(GroupService.class);
        CustomObjectMapperFactory objectMapperFactory = new CustomObjectMapperFactory();
        objectMapperFactory.setReferenceService(referenceService);
        objectMapperFactory.setTaxonService(taxonService);
        objectMapperFactory.setImageService(imageService);
        objectMapperFactory.setGroupService(groupService);
        objectMapperFactory.setUserService(userService);
        objectMapper = objectMapperFactory.getObject();
        
    	place = new Place();
    	place.setTitle("testName");
    	place.setIdentifier("test.jk.triangle");
    	Coordinate[] coords = {new Coordinate(57,26), new Coordinate(0,0), new Coordinate(25,25), new Coordinate(57,26)};
    	LinearRing shell = new LinearRing(new DefaultCoordinateSequenceFactory().create(coords), new GeometryFactory());
    	Polygon shape = new Polygon(shell, null, new GeometryFactory());
    	place.setShape(shape);
    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testReadTaxon() throws Exception {
        Reference reference = new Reference();        
        EasyMock.expect(
                referenceService.load(EasyMock
                        .eq("urn:kew.org:wcs:publication:1"))).andReturn(
                reference).anyTimes();
        
        EasyMock.replay(referenceService, imageService);
        String content = "{\"identifier\":\"urn:kew.org:wcs:taxon:2295\",\"scientificName\":\"Acorus\",\"namePublishedIn\":\"urn:kew.org:wcs:publication:1\", \"descriptions\": [{\"type\":\"habitat\",\"description\":\"Lorem ipsum\", \"references\":[\"urn:kew.org:wcs:publication:1\"]}], \"distribution\":[{\"location\":\"REU\"}]}";
        Taxon taxon = (Taxon) objectMapper.readValue(content, Taxon.class);
        EasyMock.verify(referenceService, imageService);

        assertNotNull("Returned object should not be null", taxon);
        assertEquals("The identifier should be \"urn:kew.org:wcs:taxon:2295\"",
                "urn:kew.org:wcs:taxon:2295", taxon.getIdentifier());
        assertEquals("The name should be \"Acorus\"", "Acorus", taxon.getScientificName());
        assertFalse("There should be some content", taxon.getDescriptions()
                .isEmpty());
        Description habitat = null;
        for(Description d : taxon.getDescriptions()) {
        	if(d.getType().equals(DescriptionType.habitat)) {
        		habitat = d;
        		break;
        	}
        } 
        assertNotNull("There should information on habitat", habitat);
        assertEquals("The habitat should be 'Lorem ipsum'",habitat.getDescription(), "Lorem ipsum");
        assertEquals("The taxon should be set on the content", habitat.getTaxon(), taxon);
        assertTrue("The reference should be set on the content",habitat.getReferences()
                        .contains(reference));
        assertEquals("The protologue should be set", reference,
                taxon.getNamePublishedIn());
        assertFalse("The taxon should contain a distribution", taxon
                .getDistribution().isEmpty());
        Distribution reunion = null;
        for(Distribution d : taxon.getDistribution()) {
        	if(d.getLocation().equals(Country.REU)) {
        		reunion = d;
        		break;
        	}
        } 
        assertNotNull("The taxon should occur in Reunion", reunion);

    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testWriteTaxon() throws Exception {
        String content = "{\"identifier\":\"urn:kew.org:wcs:taxon:2295\",\"name\":\"Acorus\",\"protologue\":\"urn:kew.org:wcs:publication:1\"}";
        Reference reference = new Reference();
        reference.setIdentifier("urn:kew.org:wcs:publication:1");
        Taxon taxon = new Taxon();
        taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
        taxon.setCreated(new DateTime());
        taxon.setScientificName("Acorus");
        Description description = new Description();
        description.setDescription("Lorem ipsum");
        description.setType(DescriptionType.habitat);
        description.getReferences().add(reference);
        description.setTaxon(taxon);
        taxon.getDescriptions().add(description);
        Distribution distribution = new Distribution();
        distribution.setTaxon(taxon);
        distribution.setLocation(Country.REU);
        taxon.getDistribution().add(distribution);
        taxon.getReferences().add(reference);
        taxon.setNamePublishedIn(reference);
        for (int i = 0; i < 3; i++) {
            Image image = new Image();
            image.setIdentifier("urn:identifier:image:" + i);
            image.setTaxon(taxon);
            taxon.getImages().add(image);
        }
        try {
            objectMapper.writeValueAsString(taxon);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testWriteImage() throws Exception {
        Taxon taxon = new Taxon();
        taxon.setIdentifier("urn:kew.org:wcs:taxon:2295");
        String content = "{\"identifier\":\"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\",\"caption\":\"Acorus\",\"taxa\":[\"urn:kew.org:wcs:taxon:2295\"]}";
        Image image = new Image();
        image.setIdentifier("urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg");
        image.setTitle("Acorus");
        image.getTaxa().add(taxon);

        try {
           String output = objectMapper.writeValueAsString(image);
           System.out.println(output);
           
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testReadImage() throws Exception {
        Taxon taxon = new Taxon();
        EasyMock.expect(
                taxonService.load(EasyMock.eq("urn:kew.org:wcs:taxon:2295"),
                        EasyMock.eq("taxon-page"))).andReturn(taxon).times(1);
        EasyMock.replay(referenceService, taxonService);

        String content = "{\"location\":null,\"id\":null,\"description\":null,\"taxon\":null,\"taxa\":[\"urn:kew.org:wcs:taxon:2295\"],\"title\":\"Acorus\",\"format\":null,\"subject\":null,\"spatial\":null,\"authority\":null,\"license\":null,\"created\":null,\"modified\":null,\"creator\":null,\"identifier\":\"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\"}";
        Image image = (Image) objectMapper.readValue(content, Image.class);
        EasyMock.verify(referenceService, taxonService);

        assertNotNull("Returned object should not be null", image);
        assertEquals(
                "The identifier should be \"urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg\"",
                "urn:http:upload.wikimedia.org:wikipedia.commons.2.25:Illustration_Acorus_calamus0.jpg",
                image.getIdentifier());
        assertEquals("The caption should be \"Acorus\"", "Acorus",
                image.getTitle());
        assertTrue("The taxon should be set on the image", image.getTaxa()
                .contains(taxon));
    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testReadReference() throws Exception {

        EasyMock.replay(referenceService, imageService);

        String content = "{\"identifier\":\"urn:kew.org:wcs:publication:123\",\"title\":\"Lorem ipsum\"}";
        Reference reference = (Reference) objectMapper.readValue(content,
                Reference.class);
        EasyMock.verify(referenceService, imageService);

        assertNotNull("Returned object should not be null", reference);
        assertEquals(
                "The identifier should be \"urn:kew.org:wcs:publication:123\"",
                "urn:kew.org:wcs:publication:123", reference.getIdentifier());
        assertEquals("The title should be \"Lorem ipsum\"", "Lorem ipsum",
                reference.getTitle());
    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testWriteUser() throws Exception {
        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword("123456");
        Group group = new Group();
        group.setIdentifier("groupname");
        user.getGroups().add(group);
        try{
          objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     *
     * @throws Exception
     *             if there is a problem serializing the object
     */
    @Test
    public final void testReadUser() throws Exception {
        Group group = new Group();
        EasyMock.expect(groupService.load(EasyMock.eq("TestGroup"))).andReturn(
                group);
        EasyMock.replay(groupService, userService);

        String content = "{\"identifier\":\"test@example.com\",\"password\":\"Lorem ipsum\", \"groups\":[\"TestGroup\"]}";
        User user = (User) objectMapper.readValue(content, User.class);
        EasyMock.verify(userService, groupService);

        assertNotNull("Returned object should not be null", user);
        assertEquals("The username should be \"test@example.com\"",
                "test@example.com", user.getIdentifier());
        assertEquals("The group should be \"TestGroup\"", group, user
                .getGroups().iterator().next());
    }

   /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public final void testWriteJobInstance() throws Exception {
        Map<String, JobParameter> jobParameterMap
            = new HashMap<String, JobParameter>();
        jobParameterMap.put("authority.name", new JobParameter("test"));
        JobInstance jobInstance = new JobInstance(1L, new JobParameters(
                jobParameterMap), "testJob");
        jobInstance.setVersion(1);

        try {
            objectMapper.writeValueAsString(jobInstance);
        } catch (Exception e) {
            fail("No exception expected here");
        }

    }

   /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public final void testReadJobInstance() throws Exception {
        JobInstance jobInstance = objectMapper.readValue("{\"id\":1,\"jobName\":\"testJob\", \"version\":\"1\",\"parameters\":[{\"name\":\"authority.name\",\"type\":\"STRING\",\"value\":\"test\"}]}", JobInstance.class);
        assertEquals(jobInstance.getId(), new Long(1L));
        assertEquals(jobInstance.getJobName(), "testJob");
        assertEquals(
                jobInstance.getJobParameters().getString("authority.name"),
                "test");
    }

   /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public final void testWriteAnnotation() throws Exception {
        Annotation annotation = new Annotation();
        annotation.setCode(AnnotationCode.Absent);
        annotation.setDateTime(new DateTime());
        annotation.setIdentifier("1");
        annotation.setRecordType(RecordType.Taxon);
        annotation.setText("wibble");
        annotation.setValue("wibble");
        Taxon t = new Taxon();
        t.setIdentifier("testIdentifier");
        annotation.setAnnotatedObj(t);
        try{
            objectMapper.writeValueAsString(annotation);
          } catch (Exception e) {
              fail();
          }

    }

   /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public final void testAnnotation() throws Exception {
        Taxon taxon = new Taxon();
        EasyMock.expect(taxonService.find(EasyMock.eq("testIdentifier")))
                .andReturn(taxon);
        EasyMock.replay(taxonService);
        Annotation annotation = objectMapper.readValue("{\"value\":\"wibble\",\"id\":null,\"type\":null,\"authority\":null,\"code\":\"Absent\",\"text\":\"wibble\",\"jobId\":null,\"annotatedObj\":\"testIdentifier\",\"recordType\":\"Taxon\",\"dateTime\":1321973454966,\"identifier\":\"1\"}", Annotation.class);
        EasyMock.verify(taxonService);

        assertNotNull(annotation.getAnnotatedObj());
        assertEquals(taxon, annotation.getAnnotatedObj());
    }

    /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public final void testWriteAce() throws Exception {
        AceDto ace = new AceDto();
        User user = new User();
        ace.setPermission(BasePermission.CREATE);

        ace.setObject("testIdentifier");
        ace.setPrincipal("userIdentifier");
        try {
            objectMapper.writeValueAsString(ace);
         } catch (Exception e) {
              fail("No exception expected here");
         }
    }

    /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public final void testAce() throws Exception {

        AceDto aceDto = objectMapper.readValue("{\"principal\":\"userIdentifier\",\"object\":\"testIdentifier\",\"permission\":\"CREATE\"}", AceDto.class);

        assertEquals("testIdentifier", aceDto.getObject());
        assertEquals("userIdentifier", aceDto.getPrincipal());
        assertEquals(BasePermission.CREATE, aceDto.getPermission());
    }

   /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Ignore
    @Test
    public final void testWriteJobExecutionInfo() throws Exception {
        JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
        jobExecutionInfo.setStartTime(new DateTime(2012,3,29,14,17,0,0));
        String actual = objectMapper.writeValueAsString(jobExecutionInfo);
        assertEquals("Actual JSON should match expected JSON","{\"jobInstance\":null,\"exitCode\":null,\"duration\":null,\"exitDescription\":null,\"resource\":null,\"id\":null,\"startTime\":\"2012-03-29T14:17:00.000+01:00\",\"status\":null}", actual);
    }

    /**
    *
    * @throws Exception
    *             if there is a problem serializing the object
    */
    @Test
    public final void testJobExecutionException() throws Exception {
        JobExecutionException jobExecutionException = objectMapper.readValue("{\"errors\" : { \"spring.integration.http.handler.error\" : \"A Spring Integration handler raised an exception while handling an HTTP request.  The exception is of type class org.springframework.integration.MessageHandlingException and it has a message: (org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException: A job instance already exists and is complete for parameters={query.string=from Source, attempt=9}.  If you want to run this job again, change the parameters.)\" } }", JobExecutionException.class);
    }
    
    @Test
    public final void testWriteMultiPolygon() throws Exception {
    	String serialized = objectMapper.writeValueAsString(place);
    	System.out.println(serialized);
    	assertTrue("Expected JSON to contain the identifier", serialized.contains("\"identifier\":\"test.jk.triangle\""));
    	assertTrue("Expected JSON to contain the name", serialized.contains("\"title\":\"testName\""));
    	assertTrue("Expected JSON to contain the shape", serialized.contains("\"shape\":\"POLYGON ((57 26, 0 0, 25 25, 57 26))\""));
    }
    
    @Test
    public final void testReadMultiPolygon() throws Exception {
    	String placeJson = "{\"title\":\"testName\",\"id\":null,\"shape\":\"POLYGON ((57 26, 0 0, 25 25, 57 26))\",\"point\":null,\"fipsCode\":null,\"authority\":null,\"identifier\":\"test.jk.triangle\",\"license\":null,\"created\":null,\"modified\":null}";
    	Place desrialized = objectMapper.readValue(placeJson, Place.class);
    	
    	assertEquals("Expected identifier to be " + place.getIdentifier(), place.getIdentifier(), desrialized.getIdentifier());
    	assertEquals("Expected name to be " + place.getTitle(), place.getTitle(), desrialized.getTitle());
    	assertEquals("Expected shape to be " + place.getShape().toText(),place.getShape().toText(),desrialized.getShape().toText());
    }
}
