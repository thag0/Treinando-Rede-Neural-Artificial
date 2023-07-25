package utilitarios;

import java.util.ArrayList;

/**
 * Responsável pelo manuseio do conjunto de dados
*/
public class GerenciadorDados{

   
   public GerenciadorDados(){

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
    * Remove a coluna dos dados de acordo com o índice fornecido
    * @param lista lista com os dados.
    * @param indice índice da coluna que será removida.
    * @return nova lista com a coluna removida.
    * @throws IllegalArgumentException se o indice lista for nula.
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
    *    o método irá remover todas as colunas como consequência.
    * </p>
    * @param lista lista com os dados;
    */
   public void removerNaoNumericos(ArrayList<String[]> lista){
   int indiceInicial = 0;
   boolean removerLinha = false;
   
      while (indiceInicial < lista.size()){
            removerLinha = false;

            for(int j = 0; j < lista.get(indiceInicial).length; j++){
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
    * Tenta converter o valor para um numérico do tipo double
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
    * @param lista lista com os dados
    * @return array contendo as informações das dimensões da lista, o primeiro elemento corresponde a quantidade de 
    * linhas e o segundo elemento corresponde a quantidade de colunas.
    * @throws IllegalArgumentException se a lista estiver nula.
v se a lista estiver vazia.
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
}
