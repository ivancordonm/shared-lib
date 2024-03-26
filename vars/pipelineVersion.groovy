import utils.Logger

def call(Map pipeline_config = [:]) {
    def logger = new Logger(this, 'DEBUG')
    logger.info("pipeline_config: ${pipeline_config}")
    def config
    def pom
    def version
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
                        try {
                            pom = readMavenPom file: 'pom.xml'
                        } catch (FileNotFoundException e) {
                            logger.error("Error: ${e}")
                        }
                        def pom_version = pom.version
                        logger.info("version: ${pom_version}")
                        if (pom_version.contains('SNAPSHOT')) {
                            version = pom_version.replace('-SNAPSHOT', '')
                        } else {
                            version = pom_version
                        }
                        logger.info("version: ${version.split('\\.')}")
                        switch (config.version.upgrade) {
                            case 'major':
                                def s = version.split('\\.')
                                version = "${s[0].toInteger() + 1}.${s[1]}.${s[2]}"
                                break
                            case 'minor':
                                def v = version.split('\\.')
                                version = "${v[0]}.${v[1].toInteger() + 1}.${v[2]}"
                                break
                            case 'patch':
                                def v = version.split('\\.')
                                version = "${v[0]}.${v[1]}.${v[2].toInteger() + 1}"
                                break
                            default:
                                break
                        }
                    }
                }
            }
            stage('Update Git') {
                steps {
                    script {
                        logger.info("new version: ${version}")
                        pom.version = version
                        writeMavenPom file: 'pom.xml', model: pom
                        // commit new version
                        sh "git config --global user.email 'jenkins@localhost'"
                        sh "git config --global user.name 'Jenkins'"
                        checkout scm
                        sh "git add pom.xml"
                        sh "git commit -m 'Upgrade version to ${version}'"
                        sh "git push origin master"
                        logger.info("Version updated to ${version}")
                    }
                }
            }
        }
    }
}
