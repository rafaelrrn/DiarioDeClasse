# Script para atualizar anotações do Swagger para OpenAPI 3
# Substitui @ApiOperation por @Operation e corrige imports

$files = Get-ChildItem -Path "src" -Filter "*.java" -Recurse

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    $updated = $content -replace '@ApiOperation\(', '@Operation('
    $updated = $updated -replace '@Api\(', '@Tag('
    $updated = $updated -replace '@ApiParam\(', '@Parameter('
    $updated = $updated -replace '@ApiModel\(', '@Schema('
    $updated = $updated -replace '@ApiModelProperty\(', '@Schema('
    
    # Corrigir imports
    $updated = $updated -replace 'import io\.swagger\.annotations\.ApiOperation;', 'import io.swagger.v3.oas.annotations.Operation;'
    $updated = $updated -replace 'import io\.swagger\.annotations\.Api;', 'import io.swagger.v3.oas.annotations.tags.Tag;'
    $updated = $updated -replace 'import io\.swagger\.annotations\.ApiParam;', 'import io.swagger.v3.oas.annotations.Parameter;'
    $updated = $updated -replace 'import io\.swagger\.annotations\.ApiModel;', 'import io.swagger.v3.oas.annotations.media.Schema;'
    $updated = $updated -replace 'import io\.swagger\.annotations\.ApiModelProperty;', 'import io.swagger.v3.oas.annotations.media.Schema;'
    
    if ($content -ne $updated) {
        Set-Content $file.FullName $updated
        Write-Host "Updated: $($file.FullName)"
    }
}

Write-Host "Anotações do Swagger atualizadas para OpenAPI 3!" 