pipeline {
    agent { dockerfile true }
    stages {
	stage('Compile') {
	    steps {
		sh '/usr/bin/local/mill _.compile'
	    }
	}

        stage('Test') {
            steps {
                sh '/usr/bin/local/mill _.test'
            }
        }
    }
}
