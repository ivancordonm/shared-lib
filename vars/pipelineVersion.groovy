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
                        sh "echo ${pipeline_config}"
                        def config = step.readYaml file: 'CI/JenkinsFiles/config.yml'
                        logger.info("config: ${config}")
                    }
                }
            }
        }
    }
}
