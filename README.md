# playframework-core

[![Version](https://img.shields.io/github/v/release/felipebonezi/playframework-core?logo=java)](https://github.com/felipebonezi/playframework-core/releases)
[![CLA assistant](https://cla-assistant.io/readme/badge/felipebonezi/playframework-core)](https://cla-assistant.io/felipebonezi/playframework-core)
[![Licence](https://img.shields.io/github/license/felipebonezi/playframework-core?color=blue)](https://github.com/felipebonezi/playframework-core/blob/master/LICENSE)

Core classes &amp; models to re-use on your [Play! Framework](https://playframework.com/) project.

### Dependencies

- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/)
- [Java JWT](https://github.com/auth0/java-jwt)

Check all project dependencies in our AutoPlugin
at [project/Dependencies.scala](https://github.com/justapagamentos/playframework-core/blob/master/project/Dependencies.scala)
file.

### How to use?

This project is distributed using GitHub Package Registry
using [sbt-github-packages](https://dev.to/gjuoun/publish-your-scala-library-to-github-packages-4p80) plugin, so you
need to generate or use
your [Personal Access Token](https://docs.github.com/pt/github/authenticating-to-github/creating-a-personal-access-token)
available on GitHub.

Then, you need to set an Environment Variable **OR** GitHub Config:

```sh
$ export GITHUB_TOKEN=<PERSONAL_ACCESS_TOKEN_AQUI>
```

```sh
$ git config --global github.token <PERSONAL_ACCESS_TOKEN_AQUI>
```

Now, go to your project and import in your `plugins.sbt`:

```scala
// GitHub Packages plugin.
addSbtPlugin("com.codecommit" % "sbt-github-packages" % "X.Y.Z")
```

Then, configure your GitHub credentials, Maven resolver and Library Dependencies in `build.sbt`:

```scala
// GitHub Packages config.
// Suppress Warnings about GitHub Repository Owner that is not required (OPTIONAL).
githubSuppressPublicationWarning := true

// Set your Personal Access Token using an Environment Variable or a Git Configuration.
githubTokenSource :=
  TokenSource.Environment("GITHUB_TOKEN") || TokenSource.GitConfig("github.token")

// Add another Maven Resolver using GitHub Package Registry.
resolvers += Resolver.githubPackages("justapagamentos")

// Add the playframework-core dependency.
libraryDependencies += "vc.com.justa" %% "playframework-core" % "vX.Y.Z"
```

### How to run this project?

This project requires [Play! Framework](https://playframework.com/) v2.8.X and [Scala](https://www.scala-lang.org)
2.13.X or 2.12.X to run.

Install the dependencies and devDependencies and start the server at default port (9000).

```sh
$ cd playframework-core
$ sbt compile run
```

#### Distribute

Generating pre-built zip archives for distribution:

```sh
$ cd playframework-core
$ sbt dist
```

### Authors

- [Felipe Bonezi](mailto:fb@justa.com.vc)
