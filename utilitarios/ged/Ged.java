package utilitarios.ged;

import java.util.ArrayList;

/**
 * <p>
 *    Gerenciador de Dados.
 * </p>
 * Responsável pelo manuseio do conjunto de dados, 
 * funcionalidades disponíveis:
 * <ul>
 *    <li>Funções de leitura de arquivos.</li>
 *    <li>Funções de conversão de dados.</li>
 *    <li>Funções de manipulação de dados.</li>
 *    <li>Funções de gerenciamento de treino e teste para rede neural..</li>
 * </ul>
*/
public class Ged{

   GerenciadorArquivos ga;//leitor de arquivos
   ConversorDados cd;//conversor de dados
   ManipuladorDados md;
   TreinoTeste gtt;//gerenciador de treino e teste da rede
   ImpressaoMatriz im;//exibição
   OperadorMatriz om;
   

   public Ged(){
      ga = new GerenciadorArquivos();
      cd = new ConversorDados();
      md = new ManipuladorDados();
      gtt = new TreinoTeste();
      im = new ImpressaoMatriz();
      om = new OperadorMatriz();
   }


   /**
    * Exibe pelo console as informações contidas na lista em formato csv.
    * @param lista lista com os dados.
    */
   public void imprimirCsv(ArrayList<String[]> lista){
      String separador = ",";
      String espacamemto = "   ";

      System.out.println("Csv = [");
      for(String linha[] : lista){
         for(int i = 0; i < linha.length; i++){
            if(i == 0) System.out.print(espacamemto + linha[i]);
            else System.out.print(separador + " " + linha[i]);
         }
         System.out.println();
      }
      System.out.println("]\n");
   }


   /**
    * Exibe as informações contidas na matriz fornecida.
    * @param matriz matriz com os dados
    */
   public void imprimirMatriz(int[][] matriz){
      im.imprimirMatriz(matriz);
   }


   /**
    * Exibe as informações contidas na matriz fornecida.
    * @param matriz matriz com os dados
    */
   public void imprimirMatriz(float[][] matriz){
      im.imprimirMatriz(matriz);
   }


   /**
    * Exibe as informações contidas na matriz fornecida.
    * @param matriz matriz com os dados
    */
   public void imprimirMatriz(double[][] matriz){
      im.imprimirMatriz(matriz);
   }


   /**
    * Exibe as informações contidas na matriz fornecida.
    * @param matriz matriz com os dados
    * @param nome nome personalizado da matriz para a impressão
    */
   public void imprimirMatriz(int[][] matriz, String nome){
      im.imprimirMatriz(matriz, nome);
   }


   /**
    * Exibe as informações contidas na matriz fornecida.
    * @param matriz matriz com os dados
    * @param nome nome personalizado da matriz para a impressão
    */
   public void imprimirMatriz(float[][] matriz, String nome){
      im.imprimirMatriz(matriz, nome);
   }


   /**
    * Exibe as informações contidas na matriz fornecida.
    * @param matriz matriz com os dados
    * @param nome nome personalizado da matriz para a impressão
    */
   public void imprimirMatriz(double[][] matriz, String nome){
      im.imprimirMatriz(matriz, nome);
   }


   /**
    * Verifica se os a quantidade de dados na lista é simetrico. A simetria leva em conta 
    * se todas as colunas contém o mesmo tamanho.
    * <p>
    *    A simetria leva em conta se a lista possui elementos, caso o tamanho seja zero será
    *    considerada como não simétrica.
    * <p>
    * @param lista lista com os dados.
    * @return true caso os dados sejam simétricos, false caso contrário.
    * @throws IllegalArgumentException se a lista for nula.
    */
   public boolean dadosSimetricos(ArrayList<String[]> lista){
      return md.dadosSimetricos(lista);
   }


   /**
    * Adiciona uma coluna ao final de todas as linhas da lista, 
    * a nova coluna estará em branco.
    * @param lista lista de dados.
    */
   public void adicionarColuna(ArrayList<String[]> lista){
      md.adicionarColuna(lista);
   }


   /**
    * Adiciona uma coluna no índice fornecido, a nova coluna estará em branco. Todos
    * os itens depois do índice serão deslocados para a direita.
    * @param lista lista de dados.
    * @param indice índice onde a nova coluna será adicionada.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public void adicionarColuna(ArrayList<String[]> lista, int indice){
      md.adicionarColuna(lista, indice);
   }


   /**
    * Remove uma linha inteira do conjunto de dados
    * @param lista lista com os dados.
    * @param indice índice da coluna que será removida.
    * @throws IllegalArgumentException se o indice lista for nula.
    * @throws IllegalArgumentException se o indice estiver fora de alcance da lista.
    */
   public void removerLinha(ArrayList<String[]> lista, int indice){
      md.removerLinha(lista, indice);
   }


