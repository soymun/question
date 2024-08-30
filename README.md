.\setup.exe /configure .\configuration-Office365-
x64.xml

reg add
"HKCU|Software\Microsoft\Office\16.0\Common\Exper
imentConfigs\Ecs" /v "CountryCode" /t REG_SZ /d
"std::wstring|US" /f
