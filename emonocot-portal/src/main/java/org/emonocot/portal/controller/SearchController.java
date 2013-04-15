package org.emonocot.portal.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.SearchableObject;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.Dimension;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author ben
 * 
 */
@Controller
public class SearchController {

	private static Logger queryLog = LoggerFactory.getLogger("query");

	private static Logger logger = LoggerFactory
			.getLogger(SearchController.class);

	private SearchableObjectService searchableObjectService;

	/**
	 * 
	 * @param newSearchableObjectService
	 *            set the service to search across all 'searchable' objects
	 */
	@Autowired
	public void setSearchableObjectService(
			SearchableObjectService newSearchableObjectService) {
		this.searchableObjectService = newSearchableObjectService;
	}

	/**
	 * @param query
	 * @param start
	 * @param limit
	 * @param spatial
	 * @param responseFacets
	 * @param sort
	 * @param selectedFacets
	 * @return
	 */
	private Page<? extends SearchableObject> runQuery(String query,
			Integer start, Integer limit, String spatial,
			String[] responseFacets, Map<String, String> facetPrefixes,
			String sort, Map<String, String> selectedFacets) {
		Page<? extends SearchableObject> result = searchableObjectService
				.search(query, spatial, limit, start, responseFacets,
						facetPrefixes, selectedFacets, sort, null);
		queryLog.info("Query: \'{}\', start: {}, limit: {},"
				+ "facet: [{}], {} results", new Object[] { query, start,
				limit, selectedFacets, result.getSize() });
		result.putParam("query", query);

		return result;
	}

	/**
	 * 
	 * @param view
	 *            Set the view name
	 * @param className
	 *            Set the class name
	 * @return the default limit
	 */
	private Integer setLimit(String view, String className) {
		if (view == null || view == "") {
			if (className == null) {
				return 10;
			} else if (className.equals("org.emonocot.model.Image")) {
				return 24;
			} else {
				return 10;
			}
		} else if (view.equals("list")) {
			return 10;
		} else if (view.equals("grid")) {
			return 24;
		} else {
			return 24;
		}
	}

