Write-Host -f Cyan "`n`n  Auto Update MVTTT Web Mobile`n`n"

$enableWebRequests = {
    function Set-UseUnsafeHeaderParsing() {
        param(
            [Parameter(Mandatory, ParameterSetName = "Enable")]
            [switch]$Enable,
            [Parameter(Mandatory, ParameterSetName = "Disable")]
            [switch]$Disable
        )
        $shouldEnable = $PSCmdlet.ParameterSetName -eq "Enable"
        $netAssembly = [Reflection.Assembly]::GetAssembly([System.Net.Configuration.SettingsSection])
        if ($netAssembly) {
            $bindingFlags = [Reflection.BindingFlags]"Static,GetProperty,NonPublic"
            $settingsType = $netAssembly.GetType("System.Net.Configuration.SettingsSectionInternal")
            $instance = $settingsType.InvokeMember("Section", $bindingFlags, $null, $null, @())   
            if ($instance) {
                $bindingFlags = "NonPublic", "Instance"
                $useUnsafeHeaderParsingField = $settingsType.GetField("useUnsafeHeaderParsing", $bindingFlags)
                if ($useUnsafeHeaderParsingField) {
                    $useUnsafeHeaderParsingField.SetValue($instance, $shouldEnable)
                }
            }
        }
    }
    Set-UseUnsafeHeaderParsing -Enable
  
    function Ignore-SSLCertificates {
        $Provider = New-Object Microsoft.CSharp.CSharpCodeProvider
        $Compiler = $Provider.CreateCompiler()
        $Params = New-Object System.CodeDom.Compiler.CompilerParameters
        $Params.GenerateExecutable = $false
        $Params.GenerateInMemory = $true
        $Params.IncludeDebugInformation = $false
        $Params.ReferencedAssemblies.Add("System.DLL") > $null
        $TASource = @"
            namespace Local.ToolkitExtensions.Net.CertificatePolicy
            {
                public class TrustAll : System.Net.ICertificatePolicy
                {
                    public bool CheckValidationResult(System.Net.ServicePoint sp,System.Security.Cryptography.X509Certificates.X509Certificate cert, System.Net.WebRequest req, int problem)
                    {
                        return true;
                    }
                }
            }
"@ 
        $TAResults = $Provider.CompileAssemblyFromSource($Params, $TASource)
        $TAAssembly = $TAResults.CompiledAssembly
        ## We create an instance of TrustAll and attach it to the ServicePointManager
        $TrustAll = $TAAssembly.CreateInstance("Local.ToolkitExtensions.Net.CertificatePolicy.TrustAll")
        [System.Net.ServicePointManager]::CertificatePolicy = $TrustAll
    }
    Ignore-SSLCertificates
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
}
  ([ScriptBlock]::Create($enableWebRequests)).Invoke()

$k = cat C:\Users\Administrator\Desktop\WebOrchestrator\z\k_cachePurge
$headers = @{}
$headers.Add("Content-Type","application/json")
$headers.Add("Authorization","Bearer $k")
$comp = (Get-ChildItem C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src).LastWriteTime
while (1) {
    $newComp = (Get-ChildItem C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src).LastWriteTime
    if ($null -ne (Compare-Object $comp $newComp)) {
        Write-Host -f DarkYellow "$((Get-Date).ToString("MM/dd/yyyy hh:mm:ss tt")):: Updating MVTT Web Mobile"
        $comp = (Get-ChildItem C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src).LastWriteTime
        Copy-Item C:\Users\Administrator\Desktop\MVTTT\UI\web_mobile\src\* "C:\Software\Tomcat 10.1\MVTT_WD\web\web_mobile\" -Force
        $null = Invoke-WebRequest "https://api.cloudflare.com/client/v4/zones/fbd22b95c59755aae3dca04d66f97a2d/purge_cache" -Method Post -Headers $headers -Body @"
    {
        "files":["https://mvttt.uspwin.com/","https://mvttt.uspwin.com/webMobile"]
    }
"@
    }
    Start-Sleep 1
}