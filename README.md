# Session Log Analyser
parse log file and extract useful data in a dynamic table.

## Note
this tools write for specific application log and need to fork and change implementation to support other log format.<br>

## sample log format
[INFO]  2020-06-29T22:36:52.434 channel:channel-ip   thread:[thread-number] client:[client-id] session_id:[sesstion-id] package.Class.<init>@100:

## How build
- install maven
- run `mvn clean package`

## How run
Assuming Java(8+) Runtime Environment installed.<br/>
goto target directory, open terminal and run the following command:
```bash
java -jar session-log-analyser-{version}-jar-with-dependencies.jar --log "[INFO]  2020-06-29T22:36: ..." --file "absolute/path/to/log-file"
```

a directory will be created(output).