	/**
	 * 
	 * @param query
	 *            Set the query
	 * @param limit
	 *            Limit the number of returned results
	 * @param start
	 *            Set the offset
	 * @param facets
	 *            The facets to set
	 * @param view
	 *            Set the view
	 * @param model
	 *            Set the model
	 * 
	 * @return a model and view
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "view", required = false) String view,
			Model model) {

		Map<String, String> selectedFacets = null;
		if (facets != null && !facets.isEmpty()) {
			selectedFacets = new HashMap<String, String>();
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
			logger.debug(selectedFacets.size()
					+ " facets have been selected from " + facets.size()
					+ " available");
		} else {
			logger.debug("There were no facets available to select from");
		}

		// Decide which facets to return
		List<String> responseFacetList = new ArrayList<String>();
		Map<String, String> facetPrefixes = new HashMap<String, String>();
		responseFacetList.add("base.class_s");
		responseFacetList.add("taxon.family_s");
		responseFacetList.add("taxon.distribution_TDWG_0_ss");
		responseFacetList.add("searchable.sources_ss");
		String className = null;
		if (selectedFacets == null) {
			logger.debug("No selected facets, setting default response facets");
		} else {
			if (selectedFacets.containsKey("base.class_s")) {
				className = selectedFacets.get("base.class_s");
				if (className.equals("org.emonocot.model.Taxon")) {
					logger.debug("Adding taxon specific facets");
					responseFacetList
							.add("taxon.measurement_or_fact_IUCNConservationStatus_txt");
					responseFacetList
							.add("taxon.measurement_or_fact_Lifeform_txt");
					responseFacetList
							.add("taxon.measurement_or_fact_Habitat_txt");
					responseFacetList.add("taxon.taxon_rank_s");
					responseFacetList.add("taxon.taxonomic_status_s");
				}
			}
			if (selectedFacets.containsKey("taxon.distribution_TDWG_0_ss")) {
				logger.debug("Adding region facet");
				responseFacetList.add("taxon.distribution_TDWG_1_ss");
				facetPrefixes.put("taxon.distribution_TDWG_1_ss",
						selectedFacets.get("taxon.distribution_TDWG_0_ss")
								+ "_");
			} else {
				selectedFacets.remove("taxon.distribution_TDWG_1_ss");
			}
		}
		String[] responseFacets = new String[] {};
		responseFacets = responseFacetList.toArray(responseFacets);
		limit = setLimit(view, className);

		// Run the search
		Page<? extends SearchableObject> result = runQuery(query, start, limit,
				null, responseFacets, facetPrefixes, sort, selectedFacets);

		result.putParam("view", view);
		result.setSort(sort);
		model.addAttribute("result", result);
		return "search";
	}

	/**
	 * 
	 * @param query
	 *            Set the query
	 * @param limit
	 *            Limit the number of returned results
	 * @param start
	 *            Set the offset
	 * @param facets
	 *            The facets to set
	 * @param x1
	 *            the first latitude
	 * @param x2
	 *            the second latitude
	 * @param y1
	 *            the first longitude
	 * @param y2
	 *            the second longitude
	 * @param view
	 *            Set the view
	 * @param model
	 *            Set the model
	 * 
	 * @return a model and view
	 */
	@RequestMapping(value = "/spatial", method = RequestMethod.GET)
	public String spatial(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "x1", required = false) Double x1,
			@RequestParam(value = "y1", required = false) Double y1,
			@RequestParam(value = "x2", required = false) Double x2,
			@RequestParam(value = "y2", required = false) Double y2,
			@RequestParam(value = "featureId", required = false) String featureId,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "view", required = false) String view,
			Model model) {
		String spatial = null;
		DecimalFormat decimalFormat = new DecimalFormat("###0.0");
		if (x1 != null
				&& y1 != null
				&& x2 != null
				&& y2 != null
				&& (x1 != 0.0 && y1 != 0.0 && x2 != 0.0 && x2 != 0.0 && y2 != 0.0)) {
			spatial = "Intersects(" + decimalFormat.format(x1) + " "
					+ decimalFormat.format(y1) + " " + decimalFormat.format(x2)
					+ " " + decimalFormat.format(y2) + ")";
		}

		Map<String, String> selectedFacets = null;
		if (facets != null && !facets.isEmpty()) {
			selectedFacets = new HashMap<String, String>();
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
			logger.debug(selectedFacets.size()
					+ " facets have been selected from " + facets.size()
					+ " available");
		} else {
			logger.debug("There were no facets available to select from");
		}

		// Decide which facets to return
		List<String> responseFacetList = new ArrayList<String>();
		responseFacetList.add("base.class_s");
		responseFacetList.add("taxon.family_s");
		responseFacetList.add("searchable.sources_ss");
		String className = null;
		if (selectedFacets == null) {
			logger.debug("No selected facets, setting default response facets");
		} else {
			if (selectedFacets.containsKey("base.class_s")) {
				className = selectedFacets.get("base.class_s");
				if (selectedFacets.get("base.class_s").equals(
						"org.emonocot.model.Taxon")) {
					logger.debug("Adding taxon specific facets");
					responseFacetList
							.add("taxon.measurement_or_fact_IUCNConservationStatus_txt");
					responseFacetList
							.add("taxon.measurement_or_fact_Lifeform_txt");
					responseFacetList
							.add("taxon.measurement_or_fact_Habitat_txt");
					responseFacetList.add("taxon.taxon_rank_s");
					responseFacetList.add("taxon.taxonomic_status_s");
				}
			}
			if (selectedFacets.containsKey("taxon.distribution_TDWG_0_ss")) {
				logger.debug("Removing continent facet");
				responseFacetList.remove("taxon.distribution_TDWG_0_ss");
			}
		}
		String[] responseFacets = new String[] {};
		responseFacets = responseFacetList.toArray(responseFacets);
		limit = setLimit(view, className);

		// Run the search
		Page<? extends SearchableObject> result = runQuery(query, start, limit,
				spatial, responseFacets, null, sort, selectedFacets);

		if (spatial != null) {
			result.putParam("x1", x1);
			result.putParam("y1", y1);
			result.putParam("x2", x2);
			result.putParam("y2", y2);
		}
		if (!StringUtils.isEmpty(featureId)) {
			result.putParam("featureId", featureId);
		}
		result.putParam("view", view);
		result.setSort(sort);
		model.addAttribute("result", result);
		return "spatial";
	}

	@RequestMapping(value = "/analyse", method = RequestMethod.GET, produces = "text/html")
	public String analyse(
			Model uiModel,
			@RequestParam(value = "rows", required = false) String rows,
			@RequestParam(value = "firstRow", required = false, defaultValue = "0") Integer firstRow,
			@RequestParam(value = "maxRows", required = false, defaultValue = "10") Integer maxRows,
			@RequestParam(value = "cols", required = false) String cols,
			@RequestParam(value = "firstCol", required = false, defaultValue = "0") Integer firstCol,
			@RequestParam(value = "maxCols", required = false, defaultValue = "10") Integer maxCols,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "view", required = false, defaultValue = "bar") String view
			)
			throws Exception {

		List<String> facetList = new ArrayList<String>();
		facetList.add("taxon.family_s");
		facetList.add("taxon.distribution_TDWG_0_ss");
		facetList.add("taxon.taxon_rank_s");
		facetList.add("taxon.taxonomic_status_s");
		facetList.add("searchable.sources_ss");
		facetList.add("taxon.measurement_or_fact_IUCNConservationStatus_txt");
        facetList.add("taxon.measurement_or_fact_Lifeform_txt");
        facetList.add("taxon.measurement_or_fact_Habitat_txt");

		Map<String, String> selectedFacets = null;
		if (facets != null && !facets.isEmpty()) {
			selectedFacets = new HashMap<String, String>();
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}

		Cube cube = new Cube(selectedFacets);
		cube.setDefaultLevel("taxon.order_s");
		Dimension taxonomy = new Dimension("taxonomy");
		cube.addDimension(taxonomy);

		taxonomy.addLevel("taxon.order_s", false);
		taxonomy.addLevel("taxon.family_s", false);		
		taxonomy.addLevel("taxon.genus_s", false);

		Dimension distribution = new Dimension("distribution");
		cube.addDimension(distribution);

		distribution.addLevel("taxon.distribution_TDWG_0_ss", true);
		distribution.addLevel("taxon.distribution_TDWG_1_ss", true);
		distribution.addLevel("taxon.distribution_TDWG_2_ss", true);

		Dimension taxonRank = new Dimension("taxonRank");
		cube.addDimension(taxonRank);
		taxonRank.addLevel("taxon.taxon_rank_s", false);

		Dimension taxonomicStatus = new Dimension("taxonomicStatus");
		cube.addDimension(taxonomicStatus);
		taxonomicStatus.addLevel("taxon.taxonomic_status_s", false);
		
		Dimension lifeForm = new Dimension("lifeForm");
		cube.addDimension(lifeForm);
		lifeForm.addLevel("taxon.measurement_or_fact_Lifeform_txt", false);
		
		Dimension habitat = new Dimension("habitat");
		cube.addDimension(habitat);
		habitat.addLevel("taxon.measurement_or_fact_Habitat_txt", false);
		
		Dimension conservationStatus = new Dimension("conservationStatus");
		cube.addDimension(conservationStatus);
		conservationStatus.addLevel("taxon.measurement_or_fact_IUCNConservationStatus_txt", false);
		
		Dimension withDescriptions = new Dimension("withoutDescriptions");
		cube.addDimension(withDescriptions);
		withDescriptions.addLevel("taxon.descriptions_empty_b", false);
		
		Dimension withImages = new Dimension("withoutImages");
		cube.addDimension(withImages);
		withImages.addLevel("taxon.images_empty_b", false);

		CellSet cellSet = searchableObjectService.analyse(rows, cols, firstCol, maxCols,firstRow, maxRows, selectedFacets,	facetList.toArray(new String[facetList.size()]), cube);

		uiModel.addAttribute("cellSet", cellSet);
		uiModel.addAttribute("view", view);
		return "analyse";
	}

	/**
	 * @param term
	 *            The term to search for
	 * @return A list of terms to serialize
	 */
	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Match> autocomplete(@RequestParam(required = true) String term) {
		return searchableObjectService.autocomplete(term, 10, null);
	}
}
