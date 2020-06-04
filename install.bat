@echo off
setlocal
echo  _    _      _      ______ _      _      ______                                   
echo ^| ^|  ^| ^|    ^| ^|     ^| ___ (_)    ^| ^|     ^| ___ \                                 
echo ^| ^|  ^| ^| ___^| ^|__   ^| ^|_/ /_  ___^| ^|__   ^| ^|_/ / __ ___  ___  ___ _ __   ___ ___ 
echo ^| ^|/\^| ^|/ _ \ '_ \  ^|    /^| ^|/ __^| '_ \  ^|  __/ '__/ _ \/ __^|/ _ \ '_ \ / __/ _ \
echo \  /\  /  __/ ^|_) ^| ^| ^|\ \^| ^| (__^| ^| ^| ^| ^| ^|  ^| ^| ^|  __/\__ \  __/ ^| ^| ^| (_^|  __/
echo  \/  \/ \___^|_.__/  \_^| \_^|_^|\___^|_^| ^|_^| \_^|  ^|_^|  \___^|^|___/\___^|_^| ^|_^|\___\___^|              
echo[
echo  _____          _        _ _       _   _                                         
echo ^|_   _^|        ^| ^|      ^| ^| ^|     ^| ^| (_)                                        
echo   ^| ^| _ __  ___^| ^|_ __ _^| ^| ^| __ _^| ^|_ _  ___  _ __                              
echo   ^| ^|^| '_ \/ __^| __/ _` ^| ^| ^|/ _` ^| __^| ^|/ _ \^| '_ \                             
echo  _^| ^|^| ^| ^| \__ \ ^|^| (_^| ^| ^| ^| (_^| ^| ^|_^| ^| (_) ^| ^| ^| ^|                            
echo  \___/_^| ^|_^|___/\__\__,_^|_^|_^|\__,_^|\__^|_^|\___/^|_^| ^|_^|                            
echo[
echo[

set "installFolder=%localappdata%\Programs\WebRP"
set "jarFile=%installFolder%\WebRP.jar"

:detectSrcJar
if exist WebRP.jar (
	set srcJar = WebRP.jar
) else (
	if exist target/WebRP.jar (
		set srcJar = target/WebRP.jar
	) else (
		echo The program can't find the file WebRP.jar needed for installation.
		goto quitProgramError
	)
)

:mkdir
if not exist "%installFolder%\*" md "%installFolder%" || echo An error occured while creating folder %installFolder% & goto :quitProgramError

:copyFiles
copy "target\WebRP.jar" "%localappdata%\Programs\WebRP\WebRP.jar"
if not errorlevel 0 (
	echo An error occured whle copying the file. It can be caused if the program is already running or by a problem with folder permissions.
	goto quitProgramError
)
copy "icon.ico" "%localappdata%\Programs\WebRP\icon.ico"
echo start javaw -jar -Xms1024m -Xmx1024m %jarFile% > "%installFolder%\WebRichPresence.bat"

:createStartup
set /p yesno=Do you want to start the program at startup (Y/[N])?
if /I "%yesno%" NEQ "Y" goto createShortcut
echo %installFolder%\WebRichPresence.bat > "%appdata%\Microsoft\Windows\Start Menu\Programs\Startup\WebRichPresence.bat"


:createShortcut
set /p yesno=Do you want to create a shortcut on the desktop (Y/[N])?
if /I "%yesno%" NEQ "Y" goto quitProgramSuccess
powershell "$s=(New-Object -COM WScript.Shell).CreateShortcut('%userprofile%\Desktop\Web Rich Presence.lnk');$s.TargetPath='%jarFile%';$s.IconLocation='%installFolder%\icon.ico';$s.Save()"


goto :quitProgramSuccess

:quitProgramError
echo Installation aborted
pause
exit

:quitProgramSuccess
echo Program successfully installed
pause
exit