package utilitarios.ged;

import java.util.ArrayList;


/**
 * Classe criada para centralizar um tipo de dado 
 * específico para uso dentro do Ged.
 */
public class Dados{
   private ArrayList<String[]> conteudo;


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
    * Substitui todo o conteúdo atual pela nova lista.
    * @param lista lista com os novos dados.
    */
   public void atribuir(ArrayList<String[]> lista){
      if(lista != null) this.conteudo = lista;
   }


   /**
    * Retorna todo o conteúdo presente nos dados.
    * @return estrutura {@code ArrayList<String[]>} que armazena o conteúdo dos dados.
    */
   public ArrayList<String[]> conteudo(){
      return this.conteudo;
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
    */
   public int[] shape(){
      int[] shape = {
         this.conteudo.size(), 
         this.conteudo.get(0).length
      };
      return shape;
   }
}
