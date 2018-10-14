# JCK Play! Framework

JCK is a web service, rest api using [Play! Framework](https://playframework.com).

### Dependencies

  - [PlayFramework Core](https://github.com/ehureka/playframework-core)
  - [jBCrypt](https://www.mindrot.org/projects/jBCrypt/)

### Installation

JCK requires [Play! Framework](https://playframework.com/) v2.6.20 to run.

Install the dependencies and devDependencies and start the server at default port (9000).

```sh
$ cd jck-play-ws
$ sbt compile run
```

#### Building for source
Generating pre-built zip archives for distribution:
```sh
$ sbt dist
```