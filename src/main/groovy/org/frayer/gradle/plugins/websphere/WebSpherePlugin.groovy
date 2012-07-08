package org.frayer.gradle.plugins.websphere

import org.gradle.api.Plugin
import org.gradle.api.Project


public class WebSpherePlugin implements Plugin<Project> {

    private WebSphereExtension webSphereExtension

    @Override
    public void apply(Project project) {
        def webSphereExtension = new WebSphereExtension()
        project.extensions.create('websphere', WebSphereExtension)
    }
}
