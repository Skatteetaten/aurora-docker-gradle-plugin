node {
   stage 'Checkout'
   checkout scm

   stage 'Build and Test'
   sh "./gradlew clean build upload"

   stage 'Generate Reports'
   step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/**/*.xml'])
}