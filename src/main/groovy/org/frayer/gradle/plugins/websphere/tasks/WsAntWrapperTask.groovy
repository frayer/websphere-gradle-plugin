package org.frayer.gradle.plugins.websphere.tasks

import groovy.xml.MarkupBuilder

import java.io.StringWriter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;


abstract class WsAntWrapperTask extends DefaultTask {
    String wasHome
    String workingDirectory = "${project.buildDir}/websphere-gradle-plugin"

    abstract String getAntTaskName()
    abstract String getAntTaskClassName()
    abstract def getAttributes()

    @Override
    String getGroup() {
        'WebSphere'
    }

    @TaskAction
    def executeTask() {
        writeAntScriptFile()
    }

    def writeAntScriptFile() {
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
            target(name: 'default') { "${antTaskName}"(attributes) }
        }
        writer.flush()
        writer.close()
        writer.toString()
    }
}
