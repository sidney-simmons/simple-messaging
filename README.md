# simple-messaging

simple-messaging is a gradle project meant to illustrate basic usage of a RabbitMQ message broker.  Some of what's illustrated:

* RabbitMQ between manager and multiple workers
    * Fanout exchange (manager sends message to all workers)
    * Work queue (manager submits jobs - workers accept one job at a time)
* Gradle multi-project structure

## Usage

Clone the repository and execute the following.  The manager runs on port 8001 and the workers run on ports 8002 - 8011 depending on how many worker instances you set via the build script (initially set to 3).  You can access the RabbitMQ management console at http://localhost:15672.

```
// Builds the manager and worker code
./gradlew build

// Builds and deploys the RabbitMQ server, the manager, and 5 workers
docker-compose up --build --scale worker=5
```

You can view the service logs by running the following.

```
docker-compose logs -f
```

Hit some of the application endpoints by importing the Postman collection in the `postman-collection.json` file.

## License
[MIT](https://choosealicense.com/licenses/mit/)