rem compile java files
javac -encoding UTF-8 -d WebContent\WEB-INF\classes -cp C:\pleiades\tomcat\9\lib\* ^
src\bingo\WSServlet.java ^
src\bingo\Roulette.java ^
src\dbhandler\DBHandler.java ^
src\bingo\ReceivedMessage.java ^
src\bingo\MyDecoder.java

rem start Apache Tomcat
C:\pleiades\tomcat\9\bin\startup.bat

rem start Button
rem java -cp WebContent\WEB-INF\classes bingo.RouletteButton