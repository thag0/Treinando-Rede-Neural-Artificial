package utilitarios.ged;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Manipulador de dados do Ged
 */
class ManipuladorDados{

   /**
    * Contém as implementações das manipulações no conjunto de dados 
    * do tipo {@code ArrayList<String[]>}
    */
   public ManipuladorDados(){

   }
 
   public void adicionarColuna(ArrayList<String[]> lista) {
      int nColunas = lista.get(0).length;
  
      for(int i = 0; i < lista.size(); i++){
         String[] linhaAtual = lista.get(i);
         String[] novaLinha = new String[nColunas + 1];

         System.arraycopy(linhaAtual, 0, novaLinha, 0, nColunas);

         novaLinha[nColunas] = "";

         lista.set(i, novaLinha);
      }
  }  


   public void adicionarColuna(ArrayList<String[]> lista, int indice){
      if((indice < 0) || (indice > lista.get(0).length - 1)){
         throw new IllegalArgumentException("O índice fornecido é inválido.");
      }
   
      int nColunas = lista.get(0).length;
   
      for(int i = 0; i < lista.size(); i++){
         String[] linhaAtual = lista.get(i);
         String[] novaLinha = new String[nColunas + 1];
   
         //copiando valores antigos e deslocando a partir
         //do novo indice
         System.arraycopy(linhaAtual, 0, novaLinha, 0, indice);
   
         novaLinha[indice] = "";
   
         //copiando valores restantes
         System.arraycopy(linhaAtual, indice, novaLinha, indice + 1, nColunas - indice);
   
         lista.set(i, novaLinha);
      }
   }


   public void removerLinha(ArrayList<String[]> lista, int indice){
      if(lista == null) throw new IllegalArgumentException("A lista de dados é nula.");
      if((indice < 0) || (indice > lista.get(0).length-1)){
         throw new IllegalArgumentException("Índice fornecido para remoção é inválido");
      }

      lista.remove(indice);
   }


   public void removerColuna(ArrayList<String[]> lista, int indice){
      if(lista == null) throw new IllegalArgumentException("A lista de dados é nula.");
      if((indice < 0) || (indice > lista.get(0).length-1)){
         throw new IllegalArgumentException("Índice fornecido para remoção é inválido");
      }

      for(int i = 0; i < lista.size(); i++){
         String[] linha = lista.get(i);
         //remover coluna original e substituir pelas novas categorias
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

         lista.set(i, novaLinha);
      }
   }


   public void editarValor(ArrayList<String[]> lista, int idLinha, int idColuna, String busca, String novoValor){
      if(lista == null) throw new IllegalArgumentException("A lista de dados é nula.");
      if(!dadosSimetricos(lista)) throw new IllegalArgumentException("A lista não é simétrica para a operação.");

      if(idLinha < 0 || (idLinha >= lista.size())){
         throw new IllegalArgumentException("Valor do índice de linha está fora de alcance dos índices da lista.");
      }
      if(idColuna < 0 || (idColuna >= lista.get(0).length)){
         throw new IllegalArgumentException("Valor do índice de coluna está fora de alcance dos índices da lista.");
      }

      if(busca == null) throw new IllegalArgumentException("O valor de busca é nulo");
      if(novoValor == null) throw new IllegalArgumentException("O novo valor para substituição é nulo");
   
      String linha[] = lista.get(idLinha);
      if(linha[idColuna].contains(busca)){
         linha[idColuna] = novoValor;
      }
   }


   public void editarValor(ArrayList<String[]> lista, int idColuna, String busca, String novoValor){
      if(lista == null) throw new IllegalArgumentException("A lista de dados é nula.");
      if(!dadosSimetricos(lista)) throw new IllegalArgumentException("A lista não é simétrica para a operação.");

      if(idColuna < 0 || (idColuna >= lista.get(0).length)){
         throw new IllegalArgumentException("Valor do índice de coluna está fora de alcance dos índices da lista.");
      }
      if(busca == null) throw new IllegalArgumentException("O valor de busca é nulo");
      if(novoValor == null) throw new IllegalArgumentException("O novo valor para substituição é nulo");

      for(String[] linha : lista){
         if(linha[idColuna].contains(busca)){
            linha[idColuna] = novoValor;
         }
      }
   }


