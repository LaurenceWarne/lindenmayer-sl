pipeline {
    agent { dockerfile true }
    stages {
	stage('Compile') {
	    steps {
		sh '/usr/local/bin/mill _.compile'
	    }
	}

        stage('Test') {
            steps {
                sh '/usr/local/bin/mill _.test'
            }
        }
    }
}
