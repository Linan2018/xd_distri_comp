start cmd /k "cd src && javac Server.java && javac Client.java && echo The Server program is running on this terminal. The Client program will run after 10 seconds. && java Server"
ping -n 10 127.0.0.1 > nul
start cmd /k "cd src && java Client 11.11+11 && java Client 0.1!10 && java Client 1.1*10 && java Client 111/11"
start cmd /k "cd src && java Client 2.2@222 && java Client 22-2.2 && java Client 22--"
start cmd /k "cd src && java Client 3+3+3 && java Client 3.3+ && java Client 33/3"
