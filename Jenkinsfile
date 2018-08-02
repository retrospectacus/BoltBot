void setBuildStatus(String message, String state, String context) {
    step([
        $class: "GitHubCommitStatusSetter",
        reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/DiscordBolt/BoltBot"],
        contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
        errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
        statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
    ]);
}

def isPRMergeBuild() {
    return (env.BRANCH_NAME ==~ /^PR-\d+$/)
}

pipeline {
    agent {
        docker {
            image 'gradle:4.9-jdk10'
        }
    }

    stages {
        stage('Build') {
            environment {
                DISCORD_TOKEN = credentials('discordToken');
            }
            steps {
                echo 'Stage:Build'
                withCredentials([string(credentialsId: 'discordToken', variable: 'token')]) {
                    sh "gradle build -x test -PDiscordToken=${token}"
                }
                archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                echo 'Stage:Test'
                sh 'gradle test'
                junit 'build/test-results/**/*.xml'
            }
        }
        stage('Check') {
            steps {
                echo 'Stage:Check'
                step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: '**/reports/checkstyle/main.xml'])
                script {
                    def warnings = tm('$CHECKSTYLE_COUNT').toInteger();
                    def warnings_new = tm('$CHECKSTYLE_NEW').toInteger();
                    if (warnings > 0) {
                        setBuildStatus('This commit has ' + warnings + ' checkstyle warnings. (' + warnings_new + ' new)', 'FAILURE', 'continuous-integration/jenkins/checkstyle');
                    }
                }
            }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                echo 'Stage:Deploy'
                withCredentials([string(credentialsId: 'dockerPassword', variable: 'password')]) {
                    sh "gradle jib -PDockerPassword=${password}"
                }
            }
        }
    }
    post {
        always {
            script {
                def COLOR_MAP = ['SUCCESS': 3779158, 'UNSTABLE': 15588927, 'FAILURE': 14370117, 'ABORTED': 10329501]
                def RESULT_MAP = ['SUCCESS': 'Passed', 'UNSTABLE': 'Unstable', 'FAILURE': 'Failed', 'ABORTED': 'Aborted']

                def color = COLOR_MAP[currentBuild.currentResult]
                def authorName = 'Build #' + currentBuild.number + ' ' + RESULT_MAP[currentBuild.currentResult]
                def title = "[${JOB_NAME}]"
                def titleURL = "${RUN_DISPLAY_URL}"
                def shortCommit = "{GIT_COMMIT}".substring(0, 7)
                def commitURL = "${GIT_URL}".replace(".git", "") + '/commit/' + "${GIT_COMMIT}"
                def description = '[`' + shortCommit + '`](' + commitURL + ') - '
                def footerText = 'Build completed in ' + currentBuild.durationString;
                def now = new Date()
                def timestamp = now.format("yyyyMMdd-HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
                def embed = """
                  {
                    "embeds": [
                      {
                        "color": "${color}",
                        "author": {
                          "name": "${authorName}",
                          "url": "${titleURL}"
                        },
                        "title": "[BoltBot/v3]",
                        "url": "${commitURL}",
                        "description": "[`${shortCommit}`](${commitURL})",
                        "timestamp": "${timestamp}",
                        "footer": {
                          "text": "Build took ${footerText}"
                        }
                      }
                    ]
                  }
                """

                withCredentials([string(credentialsId: 'discordWebhook', variable: 'url')]) {
                    def response = httpRequest url: "${url}", httpMode: 'POST', contentType: 'APPLICATION_JSON', requestBody: "${embed}"
                    println("Status: " + response.status)
                    println("Content: " + response.content)
                }
            }
        }
    }
}
