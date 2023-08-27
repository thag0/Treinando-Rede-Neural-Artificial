package utilitarios.ged;

import java.util.ArrayList;


/**
 * Classe criada para centralizar um tipo de dado 
 * específico para uso dentro do Ged.
 */
public class Dados{

   /**
    * Estrutura que armazena o conteúdo de dados lidos
    */
   private ArrayList<String[]> conteudo;


   /**
    * Inicializa um objeto do tipo Dados com seu conteúdo vazio.
    */
   public Dados(){
      this.conteudo = new ArrayList<>();
      this.conteudo.add(new String[0]);
   }


   /**
    * Retorna o item correspondente pela linha e coluna fornecidos.
    * @param lin linha para busca.
    * @param col coluna para busca.
    * @return valor contido com base na linha e coluna.
    */
   public String obterItem(int lin, int col){
      return this.conteudo.get(lin)[col];
   }


   /**
    * Edita o valor contido no espaço indicado pela linha e coluna.
    * @param lin linha para edição.
    * @param col coluna para edição.
    * @param valor novo valor.
    */
   public void editarItem(int lin, int col, String valor){
      String[] linha = this.conteudo.get(lin);
      linha[col] = valor;
      this.conteudo.set(lin, linha);
   }


   /**
    * Edita o valor em todas as linhas de acordo com a coluna especificada.
    * @param lin linha para edição.
    * @param col coluna para edição.
    * @param busca valor alvo.
    * @param valor novo valor.
    */
   public void editarItem(int col, String busca, String valor){
      for(String[] linha : this.conteudo){
         if(linha[col].contains(busca)){
            linha[col] = valor;
         }
      }
   }


   /**
    * Substitui todo o conteúdo atual de Dados pela nova lista.
    * @param lista lista com os novos dados.
    */
   public void atribuir(ArrayList<String[]> lista){
      if(lista != null) this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(int[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Integer.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(float[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Float.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(String[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = matriz[i][j];
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * @param matriz matriz com os dados.
    */
   public void atribuir(double[][] matriz){
      int linhas = matriz.length;
      int colunas = matriz[0].length;
      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Double.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
   }


   /**
    * Retorna todo o conteúdo presente nos dados.
    * @return estrutura {@code ArrayList<String[]>} que armazena o conteúdo dos dados.
    */
   public ArrayList<String[]> conteudo(){
      return this.conteudo;
   }


   public boolean estaVazio(){
      if(this.conteudo.isEmpty()) return true;
      
      for(int i = 0; i < this.conteudo.size(); i++){
         if(this.conteudo.get(i).length > 0) return false;
      }

      return true;
   }


   /**
    * Retorna um array contendo as linhas e colunas do conteúdo dos dados.
    * <p>
    *    {@code shape[0] = linhas}
    * </p>
    * <p>
    *    {@code shape[1] = colunas}
    * </p>
    * @return estrutura contendo o formato da lista, considerando que ela é simétrica.
    * @throws IllegalArgumentException se o conteúdo estiver vazio.
    */
   public int[] shape(){
      if(this.estaVazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }

      int[] shape = {
         this.conteudo.size(), 
         this.conteudo.get(0).length
      };
      return shape;
   }


   public String shapeInfo(){
      if(this.estaVazio()){
         return "Shape = [ (Vazio) ]";
      }

      int[] shape = {
         this.conteudo.size(), 
         this.conteudo.get(0).length
      };

      return "Shape = [" + Integer.toString(shape[0]) + ", " + Integer.toString(shape[1]) + "]";
   }


   /**
    * Método de impressão básico, via console, do conteúdo 
    * contido em formato de tabela.
    */
   public void imprimir(){
      String espacamento = "   ";
      System.out.println("Dados = [");

      if(this.estaVazio()){
         System.out.println(espacamento + "(Vazio)");
         
      }else{
         for(String linha[] : this.conteudo){
            for(int i = 0; i < linha.length; i++){
               System.out.print(espacamento + linha[i] + "\t");
            }
            System.out.println();
         }
      }
      
      System.out.println("]\n");
   }
}
