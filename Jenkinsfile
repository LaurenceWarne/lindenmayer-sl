pipeline {
    agent {
        docker { image 'node:14-alpine' }
    }
    stages {
	stage('Compile') {
	    sh 'mill _.compile'
	}

        stage('Test') {
            steps {
                sh 'mill _.test'
            }
        }
    }
}
