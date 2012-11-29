package org.emonocot.model;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.marshall.json.ReferenceDeserializer;
import org.emonocot.model.marshall.json.ReferenceSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

/**
 *
 * @author ben
 *
 */
@Entity
public class Description extends OwnedEntity {

    /**
     *
     */
    private static final long serialVersionUID = -177666938449346483L;
    /**
     *
     */
    private String description;

    /**
    *
    */
    private Taxon taxon;

    /**
    *
    */
    private DescriptionType type;
    
    /**
     * 
     */
    private String creator;
    
    /**
     *
     */
    private String contributor;
    
    /**
     *
     */
    private String audience;
    
    /**
     *
     */
    private Locale language;
    
    /**
     *
     */
    private String source;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Set<Annotation> annotations = new HashSet<Annotation>();

    /**
     *
     */
    private Set<Reference> references = new HashSet<Reference>();

    /**
     *
     */
    public Description() {
        this.identifier = UUID.randomUUID().toString();
    }

    /**
     *
     * @param newId
     *            Set the identifier of this object.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     *
     * @return Get the identifier for this object.
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    public Long getId() {
        return id;
    }

    /**
     *
     * @param newTaxon
     *            Set the taxon that this content is about.
     */   
    @JsonBackReference("descriptions-taxon")
    public void setTaxon(Taxon newTaxon) {
        this.taxon = newTaxon;
    }

    /**
     *
     * @return Return the subject that this content is about.
     */
    @Enumerated(value = EnumType.STRING)
    public DescriptionType getType() {
        return type;
    }

    /**
     *
     * @param newFeature
     *            Set the subject that this content is about.
     */
    public void setType(DescriptionType newFeature) {
        this.type = newFeature;
    }

    /**
     *
     * @return Get the taxon that this content is about.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("descriptions-taxon")
    public Taxon getTaxon() {
        return taxon;
    }

    /**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
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
     *
     * @param newContent
     *            Set the content of this object.
     */
    public void setDescription(String newContent) {
        this.description = newContent;
    }

    /**
     *
     * @return the content as a string
     */
    @Lob
    public String getDescription() {
        return description;
    }

    @Transient
    @JsonIgnore
    public final String getClassName() {
        return "Description";
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Description'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
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
     * @return the references
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({ CascadeType.SAVE_UPDATE })
    @JoinTable(name = "Description_Reference", joinColumns = { @JoinColumn(name = "Description_id") }, inverseJoinColumns = { @JoinColumn(name = "references_id") })
    @JsonSerialize(contentUsing = ReferenceSerializer.class)
    public Set<Reference> getReferences() {
        return references;
    }

    /**
     * @param newReferences the references to set
     */
    @JsonDeserialize(contentUsing = ReferenceDeserializer.class)
    public void setReferences(Set<Reference> newReferences) {
        this.references = newReferences;
    }

	/**
	 * @return the contributor
	 */
	public String getContributor() {
		return contributor;
	}

	/**
	 * @param contributor the contributor to set
	 */
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	/**
	 * @return the audience
	 */
	public String getAudience() {
		return audience;
	}

	/**
	 * @param audience the audience to set
	 */
	public void setAudience(String audience) {
		this.audience = audience;
	}

	/**
	 * @return the language
	 */
	public Locale getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(Locale language) {
		this.language = language;
	}
}
