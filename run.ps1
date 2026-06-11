$connector = "$env:USERPROFILE\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar"
$classes   = "$PSScriptRoot\target\classes"
& "C:\Program Files\Java\jdk-24\bin\java.exe" -cp "$classes;$connector" com.minierp.Main
