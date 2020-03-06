# SouyunApp3
## 1、生成app签名
> keytool -genkey -v -alias souyun -keyalg RSA -validity 20000 -keystore /Users/10g-009/Desktop/workSpace/project/sign_file/souyun3.keystore
> souyun3:
  密钥口令:souyun2020
  
## 2、查看sha1和md5
* keytool -list -v -keystore xxx.jks
* keytool -list -v -keystore xxx.keystore
* 解压apk后,keytool -printcert -file CERT.RSA