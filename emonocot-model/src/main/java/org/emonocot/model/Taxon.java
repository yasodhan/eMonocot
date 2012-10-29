package org.emonocot.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.hibernate.SpatialFilterFactory;
import org.emonocot.model.marshall.json.ReferenceDeserializer;
import org.emonocot.model.marshall.json.ReferenceSerializer;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * @author ben
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
@FullTextFilterDef(name = "spatialFilter", impl = SpatialFilterFactory.class)
public class Taxon extends SearchableObject {

    /**
     *
     */
    private static final long serialVersionUID = -3511287213090466877L;
    
    /**
     *
     */
    private Long id;
    
    /**
     *
     */
    private String bibliographicCitation;

    /**
     *
     */
    private String scientificName;
    
    /**
    *
    */
   private String scientificNameAuthorship;

   /**
    *
    */
   private String genus;
   
   /**
    *
    */
   private String subgenus;

   /**
    *
    */
   private String specificEpithet;

   /**
    *
    */
   private String infraspecificEpithet;

   /**
    *
    */
   private Rank taxonRank;

   /**
    *
    */
   private TaxonomicStatus taxonomicStatus;
   
   /**
   *
   */
  private String kingdom;

  /**
   *
   */
  private String phylum;

  /**
   *
   */
  private String clazz;

  /**
   *
   */
  private String order;

   /**
    *
    */
   private String family;

      
   /**
    *
    */
   private String subfamily;
   
   /**
    *
    */
   private String tribe;
   
   /**
    *
    */
   private String subtribe;
   
   /**
    *
    */
   private String scientificNameID;

   /**
    *
    */
   private NomenclaturalCode nomenclaturalCode;
   
   /**
    *
    */
   private String source;
   
   /**
    *
    */
   private Integer namePublishedInYear;
   
   /**
    *
    */
   private String verbatimTaxonRank;
   
   /**
    *
    */
   private String taxonRemarks;
   
   /**
    *
    */
   private String namePublishedInString; 
   
   /**
    *
    */
   private NomenclaturalStatus nomenclaturalStatus;

   /**
    *
    */
   private Set<Annotation> annotations = new HashSet<Annotation>();

   /**
    *
    */
   private Reference namePublishedIn;
   
   /**
    *
    */
   private Reference nameAccordingTo;

   /**
    *
    */
   private Image image;

   /**
    *
    */
   private List<Taxon> higherClassification = new ArrayList<Taxon>();

    /**
     *
     */
    private Taxon parentNameUsage;
    
    /**
     *
     */
    private Taxon originalNameUsage;

    /**
     *
     */
    private Set<Taxon> childNameUsages = new HashSet<Taxon>();

    /**
     *
     */
    private Taxon acceptedNameUsage;

    /**
     *
     */
    private Set<Taxon> synonymNameUsages = new HashSet<Taxon>();

    /**
     *
     */
    private List<Image> images = new ArrayList<Image>();

    /**
     *
     */
    private Set<IdentificationKey> keys = new HashSet<IdentificationKey>();

    /**
     *
     */
    private Set<Reference> references = new HashSet<Reference>();

    /**
     *
     */
    private Set<Description> descriptions = new HashSet<Description>();

    /**
     *
     */
    private Set<Distribution> distribution = new HashSet<Distribution>();

    /**
     *
     */
    private Set<Identifier> identifiers = new HashSet<Identifier>();
    
    /**
     *
     */
    private Set<TypeAndSpecimen> typesAndSpecimens = new HashSet<TypeAndSpecimen>();
    
    /**
     *
     */
    private Set<VernacularName> vernacularNames = new HashSet<VernacularName>();
    
    /**
     *
     */
    private Set<MeasurementOrFact> measurementsOrFacts = new HashSet<MeasurementOrFact>();

    /**
     * TODO Delete once we don't need it anymore
     */
	private boolean deleted;

