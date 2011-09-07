/**
 *
 */
package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.dao.FacetName;
import org.emonocot.persistence.dao.SearchableObjectDao;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.stereotype.Repository;

/**
 * @author jk00kg
 *
 */
@Repository
public class SearchableDaoImpl extends DaoImpl<SearchableObject> implements
        SearchableObjectDao {

    /**
     *
     */
    public SearchableDaoImpl() {
        super(SearchableObject.class);
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        // TODO Auto-generated method stub
        return null;
    }

   /**
    *
    * @param facetContext The faceting context of this request
    * @param facetName The name of the facet required
    * @return the faceting context
    */
    @Override
    protected final FacetingRequest createFacetingRequest(
            final FacetContext facetContext, final FacetName facetName) {

        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case CLASS:
            facetingRequest = facetContext.name(facetName.name())
                    .onField(ProjectionConstants.OBJECT_CLASS).discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        case CONTINENT:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("continent").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        case AUTHORITY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("authorities.name").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        case FAMILY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("family").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            break;
        default:
            break;
        }
        return facetingRequest;
    }

    /**
     * @return the fields to search
     */
    @Override
    protected final String[] getDocumentFields() {
        return new String[]{"name", "caption", "title"};
    }

    @Override
    protected Class getAnalyzerType() {
        return Taxon.class;
    }
}
