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
# rm -fr $localPath/libs/*
# # 构建基本依赖库
# echo "********APK build libs*******"
# cat build.conf | grep Lib | xargs -I {} /bin/bash ./build_lib.sh {}


# 调整依赖库
# cat build.conf | grep -v Lib | xargs -I {} echo "include ':{}'" > ./settings.gradle

# exit

#关闭本地测试
src="IS_TEST_VERSION = true"
dst="IS_TEST_VERSION = false"
cat $localPath/Framework_core/src/main/java/com/ottd/libs/framework/OttdFramework.java | sed "s/$src/$dst/g" > $localPath/bin/OttdFramework.java
mv -f $localPath/bin/OttdFramework.java $localPath/Framework_core/src/main/java/com/ottd/libs/framework/OttdFramework.java

echo "********APK build test start gradlew *******"
#返回上层目录启动构建
chmod +x $localPath/gradlew
cd $localPath && ./gradlew clean
cd $localPath && ./gradlew build
checkResult

timeinfo=`date +'%Y%m%d-%H%M'`
echo "********copy apk *******"
cp $localPath/Main/build/outputs/apk/debug/Main-debug.apk $localPath/bin/readhub_debug_${version}_${timeinfo}.apk
cp $localPath/Main/build/outputs/apk/release/Main-release-unsigned.apk $localPath/bin/readhub_release_${version}_unsigned_${timeinfo}.apk
$ANDROID_HOME/build-tools/25.0.2/apksigner sign --ks $localPath/debug.keystore --ks-pass pass:android --out $localPath/bin/readhub_release_${version}_${timeinfo}.apk $localPath/bin/readhub_release_${version}_unsigned_${timeinfo}.apk 
checkResult
cp $localPath/bin/readhub_release_${version}_${timeinfo}.apk $localPath/demo/readhub_release_newer.apk