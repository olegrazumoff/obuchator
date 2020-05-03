#!groovy
@org.jenkinsci.plugins.workflow.libs.Library('bercut')
//@oleg
import test.*
import test.atlas.MibTree
import test.helpers.FSHelper

def modelDbName = 'db_model'
def modelDbUrl = 'jdbc:postgresql://modeldb:5432/' + modelDbName
def modelDbUser = 'model'
def modelDbPassword = 'model'
def modelDbDataPath = '~/modeldbdata'

def appDataPath = '~/app'

node(params.nodeName) {
    try {

        Global.reportEmail = 'test@test.ru'
        Global.init(this)

        stage('Start') {
            Docker.composeUp([
                    MODELDB_DATA_PATH: modelDbDataPath + '/mydata',
                    APP_DATA_PATH    : appDataPath
            ])
        }

        stage('Finish') {
            Global.finishNormal(true, true)
        }
    } catch (Throwable e) {
        Global.finishException(e)
    }
}
