package org.frayer.gradle.plugins.websphere.tasks

import groovy.xml.MarkupBuilder

import java.io.StringWriter

import org.frayer.gradle.plugins.utils.PriorityToObjectPropertyPopulator;
import org.frayer.gradle.plugins.utils.PropertyPopulator
import org.frayer.gradle.plugins.websphere.WebSphereExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;


abstract class WsAntWrapperTask extends DefaultTask {
    static final String WAS_HOME = 'wasHome'
    static final String CONNTYPE = 'conntype'

    PropertyPopulator propertyPopulator = new PriorityToObjectPropertyPopulator()
    String workingDirectory = "${project.buildDir}/websphere-gradle-plugin"
    String wasHome
    String conntype

    abstract String getAntTaskName()
    abstract String getAntTaskClassName()
    abstract def getApplicablePropertyNames()

    @Override
    String getGroup() {
        'WebSphere'
    }

    @TaskAction
    def executeTask() {
        populateApplicableProperties()
        writeAntScript()
        executeAntScript()
    }

    def executeAntScript() {
        def wsAntProc = "${wasHome}/bin/ws_ant.sh -f ${workingDirectory}/build.xml".execute()
        wsAntProc.consumeProcessOutput(System.out, System.err)
        wsAntProc.waitFor()
    }

    def writeAntScript() {
        project.mkdir(workingDirectory)
        def file = new File("${workingDirectory}/build.xml")
        file.createNewFile()
        file.withWriter { writer ->
            writer.write(antScriptMarkup)
        }
    }

    def getAntScriptMarkup() {
        def writer = new StringWriter()
        def antProject = new MarkupBuilder(writer)
        antProject.project(name: 'gradleWebSpherePlugin', default: 'default') {
            taskdef(name: antTaskName, classname: antTaskClassName)
            target(name: 'default') { "${antTaskName}"(antAttributeValues) }
        }
        writer.flush()
        writer.close()
        writer.toString()
    }

    def getAntAttributeValues() {
        def antAttributeValues = [:]
        applicablePropertyNames.each { propertyName ->
            if (this.hasProperty(propertyName) && this[propertyName] != null) {
                antAttributeValues[propertyName] = this[propertyName]
            }
        }
        return antAttributeValues
    }

    def getApplicableExtensionPropertyValues() {
        WebSphereExtension webSphereExtension = project.websphere
        def applicableExtensionPropertyValues = [:]
        applicablePropertyNames.each { propertyName ->
            if (webSphereExtension.hasProperty(propertyName)) {
                applicableExtensionPropertyValues[propertyName] = webSphereExtension[propertyName]
            }
        }
        return applicableExtensionPropertyValues
    }

    def populateApplicableProperties() {
        propertyPopulator.populate(this, applicableExtensionPropertyValues)
    }
}
