package org.frayer.gradle.plugins.websphere

import org.frayer.gradle.plugins.websphere.tasks.WsAntListAppsTask;
import org.gradle.api.Plugin
import org.gradle.api.Project


public class WebSpherePlugin implements Plugin<Project> {

    private WebSphereExtension webSphereExtension

    @Override
    public void apply(Project project) {
        def webSphereExtension = new WebSphereExtension()

        // TODO Should use .create('websphere', WebSphereExtension) here, causing issues in STS at the moment though.
        project.extensions.add('websphere', webSphereExtension)

        project.afterEvaluate {
            project.task(type: WsAntListAppsTask, 'wsListApps') {
                wasHome = project.websphere.wasHome
                profileName = project.websphere.profileName
				conntype = project.websphere.conntype
            }
        }
    }
}