   /**
    * Remove todas as colunas dos dados de acordo com o índice fornecido.
    * @param lista lista com os dados.
    * @param indice índice da coluna que será removida.
    * @return nova lista com a coluna removida.
    * @throws IllegalArgumentException se a lista for nula.
    * @throws IllegalArgumentException se o indice estiver fora de alcance da lista.
    */
   public void removerColuna(ArrayList<String[]> lista, int indice){
      md.removerColuna(lista, indice);
   }


   /**
    * Substitui o valor de busca pelo novo valor fornecido, de acordo com a linha e coluna especificadas.
    * @param lista lista com os dados lidos.
    * @param idLinha índice da linha alvo para a alteração dos dados.
    * @param idColuna índice da coluna alvo para a alteração dos dados.
    * @param busca valor que será procurado para ser substituído.
    * @param novoValor novo valor que será colocado.
    * @throws IllegalArgumentException se a lista estiver nula.
    * @throws IllegalArgumentException se a lista não for simétrica.
    * @throws IllegalArgumentException se o valor do índice da linha fornecido estiver fora de alcance.
    * @throws IllegalArgumentException se o valor do índice da coluna fornecido estiver fora de alcance.
    * @throws IllegalArgumentException se o valor de busca for nulo.
    * @throws IllegalArgumentException se o novo valor de substituição for nulo;
    */
   public void editarValor(ArrayList<String[]> lista, int idLinha, int idColuna, String busca, String novoValor){
      md.editarValor(lista, idLinha, idColuna, busca, novoValor);
   }


   /**
    * Substitui todas as linhas dos dados pelo valor fornecido, caso na coluna fornecida tenha o valor buscado.
    * @param lista lista com os dados lidos.
    * @param idColuna índice da coluna alvo para a alteração dos dados.
    * @param busca valor que será procurado para ser substituído.
    * @param novoValor novo valor que será colocado.
    * @throws IllegalArgumentException se a lista estiver nula.
    * @throws IllegalArgumentException se a lista não for simétrica.
    * @throws IllegalArgumentException se o valor do índice fornecido estiver fora de alcance.
    * @throws IllegalArgumentException se o valor de busca for nulo.
    * @throws IllegalArgumentException se o novo valor de substituição for nulo;
    */
   public void editarValor(ArrayList<String[]> lista, int idColuna, String busca, String novoValor){
      md.editarValor(lista, idColuna, busca, novoValor);
   }


   /**
    * Troca os valores das colunas na lista de dados de acordo os com índicer fornecidos.
    * @param lista A lista de dados.
    * @param idColuna1 índice da primeira coluna que será trocada.
    * @param idColuna2 índice da segunda coluna que será trocada.
    * @throws IllegalArgumentException se a lista estiver nula.
    * @throws IllegalArgumentException se a lista não for simétrica.
    * @throws IllegalArgumentException se a lista não tiver pelo menos duas colunas.
    * @throws IllegalArgumentException se os índices fornecidos estiverem fora de alcance do tamanho das colunas.
    * @throws IllegalArgumentException se as colunas fornecidas forem iguais.
    */
   public void trocarColunas(ArrayList<String[]> lista, int idColuna1, int idColuna2){
      md.trocarColunas(lista, idColuna1, idColuna2);
   }


   /**
    * Remove a linha inteira dos dados caso exista algum valor nas colunas que não consiga ser convertido para
    * um valor numérico.
    * <p>
    *    É importante verificar e ter certeza se os dados não possuem nenhuma coluna com caracteres, caso isso seja verdade
    *    o método irá remover todas as colunas como consequência e a lista ficará vazia.
    * </p>
    * @param lista lista com os dados;
    */
   public void removerNaoNumericos(ArrayList<String[]> lista){
      md.removerNaoNumericos(lista);
   }


   /**
    * Categoriza a lista de dados na coluna relativa ao índice fornecido,
    * usando a técnica de One-Hot Encoding. As novas colunas adicionadas
    * representarão as categorias únicas encontradas na coluna especificada, e
    * os valores das novas colunas serão definidos como "1" quando a categoria
    * correspondente estiver presente na linha e "0" caso contrário.
    *
    * @param lista lista contendo os dados
    * @param indice O índice da coluna na qual a categorização será aplicada.
    * @throws IllegalArgumentException se a lista não for simétrica.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public void categorizar(ArrayList<String[]> lista, int indice){
      md.categorizar(lista, indice);
   }


   /**
    * Retorna uma matriz menor contendo as mesmas informações da matriz original, apenas
    * com os dados do ponto de inínio e fim.
    * @param dados matriz contendo os dados completos.
    * @param inicio índice inicial do corte (inclusivo).
    * @param fim índice final do corte (exclusivo).
    * @return submatriz contendo o conteúdo da matriz original, com os dados selecionados.
    * @throws IllegalArgumentException se os índices fornecidos forem inválidos.
    */
   public double[][] obterSubMatriz(double[][] dados, int inicio, int fim){
      return md.obterSubMatriz(dados, inicio, fim);
   }



