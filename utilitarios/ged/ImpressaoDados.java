package utilitarios.ged;

public class ImpressaoDados {
   public ImpressaoDados(){

   }


   public void imprimirInicio(Dados dados){
      if(dados.vazio()){
         dados.imprimir();
         return;
      }
      
      String espacamento = "   ";
      int[] shape = dados.shape();
      int linPadrao = (shape[0] < 5) ? shape[0] : 5;

      System.out.println("Início " + "\"" + dados.nome() + "\" (" + shape[0] + ", " + shape[1] + ")" +" = [");

      for(int i = 0; i < linPadrao; i++){

         String[] linha = dados.conteudo().get(i);
         for(int j = 0; j < linha.length; j++){
            System.out.print(espacamento + linha[j] + "\t");
         }
         System.out.println();
      }

      System.out.println("]");
   }
}
