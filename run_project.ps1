# Script para executar o projeto e capturar logs

Write-Host "Executando o projeto Diario de Classe..."

# Parar processos Java existentes
taskkill /F /IM java.exe 2>$null

# Executar o projeto
C:\maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run 2>&1 | Tee-Object -FilePath "project_log.txt"

Write-Host "Logs salvos em project_log.txt" 