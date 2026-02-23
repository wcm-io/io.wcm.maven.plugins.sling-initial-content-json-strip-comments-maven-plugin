About Sling-Initial-Content JSON Strip Comments Maven Plugin
========================================

Removes all comments from JSON files in Sling-Initial-Content.

This plugin fixes an issue with AEM 6.6.2 (AEM 6.5 LTS SP2) which ships with glassfish instead of Jackson 2 as jakarta.json implementation. This breaks deployment of Sling-Initial-Content with JSON files containing comments. This plugins strips all comments from the JSON files in SLING-INF folders of the bundles as a workaround.


[![Maven Central](https://img.shields.io/maven-central/v/io.wcm.maven.plugins/sling-initial-content-json-strip-comments-maven-plugin)](https://repo1.maven.org/maven2/io/wcm/maven/plugins/sling-initial-content-json-strip-comments-maven-plugin)


### Documentation

* [Plugin Documentation][plugindocs]
* [Changelog][changelog]


### GitHub Repository

Sources: https://github.com/wcm-io/io.wcm.maven.plugins.sling-initial-content-json-strip-comments-maven-plugin


[plugindocs]: plugin-info.html
[changelog]: changes.html