   /**
    * Descreve as dimensões da lista, tanto em questão de quantidade de linhas qunanto quantidade de colunas.
    * @param lista lista com os dados.
    * @return array contendo as informações das dimensões da lista, o primeiro elemento corresponde a quantidade de 
    * linhas e o segundo elemento corresponde a quantidade de colunas.
    * @throws IllegalArgumentException se a lista estiver nula.
    * @throws IllegalArgumentException se a lista estiver vazia.
    * @throws IllegalArgumentException se os dados não forem simétricos, tendo colunas com tamanhos diferentes.
    */
   public int[] obterShapeLista(ArrayList<String[]> lista){
      if(lista == null) throw new IllegalArgumentException("A lista fornecida é nula.");
      if(lista.size() == 0) throw new IllegalArgumentException("A lista fornecida está vazia.");
      if(!dadosSimetricos(lista)) throw new IllegalArgumentException("As dimensões dos dados não são simétricas.");

      int[] shape = new int[2];
      shape[0] = lista.size();
      shape[1] = lista.get(0).length;
      return shape;
   }


   //GERENCIADOR DE ARQUIVOS ---------------------

   /**
    * Lê o arquivo .csv de acordo com o caminho especificado.
    * Espaços contidos serão removidos.
    *
    * <p>
    *    O formato da estrutura de dados será um objeto do tipo 
    *    {@code ArrayList<String[]>}, contendo as linhas e colunas das informações lidas.
    * </p>
    * @param caminho caminho relativo do arquivo.
    * @return objeto contendo as informações do arquivo lido.
    * @throws IllegalArgumentException caso não encontre o diretório fornecido.
    */
   public ArrayList<String[]> lerCsv(String caminho){
      return ga.lerCsv(caminho);
   }


   /**
    * Grava os dados no formato {@code ArrayList<String[]>} em um arquivo CSV.
    * @param lista lista de arrays contendo os dados.
    * @param filePath caminho do arquivo onde os dados serão gravados, excluindo a extensão .csv.
    */
   public void exportarCsv(ArrayList<String[]> lista, String caminho){
      ga.exportarCsv(lista, caminho);;
   }


   /**
    * Grava os dados no formato {@code double[][]} em um arquivo CSV.
    * @param lista lista de arrays contendo os dados.
    * @param filePath caminho do arquivo onde os dados serão gravados, excluindo a extensão .csv.
    */
   public void exportarCsv(double[][] dados, String caminho){
      ga.exportarCsv(dados, caminho);
   }


   // GERENCIADOR TREINO TESTE ---------------------

   /**
    * Embaralha o conjunto de dados aleatoriamente.
    * <p>
    *    A alteração irá afetar o conteúdo dos dados recebidos.
    *    Caso queira manter os dados originais, é recomendado fazer uma cópia previamente.
    * </p>
    * @param dados conjunto de dados completo.
    */
   public void embaralharDados(double[][] dados){
      gtt.embaralharDados(dados);
   }


   /**
    * <p>
    *    Método para treino da rede neural.
    * </p>
    *
    * Separa os dados que serão usados como entrada de acordo com os valores fornecidos.
    * @param dados conjunto de dados completo.
    * @param colunas quantidade de colunas que serão preservadas, começando pela primeira até o valor fornecido.
    * @return nova matriz de dados apenas com as colunas desejadas.
    * @throws IllegalArgumentException Se o número de colunas for maior que o número de colunas disponíveis nos dados.
    * @throws IllegalArgumentException Se o número de colunas for menor que um.
    */
   public double[][] separarDadosEntrada(double[][] dados, int colunas){
      return gtt.separarDadosEntrada(dados, colunas);
   }


   /**
    * <p>
    *    Método para treino da rede neural.
    * </p>
    *
    * Extrai os dados de saída do conjunto de dados e devolve um novo conjunto de dados contendo apenas as 
    * colunas de dados de saída especificadas.
    * @param dados O conjunto de dados com as informações completas.
    * @param colunas O número de colunas de dados de saída que serão extraídas.
    * @return novo conjunto de dados com apenas as colunas de dados de saída.
    * @throws IllegalArgumentException Se o número de colunas for maior que o número de colunas disponíveis nos dados.
    * @throws IllegalArgumentException Se o número de colunas for menor que um.
    */
   public double[][] separarDadosSaida(double[][] dados, int colunas){
      return gtt.separarDadosSaida(dados, colunas);
   }


