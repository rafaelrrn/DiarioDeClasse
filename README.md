# DiarioDeClasse
Reprodução de um diário de classe

# Configuração de Ambiente

 - Win: 
   - setx DIARIO_DE_CLASSE_ACTIVE_PROFILE "local" /M 
 - Linux:
      - echo 'export DIARIO_DE_CLASSE_ACTIVE_PROFILE="local"' >> ~/.bashrc
      - source ~/.bashrc
    
### Vars:

 DIARIO_DE_CLASSE_ACTIVE_PROFILE "<local | prod> <br>
 DIARIO_DE_CLASSE_DB_HOST_URL <jdbc:mysql://localhost:3306/> <br>
 DIARIO_DE_CLASSE_DB_NAME <db-diario-de-classe> <br>
 DIARIO_DE_CLASSE_DB_EXTRA_PARAMS <?zeroDateTimeBehavior=convertToNull> <br>
 DIARIO_DE_CLASSE_DB_USER <root> rafa <br>
 DIARIO_DE_CLASSE_DB_PASSWORD <pwd> rafa <br>
 
### DB:

local -> application-local.properties <br>
prod  -> env. var <br>