import utils.Logger

def call(Map pipeline_config = [:]) {
    def logger = new Logger(this, 'DEBUG')
    logger.info("pipeline_config: ${pipeline_config}")
    pipeline {
        agent { label 'linux' }
        stages {
            stage('Config') {
                steps {
                    script {
                        def config
                        try {
                            config = readYaml file: "CI/JenkinsFiles/config.yml"
                        } catch (FileNotFoundException e) {
                            logger.error("Error: ${e}")
                        }
                        logger.info("config: ${config}")
                    }
                }
            }
            stage('Version') {
                steps {
                    script {
                        //read pom.xml
                        def pom
                        try {
                            pom = readMavenPom file: 'pom.xml'
                        } catch (FileNotFoundException e) {
                            logger.error("Error: ${e}")
                        }
                    }
                }
            }
        }
    }
}
