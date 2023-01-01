# AndroidLogcat
android logcat command wrapper

```java
String logFilePath = "/sdcard/Android/data/<your app package name>/cache/logcat.txt";
Logcat.logToFile(logFilePath);
```

```java
Result<Logcat> result = new Logcat.Builder()
        .file("")
        .format("threadtime") // -v
        .filterspecs("*:D")
        .logcat();
if (result.getError() != null) {
    // handle Exception
}
Logcat logcat = result.get();
```
