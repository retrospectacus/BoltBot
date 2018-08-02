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
                sh 'gradle build -x test'
                archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                sh 'gradle test'
                junit 'build/test-results/**/*.xml'
            }
        }
        stage('Check') {
            steps {
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
                def buildStatus = "Build #${currentBuild.number} " + RESULT_MAP[currentBuild.currentResult]
                def buildURL = "${RUN_DISPLAY_URL}"
                def projectBranch = "[${JOB_NAME}]"
                def repoURL =  "${GIT_URL}".replace(".git", "")
                def branchURL = "${repoURL}/tree/${GIT_BRANCH}"
                def commitURL = "${repoURL}/commit/${GIT_COMMIT}"
                def shortCommit = "${GIT_COMMIT}".substring(0, 7)
                def commitDescription = "Commit: [`${shortCommit}`](${commitURL})"
                def buildTime = "Build completed in ${currentBuild.durationString}".replace(' and counting', '')
                def timestamp = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeZone.getTimeZone('UTC'))
                def body = """
                  {
                    "embeds": [
                      {
                        "color": ${color},
                        "author": {
                          "name": "${buildStatus}",
                          "url": "${buildURL}"
                        },
                        "title": "${projectBranch}",
                        "url": "${branchURL}",
                        "description": "${commitDescription}",
                        "footer": {
                          "text": "${buildTime}"
                        },
                        "timestamp": "${timestamp}"
                      }
                    ]
                  }
                """
                withCredentials([string(credentialsId: 'discordWebhook', variable: 'url')]) {
                    httpRequest url: "${url}", httpMode: 'POST', contentType: 'APPLICATION_JSON', requestBody: "${body}"
                }
            }
        }
    }
}
