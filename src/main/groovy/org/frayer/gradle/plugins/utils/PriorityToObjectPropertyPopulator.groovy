package org.frayer.gradle.plugins.utils


/**
 * Populates the properties of an Object only if that Object property currently has a null value.
 *  
 * @author Michael Frayer
 */
class PriorityToObjectPropertyPopulator implements PropertyPopulator {

    @Override
    public void populate(Object obj, Map propertyValues) {
        propertyValues.each { key, value ->
            if (obj.hasProperty(key) && (obj[key] == null)) {
                obj[key] = value
            }
        }
    }
}
