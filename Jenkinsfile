def jenkinsfile

def overrides = [
    scriptVersion  : 'v7',
    iq : true,
    iqOrganizationName : 'Team APS',
    checkstyle : false,
    openShiftBuild: false,
    disableAllReports: true,
    pipelineScript: 'https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git',
    credentialsId: "github",
    jiraFiksetIKomponentversjon: true,
    deployTo: "gradle-plugin-portal",
    chatRoom: "#aos-notifications",
    deployGoal : "publishPlugins",
    versionStrategy: [
      [branch: 'master', versionHint: '2']
    ]
]

fileLoader.withGit(overrides.pipelineScript,, overrides.scriptVersion) {
   jenkinsfile = fileLoader.load('templates/leveransepakke')
}

jenkinsfile.gradle(overrides.scriptVersion, overrides, {

  if(it.isSnapshotVersion) {
    //it.version="2.2.3-rc4"
    error("Cannot publish snapshot version to gradle plugin portal")
  }

})
