# Script para baixar e configurar o Maven

Write-Host "Baixando Maven..."

# Criar diretório para Maven
$mavenDir = "C:\maven"
if (!(Test-Path $mavenDir)) {
    New-Item -ItemType Directory -Path $mavenDir
}

# URL do Maven
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
$mavenZip = "$mavenDir\maven.zip"

# Baixar Maven
Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip

# Extrair Maven
Expand-Archive -Path $mavenZip -DestinationPath $mavenDir -Force

# Configurar variáveis de ambiente
$mavenHome = "$mavenDir\apache-maven-3.9.6"
$mavenBin = "$mavenHome\bin"

# Adicionar ao PATH temporariamente
$env:PATH += ";$mavenBin"

Write-Host "Maven configurado em: $mavenHome"
Write-Host "Maven adicionado ao PATH temporariamente"

# Testar Maven
try {
    & "$mavenBin\mvn.cmd" --version
    Write-Host "Maven instalado com sucesso!"
} catch {
    Write-Host "Erro ao testar Maven: $_"
} 