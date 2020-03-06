start cmd /k "javac Server.java && javac Client.java && echo The Server program is running on this terminal. The Client program will run after 10 seconds. && java Server"
ping -n 10 127.0.0.1 > nul
start cmd /k "java Client 11.11+11"
start cmd /k "java Client 2.2@222"
start cmd /k "java Client 3+3+3"
start cmd /k "java Client 4--"
