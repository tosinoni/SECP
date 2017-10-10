# SECP
Secure Electronic Communication Platform

The platform uses [AngularJS](http://angularjs.org) for client side and [Dropwizard](http://www.dropwizard.io) for server side.

##	Steps for setting up development environment
Install [Git](http://git-scm.com), [node.js](http://nodejs.org), [JDK 8](https://www.java.com), and [Maven 3](http://maven.apache.org/).

Install Yeoman:

    npm install -g yo

Install Bower:

    npm install -g bower

Install Bower:

    npm install -g grunt

## Running the server

In the SECP directory, run the server:

      npm install
      bower install
      mvn compile exec:exec -pl SECP-service

Your server will run at [http://localhost:8080](http://localhost:8080).

## Running the client

In the SECP directory, run the server:

      grunt server

The Grunt server will run at [http://localhost:9000](http://localhost:9000).  It will proxy REST requests to the Dropwizard server running at [http://localhost:8080](http://localhost:8080).

The Grunt server supports hot reloading of client-side HTML/CSS/Javascript file changes, while the Dropwizard service supports hot reloading of Java class file changes. This means you do not have to stop the server when you make changes.

##	Information about editing the code

The project is making use of angular-dropwizard generator plugin. For more Information, please visit [Angular-Dropwizard](https://www.npmjs.com/package/generator-angular-dropwizard) website.
