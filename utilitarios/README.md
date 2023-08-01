*Diretório dedicado para as ferramentas auxiliares na manipulação de dados*

# Gerenciador de Dados (Ged)

<p>
   Ele é responsável pelo manuseio do conjunto de dados, no momento a estrutura de dados que eu uso pra trabalhar é
   só uma array list com vetores de strings que guardam os valores lidos pelo csv, foi como eu iniciei toda a estrutura 
   e ta sendo em volta dessa estrutura que to criando esse eco sistema. 
</p>
<p>
   Por enquanto to adicionando novas funcionalidades de acordo com a minha necessidade de manuseio dos dados, mas atualmente 
   ele já possui uma boa diversidade de métodos que devem satisfazer bem em uso mais simples
</p>

Segue a lista das atuais funcionalidades presentes no Ged:
<ul>
   <li>Imprimir, via console, a lista de dados no formato csv.</li>
   <li>Verificar se a lista de dados é simétrica, ou seja, se a quantidade de de colunas é a mesma para todas as linhas.</li>
   <li>Adicionar uma nova coluna no conjuntos de dados, tanto no final, quanto por um índice específicado.</li>
   <li>Remover uma linha do conjunto de dados de acordo com um índice especificado.</li>
   <li>Remover uma coluna do conjunto de dados de acordo com um índice especificado, todas as linhas sofrão a mesma alteração.</li>
   <li>Editar um valor dentro do conjunto de dados, tanto especificando apenas a coluna e alterando todas as linhas, tanto especificando a linha e coluna desejada.</li>
   <li>Remover valores que não sejam numéricos, tanto int, como float e double.</li>
   <li>Gerar colunas categóricas de acorod com os dados da coluna especificada.</li>
   <li>Ler e exportar arquivos no formato csv.</li>
</ul>
Adicionalmente possui algumas ferramentas para lidar com dados para o treino da rede neural que eu modelei:
<ul>
   <li>Embaralhar conjunto de dados.</li>
   <li>Separar dados específicos para a entrada da rede neural.</li>
   <li>Separar dados específicos para a saída da rede, para usar durante o treino.</li>
   <li>Separar dados para treino e teste, evitando overfitting.</li>
   <li>Converter a lista de dados para int, float ou double.</li>
</ul>

# Gerenciador de imagem (Geim)