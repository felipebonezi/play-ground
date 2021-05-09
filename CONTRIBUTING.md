<!--- Copyright (C) Justa Serviços Financeiros SA. <https://www.justa.com.vc> -->

# Justa contributor guidelines

The canonical version of this document can be found on
the [Justa contributor guidelines](https://justa.com.vc/contributing) page of the Justa website.

## Prerequisites

Before making a contribution, it is important to make sure that the change you wish to make, and the approach you wish
to take will likely be accepted, otherwise you may end up doing a lot of work for nothing. If the change is only small,
for example, if it's a documentation change or a simple bugfix, then it's likely to be accepted with no prior
discussion.

Additionally, there are issues labels you can use to navigate issues that a good start to contribute:

- [`Status: Need Help!`](https://github.com/justapagamentos/playframework-core/labels/Status%3A%20Need%20Help%21)
- [`Type: Community`](https://github.com/justapagamentos/playframework-core/labels/Type%3A%20Community)
- [`Type: Good First Issue`](https://github.com/justapagamentos/playframework-core/labels/Type%3A%20Good%20First%20Issue)

### Procedure

1. Make sure you have signed the [Justa CLA](https://cla-assistant.io/justapagamentos/playframework-core); if not, sign
   it online.
2. Ensure that your contribution meets the following guidelines:
    1. Live up to the current code standard:
        - Not violate [DRY](https://www.oreilly.com/library/view/97-things-every/9780596809515/ch30.html).
        - [Boy Scout Rule](https://www.oreilly.com/library/view/97-things-every/9780596809515/ch08.html) needs to have
          been applied.
    2. Regardless of whether the code introduces new features or fixes bugs or regressions, it must have comprehensive
       tests. This includes when modifying existing code that isn't tested.
    3. Implementation-wise, the following things should be avoided as much as possible:
        - Global state
        - Public mutable state
        - Implicit conversions
        - ThreadLocal
        - Locks
        - Casting
        - Introducing new, heavy external dependencies
    4. The Playframework-Core API design rules are the following:
        - Play is a Java framework.
        - Java APIs should go to `src/main/java`, package structure is `core.myapipackage.xxxx`
        - Features are forever, always think about whether a new feature really belongs to the core framework or if it
          should be implemented as a module.
        - Code must conform to standard style guidelines and pass all tests.
    5. Basic local validation:
        - Not use `@author` tags since it does not
          encourage [Collective Code Ownership](https://www.extremeprogramming.org/rules/collective.html).
3. Submit a pull request.

If the pull request does not meet the above requirements then the code should **not** be merged into master, or even
reviewed - regardless of how good or important it is. No exceptions.

The pull request will be reviewed according to Justa team availability. For more
infos, [mail us](mailto:tech@justa.com.vc).