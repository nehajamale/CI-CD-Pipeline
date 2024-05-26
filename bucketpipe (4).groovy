pipeline {
    agent {
        label 'simple1'
    }
    stages {
        stage('installing maven') {
            steps {
                sh '''
                   echo "we are installing maven"
                   sudo apt update
                   sudo apt install maven -y
                   '''
            }
        }
        stage('clone git on /home/ubuntu/workspace/project') {
            steps {
              
                echo "git pulling from public repository"
                 git 'https://github.com/AnupDudhe/studentapp-ui.git'
                
            }
        }
        stage('Artifact creation') {
            steps {
                sh '''sudo mvn clean
                sudo mvn package'''
            }
        }
        stage('Configuring aws cli') {
            steps {
                sh '''
                sudo apt install awscli -y
                sudo aws s3 cp /home/ubuntu/workspace/s3-project/target/studentapp-2.2-SNAPSHOT.war  s3://newbucketcbz/
                sudo aws s3 cp s3://newbucketcbz/NVanupDelete.pem /
                '''
            }
        }
         stage('Tomcat-installation') {
            steps {
                sh '''sudo wget https://dlcdn.apache.org/tomcat/tomcat-8/v8.5.98/bin/apache-tomcat-8.5.98.zip
                      sudo apt install unzip -y
                      sudo unzip apache-tomcat-8.5.98.zip
                      sudo aws s3 cp s3://newbucketcbz/studentapp-2.2-SNAPSHOT.war  apache-tomcat-8.5.98/webapps/student.war
                      sudo bash apache-tomcat-8.5.98/bin/catalina.sh stop
                      sudo bash apache-tomcat-8.5.98/bin/catalina.sh start
                    '''
                
            }
        }
    }
}