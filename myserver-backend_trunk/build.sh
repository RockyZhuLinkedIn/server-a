gradle clean && rm -rf build/ && rm -rf myserver/*/build/ myserver/rest-api/src/main/idl/ myserver/rest-api/src/main/snapshot/
gradle build
gradle myserver:config-impl:build  ## client bindings has to be generated in a seperate gradle task

rm -f *.ipr *.iml *.iws
gradle idea
