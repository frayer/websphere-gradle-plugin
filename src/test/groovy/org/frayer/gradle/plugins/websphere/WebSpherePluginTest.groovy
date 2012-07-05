package org.frayer.gradle.plugins.websphere

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder;

import spock.lang.Specification

class WebSpherePluginTest extends Specification {

    def Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'websphere'
    }

    def "plugin defines a 'websphere' extension for the project"() {
        expect:
        project.extensions.websphere != null
    }

    def "the 'websphere' extension is configurable"() {
        given:
        project.websphere { profileName = 'testProfile' }

        expect:
        project.extensions.websphere.profileName == 'testProfile'
    }
}
