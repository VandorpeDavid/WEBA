# Requirements
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

* Java 11
* A (postgres) database (configurable)
  * If the db user does not have superuser permissions (it shouldn't), run `CREATE EXTENSION btree_gist;` in the database as a superuser before setup.
* Maven

Build by running `mvn package` (jar will be in the target directory).

Run with `SPRING_CONFIG_LOCATION=CONFIG_DIR_HERE java -jar -Dspring.profiles.active=production election-0.0.1-SNAPSHOT.jar`, 
with the `SPRING_CONFIG_LOCATION` variable pointing to a directory containing an `application.yml` file. Check `src/main/resources/` for an example.
The application should now be hosted at port 8080 (configurable).


## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/VandorpeDavid"><img src="https://avatars.githubusercontent.com/u/11561878?v=4?s=100" width="100px;" alt=""/><br /><sub><b>David Vandorpe</b></sub></a><br /><a href="https://github.com/VandorpeDavid/WEBA/commits?author=VandorpeDavid" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!