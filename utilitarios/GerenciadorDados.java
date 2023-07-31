package utilitarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Responsável pelo manuseio do conjunto de dados
*/
public class GerenciadorDados{

   
   public GerenciadorDados(){

   }


   /**
    * Lê o arquivo .csv de acordo com o caminho especificado.
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
      ArrayList<String[]> dados = new ArrayList<>();
      String separador = ",";
      String linha = "";

      //diretório não existe
      if(!(new File(caminho).exists())){
         throw new IllegalArgumentException("O caminho especificado não existe ou não foi encontrado.");
      }

      try{
         BufferedReader br = new BufferedReader(new FileReader(caminho));
         while((linha = br.readLine()) != null){
            String linhaDados[] = linha.split(separador);
            dados.add(linhaDados);
         }
         br.close();

      }catch(Exception e){
         e.printStackTrace();
      }
      
      return dados;
   }


   /**
    * Exibe pelo console as informações contidas na lista.
    * @param lista lista com os dados.
    */
   public void imprimirCsv(ArrayList<String[]> lista){
      String separador = ",";
      for(String linha[] : lista){
         for(int i = 0; i < linha.length; i++){
            if(i == 0) System.out.print(linha[i]);
            else System.out.print(separador + " " + linha[i]);
         }
         System.out.println();
      }
   }


   /**
    * Remove uma linha inteira do conjunto de dados
    * @param lista lista com os dados.
    * @param indice índice da coluna que será removida.
    * @throws IllegalArgumentException se o indice lista for nula.
    * @throws IllegalArgumentException se o indice estiver fora de alcance da lista.
    */
   public void removerLinhaDados(ArrayList<String[]> lista, int indice){
      if(lista == null) throw new IllegalArgumentException("A lista de dados é nula.");
      if((indice < 0) || (indice > lista.get(0).length-1)){
         throw new IllegalArgumentException("Índice fornecido para remoção é inválido");
      }

      lista.remove(indice);
   }


   /**
    * Remove todas as colunas dos dados de acordo com o índice fornecido.
    * @param lista lista com os dados.
    * @param indice índice da coluna que será removida.
    * @return nova lista com a coluna removida.
    * @throws IllegalArgumentException se a lista for nula.
    * @throws IllegalArgumentException se o indice estiver fora de alcance da lista.
    */
   public ArrayList<String[]> removerColunaDados(ArrayList<String[]> lista, int indice){
      if(lista == null) throw new IllegalArgumentException("A lista de dados é nula.");
      if((indice < 0) || (indice > lista.get(0).length-1)){
         throw new IllegalArgumentException("Índice fornecido para remoção é inválido");
      }

      ArrayList<String[]> novaLista = new ArrayList<String[]>();

      for(int i = 0; i < lista.size(); i++){
         String[] linha = lista.get(i);
         String[] novaLinha = new String[linha.length - 1];

         int contador1 = 0;
         int contador2 = 0;

         while(contador2 < linha.length){
            if(contador2 == indice){
                  contador2++;
            }else{
               novaLinha[contador1] = linha[contador2];
               contador1++;
               contador2++;
            }
         }

         novaLista.add(novaLinha);
      }
      
      return novaLista;
   }


   /**
    * Substitui todas as linhas dos dados pelo valor fornecido, caso na coluna fornecida tenha o valor buscado.
    * @param lista lista com os dados lidos.
    * @param indice índice da coluna alvo para a alteração dos dados.
    * @param valorBusca valor que será procurado para ser substituído.
    * @param novoValor novo valor que será colocado.
    * @throws IllegalArgumentException se a lista estiver nula.
    * @throws IllegalArgumentException se o valor do índice fornecido estiver fora de alcance.
    * @throws IllegalArgumentException se o valor de busca for nulo.
    * @throws IllegalArgumentException se o novo valor de substituição for nulo;
    */
   public void editarValorDados(ArrayList<String[]> lista, int indice, String valorBusca, String novoValor){
      if(lista == null) throw new IllegalArgumentException("A lista de dados é nula.");
      if(indice < 0 || (indice > lista.get(0).length-1)){
         throw new IllegalArgumentException("Valor do índice está fora de alcance dos índices da lista.");
      }
      if(valorBusca == null) throw new IllegalArgumentException("O valor de busca é nulo");
      if(novoValor == null) throw new IllegalArgumentException("O novo valor para substituição é nulo");

      for(String[] linha : lista){
         if(linha[indice].contains(valorBusca)){
            linha[indice] = novoValor;
         }
      }
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
      int indiceInicial = 0;
      boolean removerLinha = false;
   
      while(indiceInicial < lista.size()){
         removerLinha = false;

         for(int j = 0; j < lista.get(indiceInicial).length; j++){
            //verificar se existe algum valor que não possa ser convertido para número.
            if((valorInt(lista.get(indiceInicial)[j]) == false) || 
               (valorFloat(lista.get(indiceInicial)[j]) == false) || 
               (valorDouble(lista.get(indiceInicial)[j]) == false)
            ){
               removerLinha = true;
               break;
            }
         }

         if(removerLinha) lista.remove(indiceInicial);
         else indiceInicial++; 
      }
   }