   public void trocarColunas(ArrayList<String[]> lista, int idColuna1, int idColuna2){
      if(lista == null) throw new IllegalArgumentException("A lista fornicida está nula.");
      if(lista.size() < 2) throw new IllegalArgumentException("É necessário que a lista tenha pelo menos duas colunas.");
      if(!(dadosSimetricos(lista))) throw new IllegalArgumentException("A lista deve ser simétrica.");

      if(idColuna1 < 0 || idColuna1 >= lista.get(0).length) throw new IllegalArgumentException("O índice fornecido da coluna 1 é inválido.");
      if(idColuna2 < 0 || idColuna2 >= lista.get(0).length) throw new IllegalArgumentException("O índice fornecido da coluna 2 é inválido.");
      if(idColuna1 == idColuna2) throw new IllegalArgumentException("Os índices fornecidos devem ser diferentes.");

      for(String[] linha : lista){
         String intermediario = linha[idColuna1];
         linha[idColuna1] = linha[idColuna2];
         linha[idColuna2] = intermediario;
      }
   }


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


   public void categorizar(ArrayList<String[]> lista, int indice){
      if(!dadosSimetricos(lista)){
         throw new IllegalArgumentException("A lista deve ser simétrica para categorizar.");
      }
      if((indice < 0) || (indice >= lista.get(0).length)){
         throw new IllegalArgumentException("O índice fornecido é inválido.");
      }

      //objeto que recebe apenas valores únicos
      HashSet<String> categoriasUnicas = new HashSet<>();
      for(String[] linha : lista){
         categoriasUnicas.add(linha[indice]);
      }

      //definindo indice de cada categoria
      int nCategorias = categoriasUnicas.size();
      String[] listaCategorias = categoriasUnicas.toArray(new String[0]);
      int[] indiceCategorias = new int[nCategorias];
      for(int i = 0; i < indiceCategorias.length; i++){
         indiceCategorias[i] = i;
      }

      //nova lista para armazenar as linhas atualizadas
      ArrayList<String[]> novaLista = new ArrayList<>();

      //reestruturando a lista com as novas categorias
      for(String[] linha : lista){
         //remover coluna original e substituir pelas novas categorias
         String[] novaLinha = new String[(linha.length - 1) + nCategorias];

         //copiando valores existentes até o índice original
         for(int j = 0; j < indice; j++){
            novaLinha[j] = linha[j];
         }

         //novos valores para as categorias
         for(int j = 0; j < nCategorias; j++){
            if(listaCategorias[j].equals(linha[indice])) novaLinha[indice + j] = "1";
            else novaLinha[indice + j] = "0";
         }

         //copiando valores existentes após o índice original
         for(int j = indice + 1; j < linha.length; j++){
            novaLinha[j + nCategorias - 1] = linha[j];
         }

         novaLista.add(novaLinha);
      }

      //substituindo a lista original pela nova lista modificada
      lista.clear();
      lista.addAll(novaLista);
   }


   public double[][] obterSubMatriz(double[][] dados, int inicio, int fim){
      if(inicio < 0 || fim > dados.length || inicio >= fim){
         throw new IllegalArgumentException("Índices de início ou fim inválidos.");
      }

      int linhas = fim - inicio;
      int colunas = dados[0].length;
      double[][] subMatriz = new double[linhas][colunas];

      for(int i = 0; i < linhas; i++){
         System.arraycopy(dados[inicio + i], 0, subMatriz[i], 0, colunas);
      }

      return subMatriz;
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


   public boolean dadosSimetricos(ArrayList<String[]> lista){
      if(lista == null) throw new IllegalArgumentException("A lista é nula.");
      //lista sem dados é considerada como não simétrica
      if(lista.size() == 0) return false;

      int colunas = lista.get(0).length;// tamanho base
      for(int i = 0; i < lista.size(); i++){
         if(lista.get(i).length != colunas) return false;
      }

      return true;
   }
}