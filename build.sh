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

#构建md5
echo "********build*******"
chmod +x $localPath/gradlew
cd $localPath && ./gradlew clean
cd $localPath && ./gradlew build
checkResult

timeinfo=`date +'%Y%m%d'`
echo "********copy apk *******"
cp $localPath/app/build/outputs/apk/app-debug.apk $localPath/bin/readhub-debug-${timeinfo}.apk
cp $localPath/app/build/outputs/apk/app-release-unsigned.apk $localPath/bin/readhub-release-unsigned-${timeinfo}.apk
$ANDROID_HOME/build-tools/25.0.2/apksigner sign --ks $localPath/debug.keystore --ks-pass pass:android --out $localPath/bin/readhub-release-${timeinfo}.apk $localPath/bin/readhub-release-unsigned-${timeinfo}.apk 
checkResult
cp $localPath/bin/readhub-release-${timeinfo}.apk $localPath/demo/readhub-release-newer.apk