    /**
     * @param newId
     *            Set the identifier of this object.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * @return Get the identifier for this object.
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
    public Long getId() {
        return id;
    }

    /**
     * @return a list of images of the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
    @JsonIgnore
    public List<Image> getImages() {
        return images;
    }

    /**
     * @return the image
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return a list of references about the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
    @JsonIgnore
    public Set<Reference> getReferences() {
        return references;
    }

    /**
     * @return a map of descriptions about the taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL })
    @JsonManagedReference("descriptions-taxon")
    @IndexedEmbedded
    public Set<Description> getDescriptions() {
        return descriptions;
    }
    
    /**
     * @param newDescriptions
     *            Set the descriptions associated with this taxon
     */
    @JsonManagedReference("descriptions-taxon")
    public void setDescriptions(Set<Description> newDescriptions) {
        this.descriptions = newDescriptions;
    }

    /**
     * @param newImages
     *            Set the images associated with this taxon
     */
    @JsonIgnore
    public void setImages(List<Image> newImages) {
        this.images = newImages;
    }

    /**
     * @param newReferences
     *            Set the references associated with this taxon
     */
    @JsonIgnore
    public void setReferences(Set<Reference> newReferences) {
        this.references = newReferences;
    }

    /**
     * @return the full taxonomic name of the taxon, including authority
     */
    @Fields(value = {@Field, @Field(name = "label", index = Index.UN_TOKENIZED)})
    @Size(max = 128)
    public String getScientificName() {
        return scientificName;
    }

    /**
     * @param newName
     *            Set the taxonomic name of the taxon
     */
    public void setScientificName(String newName) {
        this.scientificName = newName;
    }

    /**
	 * @return the namePublishedInString
	 */
	public String getNamePublishedInString() {
		return namePublishedInString;
	}

	/**
	 * @param namePublishedInString the namePublishedInString to set
	 */
	public void setNamePublishedInString(String namePublishedInString) {
		this.namePublishedInString = namePublishedInString;
	}

	/**
     * @return the immediate taxonomic parent
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = TaxonSerializer.class)
    public Taxon getParentNameUsage() {
        return parentNameUsage;
    }

    /**
     * @param newParent
     *            Set the taxonomic parent
     */
    @JsonDeserialize(using = TaxonDeserializer.class)
    public void setParentNameUsage(Taxon newParent) {
        this.parentNameUsage = newParent;
    }

    /**
     * @return Get the immediate taxonomic children
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentNameUsage")
    @JsonIgnore
    public Set<Taxon> getChildNameUsages() {
        return childNameUsages;
    }

    /**
     * @param newChildren
     *            Set the taxonomic children
     */
    public void setChildNameUsages(Set<Taxon> newChildren) {
        this.childNameUsages = newChildren;
    }

    /**
     * @return get the accepted name of this synonym
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = TaxonSerializer.class)
    public Taxon getAcceptedNameUsage() {
        return acceptedNameUsage;
    }

    /**
     * @param newAccepted
     *            Set the accepted name
     */
    @JsonDeserialize(using = TaxonDeserializer.class)
    public void setAcceptedNameUsage(Taxon newAccepted) {
        this.acceptedNameUsage = newAccepted;
    }

