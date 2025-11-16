param(
    [string]$Root = ".",
    [switch]$DryRun
)

# Extensiones a procesar (texto)
$exts = @(
    "*.java","*.fxml","*.css","*.xml","*.properties","*.html","*.htm",
    "*.js","*.ts","*.yml","*.yaml","*.sql","*.txt","*.md","*.gradle",
    "*.bat","*.cmd","pom.xml"
)

Write-Output "Buscando archivos en: $Root"
Get-ChildItem -Path $Root -Recurse -Include $exts -File | ForEach-Object {
    $path = $_.FullName
    try {
        $text = Get-Content -Raw -Encoding UTF8 $path
    } catch {
        Write-Output "No se pudo leer (skip): $path"
        return
    }
    $orig = $text

    # Eliminar comentarios de bloque /* ... */ (multilínea)
    $text = [regex]::Replace($text, '(?s)/\*.*?\*/', '')

    # Eliminar comentarios de línea //... pero evitar cortar URLs (http://, https://)
    # Usamos lookbehind para no eliminar // precedidos por ':' (por ejemplo http://)
    $text = [regex]::Replace($text, '(?m)(?<!:)//.*?$', '')

    # Eliminar comentarios XML/HTML <!-- ... -->
    $text = [regex]::Replace($text, '(?s)<!--.*?-->', '')

    # Eliminar líneas de comentario en .properties que comienzan con # o !
    $text = [regex]::Replace($text, '(?m)^[ \t]*[#\!].*?(\r?\n|$)', '')

    if ($text -ne $orig) {
        if ($DryRun) {
            Write-Output "MODIFIED: $path"
        } else {
            $bak = $path + '.bak'
            Copy-Item -Path $path -Destination $bak -Force
            Set-Content -Path $path -Value $text -Encoding UTF8
            Write-Output "Cleaned: $path (backup: $bak)"
        }
    }
}

Write-Output "Proceso finalizado. Revisa los archivos .bak si necesitas restaurar."
