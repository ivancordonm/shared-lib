import utils.Logger

def call(Map pipeline_config = [:]) {
    def logger = new Logger(this, 'DEBUG')
    logger.info("pipeline_config: ${pipeline_config}")
    def config
    pipeline {
        agent { label 'linux' }
        stages {
            stage('Config') {
                steps {
                    script {
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
                        def pom_version = pom.version
                        logger.info("version: ${pom_version}")
                        def version
                        if (pom_version.contains('SNAPSHOT')) {
                            version = pom_version.replace('-SNAPSHOT', '')
                            switch (config.version.upgrade) {
                                case 'major':
                                    version = version.split('\\.')[0].toInteger() + 1
                                    version = "${version}.0.0"
                                    break
                                case 'minor':
                                    version = version.split('\\.')[1].toInteger() + 1
                                    version = "${version}.0"
                                    break
                                case 'patch':
                                    version = version.split('\\.')[2].toInteger() + 1
                                    version = "${version}"
                                    break
                                default:
                                    version = version
                                    break
                            }

                            logger.info("new version: ${version}")

                        }
                    }
                }
            }
        }
    }
}
