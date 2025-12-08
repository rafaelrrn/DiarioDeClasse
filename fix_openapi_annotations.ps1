# Script para corrigir as anotações do OpenAPI
# Remove parâmetros inválidos das anotações @Operation

$files = Get-ChildItem -Path "src" -Filter "*.java" -Recurse

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    
    # Corrigir anotações @Operation removendo parâmetros inválidos
    $updated = $content -replace '@Operation\(value = "([^"]+)", produces = MediaType\.APPLICATION_JSON_VALUE\)', '@Operation(summary = "$1")'
    $updated = $updated -replace '@Operation\(value = "([^"]+)"\)', '@Operation(summary = "$1")'
    
    if ($content -ne $updated) {
        Set-Content $file.FullName $updated
        Write-Host "Fixed: $($file.FullName)"
    }
}

Write-Host "Anotacoes do OpenAPI corrigidas!" 