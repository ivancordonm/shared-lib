import utils.Logger

def call(Map pipeline_config = [:]) {
    def logger = new Logger(this, 'DEBUG')
    logger.info("pipeline_config: ${pipeline_config}")
    pipeline {
        agent { label 'linux' }
        stages {
            stage('Get Pipeline Version') {
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
        }
    }
}
