/**
 *
 */
package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.hibernate.FetchMode;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class SearchableObjectDaoImpl extends
        SearchableDaoImpl<SearchableObject> implements SearchableObjectDao {

	/**
    *
    */
   private static Map<String, Fetch[]> FETCH_PROFILES;
   
   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
       FETCH_PROFILES.put("taxon-with-image", new Fetch[] {
               new Fetch("image", FetchMode.SELECT) });
   }
	
    /**
     *
     */
    public SearchableObjectDaoImpl() {
        super(SearchableObject.class, Taxon.class, Image.class);
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return SearchableObjectDaoImpl.FETCH_PROFILES.get(profile);
    }

    @Override
    protected final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {

        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case CLASS:
            facetingRequest = facetContext.name(facetName.name())
                    .onField(ProjectionConstants.OBJECT_CLASS).discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case CONTINENT:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("continent").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case REGION:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("region").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case AUTHORITY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("sources.identifier").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case FAMILY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("family").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        default:
            break;
        }
    }

    /**
    *
    * @param facetName Set the facet name
    * @param facetManager Set the facet manager
    * @param selectedFacet Set the selected facet
    */
   @Override
   protected final void selectFacet(final FacetName facetName,
           final FacetManager facetManager,
           final String selectedFacet) {
       switch (facetName) {
       case CLASS:
       case CONTINENT:
       case REGION:
       case AUTHORITY:
       case FAMILY:
           doSelectFacet(facetName, facetManager, selectedFacet);
       default:
           break;
       }
   }

    @Override
    protected final void addFacet(final Page<SearchableObject> page,
            final FacetName facetName, final FacetManager facetManager) {
       switch (facetName) {
       case CLASS:
       case CONTINENT:
       case REGION:
       case AUTHORITY:
       case FAMILY:
           page.addFacets(facetName.name(),
                   facetManager.getFacets(facetName.name()));
       default:
           break;
       }
   }

    /**
     * @return the fields to search
     */
    @Override
    protected final String[] getDocumentFields() {
        return new String[]{"name", "caption", "title", "content.content"};
    }

    /**
     *
     */
    @Override
    protected Class getAnalyzerType() {
        return Taxon.class;
    }
}
