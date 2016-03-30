# start platform

start "config-server"   /D .\config-server       gradle bootRun
timeout 20
start "eureka-registry" /D .\webservice-registry gradle bootRun
timeout 20
start "cargo"           /D .\cargo               gradle bootRun
start "customer"        /D .\customer            gradle bootRun
start "financial"       /D .\financial           gradle bootRun

timeout 25
start "api-gateway"     /D .\api-gateway         gradle bootRun
