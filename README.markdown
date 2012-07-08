websphere-gradle-plugin
=======================

Overview
--------

A Gradle Plugin which wraps the ws\_ant.(sh|bat) script to call the
[WebSphere ANT Tasks](http://pic.dhe.ibm.com/infocenter/wasinfo/v6r1/index.jsp?topic=/com.ibm.websphere.javadoc.doc/public_html/api/com/ibm/websphere/ant/tasks/package-tree.html).
This plugin draws heavy inspiration from [was6-maven-plugin](http://mojo.codehaus.org/was6-maven-plugin/) developed over at codehaus.

Stay tuned for more details and examples as this project is still in the early stages.

Usage
-----

You'll need to build this plugin yourself for now to test it out. Builds can be done with
Gradle v1.0 with the following command in the root of the project:

    gradle build

This will produce a JAR at "build/libs/websphere-gradle-plugin-0.0.1-SNAPSHOT.jar" relative
to the root of the project. The only WebSphere ANT Task working at the moment is wsListApps.
The following is an example Gradle build file showing how this plugin can be used.

    import org.frayer.gradle.plugins.websphere.tasks.WsAntListAppsTask

    apply plugin: 'websphere'

    repositories {
        mavenCentral()
    }

    buildscript {
        dependencies {
            classpath fileTree(dir: 'build/libs', include: '**/*.jar')
        }
    }

    websphere {
        wasHome = '/Users/user/IBM/WebSphere/AppServer'
        conntype = 'NONE'
    }

    task 'wsListApps'(type: WsAntListAppsTask) {
        profileName = 'myProfile'
    }


Assuming this file was named "test.gradle" and was in the root of this project you
could execute the following command:

    gradle -b test.gradle tasks
    
and see this output:

    Help tasks
    ----------
    dependencies - Displays the dependencies of root project 'websphere-gradle-plugin'.
    help - Displays a help message
    projects - Displays the sub-projects of root project 'websphere-gradle-plugin'.
    properties - Displays the properties of root project 'websphere-gradle-plugin'.
    tasks - Displays the tasks runnable from root project 'websphere-gradle-plugin' (some of the displayed tasks may belong to subprojects).

    WebSphere tasks
    ---------------
    wsListApps - Lists all the applications installed on a WebSphere Server or Cell

To acutally run the "wsListApps" task, execute the following command:

    gradle -b test.gradle wsListApps

And you'll see something like the following depending on your environment.

    :wsListApps
    Buildfile: /home/was-user/Development/websphere-gradle-plugin/build/websphere-gradle-plugin/build.xml

    default:
    [wsListApps] profileName=AppSrv01 registry=/home/was-user/IBM/WebSphere/AppServer/properties/profileRegistry.xml
    [wsListApps] profileHome=/home/was-user/IBM/WebSphere/AppServer/profiles/AppSrv01
      [wsadmin] WASX7357I: By request, this scripting client is not connected to any server process. Certain configuration and application operations will be available in local mode.
      [wsadmin] DefaultApplication
      [wsadmin] ivtApp
      [wsadmin] query


