package org.emonocot.job.gbif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;

public class HasMoreRecords implements StepExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(HasMoreRecords.class);
	
	private StepExecution stepExecution;
	
	private Unmarshaller unmarshaller;
	
	public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }
	
	public ExitStatus execute(String temporaryFileName) throws Exception {
        try {
            StaxEventItemReader<Header> staxEventItemReader = new StaxEventItemReader<Header>();
            staxEventItemReader.setFragmentRootElementName(
                    "{http://portal.gbif.org/ws/response/gbif}header");
            staxEventItemReader.setUnmarshaller(unmarshaller);
            staxEventItemReader.setResource(new FileSystemResource(
                    temporaryFileName));

            staxEventItemReader.afterPropertiesSet();
            staxEventItemReader.open(stepExecution.getExecutionContext());

            Header header = staxEventItemReader.read();
            staxEventItemReader.close();
            if (header == null) {
                logger.info("Header Not Found");                
                return new ExitStatus("NO_MORE_RECORDS");
            } else {
                if(header.getSummary().getNext() != null){
					stepExecution.getJobExecution().getExecutionContext().remove("startindex");
					stepExecution.getJobExecution().getExecutionContext().put("startindex", header.getSummary().getNext());
                    return new ExitStatus("HAS_MORE_RECORDS");
				} else {
					return new ExitStatus("NO_MORE_RECORDS");
				}
            }
        } catch (UnexpectedInputException e) {
            logger.error(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                logger.error(ste.toString());
            }
            return ExitStatus.FAILED;
        } catch (ParseException e) {
            logger.error(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                logger.error(ste.toString());
            }
            return ExitStatus.FAILED;
        } catch (Exception e) {
            logger.error(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                logger.error(ste.toString());
            }
            return ExitStatus.FAILED;
        }
	}
	
	public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
	
}