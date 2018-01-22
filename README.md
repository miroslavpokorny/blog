# Blog

School project implementing simple blog web application.

## Setup of project

Backend is Maven (POM) project so all dependencies should be downloaded automatically by your IDE.

### Install

**node.js** - node js used for front-end development

**yarn** - used to manage front end dependencies

**MySQL Workbench** - Used to initialize database scheme from *blog-db.mwb*

**MariaDB** -- Database server (MySQL should be used as well) 

### Development of Administration app (Front-end) 

To develop frontend change dir to *./blog-admin/* then run **yarn install** after installation of dependencies run **yarn start** ot run development server.

#### Build 

To make production run **yarn build** command and then copy content of build dir to *../main/resources/static/admin* and copy file *index.html* to *../main/resources/templates* as admin.html and close all single tags (html must be valid well formed xml (because of thymeleaf server-side template system))

## License

Licensed under MIT