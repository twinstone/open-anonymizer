# open anonymizer

[![MIT License](https://img.shields.io/apm/l/atomic-design-ui.svg?)](https://opensource.org/licenses/MIT)
[![GitHub Release](https://img.shields.io/badge/version-v1.0.0-blue)]() 

## Features
<ul> 
<li>Easy to configure</li>
<li>Dynamic data source class configuration. See <a href="/#ds">Data sources</a>. In current version next data sources are supported:
<ul>
<li>MySql</li>
<li>MongoDB</li>
<li>Neo4J</li>
<li>CSV files</li>
<li>SQL insert script files</li>
</ul>
 </li>
<li>Dynamic anonymization class configuration. See <a href="/#anon">Anonymization classes</a></li>
</ul>

## Open Source
Open anonymizer is an open source project under ... license. Did you find bug or have any idea how to make application better,
please, let me know by make a feature request or filling a bug report at github.com.

## Quick start
Here is a quick guide how to set up your own data anonymization process. 

#### Configuration file
Through a configuration file in JSON format we need to tell open anonymizer how to 
connect to input and output data source and how to process entities. Simple configuration 
file should look like:
```
{
   "in_connection": {
      "data_source_builder": "MYSQL",
     "host": "localhost",
      "port": 3306,
      "database": "production",
      "user": "username",
      "password": "password",
      "params": "useLegacyDatetimeCode=false&serverTimezone=UTC"
    },
    "out_connection": {
      "data_source_builder": "NEO4J",
      "host": "localhost",
      "port": 7687,
      "database": "Graph",
      "user": "user",
      "password": "password"
    },
    "entities": [
      {
        "name": "UserEntity",
        "source": "users",
        "id": "id",
        "fields": [
          {
            "name": "id"
          },
          {
            "name": "first_name",
            "configuration": {
              "anonymizationClass": "DICTIONARY",
              "params" : {
                "dictionary": "first_name"
              }
            }
          },
          {
            "name": "last_name",
            "configuration": {
              "anonymizationClass": "DICTIONARY",
              "params" : {
                "dictionary": "last_name"
              }
            }
          },
          {
            "name": "email",
            "unique": true,
            "configuration": {
              "anonymizationClass": "HASH",
              "secret": "password"
            }
          },
          {
           "name": "gender",
           "allowsNull": true,
           "configuration": {
             "anonymizationClass": "DELETE"
           }
          }
        ]
      }
    ],
    "stages":[
         "openanonymizer.core.stage.ValidationStage",
         "openanonymizer.core.stage.AnonymizationStage"
    ],
    "threads": 2,
    "page_size": 100,
    "dict_path": "/src/main/resources/dict"
}
```
In this example we tell open anonymizer the following:
<ul>
<li> How to connect to input source database. In this example we use MySql database which runs
on localhost on port 3306.</li>
<li> How to connect to output database. In this example we use Neo4J database which runs on
localhost on port 7687.</li>
<li>Which entities during anonymization will be processed. In  this example we want to process
table with user's data. Next anonymization functions will be applied:
<ul>
<li>Value in field <b>first_name</b> should be replaced by random value from the first name dictionary.</li>
<li>Value in field <b>last_name</b> should be replaced by random value from the last name dictionary.</li>
<li>Value in field <b>email</b> should be hashed.</li>
<li>Value in field <b>gender</b> should be removed from the output data.</li>
</ul>
You could define your own anonymization class. See <a href="/#anon">Anonymization classes</a>.
</li>
<li>Data should be processed in two stages:
<ul>
<li><b>openanonymizer.core.stage.ValidationStage</b> - validates configuration file, that all required fields 
are filled and relations are valid.</li>
<li><b>openanonymizer.core.stage.AnonymizationStage</b> - process data from input data source and save them 
to output data source.</li>
</ul>
You could define your own stage. See <a href="/#stages">Stages</a>.
</li>
<li> We want to read and process data from input data source using 2 simultaneously running treads.</li>
<li> We want to read maximally 100 entities per database request.</li>
<li> Path to dictionary files is set up to some directory on our file system.</li>
</ul>

#### Running application
Add jar file to your class path. If you use Maven or Gradle - download jar file from Maven repository.
Sample runnable class looks like:
```
package my.awesome.project;

import openanonymizer.config.Configuration;
import openanonymizer.config.Logging;
import openanonymizer.config.loader.ConfigurationLoader;
import openanonymizer.config.loader.JsonConfigurationLoader;
import openanonymizer.core.stage.StageManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //Set up configuration file for log4j
        Logging.configure("log4j.xml");
        //Configuration is loaded from file passed in programm argument
        ConfigurationLoader loader = new JsonConfigurationLoader();
        Configuration configuration = loader.readConfiguration(args[0]);
        StageManager manager = StageManager.fromConfiguration(configuration);
        manager.processChain();
        configuration.getInputSource().close();
        configuration.getOutputSource().close();
    }
}
```

## User extensions
It could happen that in current version open anonymizer would not all have all features you need. So you could 
dynamically add personal implementation of data source, anonymization class, transformer or stage.  

<div id="ds"></div>

#### Data sources
Data sources are used for executing basic CRUD operations under databases or files. To create your own data source 
simply implements interface ``openanonymizer.datasource.DataSource`` like in example below:
```
package my.awesome.project;

public class MyAwesomeDataSource implements DataSource {

    public CsvDataSource() {
        // Your code goes here
    }

    @Override
    public DataSet readDataSet(EntityDescriber describer) {
        // Your code goes here
    }

    @Override
    public void saveEntities(EntityDescriber describer, DataSet dataSet) {
        // Your code goes here
    }

    @Override
    public void updateEntities(EntityDescriber describer, DataSet dataSet) {
        // Your code goes here
    }

    @Override
    public void close() {
        // Your code goes here
    }
}
```
After that create builder class for your data source. You could use your own builder or implements interface 
``openanonymizer.datasource.DataSourceBuilder`` like in example below:
```
package my.awesome.project;

public class MyDataSourceBuilder implements DataSourceBuilder<MyAwesomeDataSource, S> {

    @Override
    public MyAwesomeDataSource fromSource(S source) {
        // Your code goes here
    }
}
```
Next add its reference to data source configuration:
```
"in_connection": {
    "data_source_builder": "my.awesome.project.MyDataSourceBuilder"
    // Your params
}
```

<div id="anon"></div>

#### Anonymization classes
Anonymization classes are used for field value transformation during data process. 
To create your own anonymization class simply implements interface ``openanonymizer.anonymizer.Anonymizer`` like in example below:
```
package my.awesome.project;
   
public class MyAwesomeAnonymizer extends Anonymizer {
   @Override
   public void anonymize(EntityWrapper wrapper, FieldDescriber describer, Configuration configuration) {
       // Your code goes here
   }
}
``` 
After that add its reference to field configuration:
```
{
   "name": "my_field",
   "configuration": {
        "anonymizationClass": "my.awesome.project.MyAwesomeAnonymizer",
        "params" : {
            // additional params
        }
   }
}
```

<div id="transformers"></div>

#### Transformers
Transformers are used to convert text data into representation of Java class. In current version ``LocalDateTransformer`` and 
``LocalDateTimeTransformer`` are implemented. To create your own transformer class simply implements interface ``openanonymizer.core.transformer.Transformer``
like in example below:
```
package my.awesome.project;

public class MyAwesomeTransformer implements Transformer<T> {
    @Override
    public T transform(String input) {
        // Your code goes here
    }
}
``` 
After that add its reference to field configuration:
```
{
   "name": "my_field",
   "configuration": {
        "anonymizationClass": "my.awesome.project.MyAwesomeAnonymizer",
        "params" : {
            transformer: "my.awesome.project.MyAwesomeTransformer"
        }
   }
}
```

<div id="stages"></div>

#### Stages
Stages are used to divide anonymization process into several pipelines. For example, before running data transformation
you need to remove or archive old data. To create your own stage class simply implements interface ``openanonymizer.core.stage.Stage`` like in example below:
```
package my.awesome.project;

public class MyAwesomeStage implements Stage {
    @Override
    public void executeStage(Configuration configuration) {
        // Your code goes here
    }
}
```
After that add its reference to stages configuration:
```
"stages":[
    "my.awesome.project.MyAwesomeStage"
]
```