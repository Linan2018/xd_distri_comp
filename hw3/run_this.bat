start cmd /k "cd code\rmiserver && javac *.java && start /b rmiregistry 1099 && java MyRMIServer && echo The Server program is running on this terminal. The Client program will run after 5 seconds. && java Server"
ping -n 3 127.0.0.1 > nul
start cmd /k "cd code\rmiclient && javac *.java && java MyRMIClient