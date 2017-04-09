## Synopsis

An angular JS/springBoot application to allow business users to edit java properties files using a simple browser editor. The properties files are expected to have keys in the form abc.def.ghi=Some value. This project is especially for editing values in resource bundles.

## Installation

This is a gradle project. To run the projectc simple type
```
gradle bootRun
```

The list of properties files to be included in the editor can be configured in the appilcation.yml file located in 
src/main/resources/config/application.yml

This can also be copied into the root directory for as per Sprign Boot.

## Tests
This is a gradle project. To run the tests simple type
```
gradle test
```