   /**
    * Separa o conjunto de dados em dados de treino e dados de teste, de acordo com o tamanho do teste fornecido.
    * 
    * <p>
    *    A função recebe um conjunto de dados completo e separa ele em duas matrizes, uma para treino e outra para teste.
    *    A quantidade de dados para o conjunto de teste é determinada pelo parâmetro tamanhoTeste.
    * </p>
    * 
    * <p>
    *    Exemplo de uso:
    * </p>
    * <pre>{@code 
    * double[][][] treinoTeste = separarTreinoTeste(dados, 0.25f);
    * double[][] treino = treinoTeste[0];
    * double[][] teste = treinoTeste[1];}
    * </pre>
    * @param dados O conjunto de dados completo.
    * @param tamanhoTeste O tamanho relativo do conjunto de teste (entre 0 e 1).
    * @return Um array de duas matrizes contendo os dados de treino e teste, respectivamente.
    * @throws IllegalArgumentException caso o conjunto de dados for nulo.
    * @throws IllegalArgumentException caso o tamanho de teste estiver fora do intervalo (0, 1).
    */
   public double[][][] separarTreinoTeste(double[][] dados, float tamanhoTeste){
      return gtt.separarTreinoTeste(dados, tamanhoTeste);
   }


   //CONVERSOR DE DADOS ----------------------

   /**
    * Converte a lista no formato {@code ArrayList<String[]>} para uma matriz bidimensional 
    * com os valores numéricos.
    * @param lista lista com os dados.
    * @return matriz convertida para valores tipo int.
    */
   public int[][] listaParaDadosInt(ArrayList<String[]> lista){
      return cd.listaParaDadosInt(lista);
   }


   /**
    * Converte a lista no formato {@code ArrayList<String[]>} para uma matriz bidimensional 
    * com os valores numéricos.
    * @param lista lista com os dados 
    * @return matriz convertida para valores tipo float.
    */
   public float[][] listaParaDadosFloat(ArrayList<String[]> lista){
      return cd.listaParaDadosFloat(lista);
   }


   /**
    * Converte a lista no formato {@code ArrayList<String[]>} para uma matriz bidimensional 
    * com os valores numéricos.
    * @param lista lista com os dados 
    * @return matriz convertida para valores tipo double.
    */
   public double[][] listaParaDadosDouble(ArrayList<String[]> lista){
      return cd.listaParaDadosDouble(lista);
   }


   //OPERADOR MATRIZ --------------

   /**
    * Preenche o conteúdo da matriz de acordo com o valor fornecido.
    * @param matriz matriz com os dados.
    * @param valor valor de preenchimento.
    */
   public void preencherMatriz(int[][] matriz, int valor){
      om.preencherMatriz(matriz, valor);
   }


   /**
    * Preenche o conteúdo da matriz de acordo com o valor fornecido.
    * @param matriz matriz com os dados.
    * @param valor valor de preenchimento.
    */
   public void preencherMatriz(float[][] matriz, float valor){
      om.preencherMatriz(matriz, valor);
   }


   /**
    * Preenche o conteúdo da matriz de acordo com o valor fornecido.
    * @param matriz matriz com os dados.
    * @param valor valor de preenchimento.
    */
   public void preencherMatriz(double[][] matriz, double valor){
      om.preencherMatriz(matriz, valor);
   }


   /**
    * Preenche o conteúdo da matriz para que fique no formato identidade, onde
    * apenas os elementos da diagonal esquerda para direita tem valores iguais a 1.
    * <p>Exemplo:<pre>
    * m = [
    *  1, 0, 0
    *  0, 1, 0
    *  0, 0, 1
    * ]
    * </pre></p>
    * @param matriz
    */
   public void matrizIdentidade(int[][] matriz){
      om.matrizIdentidade(matriz);
   }


   /**
    * Preenche o conteúdo da matriz para que fique no formato identidade, onde
    * apenas os elementos da diagonal esquerda para direita tem valores iguais a 1.
    * <p>Exemplo:<pre>
    * m = [
    *  1, 0, 0
    *  0, 1, 0
    *  0, 0, 1
    * ]
    * </pre></p>
    * @param matriz
    */
   public void matrizIdentidade(float[][] matriz){
      om.matrizIdentidade(matriz);
   }


   /**
    * Preenche o conteúdo da matriz para que fique no formato identidade, onde
    * apenas os elementos da diagonal esquerda para direita tem valores iguais a 1.
    * <p>Exemplo:<pre>
    * m = [
    *  1, 0, 0
    *  0, 1, 0
    *  0, 0, 1
    * ]
    * </pre></p>
    * @param matriz
    */
   public void matrizIdentidade(double[][] matriz){
      om.matrizIdentidade(matriz);
   }
}
