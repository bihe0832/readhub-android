#!/bin/sh
# author hardyshi

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

deleteempty() {
  find ${1:-.} -mindepth 1 -maxdepth 1 -type d | while read -r dir
  do
    if [[ -z "$(find "$dir" -mindepth 1 -type f)" ]] >/dev/null
    then
      echo "$dir"
      rm -rf ${dir} 2>&- && echo "Empty, Deleted!" || echo "Delete error"
    fi
    if [ -d ${dir} ]
    then
      deleteempty "$dir"
    fi
  done
}



libName=$1
echo "include ':$1'" > ./settings.gradle && ./gradlew clean build uploadArchives 
