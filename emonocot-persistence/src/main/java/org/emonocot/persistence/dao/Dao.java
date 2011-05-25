package org.emonocot.persistence.dao;

import org.emonocot.model.common.Base;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public interface Dao<T extends Base> {

    /**
     *
     * @param identifier
     *            Set the identifier of the object you would like to retrieve
     * @return the object or throw and exception if that object does not exist
     */
    T load(String identifier);

    /**
    *
    * @param identifier
    *            Set the identifier of the object you would like to retrieve
    * @return the object or null if that object does not exist
    */
    T find(String identifier);

   /**
    *
    * @param identifier
    *            Set the identifier of the object you would like to retrieve
    * @param fetch Set the fetch profile to use
    * @return the object or throw and exception if that object does not exist
    */
   T load(String identifier, String fetch);

  /**
   *
   * @param identifier
   *            Set the identifier of the object you would like to retrieve
   * @param fetch Set the fetch profile to use
   * @return the object or null if that object does not exist
   */
   T find(String identifier, String fetch);

}