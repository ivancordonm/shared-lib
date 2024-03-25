def call(step, pipeline_config) {
    if(pipeline_config == null) {
        pipeline_config = [:]
    }

    stage('Configure') {
        echo "Configuring pipeline"
        echo "Pipeline config: ${pipeline_config}"
        echo "Step: ${step}"


    }
}
