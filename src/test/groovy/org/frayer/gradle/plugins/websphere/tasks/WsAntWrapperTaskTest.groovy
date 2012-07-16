package org.frayer.gradle.plugins.websphere.tasks

import org.frayer.gradle.plugins.websphere.WebSphereExtension;
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification

class WsAntWrapperTaskTest extends Specification {

    static final String WAS_HOME_VALUE = 'wasHome value'
    static final String CONNTYPE_VALUE = 'conntype value'
    static final String PROFILE_NAME_VALUE = 'profileName value'

    Project project
    WebSphereExtension webSphereExtension
    WsAntListAppsTask wsAntListAppsTask

    def setup() {
        project = ProjectBuilder.builder().build()

        project.extensions.create('websphere', WebSphereExtension)
        project.websphere {
            wasHome = WAS_HOME_VALUE
            conntype = CONNTYPE_VALUE
            profileName = PROFILE_NAME_VALUE
        }

        project.task(type: WsAntListAppsTask, 'wsListApps')
        wsAntListAppsTask = project.wsListApps
    }

    def "WsAntListAppsTask populates itself where appropriate"() {
        when:
        wsAntListAppsTask.populateApplicableProperties()

        then:
        wsAntListAppsTask.wasHome == WAS_HOME_VALUE
        wsAntListAppsTask.conntype == CONNTYPE_VALUE
        wsAntListAppsTask.profileName == PROFILE_NAME_VALUE
    }

    def "WsAntListAppsTask returns the correct ANT Task Attribute Values"() {
        when:
        wsAntListAppsTask.populateApplicableProperties()
        def antAttributeValues = wsAntListAppsTask.antAttributeValues

        then:
        antAttributeValues.wasHome == WAS_HOME_VALUE
        antAttributeValues.conntype == CONNTYPE_VALUE
        antAttributeValues.profileName == PROFILE_NAME_VALUE
    }

    def "it returns unique ANT Build Script paths"() {
        when:
        wsAntListAppsTask.buildScriptCounter = 0
        
        then:
        wsAntListAppsTask.nextWsAntBuildScriptPath.endsWith('websphere-gradle-plugin/build-wsListApps-1.xml')
        wsAntListAppsTask.nextWsAntBuildScriptPath.endsWith('websphere-gradle-plugin/build-wsListApps-2.xml')
        wsAntListAppsTask.nextWsAntBuildScriptPath.endsWith('websphere-gradle-plugin/build-wsListApps-3.xml')
    }
}
