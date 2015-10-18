#!/bin/sh

# ========================================
GITHUB_ACCOUNT=$1
REPO_NAME=$2
if [ "$3" == "" ]; then APP_BROWSER="NOBROWSER"; else APP_BROWSER=$3; fi
if [ "$4" == "" ]; then APP_VERSION="1.0.0"; else APP_VERSION=$4; fi
ARGS_01=$5
# ========================================
SNAP_SHOT=ScreenShotWebDriverFF
NAME_SNAP_SHOT=$REPO_NAME-$APP_BROWSER-$APP_VERSION
MAIN_CLASS=core.Selenium
# ========================================
if [ "$1" == "" ]; then echo "No arguments"; return; fi
#if [ $# -lt 4 ]; then echo "Should be 4 arguments"; return; fi

# java -version &>jv.txt;grep "java version" jv.txt | awk '{print $1,$3}'; 
if which java >/dev/null 2>&1 ; then java -version; else echo Java not installed; return; fi
# mvn --version &>mv.txt; grep "Apache Maven" mv.txt | awk '{print $2,$3}';
if which mvn >/dev/null 2>&1 ; then mvn --version; else echo Maven not installed; return; fi
# git --version &>gv.txt; grep "git version" gv.txt | awk '{print $1,$3}';
if which git >/dev/null 2>&1 ; then git --version; else echo Git not instlled; return; fi

# if [ -d "$WS_DIR" ] ; then cd ~/$WS_DIR; else echo $WS_DIR directory is not exist; return; fi
if [ -d "$REPO_NAME" ]; then rm -rf $REPO_NAME; fi
if [ -d "$SNAP_SHOT" ]; then rm -rf $SNAP_SHOT; fi
# :: вывод в файл
if [ "$NAME_SNAP_SHOT"".png" ]; then rm "$NAME_SNAP_SHOT"".png"; fi

# I
# клонируем jar-file для создания снимка браузера
git clone https://github.com/$GITHUB_ACCOUNT/$SNAP_SHOT.git
cd $SNAP_SHOT

# II
git clone https://github.com/$GITHUB_ACCOUNT/$REPO_NAME.git
cd  $REPO_NAME

# III
# SLEEP 2
echo Executing MVN SITE...
#mvn clean package site test -Dtest=AllTests -Dversion=$APP_VERSION-with-APP_BROWSER
mvn clean site test -Dtest=AllTests -Dbuild.version=$APP_BROWSER-v$APP_VERSION -DtypeWebDriver=$APP_BROWSER

# IV
cd ..
echo Create SnapShot
# echo Executing Java programm ...
# вывод в файл ->  >> ../$REPO_NAME.txt
#java -cp ./target/$REPO_NAME-$APP_VERSION-with-$APP_BROWSER.jar $MAIN_CLASS >> ../$REPO_NAME.txt
#java -jar ./target/$REPO_NAME-$APP_VERSION-with-$APP_BROWSER-jar-with-dependencies.jar >> ../$REPO_NAME.txt
java -jar $SNAP_SHOT-1.1-jar-with-dependencies.jar $REPO_NAME
exitcode=$?
#echo $exitcode

# Change lavale on the *.sh-file
cd ..

#pwd
if [ "$exitcode" -ne "0" ]; 
then
	# V - II
	# ERROR
	echo "SnapShot DIDN'T Create"
	echo "Copy FOLDER... SITE"
	mv ./$SNAP_SHOT/$REPO_NAME/target/site ./site
	echo "Rename FOLDER... SITE IN $REPO_NAME"
	mv ./site $NAME_SNAP_SHOT
else
	# V - I
	# NO ERROR
	echo "SnapShot Created"
	echo "Copy FILE... $REPO_NAME.png"
	mv ./$SNAP_SHOT/$REPO_NAME.png ./$NAME_SNAP_SHOT.png
fi

# VI
echo Delete...
# if [ -d "$REPO_NAME" ]; then rm -rf $REPO_NAME; fi
if [ -d "$SNAP_SHOT" ]; then rm -rf $SNAP_SHOT; fi
