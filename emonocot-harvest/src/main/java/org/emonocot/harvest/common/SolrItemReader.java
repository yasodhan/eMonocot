/**
 * 
 */
package org.emonocot.harvest.common;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.SearchableService;
import org.emonocot.model.SearchableObject;
import org.springframework.batch.item.database.AbstractPagingItemReader;

/**
 * @author jk
 *
 */
public class SolrItemReader<T extends SearchableObject> extends AbstractPagingItemReader<T> {

	private SearchableService<T> service = null;

	private String queryString = null;
	
	private Map<String,String> selectedFacets = new HashMap<String,String>();
	
	private String sort = null;
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	/**
	 * @param service the service to set
	 */
	public void setService(SearchableService<T> service) {
		this.service = service;
		logger.info("Service set " + service.toString());
	}
	
	public void setSelectedFacets(String[] selectedFacets) {
		if(selectedFacets != null) {
		    for(String selectedFacet : selectedFacets) {
		        String[] f = selectedFacet.split("\\=");
		        this.selectedFacets.put(f[0],f[1]);
		    }
		}
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.AbstractPagingItemReader#doReadPage()
	 */
	@Override
	protected void doReadPage() {
	    results = service.search(queryString, null, getPageSize(), getPage(), null, selectedFacets, sort, "object-page").getRecords();
		logger.debug("Search for " + queryString + " (page number " + getPage() + " got a page of " + results.size()
					+ " (results");
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.AbstractPagingItemReader#doJumpToPage(int)
	 */
	@Override
	protected void doJumpToPage(int itemIndex) {
	}

}