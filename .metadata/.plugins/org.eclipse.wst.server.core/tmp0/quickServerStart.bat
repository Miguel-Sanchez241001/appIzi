@REM C:\Users\pra_msanchezs\IBM\rationalsdp\workspace-experim\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\quickServerStart.bat
@REM Generated: Mon Oct 21 13:59:08 COT 2024

@setlocal
@echo off

@REM Bootstrap values ...
cd C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1\bin
call "C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1\bin\setupCmdLine.bat"
@REM For debugging the server process:
@REM set DEBUG=-Djava.compiler=NONE -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=7777

@REM Environment Settings
SET PATH=%WAS_PATH%


@REM Launch Command
"C:\RAD9\IBM\WebSphere\AppServer/java/bin/java"  %DEBUG% "-Dosgi.install.area=C:\RAD9\IBM\WebSphere\AppServer" "-Dosgi.configuration.area=C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1/servers/server1/configuration" "-Dosgi.framework.extensions=com.ibm.cds,com.ibm.ws.eclipse.adaptors" "-Xshareclasses:name=webspherev85_1.8_64_bundled,nonFatal" "-Dsun.reflect.inflationThreshold=250" "-Dcom.ibm.xtq.processor.overrideSecureProcessing=true" "-Djava.security.properties=C:\RAD9\IBM\WebSphere\AppServer/properties/java.security" "-Dwas.debug.mode=true" "-Dcom.ibm.ws.classloader.j9enabled=true" "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=7777" "-Xbootclasspath/p:C:\RAD9\IBM\WebSphere\AppServer/lib/dertrjrt.jar;C:\RAD9\IBM\WebSphere\AppServer/java/jre/lib/ibmorb.jar" "-classpath" "C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1/properties;C:\RAD9\IBM\WebSphere\AppServer/properties;C:\RAD9\IBM\WebSphere\AppServer/lib/startup.jar;C:\RAD9\IBM\WebSphere\AppServer/lib/bootstrap.jar;C:\RAD9\IBM\WebSphere\AppServer/lib/lmproxy.jar;C:\RAD9\IBM\WebSphere\AppServer/lib/urlprotocols.jar;C:\RAD9\IBM\WebSphere\AppServer/deploytool/itp/batchboot.jar;C:\RAD9\IBM\WebSphere\AppServer/deploytool/itp/batch2.jar;C:\RAD9\IBM\WebSphere\AppServer/java/lib/tools.jar" "-Dibm.websphere.internalClassAccessMode=allow" "-Xms4096m" "-Xmx8192m" "-Xcompressedrefs" "-Xscmaxaot12M" "-Xscmx90M" "-Dws.ext.dirs=C:\RAD9\IBM\WebSphere\AppServer/java/lib;C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1/classes;C:\RAD9\IBM\WebSphere\AppServer/classes;C:\RAD9\IBM\WebSphere\AppServer/lib;C:\RAD9\IBM\WebSphere\AppServer/installedChannels;C:\RAD9\IBM\WebSphere\AppServer/lib/ext;C:\RAD9\IBM\WebSphere\AppServer/web/help;C:\RAD9\IBM\WebSphere\AppServer/deploytool/itp/plugins/com.ibm.etools.ejbdeploy/runtime" "-Dderby.system.home=C:\RAD9\IBM\WebSphere\AppServer/derby" "-Dcom.ibm.itp.location=C:\RAD9\IBM\WebSphere\AppServer/bin" "-Djava.util.logging.configureByServer=true" "-Duser.install.root=C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1" "-Djava.ext.dirs=C:\RAD9\IBM\WebSphere\AppServer/tivoli/tam;C:\RAD9\IBM\WebSphere\AppServer/java/jre/lib/ext" "-Djavax.management.builder.initial=com.ibm.ws.management.PlatformMBeanServerBuilder" "-Dpython.cachedir=C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1/temp/cachedir" "-Dwas.install.root=C:\RAD9\IBM\WebSphere\AppServer" "-Djava.util.logging.manager=com.ibm.ws.bootstrap.WsLogManager" "-Dserver.root=C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1" "-Dcom.ibm.security.jgss.debug=off" "-Dcom.ibm.security.krb5.Krb5Debug=off" "-Dcom.ibm.ws.management.event.pull_notification_timeout=120000" "-Dcom.ibm.ws.wspolicy.ignoreWSP12inPackagedWSDL=true" "-Djaxws.ignore.extraWSDLOps=true" "-Djava.library.path=C:\RAD9\IBM\WebSphere\AppServer/lib/native/win/x86_64/;C:\RAD9\IBM\WebSphere\AppServer\java\jre\bin\compressedrefs;C:\RAD9\IBM\WebSphere\AppServer\java\jre\bin;C:\WINDOWS\system32;C:\WINDOWS;C:\RAD9\IBM\WebSphere\AppServer\lib\native\win\x86_64;C:\RAD9\IBM\WebSphere\AppServer\bin;C:\RAD9\IBM\WebSphere\AppServer\java\bin;C:\RAD9\IBM\WebSphere\AppServer\java\jre\bin;C:\Program Files\Java\jdk1.8.0_201\bin;C:\Program Files (x86)\Common Files\Oracle\Java\javapath;C:\Oracle12c\app\client\pra_msanchezs\product\12.1.0\client_1\BIN;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\WINDOWS\System32\WindowsPowerShell\v1.0\;C:\WINDOWS\System32\OpenSSH\;C:\Windows\CCM;C:\Program Files\Microsoft SQL Server\120\Tools\Binn\;C:\Program Files\Microsoft SQL Server\Client SDK\ODBC\110\Tools\Binn\;C:\Program Files (x86)\Microsoft SQL Server\120\Tools\Binn\;C:\Program Files\Microsoft SQL Server\120\DTS\Binn\;C:\Program Files (x86)\Microsoft SQL Server\120\Tools\Binn\ManagementStudio\;C:\Program Files (x86)\Microsoft SQL Server\120\DTS\Binn\;C:\Oracle12c\app\client\pra_msanchezs\product\12.1.0\client_1\network\admin;D:\maven\apache-maven-3.9.5-bin\apache-maven-3.9.5\bin;C:\Program Files\nodejs\;C:\Program Files\Git\cmd;C:\Program Files\PostgreSQL\16\bin;C:\Program Files (x86)\Common Files\Oracle\Java\javapath;C:\Oracle12c\app\client\pra_msanchezs\product\12.1.0\client_1\BIN;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\WINDOWS\System32\WindowsPowerShell\v1.0\;C:\WINDOWS\System32\OpenSSH\;C:\Windows\CCM;C:\Program Files\Microsoft SQL Server\120\Tools\Binn\;C:\Program Files\Microsoft SQL Server\Client SDK\ODBC\110\Tools\Binn\;C:\Program Files (x86)\Microsoft SQL Server\120\Tools\Binn\;C:\Program Files\Microsoft SQL Server\120\DTS\Binn\;C:\Program Files (x86)\Microsoft SQL Server\120\Tools\Binn\ManagementStudio\;C:\Program Files (x86)\Microsoft SQL Server\120\DTS\Binn\;C:\Oracle12c\app\client\pra_msanchezs\product\12.1.0\client_1\network\admin;D:\maven\apache-maven-3.9.5-bin\apache-maven-3.9.5\bin;C:\Program Files\nodejs\;C:\Program Files\Git\cmd;C:\Users\pra_msanchezs\AppData\Local\Microsoft\WindowsApps;C:\Users\pra_msanchezs\AppData\Local\Programs\Git\cmd;C:\Users\pra_msanchezs\AppData\Local\Microsoft\WindowsApps;C:\Users\pra_msanchezs\AppData\Local\Programs\Git\cmd;C:\Users\pra_msanchezs\AppData\Roaming\npm;C:\Users\pra_msanchezs\AppData\Local\Programs\Microsoft VS Code;C:\Users\pra_msanchezs\AppData\Local\JetBrains\Toolbox\scripts;C:\IBM\WebSphere\AppServer\bin;.;" "-Djava.endorsed.dirs=C:\RAD9\IBM\WebSphere\AppServer/endorsed_apis;C:\RAD9\IBM\WebSphere\AppServer/java/jre/lib/endorsed;C:\RAD9\IBM\WebSphere\AppServer\endorsed_apis;C:\RAD9\IBM\WebSphere\AppServer\java\jre\lib\endorsed" "-Djava.security.auth.login.config=C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1/properties/wsjaas.conf" "-Djava.security.policy=C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1/properties/server.policy" "com.ibm.wsspi.bootstrap.WSPreLauncher" "-nosplash" "-application" "com.ibm.ws.bootstrap.WSLauncher" "com.ibm.ws.runtime.WsServer" "C:\RAD9\IBM\WebSphere\AppServer\profiles\AppSrv1\config" "592448L2561Node01Cell" "592448L2561Node01" "server1"

@endlocal