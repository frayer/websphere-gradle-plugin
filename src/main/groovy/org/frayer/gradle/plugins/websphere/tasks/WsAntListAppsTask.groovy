package org.frayer.gradle.plugins.websphere.tasks


/**
 * Lists all the applications installed on a WebSphere Server or Cell. This goal is a wrapper for the AdminApp.list()
 * command of the wsadmin tool. Refer to the wsadmin documentation for information on this operation.
 *
 * @author Michael Frayer
 */
class WsAntListAppsTask extends WsAntWrapperTask {
    static final String JAVA_PROPERTIES_GRADLE = 'javaProperties'
    static final String JAVA_PROPERTIES_ANT = 'properties'
    static final String PROFILE = 'profile'
    static final String PROFILE_NAME = 'profileName'
    static final String HOST = 'host'
    static final String PORT = 'port'
    static final String USER = 'user'
    static final String PASSWORD = 'password'

    String javaProperties
    String profile
    String profileName
    String host
    String port
    String user
    String password

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
            [gradleName: WAS_HOME,               antName: WAS_HOME],
            [gradleName: JAVA_PROPERTIES_GRADLE, antName: JAVA_PROPERTIES_ANT],
            [gradleName: PROFILE,                antName: PROFILE],
            [gradleName: PROFILE_NAME,           antName: PROFILE_NAME],
            [gradleName: CONNTYPE,               antName: CONNTYPE],
            [gradleName: HOST,                   antName: HOST],
            [gradleName: PORT,                   antName: PORT],
            [gradleName: USER,                   antName: USER],
            [gradleName: PASSWORD,               antName: PASSWORD]
        ]
    }
}
