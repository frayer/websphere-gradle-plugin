package org.frayer.gradle.plugins.websphere.tasks


/**
 * Lists all the applications installed on a WebSphere Server or Cell. This goal is a wrapper for the AdminApp.list()
 * command of the wsadmin tool. Refer to the wsadmin documentation for information on this operation.
 *
 * @author <a href="mailto:frayer@frayer.org">Michael Frayer</a>
 */
class WsAntListAppsTask extends WsAntWrapperTask {
    String profileName

    @Override
    String getDescription() {
        'Lists all the applications installed on a WebSphere Server or Cell'
    }

    @Override
    String getAntTaskName() {
        'wsListApps'
    }

    @Override
    String getAntTaskClassName() {
        'com.ibm.websphere.ant.tasks.ListApplications'
    }

    @Override
    def getAttributes() {
        [wasHome: wasHome, profileName: profileName, conntype: conntype]
    }
}
