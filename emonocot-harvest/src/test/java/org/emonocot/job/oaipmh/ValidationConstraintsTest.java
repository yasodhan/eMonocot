package org.emonocot.job.oaipmh;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.emonocot.api.SourceService;
import org.emonocot.job.oaipmh.Validator;
import org.emonocot.model.source.Source;
import org.emonocot.model.taxon.Taxon;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Test to verify that the OaiPmhProcessor recognised constraints on the objects
 * and tries to rectify where possible.
 *
 * http://build.e-monocot.org/bugzilla/show_bug.cgi?id=101
 *
 * @author ben
 *
 */
public class ValidationConstraintsTest {

    /**
    *
    */
   private Validator processor;

   /**
    *
    */
   private Taxon taxon;

   /**
    *
    */
   private SourceService sourceService;

   /**
    * @throws Exception if something goes wrong
    */
   @Before
   public final void setUp() throws Exception {
       taxon = new Taxon();
       taxon.setIdentifier("identifier");
       LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
       validatorFactoryBean.afterPropertiesSet();
       processor = new Validator();

       sourceService = EasyMock.createMock(SourceService.class);
       processor.setSourceService(sourceService);
       processor.setValidator(validatorFactoryBean);
       processor.setSourceName("test");
       processor.beforeStep(new StepExecution("test", new JobExecution(1L)));
   }

   /**
    * @throws Exception if something goes wrong
    */
   @Test
   public final void testValidObject() throws Exception {
       String validAuthorship = "                                        ";
       taxon.setAuthorship(validAuthorship);
       EasyMock.replay(sourceService);

       Taxon t = processor.process(taxon);
       assertEquals("Authorship not be changed", t.getAuthorship(), validAuthorship);
       EasyMock.verify(sourceService);
   }

   /**
    * @throws Exception if something goes wrong
    */
   @Test
   public final void testInvalidRootObject() throws Exception {
       taxon.setAuthorship("                                                                                                                                                                                                                                                                                                                                                                                                                                                        ");
       EasyMock.expect(
               sourceService.load(EasyMock.eq("test"))).andReturn(new Source());
       EasyMock.replay(sourceService);

       Taxon t = processor.process(taxon);
       assertEquals("Authorship be truncated", t.getAuthorship().length(), 128);
       EasyMock.verify(sourceService);
   }
}