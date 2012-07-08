package org.frayer.gradle.plugins.utils



import spock.lang.Specification

class PriorityToTaskPropertyPopulatorTest extends Specification {

    PropertyPopulator populator
    Object objectToPopulate

    def setup() {
        populator = new PriorityToObjectPropertyPopulator()
        objectToPopulate = new Object() {
                    def value1
                    def value2
                    def value3
                }
    }

    def "it populates an Object with all provided properties when the object has all null properties"() {
        when:
        populator.populate(objectToPopulate, [value1: 'foo', value2: 'bar', value3: 'zap'])

        then:
        objectToPopulate.value1 == 'foo'
        objectToPopulate.value2 == 'bar'
        objectToPopulate.value3 == 'zap'
    }

    def "it does not overwrite non-null properties which exist in the object"() {
        given:
        objectToPopulate.value2 = 'predefined value'

        when:
        populator.populate(objectToPopulate, [value1: 'foo', value2: 'bar', value3: 'zap'])

        then:
        objectToPopulate.value1 == 'foo'
        objectToPopulate.value2 == 'predefined value'
        objectToPopulate.value3 == 'zap'
    }

    def "a null incoming value won't overwrite non-null properties which exist in the object"() {
        given:
        objectToPopulate.value2 = 'predefined value'

        when:
        populator.populate(objectToPopulate, [value1: 'foo', value2: null, value3: 'zap'])

        then:
        objectToPopulate.value1 == 'foo'
        objectToPopulate.value2 == 'predefined value'
        objectToPopulate.value3 == 'zap'
    }

    def "only those properties provided will get set in the target object"() {
        given:
        objectToPopulate.value2 = 'predefined value'

        when:
        populator.populate(objectToPopulate, [value2: 'bar', value3: 'zap'])

        then:
        objectToPopulate.value1 == null
        objectToPopulate.value2 == 'predefined value'
        objectToPopulate.value3 == 'zap'
    }
}
