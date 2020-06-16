rem compile java files
javac -d WebContent\WEB-INF\classes -cp C:\Apache\apache-tomcat-9.0.36\lib\* src\bingo\WSServlet.java src\bingo\Roulette.java src\bingo\DBHandler.java

rem start Apache Tomcat
C:\Apache\apache-tomcat-9.0.36\bin\startup.bat

rem start Button
rem java -cp WebContent\WEB-INF\classes bingo.RouletteButton