    /**
     * @return the synonyms of this taxon
     */
    @IndexedEmbedded(depth = 1)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "acceptedNameUsage")
    @JsonIgnore
    public Set<Taxon> getSynonymNameUsages() {
        return synonymNameUsages;
    }

    /**
     * @param newSynonyms
     *            Set the synonyms of this taxon
     */
    public void setSynonymNameUsages(Set<Taxon> newSynonyms) {
        this.synonymNameUsages = newSynonyms;
    }

    /**
     * @return the distribution associated with this taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    @IndexedEmbedded
    @JsonManagedReference("distribution-taxon")
    public Set<Distribution> getDistribution() {
        return distribution;
    }

    /**
     * @param newDistribution
     *            Set the distribution associated with this taxon
     */
    @JsonManagedReference("distribution-taxon")
    public void setDistribution(Set<Distribution> newDistribution) {
        this.distribution = newDistribution;
    }

    /**
     * @param newAuthorship
     *            set the authorship
     */
    public void setScientificNameAuthorship(String newAuthorship) {
        this.scientificNameAuthorship = newAuthorship;
    }   

    /**
     * @param newGenusPart
     *            Set the genus part of the name
     */
    public void setGenus(String newGenusPart) {
        this.genus = newGenusPart;
    }

    /**
     * @param newSpecificEpithet
     *            set the specific epithet
     */
    public void setSpecificEpithet(String newSpecificEpithet) {
        this.specificEpithet = newSpecificEpithet;
    }

    /**
     * @param newInfraspecificEpithet
     *            Set the infraspecific epithet
     */
    public void setInfraspecificEpithet(String newInfraspecificEpithet) {
        this.infraspecificEpithet = newInfraspecificEpithet;
    }

    /**
     * @param newRank
     *            set the rank of this taxon
     */
    public void setTaxonRank(Rank newRank) {
        this.taxonRank = newRank;
    }

    /**
     * @return the authorship
     */
    @Field
    @Size(max = 128)
    public String getScientificNameAuthorship() {
        return scientificNameAuthorship;
    }

    /**
     * @return the genus
     */
    @Field
    @Size(max = 128)
    public String getGenus() {
        return genus;
    }

    /**
     * @return the specificEpithet
     */
    @Field
    @Size(max = 128)
    public String getSpecificEpithet() {
        return specificEpithet;
    }

    /**
     * @return the infraSpecificEpithet
     */
    @Field
    @Size(max = 128)
    public String getInfraspecificEpithet() {
        return infraspecificEpithet;
    }

    /**
     * @return the rank
     */
    @Field
    @Enumerated(value = EnumType.STRING)
    public Rank getTaxonRank() {
        return taxonRank;
    }

    /**
     * @param newStatus
     *            Set the taxonomic status
     */
    public void setTaxonomicStatus(TaxonomicStatus newStatus) {
        this.taxonomicStatus = newStatus;
    }

    @Field
    @Enumerated(value = EnumType.STRING)
    public TaxonomicStatus getTaxonomicStatus() {
        return taxonomicStatus;
    }

    /**
     * @param newInfraGenericEpithet
     *            Set the infrageneric epithet
     */
    public void setSubgenus(String newSubgenus) {
        this.subgenus = newSubgenus;
    }

    /**
     * @return the infrageneric epithet
     */
    @Field
    @Size(max = 128)
    public String getSubgenus() {
        return subgenus;
    }

    /**
     * @param newAccordingTo
     *            Set the according to
     */
    @JsonDeserialize(using = ReferenceDeserializer.class)
    public void setNameAccordingTo(Reference newNameAccordingTo) {
        this.nameAccordingTo = newNameAccordingTo;
    }

    /**
     * @return the according to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = ReferenceSerializer.class)
    public Reference getNameAccordingTo() {
        return nameAccordingTo;
    }

    /**
     * @param newFamily
     *            set the family
     */
    public void setFamily(String newFamily) {
        this.family = newFamily;
    }

    /**
     * @param newKingdom
     *            set the kingdom
     */
    public void setKingdom(String newKingdom) {
        this.kingdom = newKingdom;
    }

    /**
     * @param newPhylum
     *            set the phylum
     */
    public void setPhylum(String newPhylum) {
        this.phylum = newPhylum;
    }

    /**
     * @param newClass
     *            set the class
     */
    public void setClazz(String newClass) {
        this.clazz = newClass;
    }

    /**
     * @return the taxonomic class the taxon classified in
     */
    @Field
    @Column(name = "class")
    public String getClazz() {
        return clazz;
    }

    /**
     * @param newOrder
     *            set the order
     */
    public void setOrder(String newOrder) {
        this.order = newOrder;
    }

    /**
     * @param newNomenclaturalCode
     */
    public void setNomenclaturalCode(NomenclaturalCode newNomenclaturalCode) {
        this.nomenclaturalCode = newNomenclaturalCode;
    }

    /**
     * @return the nomenclatural code
     */
    @Enumerated(EnumType.STRING)
    public NomenclaturalCode getNomenclaturalCode() {
        return nomenclaturalCode;
    }

    /**
     * @return the family
     */
    @Fields({@Field,
            @Field(analyzer = @Analyzer(definition = "facetAnalyzer"))})
    @Size(max = 128)
    public String getFamily() {
        return family;
    }

    /**
     * @return the kingdom
     */
    @Field
    @Size(max = 128)
    public String getKingdom() {
        return kingdom;
    }

    /**
     * @return the phylum
     */
    @Field
    @Size(max = 128)
    public String getPhylum() {
        return phylum;
    }

    /**
     * @return the order
     */
    @Field
    @Column(name = "ordr")
    @Size(max = 128)
    public String getOrder() {
        return order;
    }
    
	/**
	 * @return the subfamily
	 */
    @Field
	public String getSubfamily() {
		return subfamily;
	}

	/**
	 * @param subfamily the subfamily to set
	 */
	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	/**
	 * @return the tribe
	 */
    @Field
	public String getTribe() {
		return tribe;
	}

	/**
	 * @param tribe the tribe to set
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	/**
	 * @return the subtribe
	 */
    @Field
	public String getSubtribe() {
		return subtribe;
	}

	/**
	 * @param subtribe the subtribe to set
	 */
	public void setSubtribe(String subtribe) {
		nomenclaturalStatus = NomenclaturalStatus.Illegitimate;
		this.subtribe = subtribe;
	}

	/**
	 * @return the bibliographicCitation
	 */
	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	/**
	 * @param bibliographicCitation the bibliographicCitation to set
	 */
	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	/**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Taxon'")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE})
    @JsonIgnore
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations
     *            the annotations to set
     */
    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    /**
     * @param reference
     *            set the protologue
     */
    @JsonDeserialize(using = ReferenceDeserializer.class)
    public void setNamePublishedIn(Reference reference) {
        this.namePublishedIn = reference;
    }

    /**
     * @return the protologue
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = ReferenceSerializer.class)
    @IndexedEmbedded
    public Reference getNamePublishedIn() {
        return namePublishedIn;
    }

    /**
     * @return the ancestors of the taxon
     */
    @JsonIgnore
    @Transient
    public List<Taxon> getHigherClassification() {
        return higherClassification;
    }

    /**
     * @param ancestors
     *            Set the ancestors of the taxon
     */
    @JsonIgnore
    public void setHigherClassification(List<Taxon> newHigherClassification) {
        this.higherClassification = newHigherClassification;
    }

    /**
     * @param newIdentifier
     *            Set the name identifier
     */
    public void setScientificNameID(String newIdentifier) {
        this.scientificNameID = newIdentifier;
    }

    /**
     *
     * @return the name identifier
     */
    public String getScientificNameID() {
        return scientificNameID;
    }

    /**
     * @return a list of identifiers the taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL })
    @JsonManagedReference("identifier-taxon")
    public Set<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param newIdentifiers
     *            Set the identifiers associated with this taxon
     */
    @JsonManagedReference("identifier-taxon")
    public void setIdentifiers(Set<Identifier> newIdentifiers) {
        this.identifiers = newIdentifiers;
    }

    /**
     * @return the keys
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon")
    @JsonSerialize(contentUsing = ReferenceSerializer.class)
    public Set<IdentificationKey> getKeys() {
        return keys;
    }
    
    /**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the namePublishedInYear
	 */
	public Integer getNamePublishedInYear() {
		return namePublishedInYear;
	}

	/**
	 * @param namePublishedInYear the namePublishedInYear to set
	 */
	public void setNamePublishedInYear(Integer namePublishedInYear) {
		this.namePublishedInYear = namePublishedInYear;
	}

	/**
	 * @return the verbatimTaxonRank
	 */
	public String getVerbatimTaxonRank() {
		return verbatimTaxonRank;
	}

	/**
	 * @param verbatimTaxonRank the verbatimTaxonRank to set
	 */
	public void setVerbatimTaxonRank(String verbatimTaxonRank) {
		this.verbatimTaxonRank = verbatimTaxonRank;
	}

	/**
	 * @return the taxonRemarks
	 */
	public String getTaxonRemarks() {
		return taxonRemarks;
	}

	/**
	 * @param taxonRemarks the taxonRemarks to set
	 */
	public void setTaxonRemarks(String taxonRemarks) {
		this.taxonRemarks = taxonRemarks;
	}

	/**
	 * @return the nomenclaturalStatus
	 */
	@Field
    @Enumerated(value = EnumType.STRING)
	public NomenclaturalStatus getNomenclaturalStatus() {
		return nomenclaturalStatus;
	}

	/**
	 * @param nomenclaturalStatus the nomenclaturalStatus to set
	 */
	public void setNomenclaturalStatus(NomenclaturalStatus nomenclaturalStatus) {
		this.nomenclaturalStatus = nomenclaturalStatus;
	}

	/**
	 * @return the originalNameUsage
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = TaxonSerializer.class)
	public Taxon getOriginalNameUsage() {
		return originalNameUsage;
	}

	/**
	 * @param originalNameUsage the originalNameUsage to set
	 */
	@JsonDeserialize(using = TaxonDeserializer.class)
	public void setOriginalNameUsage(Taxon originalNameUsage) {
		this.originalNameUsage = originalNameUsage;
	}

	/**
     * @param keys the keys to set
     */
    @JsonIgnore
    public void setKeys(Set<IdentificationKey> keys) {
        this.keys = keys;
    }
    
    /**
     * @return a list of types and specimens
     */
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
    @JsonIgnore
    public Set<TypeAndSpecimen> getTypesAndSpecimens() {
        return typesAndSpecimens;
    }
    
    /**
     *
     * @param typesAndSpecimens
     */
    @JsonIgnore
    public void setTypesAndSpecimens(Set<TypeAndSpecimen> typesAndSpecimens) {
    	this.typesAndSpecimens = typesAndSpecimens;
    }
    
    
    /**
     * @return a map of vernacularNames for the taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL })
    @JsonManagedReference("vernacularNames-taxon")
    @IndexedEmbedded
    public Set<VernacularName> getVernacularNames() {
        return vernacularNames;
    }
    
    /**
     * @param newVernacularNames
     *            Set the vernacular names for this taxon
     */
    @JsonManagedReference("vernacularNames-taxon")
    public void setVernacularNames(Set<VernacularName> newVernacularNames) {
        this.vernacularNames = newVernacularNames;
    }
    
    /**
     * @return a set of measurements or facts about the taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL })
    @JsonManagedReference("measurementsOrFacts-taxon")
    @IndexedEmbedded
    public Set<MeasurementOrFact> getMeasurementsOrFacts() {
        return measurementsOrFacts;
    }
    
    /**
     * @param newMeasurementsOrFacts
     *            Set the measurements or facts about this taxon
     */
    @JsonManagedReference("measurementsOrFacts-taxon")
    public void setMeasurementsOrFacts(Set<MeasurementOrFact> newMeasurementsOrFacts) {
        this.measurementsOrFacts = newMeasurementsOrFacts;
    }
    
    /**
     * TODO REMOVE once refactoring complete
     * @param b
     */

	public void setDeleted(boolean b) {
		this.deleted = b;
	}
	
	/**
	 * TODO REMOVE once refactoring complete
	 */
	@Transient
	@JsonIgnore
	public boolean isDeleted() {
		return deleted;
	}
    
}
