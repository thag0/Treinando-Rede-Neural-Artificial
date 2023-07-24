package utilitarios;

import java.util.ArrayList;

public class GerenciadorDados{

   /**
    * Responsável pelo manuseio do conjunto de dados
    */
   public GerenciadorDados(){

   }

   public void removerLinhaDados(ArrayList<String[]> lista, int indice){
      lista.remove(indice);
   }


   /**
    * Remove a coluna dos dados de acordo com o índice fornecido
    * @param lista lista com os dados.
    * @param indice índice da coluna que será removida.
    * @return nova lista com a coluna removida.
    */
   public ArrayList<String[]> removerColunaDados(ArrayList<String[]> lista, int indice){
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
    * Separa os dados que serão usados como entrada de acordo com os valores fornecidos.
    * @param dados conjunto de dados completo.
    * @param colunas quantidade de colunas que serão preservadas, começando pela primeira até o valor fornecido.
    * @return nova matriz de dados apenas com as colunas desejadas.
    */
   public double[][] separarDadosEntrada(double[][] dados, int colunas){
      double[][] dadosEntrada = new double[dados.length][colunas];
      for(int i = 0; i < dadosEntrada.length; i++){
         for(int j = 0; j < colunas; j++){
            dadosEntrada[i][j] = dados[i][j];
         }
      }
      return dadosEntrada;
   }


   /**
    * 
    * @param dados
    * @param dadosEntrada
    * @param dadosSaida
    * @return
    */
   public double[][] separarDadosSaida(double[][] dados, int colunas){
      double[][] dadosSaida = new double[dados.length][colunas];
      
      int indiceInicial = (dados[0].length) - colunas;

      for(int i = 0; i < dados.length; i++){// percorrer linhas dos dados
         int contador1 = indiceInicial;// contador coluna dados
         int contador2 = 0;// contador coluna saída
         
         while(contador2 < (dadosSaida[0].length)){
            dadosSaida[i][contador2] = dados[i][contador1];
            contador1++;
            contador2++;
         }
      }
      return dadosSaida;
   }
}