   /**
    * Tenta converter o valor para um numérico do tipo int
    * @param valor valor que será testado.
    * @return resultado da verificação, verdadeiro se foi convertido ou false se não
    */
   private boolean valorInt(String valor){
      try{
         Integer.parseInt(valor);
         return true;
      
      }catch(Exception e){
         return false;
      }
   }


   /**
    * Tenta converter o valor para um numérico do tipo float.
    * @param valor valor que será testado.
    * @return resultado da verificação, verdadeiro se foi convertido ou false se não
    */
   private boolean valorFloat(String valor){
      try{
         Float.parseFloat(valor);
         return true;
      
      }catch(Exception e){
         return false;
      }
   }


   /**
    * Tenta converter o valor para um numérico do tipo double
    * @param valor valor que será testado.
    * @return resultado da verificação, verdadeiro se foi convertido ou false se não
    */
   private boolean valorDouble(String valor){
      try{
         Double.parseDouble(valor);
         return true;
      
      }catch(Exception e){
         return false;
      }
   }


   /**
    * Descreve as dimensões da lista, tanto em questão de quantidade de linhas qunanto quantidade de colunas.
    * @param lista lista com os dados.
    * @return array contendo as informações das dimensões da lista, o primeiro elemento corresponde a quantidade de 
    * linhas e o segundo elemento corresponde a quantidade de colunas.
    * @throws IllegalArgumentException se a lista estiver nula.
    * @throws IllegalArgumentException se a lista estiver vazia.
    */
   public int[] obterShapeLista(ArrayList<String[]> lista){
      if(lista == null) throw new IllegalArgumentException("A lista fornecida é nula.");
      if(lista.size() == 0) throw new IllegalArgumentException("A lista fornecida está vazia.");

      int[] shape = new int[2];
      shape[0] = lista.size();
      shape[1] = lista.get(0).length;
      return shape;
   }


   /**
    * Embaralha o conjunto de dados aleatoriamente.
    * <p>
    *    A alteração irá afetar o conteúdo dos dados recebidos.
    *    Caso queira manter os dados originais, é recomendado fazer uma cópia previamente.
    * </p>
    * @param dados conjunto de dados completo.
    */
   public void embaralharDados(double[][] dados){
      Random random = new Random();
      int linhas = dados.length;

      for(int i = linhas - 1; i > 0; i--){
         int j = random.nextInt(i + 1);

         double[] temp = dados[i];
         dados[i] = dados[j];
         dados[j] = temp;
      }
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
      if (colunas > dados[0].length) {
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      double[][] dadosEntrada = new double[dados.length][colunas];
      for(int i = 0; i < dadosEntrada.length; i++){
         for(int j = 0; j < colunas; j++){
            dadosEntrada[i][j] = dados[i][j];
         }
      }
      return dadosEntrada;
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
      if(colunas > dados[0].length){
         throw new IllegalArgumentException("O número de colunas fornecido é maior do que o número de colunas disponíveis nos dados.");
      }
      if(colunas < 1){
         throw new IllegalArgumentException("A quantidade de colunas extraídas não pode ser menor que um");
      }

      double[][] dadosSaida = new double[dados.length][colunas];
      int indiceInicial = dados[0].length - colunas;

      for (int i = 0; i < dados.length; i++) { // percorrer linhas dos dados
         int contador1 = indiceInicial; // contador coluna dados
         int contador2 = 0; // contador coluna saída

         // copiando os valores 
         while (contador2 < colunas) {
            dadosSaida[i][contador2] = dados[i][contador1];
            contador1++;
            contador2++;
         }
      }

      return dadosSaida;
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
    * @throws IllegalArgumentException caso o conjunto de dados for nulo ou o tamanho de teste estiver fora do intervalo (0, 1).
    */
   public double[][][] separarTreinoTeste(double[][] dados, float tamanhoTeste){
      if(dados == null) throw new IllegalArgumentException("O conjunto de dados é nulo.");
      if(tamanhoTeste < 0 || tamanhoTeste > 1){
         throw new IllegalArgumentException("O tamanho dos dados de teste deve ser maior que zero e menor que um.");
      }

      int linhasTeste = (int) (dados.length*tamanhoTeste);
      int linhasTreino = dados.length - linhasTeste;
      int colunas = dados[0].length;

      double[][] treino = new double[linhasTreino][colunas];
      double[][] teste = new double[linhasTeste][colunas];

      //método nativo, parece ser mais rápido que uma cópia manual
      System.arraycopy(dados, 0, treino, 0, linhasTreino);// copiar treino
      System.arraycopy(dados, linhasTreino, teste, 0, linhasTeste);// copiar teste

      return new double[][][]{treino, teste};
   }
}
