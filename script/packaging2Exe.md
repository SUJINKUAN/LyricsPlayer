```shell 
jlink --module-path C:\Users\justin.sjk\Desktop\notes\env\openjfx-21.0.2_windows-x64_bin-jmods\javafx-jmods-21.0.2 --add-modules javafx.controls,javafx.fxml --output ../custom-jre
```
```shell
jpackage --type exe --input ..\target --dest ..\target --main-jar .\LyricesPlayer-1.0-SNAPSHOT.jar --main-class com.tool.lyrics.LyricsPlayerApp --runtime-image ..\custom-jre --win-shortcut --win-menu
```

