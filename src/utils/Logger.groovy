package utils

class Logger {

    def LOGGING_LEVELS = [FINE: 0, TRACE: 1, DEBUG: 2, INFO: 3, WARN: 4, ERROR: 5]

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

    def fine(message, instance = null, method = "") {
        if (this.level <= LOGGING_LEVELS.FINE) {
            printer("FINE", message, instance, method)
        }
    }

    def trace(message, instance = null, method = "") {
        if (this.level <= LOGGING_LEVELS.TRACE) {
            printer("TRACE", message, instance, method)
        }
    }

    def debug(message, instance = null, method = "") {
        if (this.level <= LOGGING_LEVELS.DEBUG) {
            printer("DEBUG", message, instance, method)
        }
    }

    def info(message, instance = null, method = "") {
        if (this.level <= LOGGING_LEVELS.INFO) {
            printer("INFO", message, instance, method)
        }
    }

    def warn(message, instance = null, method = "") {
        if (this.level <= LOGGING_LEVELS.WARN) {
            printer("WARN", message, instance, method)
        }
    }

    def error(message, instance = null, method = "") {
        if (this.level <= LOGGING_LEVELS.ERROR) {
            printer("ERROR", message, instance, method)
        }
    }

    def printer(level, message, instance, method) {
        try {
            if (instance)
                if (method)
                    step.println("${level}: ${instance.getClass().getSimpleName()}.${method} - ${message}")
                else
                    step.println("${level}: ${instance.getClass().getSimpleName()} - ${message}")
            else
                step.println("${level}: ${message}")
        } catch (Exception e) {
            step.println("WARN: Some error occurred while logging")
            step.println("${level}: ${message}")
        }
    }
}
