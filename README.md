# Selfregistration

To run locally:

    mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=dev"

Or use the shortcut:

    ./start.sh

## Building

We use sass to ease the pain of CSS development. You can best install sass using ruby. Best is to manage your rubies
with [rbenv](https://github.com/sstephenson/rbenv). After installing rbenv ```cd``` into this directory and run:

    gem install sass

    sass --watch src/main/sass/application.sass:src/main/resources/static/application.css

## ServiceRegistry

It uses the API on serviceregistry to add new services. See
https://serviceregistry.test.surfconext.nl/janus/app.php/api/doc/ for details.

Full documentation is available at https://github.com/janus-ssp/janus/blob/develop/docs/api/rest-api-details.md.
