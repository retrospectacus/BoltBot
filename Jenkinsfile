void setBuildStatus(String message, String state, String context) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/DiscordBolt/BoltBot"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: context],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
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
    stage('Checkout') {
      steps {
        echo 'Stage:Checkout'
        //git 'https://github.com/DiscordBolt/BoltBot'
      }
    }
    stage('Build') {
      steps {
        echo 'Stage:Build'
        withCredentials([string(credentialsId: 'discordToken', variable: 'TOKEN')]) {
        sh '''
          //set +x
          gradle build -x test -pdiscordToken="$TOKEN"
         '''
        }
      }
    }
    stage('Test') {
      steps {
        echo 'Stage:Test'
        sh 'gradle test'
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
            setBuildStatus("This commit has " + warnings + " checkstyle warnings. (" + warnings_new + " new)", "FAILURE", "continuous-integration/jenkins/checkstyle");
          }
        }
      }
    }
    stage('Deploy') {
      steps {
        echo 'Stage:Deploy'
        withCredentials([usernamePassword(credentialsId: 'dockerHub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh '''
            //set +x
            gradle jib -PdockerUser="$USERNAME" -pdockerPass="$PASSWORD"
          '''
        }
        sh 'docker image ls'
      }
    }
  }
  post {
    always {
      archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
      junit 'build/test-results/**/*.xml'
    }
  }
}
