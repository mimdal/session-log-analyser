# Session Log Analyser
Parse log file and extract useful data in a dynamic table.

## Note
The tool is written for specific application log format and needed to fork and change implementation to support other log formats.

## sample log format
[INFO]  2020-06-29T22:36:52.434 channel:channel-ip   thread:[thread-number] client:[client-id] 
session_id:[sesstion-id] package.Class.method<init>@100:

## How build
- install maven
- run `mvn clean package`

## How run
Assuming Java (8+) Runtime Environment installed.<br/>
GOTO target directory, open terminal and run the following command:
```bash
java -jar session-log-analyser-{version}-jar-with-dependencies.jar 
    --log "[INFO]  2020-06-29T22:36:52.434 ... [sesstion-id] package.Class.method<init>@100:" 
    --file "absolute/path/to/log-file"
```

A new directory with name output will be created in the target folder.
