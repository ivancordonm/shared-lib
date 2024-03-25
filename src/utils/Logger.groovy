package utils

class Logger {

    def LOGGING_LEVELS = [FINE:0, TRACE:1, DEBUG:2, INFO:3, WARN:4, ERROR:5]

    def level
    def step

    Logger(step, level) {
        this.step = step
        this.level = (LOGGING_LEVELS."${level}" ?: LOGGING_LEVELS.INFO)
    }

    Logger(step) {
        this.step = step
        this.level = LOGGING_LEVELS.INFO
    }

    def setLevel(level) {
        try {
            this.level = LOGGING_LEVELS."${level}"
        } catch (Exception e) {
            this.level = LOGGING_LEVELS.INFO
        }
    }

    def fine(message) {
        if (this.level <= LOGGING_LEVELS.FINE) {
            println "FINE: ${this.step} - ${message}"
        }
    }

    def trace(message) {
        if (this.level <= LOGGING_LEVELS.TRACE) {
            println "TRACE: ${this.step} - ${message}"
        }
    }

    def debug(message) {
        if (this.level <= LOGGING_LEVELS.DEBUG) {
            println "DEBUG: ${this.step} - ${message}"
        }
    }

    def info(message) {
        if (this.level <= LOGGING_LEVELS.INFO) {
            println "INFO: ${this.step} - ${message}"
        }
    }

    def warn(message) {
        if (this.level <= LOGGING_LEVELS.WARN) {
            println "WARN: ${this.step} - ${message}"
        }
    }

    def error(message) {
        if (this.level <= LOGGING_LEVELS.ERROR) {
            println "ERROR: ${this.step} - ${message}"
        }
    }


    def log(level, message) {
        if (this.level <= LOGGING_LEVELS."${level}") {
            println "${level}: ${this.step} - ${message}"
        }
    }
}
