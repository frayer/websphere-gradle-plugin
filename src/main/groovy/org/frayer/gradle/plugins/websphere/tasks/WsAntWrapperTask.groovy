package org.frayer.gradle.plugins.websphere.tasks

import groovy.xml.MarkupBuilder

import java.io.StringWriter

import org.frayer.gradle.plugins.utils.PriorityToObjectPropertyPopulator;
import org.frayer.gradle.plugins.utils.PropertyPopulator
import org.frayer.gradle.plugins.websphere.WebSphereExtension
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.tasks.TaskAction;


abstract class WsAntWrapperTask extends DefaultTask {
    static final String WAS_HOME = 'wasHome'
    static final String CONNTYPE = 'conntype'

    int buildScriptCounter = 0
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
        if (validate()) {
            def wsAntBuildScriptPath = getNextWsAntBuildScriptPath()
            writeAntScript(wsAntBuildScriptPath)
            executeAntScript(wsAntBuildScriptPath)
        }
    }

    def getNextWsAntBuildScriptPath() {
        buildScriptCounter++
        "${workingDirectory}/build-${antTaskName}-${buildScriptCounter}.xml"
    }

    def getPathToWsAntScript() {
        def wsAntPathLocation = "${wasHome}/bin"
        def wsAntFileNamePattern = ~/^ws_ant\.(sh|bat)$/

        def wasAntDirectory = new File(wsAntPathLocation)
        wasAntDirectory.listFiles().find { it.name ==~ wsAntFileNamePattern }?.absolutePath
    }

    def executeAntScript(antBuildScriptPath) {
        def wsAntScriptPath = pathToWsAntScript

        if (!wsAntScriptPath) {
            def message = "Could not locate the ws_ant.(sh|bat) script needed to run this task. Please check the value provided for 'wasHome'"
            logger.error(message)
            throw new InvalidUserDataException(message)
        }

        def wsAntProc = "${pathToWsAntScript} -f ${antBuildScriptPath}".execute()
        wsAntProc.consumeProcessOutput(System.out, System.err)
        wsAntProc.waitFor()
    }

    def writeAntScript(antBuildScriptPath) {
        project.mkdir(workingDirectory)
        def file = new File(antBuildScriptPath)
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
        applicablePropertyNames.each { property ->
            if (this.hasProperty(property.gradleName) && this[property.gradleName] != null) {
                antAttributeValues[property.antName] = this[property.gradleName]
            }
        }
        return antAttributeValues
    }

    def getApplicableExtensionPropertyValues() {
        WebSphereExtension webSphereExtension = project.websphere
        def applicableExtensionPropertyValues = [:]
        applicablePropertyNames.each { property ->
            if (webSphereExtension.hasProperty(property.gradleName)) {
                applicableExtensionPropertyValues[property.gradleName] = webSphereExtension[property.gradleName]
            }
        }
        return applicableExtensionPropertyValues
    }

    def populateApplicableProperties() {
        propertyPopulator.populate(this, applicableExtensionPropertyValues)
    }

    boolean validate() {
        if (wasHome == null) {
            logger.warn("'wasHome' must be provided in order for ${name} to run. This task is being skipped.")
            return false
        }
        return true
    }
}
