# Project demo link

https://karolina.place/SendHybrid/

# Video demo

https://drive.google.com/file/d/1A8g7AGfIMdHwhtUKfT_rDxJxsgoYqJG0/view?usp=sharing

# Frameworks, compilers, runtime environment, libraries

* JDK17
* Spring
* Hibernate
* PDFBox
* Apache Tika
* JavaScript
* React

# User Manual

From *Documents* tab user can access the list of uploaded documents, see their status and validation report, and Add new
documents.

By clicking the *See report* button on the validated document row, user can see the validation report. If file is
repairable the system offer a fix with the button *Repair file automaticaly. Report of the correct file lists the
metadata, and offers to download the PDF file with automatically set headers with *Download file* button.

It is possible to check current validation parameters by going to the *Admin settings* tab.

# How to run

## Backend

The simplest approach is to run the *main* function in the `HackYeahBackendApplication` from the IntelliJ.

Another way is to run it with the Docker container, you need to perform the following steps to do so:

* `./gradlew build`
* go to `build/libs` directory within project
* `java -jar ./HackYeahBackend-0.0.1-SNAPSHOT-boot.jar`

## Frontend

Run the following commands:

* `npm install`
* `npm start`
* Open browser and enter *http://localhost:3000/SendHybrid*

To change the environment pointed by the frontend, follow the instructions described in the *Administrator guide*
section.

# Administrator guide

The Backend environment pointed by the frontend can be changed in the `callApi.js` file just replace
the `https://hack-yeah-backend-wixa-tech.herokuapp.com` part in the `url` string by something else.

# Architecture

The backend part was deployed to Heroku with use of the CI/CD pipeline configured in the GitHub repository. The frontend
part was deployed with use of the GitHub pages.

The idea of the app was to offer non-blocking uploads. Meaning that the client doesn't have to wait until the documents
are validated. Core of the documents processing happens in the `ValidatorEngineService`. Validation flow consists of
multiple chains, which can be added by creating components implementing `ValidationPlugin`
or `ValidationPluginWithInput` interfaces.

# Original location of repositories

The final version of the repo consists of files copied from two separate repositories:

https://github.com/WixaTech/backend 

&

https://github.com/Keikeu/SendHybrid

# heroku

`git push heroku main

`heroku logs --tail``

`heroku ps:scale web=1`
