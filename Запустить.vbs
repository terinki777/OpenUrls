Set objShell = CreateObject("WScript.Shell")
objShell.Run "mvn clean"
WScript.sleep 1000*2
objShell.Run "mvn clean -Djunit.jupiter.execution.parallel.enabled=true test"
Set objShell = Nothing