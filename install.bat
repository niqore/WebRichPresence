copy "target\WebRP.jar" "%localappdata%\Programs\WebRP.jar"
echo start javaw -jar -Xms1024m -Xmx1024m %localappdata%\Programs\WebRP.jar > "%appdata%\Microsoft\Windows\Start Menu\Programs\Startup\webrp.bat"
echo Program successfully installed
pause