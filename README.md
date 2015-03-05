# Selfregistration

## Development

Create database

    mysql -uroot
    CREATE DATABASE selfregistration_dev_db DEFAULT CHARACTER SET utf8
    create user 'selfregistration'@'localhost' identified by 'selfregistration'
    grant all on selfregistration_dev_db.* to 'selfregistration'@'localhost';

To run locally:

    mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=dev"

Or use the shortcut:

    ./start.sh

We use sass to ease the pain of CSS development. You can best install sass using ruby. Best is to manage your rubies
with [rbenv](https://github.com/sstephenson/rbenv). After installing rbenv ```cd``` into this directory and run:

    gem install sass

    sass --watch src/main/sass/application.sass:src/main/resources/static/css/application.css

Goto http://localhost:8080/fedops

### ServiceRegistry

It uses the API on serviceregistry to add new services. See
https://serviceregistry.test.surfconext.nl/janus/app.php/api/doc/ for details.

Full documentation is available at https://github.com/janus-ssp/janus/blob/develop/docs/api/rest-api-details.md.

## Installing

1. Create database if not exists

        mysql -uroot
        CREATE DATABASE selfregistration_db DEFAULT CHARACTER SET utf8
        create user 'selfregistration'@'localhost' identified by '[PASSWORD_HERE]'
        grant all on selfregistration_db.* to 'selfregistration'@'localhost';

2. Download a [released](http://build.surfconext.nl/repository/public/releases/org/surfnet/coin/selfregistration)
  or [snapshot](https://build.surfconext.nl/repository/public/snapshots/org/surfnet/coin/selfregistration/) version
  of the selfregistration jar
2. The files produced by [Stoker](https://github.com/OpenConext/OpenConext-stoker/) need to be accessible for
  selfregistration.
2. Copy ```src/main/example/application-env.properties``` to application-{ENV}.properties. Where {ENV}
  is the environment on which the application runs (e.g. acc/prod). Adjust the properties accordingly.
3. Move the downloaded selfregistration jar and the created application-{ENV}.properties into the same direcory
  e.g. ```/opt/selfregistration```
4. Start the application from the install folder as follows: ```java -jar selfregistration.jar --spring.profiles.active={ENV}```
5. Alternatively copy ```src/main/example/selfregistration.example``` and adjust the variables accordingly.
  Place the file in ```/etc/init.d``` so that the application can be started and stopped like
  ```/etc/init.d/selfregistation start|stop```.

