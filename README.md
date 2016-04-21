## Abstract
This is FREE to use and extend project. Purpose of this project is to expose REST endpoints to act as webhooks with specific purpose. In it's very first commit, it accepts `POST` request from 3scale admin portal on CREATION of new developer and inserts new subscriber to MAILCHIMP specific list. Like any other project, more end points can be added and more integration from same provider (ex. MailChimp) or new Providers can be added. 

## Technology Stack
- [x] JAVA language is being used.
- [x] Latest spring boot libraries. (1.3.x+)
- [x] MAVEN for build system
- [x] Use your own favourite IDE (vim, eclipse, intellij, notepad

## How to run it
- Clone (or fork, then clone) this project to your local environment.
- Like any other spring boot app, it has it's environment configuration in application.properties.
  - Either you update required values in that file directly
  - Or you can drop in application-webhook.properties with your own values. 
 - In case of MailChimp, log in to your MC account and generate API key. 
  - Refer [MailChimp API Getting Started](http://developer.mailchimp.com/documentation/mailchimp/guides/get-started-with-mailchimp-api-3/)

### Command line commands
#### Initial setup
```
git clone https://github.com/ajainy/webhook.git
cd webhook
mvn clean compile
mvn package
```
#### Configuration
```
vi src/main/resources/application.properties <-- Add right values.
OR override application.properties using own file.
vi src/main/resources/`application-webhook.properties` <-- and override some or all values, as defined in `application.properties`
OR you can make jar file and provide `application-webhook.properties` file from filesystem. (Preferred in PRODUCTION like envs.) See below, how to execute.
```
#### Run
**if you have configured properties file in output already**
```
mvn spring-boot:run
java -jar target\webhook-0.0.1-SNAPSHOT.jar
```
**if you want to provide external properties file**
```
java -jar target\webhook-0.0.1-SNAPSHOT.jar --spring.config.location=file:<path to directory> --spring.profiles.active=webhook 
```
## How to extend it
Very first release or commit is for specific purpose. (3scale to mailchimp). But it's quite possible to use better abstraction (or interfaces to have multiple implementations and better factory pattern applied)
For example
- Add more methods in [MailChimp class](https://github.com/ajainy/webhook/blob/master/src/main/java/io/snapcx/webhook/integrations/MailChimp.java) for other things. Refer MailChimp api docs for all possible methods. (http://developer.mailchimp.com/documentation/mailchimp/reference/overview/)
- Add more endpoints either in same class or drop in new class. 
- General recommendation is, avoid any hardcoded values. Use application.properties for any config values.
## Tentative Roadmap
As of now, envision is to add more endpoints which caters (or act as) webhooks for specific purpose. 
### More webhooks and end points
- We can add more methods for MailChimp
- We can have generic endpoints for multiple purpose. 
- We can add endpoints for different clients. (in this case, we have 3scale). 
### Tests?
Having one or more test for each end point provides some basic smoketest. It needs to be enforced. 
General recommendation is to use spring framework provided testing patterns. 
Not a big fan of _unit tests_. Inidividual developers can fork or add tests, as much they want.
### Security?
As of now, there is no security key etc for authentication and authorization purpose. If this webservice is running on public node, it's exposed to be misused.
Or if you are more serious about metering, then involving API Management solutions might be good idea.
### Metrics?
As we get time, add better metrics than already available default ones.
