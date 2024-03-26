import utils.Logger

def call(Map pipeline_config = [:]) {
    def logger = new Logger(this, pipeline_config?.logger?.level ?: 'INFO')
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
                        logger.info("Reading config.yml")
                        try {
                            config = readYaml file: "CI/JenkinsFiles/config.yml"
                        } catch (FileNotFoundException e) {
                            logger.error("Error: ${e}")
                        }
                        logger.debug("config: ${config}")
                    }
                }
            }
            stage('Check enable') {
                steps {
                    script {
                        if (!config.version.enable) {
                            logger.info("Versioning is disabled")
                            return
                        }
                    }
                }
            }
            stage('Version') {
                steps {
                    script {
                        logger.info("Reading pom.xml")
                        try {
                            pom = readMavenPom file: 'pom.xml'
                        } catch (FileNotFoundException e) {
                            logger.error("Error: ${e}")
                        }
                        def initialVersion = pom.version
                        logger.debug("version: ${initialVersion}")
                        if (initialVersion.contains('SNAPSHOT')) {
                            initialVersion = initialVersion.replace('-SNAPSHOT', '')
                        }
                        logger.debug("version: ${initialVersion.split('\\.')}")
                        def upgradedVersion = initialVersion
                        switch (config.version.increment) {
                            case 'major':
                                def s = initialVersion.split('\\.')
                                upgradedVersion = "${s[0].toInteger() + 1}.${s[1]}.${s[2]}"
                                break
                            case 'minor':
                                def v = initialVersion.split('\\.')
                                upgradedVersion = "${v[0]}.${v[1].toInteger() + 1}.${v[2]}"
                                break
                            case 'patch':
                                def v = initialVersion.split('\\.')
                                upgradedVersion = "${v[0]}.${v[1]}.${v[2].toInteger() + 1}"
                                break
                            default:
                                break
                        }
                        version = upgradedVersion
                    }
                }
            }
            stage('Update Git') {
                steps {
                    script {
                        logger.info("Updating version in pom.xml to ${version}")
                        pom.version = version
                        // commit new version
//                        sh "git config --global user.email 'ivancordonm@gmail.com'"
//                        sh "git config --global user.name 'icordonm'"
                        withCredentials([gitUsernamePassword(credentialsId: 'Ivan-Github', gitToolName: 'git-tool')]) {
                            checkout scm
                            writeMavenPom file: 'pom.xml', model: pom
                            sh "git add pom.xml"
                            sh "git commit -m 'Upgrade version to ${version}'"
                            sh "git push origin main -f"
                        }
                        logger.info("Version updated to ${version}")
                    }
                }
            }
        }
    }
}
