cd C:\Users\Administrator\Desktop\MVTTT\Server
mvn clean package
Copy-Item .\target\MVTTT.war "C:\Software\Tomcat 10.1\webapps" -Force
Copy-Item .\src\main\pages\* "C:\Software\Tomcat 10.1\MVTT_WD" -Force
#&"C:\Program Files\Google\Chrome\Application\chrome.exe" "http://127.0.0.1:7181/MVTTT"

#needs web req to init database