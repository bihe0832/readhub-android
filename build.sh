#!/bin/sh
#函数定义，检测执行结果
function checkResult() {  
   result=$?
   echo "result : $result"
   if [ $result -eq 0 ];then
      echo "checkResult: execCommand succ"
   else
    echo "checkResult: execCommand failed"
    exit $result
   fi
}  

echo "********build mkdir bin *******"

localPath=`pwd`
echo $localPath
#创建打包目录
if [ ! -d "./bin" ]; then
  mkdir $localPath/bin
fi

#进入打包目录并清空目录
cd $localPath/bin && rm -rf  * && cd $localPath
version=`git rev-list HEAD --count`

# 删除本地可能存在的依赖库
rm -fr $localPath/libs/*
# 构建基本依赖库
echo "********APK build libs*******"
cat build.conf | grep Lib | xargs -I {} /bin/bash ./build_lib.sh {}

echo "********APK build test start gradlew *******"
#返回上层目录启动构建
cat build.conf | grep -v Lib | xargs -I {} echo "include ':{}'" > ./settings.gradle
chmod +x $localPath/gradlew
cd $localPath && ./gradlew clean
cd $localPath && ./gradlew build
checkResult

timeinfo=`date +'%Y%m%d-%H%M'`
echo "********copy apk *******"
# cp $localPath/app/build/outputs/apk/app-debug.apk $localPath/bin/readhub-debug-${version}-${timeinfo}.apk
# cp $localPath/app/build/outputs/apk/app-release-unsigned.apk $localPath/bin/readhub-release-${version}-unsigned-${timeinfo}.apk
# $ANDROID_HOME/build-tools/25.0.2/apksigner sign --ks $localPath/debug.keystore --ks-pass pass:android --out $localPath/bin/readhub-release-${version}-${timeinfo}.apk $localPath/bin/readhub-release-${version}-unsigned-${timeinfo}.apk 
# checkResult
# cp $localPath/bin/readhub-release-${version}-${timeinfo}.apk $localPath/demo/readhub-release-newer.apk