*Diretório dedicado para as ferramentas auxiliares na manipulação de dados*

Ambos os gerenciados trabalham com os dados brutos, mas eu preferi separar em dois quando comecei a mexer com imagens para organizar melhor as ideias. Publicamente são duas blibiotecas, uma dos dados e uma para as imagens, especificamente para os dados (como criei muitas funções e o arquivo já tava ficando enorme) decidi separar mais ainda as ideias em conjuntos menores pra facilitar a legibilidade e manutenção do código.

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
   <li>Verificar se a lista de dados é simétrica, ou seja, se a quantidade de colunas é a mesma para todas as linhas.</li>
   <li>Adicionar uma nova coluna no conjuntos de dados, tanto no final, quanto por um índice específicado.</li>
   <li>Remover uma linha do conjunto de dados de acordo com um índice especificado.</li>
   <li>Remover uma coluna do conjunto de dados de acordo com um índice especificado, todas as linhas sofrão a mesma alteração.</li>
   <li>Editar um valor dentro do conjunto de dados, tanto especificando apenas a coluna e alterando todas as linhas, tanto especificando a linha e coluna desejada.</li>
   <li>Trocas duas colunas de lugar entre si.</li>
   <li>Remover valores que não sejam numéricos, tanto int, como float e double.</li>
   <li>Gerar colunas categóricas de acorod com os dados da coluna especificada.</li>
   <li>Ler e exportar arquivos no formato csv.</li>
</ul>

Adicionalmente, possui algumas ferramentas para lidar com dados para o treino da rede neural que eu modelei:
<ul>
   <li>Embaralhar conjunto de dados.</li>
   <li>Separar dados específicos para a entrada da rede neural.</li>
   <li>Separar dados específicos para a saída da rede, para usar durante o treino.</li>
   <li>Separar dados para treino e teste, evitando overfitting.</li>
   <li>Converter a lista de dados para int, float ou double.</li>
</ul>

# Gerenciador de imagem (Geim)

<p>
   O Geim possui um conjunto menor de ferramentas porque não tive necessidade até o momento de criar mais coisa pra ele.
</p>
<p>
   Diferente do Ged, resolvi começar já organizando melhor a estrutura de dados do Geim, ele trabalha com pixels, que é a menor unidade de dados que a imagem pode ter, por enquanto contendo apenas os valores de cor rgb. O Geim trabalha com uma estrutura de matrizes de pixels e tem suas funcionalidades baseadas nela.
</p>

Funcionalidades do Geim:
<ul>
   <li>Ler uma imagem e transformar num objeto BufferedImage (só testei com png).</li>
   <li>Gerar uma estrutura de imagem, a estrutura é baseada numa matriz de pixels contendo as informações de cor da imagem. Os valores de cor dos pixel serão automaticamente copiados para a nova estrutura</li>
   <li>Gerar uma estrutura de imagem vazia, sem os valores de cor RGB, de acordo com as dimensões fornecidas.</li>
   <li>Editar o valor da cor de um pixel numa esrtutura de imagem.</li>
   <li>Exibir, pelo terminal, o valor contido na estrutura de imagem.</li>
   <li>Exportar a estrutura de imagem para um arquivo png.</li>
   <li>Transformar a imagem carregada tanto em escala de cinza, quanto em rgb.</li>
   <li>Exportar as imagens, tanto em escala de cinza quanto em rgb, usando a rede neural que eu modelei de acordo com um valor de escala.</li>
</ul>

Importante destacar que quando os dados da imagem são convertido para treino da rede, eles são normalizados numa escala de 0-1, tantos as posições x e y, 
quanto o valor das cores (tomando como máximo e mínimo do padrão rgb que vai de 0-255).