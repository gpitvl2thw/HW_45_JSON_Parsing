@ECHO OFF

::==================================================
SET GITHUB_ACCOUNT=%1
SET REPO_NAME=%2
IF "%3" == "" (SET APP_BROWSER="NOBROWSER") ELSE (SET APP_BROWSER=%3)
IF "%4" == "" (SET APP_VERSION="1.0.0") ELSE (SET APP_VERSION=%4)
SET ARGS_01=%5
::==================================================
SET SNAP_SHOT=ScreenShotWebDriverFF
SET NAME_SNAP_SHOT=%REPO_NAME%-%APP_BROWSER%-%APP_VERSION%
SET MAIN_CLASS=core.Selenium
::==================================================

IF "%JAVA_HOME%" == "" GOTO EXIT_JAVA
ECHO Java installed
IF "%M2%" == "" GOTO EXIT_MVN
ECHO Maven installed
CALL git --version > nul 2>&1
IF NOT %ERRORLEVEL% == 0 GOTO EXIT_GIT
ECHO Git installed

GOTO NEXT
:NEXT
::rmdir /s /q %REPO_NAME%
::DEL /S /Q %REPO_NAME%.txt
IF EXIST %REPO_NAME% RMDIR /S /Q %REPO_NAME%
IF EXIST %SNAP_SHOT% RMDIR /S /Q %SNAP_SHOT%
:: вывод в файл
IF EXIST %NAME_SNAP_SHOT%.png DEL /S /Q %NAME_SNAP_SHOT%.png

:: I
:: клонируем jar-file для создания снимка браузера
git clone https://github.com/%GITHUB_ACCOUNT%/%SNAP_SHOT%.git
CD %SNAP_SHOT%

:: II
git clone https://github.com/%GITHUB_ACCOUNT%/%REPO_NAME%.git
CD %REPO_NAME%

:: III
:: SLEEP 2
ECHO Executing MVN SITE...
::CALL mvn clean package site test -Dtest=AllTests -Dversion=%APP_VERSION%-with-%APP_BROWSER%
CALL mvn clean site test -Dtest=AllTests -Dbuild.version=%APP_BROWSER%-v%APP_VERSION% -DtypeWebDriver=%APP_BROWSER%

:: IV
CD ..
ECHO Create SnapShot
::ECHO Executing Java programm ...
:: вывод в файл ->  >> ..\%REPO_NAME%.txt
::java -cp ..\target\%REPO_NAME%-%APP_VERSION%-with-%APP_BROWSER%.jar %MAIN_CLASS% %ARG_01% >> ..\%REPO_NAME%.txt
::java -jar .\target\%REPO_NAME%-%APP_VERSION%-with-%APP_BROWSER%-jar-with-dependencies.jar %ARG_01% >> ..\%REPO_NAME%.txt
java -jar %SNAP_SHOT%-1.1-jar-with-dependencies.jar %REPO_NAME%
SET exitcode=%ERRORLEVEL%
::ECHO %exitcode%

:: Change lavale on the *.but-file
CD ..
::ECHO %cd%
IF NOT %exitcode% == 0 GOTO SNAPSHOT_N

:: V - I
GOTO SNAPSHOT_Y
:SNAPSHOT_Y
ECHO SnapShot Created
ECHO Copy FILE... %REPO_NAME%.png
copy .\%SNAP_SHOT%\%REPO_NAME%.png .\%NAME_SNAP_SHOT%.png
GOTO DELETE

:: V - II
:SNAPSHOT_N
ECHO SnapShot DIDN'T Create
::ECHO Copy FILE... surefire-report.html
::copy .\target\site\surefire-report.html ..\surefire-report.html
ECHO Copy FOLDER... SITE
xcopy .\%SNAP_SHOT%\%REPO_NAME%\target\site site\ /H /E /G /Q /R /Y
ECHO Rename FOLDER... SITE IN %REPO_NAME%
ren site %NAME_SNAP_SHOT%
GOTO DELETE

:: VI
:DELETE
ECHO Delete...
::IF EXIST %REPO_NAME% RMDIR /S /Q %REPO_NAME%
IF EXIST %SNAP_SHOT% RMDIR /S /Q %SNAP_SHOT%
GOTO END


:EXIT_JAVA
ECHO No Java installed
GOTO END
:EXIT_MVN
ECHO No Maven installed
GOTO END
:EXIT_GIT
ECHO No Git installed
GOTO END
:NO_WORKSPACE
ECHO %WS_DIR% is not exists

GOTO END
:END
