/**
 * 
 */
package org.emonocot.model.geography;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.emonocot.model.common.SearchableObject;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author jk00kg
 *
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
public class Place extends SearchableObject {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(Place.class);

	/**
	 * 
	 */
	private Long id;

	/**
	 * 
	 */
	private String name;
	
	/**
	 * 
	 */
	private String fipsCode;

	/**
	 * A JTS Polygon for this place
	 * @see com.vividsolutions.jts.geom.Polygon
	 */
	private MultiPolygon shape;

	/**
	 * Used for places only known as a single point
	 */
	private Point point;

	/**
	 * The smallest rectangle that bounds the place.
	 * TODO: It is persisted to  optimise querying   
	 */
	//private Envelope envelope;

	/* (non-Javadoc)
	 * @see org.emonocot.model.common.SecuredObject#getId()
	 */
    @Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
	public Long getId() {
		return id;
	}

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

	/**
	 * @return the name
	 */
    @Fields(value={@Field, @Field(name="label", index=Index.UN_TOKENIZED)})
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the fipsCode
	 */
	public final String getFipsCode() {
		return fipsCode;
	}

	/**
	 * @param fipsCode the fipsCode to set
	 */
	public final void setFipsCode(String fipsCode) {
		this.fipsCode = fipsCode;
	}

	/**
	 * @return the shape
	 */
	@Type(type="spatialType")
	public final MultiPolygon getShape() {
		return shape;
	}

	/**
	 * @param shape the shape to set
	 */
	public final void setShape(Geometry shape) {
		logger.debug("Setting a " + shape.toText() + " with " + shape.getNumGeometries() + " geometries");
		try{
			if(shape instanceof Polygon){
				this.shape = new MultiPolygon(new Polygon[] {(Polygon)shape}, new GeometryFactory());
			} else {
				this.shape = (MultiPolygon) shape;
			}
		} catch (ClassCastException e) {
			logger.error("Unable to get multipolygon from" + shape.toText() + " " + shape, e);
		}
		
	}

	/**
	 * @return the point
	 */
	@Type(type="spatialType")
	public final Point getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public final void setPoint(Geometry point) {
		logger.debug("Setting a " + point.toText() + " with " + point.getNumGeometries() + " geometries");
		try{
			if (point instanceof Point){
				this.point = (Point) point;
			} else if (shape.getNumGeometries() == 1) {
				logger.warn("Inferring a point from the centroid of " + point.toText() + " " + point);
				this.point = point.getCentroid();
			}
		} catch (ClassCastException e) {
			logger.error("Expected a point but got " + point.toText() + " " + point, e);
		}
	}
    
    

}