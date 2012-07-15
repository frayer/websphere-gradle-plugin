package org.frayer.gradle.plugins.websphere.tasks


/**
 * Lists all the applications installed on a WebSphere Server or Cell. This goal is a wrapper for the AdminApp.list()
 * command of the wsadmin tool. Refer to the wsadmin documentation for information on this operation.
 *
 * @author Michael Frayer
 */
class WsAntListAppsTask extends WsAntWrapperTask {
    static final String PROFILE_NAME = 'profileName'

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
    def getApplicablePropertyNames() {
        [
            WAS_HOME,
            CONNTYPE,
            PROFILE_NAME
        ]
    }
}
