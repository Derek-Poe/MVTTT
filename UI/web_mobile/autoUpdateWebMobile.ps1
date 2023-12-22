Write-Host -f Cyan "`n`n  Auto Update MVTTT Web Mobile`n`n"

$comp = (Get-ChildItem C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src).LastWriteTime
while(1){
    $newComp = (Get-ChildItem C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src).LastWriteTime
    if($null -ne (Compare-Object $comp $newComp)){
        Write-Host -f DarkYellow "$((Get-Date).ToString("MM/dd/yyyy hh:mm:ss tt")):: Updating MVTT Web Mobile"
        $comp = (Get-ChildItem C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src).LastWriteTime
        Copy-Item C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src\* "C:\Software\Tomcat 10.1\MVTT_WD\web\web_mobile\" -Force
    }
    Start-Sleep 1
}