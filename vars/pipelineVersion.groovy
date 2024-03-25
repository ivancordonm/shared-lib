import utils.Logger

def call(Map pipeline_config = [:]) {
    def logger = new Logger(this, 'DEBUG')
    logger.debug("pipeline_config: ${pipeline_config}")
    pipeline {
        agent { label 'linux' }
        stages {
            stage('Get Pipeline Version') {
                steps {
                    script {
                        sh "echo ${pipeline_config['pipelineVersion']} > pipelineVersion"
                    }
                }
            }
        }
    }
}
