import rna.RedeNeural;

public class Auxiliares{
   static void imprimirSaidasRede(RedeNeural rede){
      System.out.println("Entrada:");
      for(int i = 0; i < rede.entrada.neuronios.length; i++){
         System.out.print("[n" + i + " " + rede.entrada.neuronios[i].saida + "]");
      }
      

      System.out.println("\n\nOcultas:");
      for(int i = 0; i < rede.ocultas.length; i++){
         System.out.print("O" + i + " ");
         for (int j = 0; j < rede.ocultas[i].neuronios.length; j++) {
            System.out.print("[n" + j + " " + rede.ocultas[i].neuronios[j].saida + "]");
         }
         System.out.println();
      }

      System.out.println("\nSaida:");
      for(int i = 0; i < rede.saida.neuronios.length; i++){
         System.out.print("[n" + i + " " + rede.saida.neuronios[i].saida + "]");
      }
      System.out.println();
   }


   static void imprimirarApenasSaidasRede(RedeNeural rede){
      System.out.println("\nSaida:");
      for(int i = 0; i < rede.saida.neuronios.length; i++){
         System.out.println("[n" + i + " " + rede.saida.neuronios[i].saida + "]");
      }
      System.out.println();
   }


   static void imprimirRede(RedeNeural rede){
      String espacamento = "  ";
      String espacamentoDuplo = espacamento + espacamento;
      String espacamentoTriplo = espacamento + espacamento + espacamento;
      System.out.println("\n" + rede.getClass().getSimpleName() + " = [");
      
      //entrada
      System.out.println(espacamento + "Entrada = [");
      for(int i = 0; i < rede.entrada.neuronios.length; i++){
         
         System.out.println(espacamentoDuplo + "n" + i + " = [");
         for(int j = 0; j < rede.entrada.neuronios[i].pesos.length; j++){
            System.out.println(espacamentoTriplo + "p" + j + " : " + rede.entrada.neuronios[i].pesos[j]);
         }
         System.out.println(espacamentoDuplo + "]");
      }
      System.out.println(espacamento + "]\n");

      //ocultas
      for(int i = 0; i < rede.ocultas.length; i++){
         System.out.println(espacamento + "Oculta " + i + " = [");
         for(int j = 0; j < rede.ocultas[i].neuronios.length; j++){
            
            System.out.println(espacamento + espacamento + "n" + j + " = [");
            for(int k = 0; k < rede.ocultas[i].neuronios[j].pesos.length; k++){
               System.out.println(espacamentoTriplo + "p" + k + " : " + rede.ocultas[i].neuronios[j].pesos[k]);
            }
            System.out.println(espacamentoDuplo + "]\n");
         }
         System.out.println(espacamento + "]");
      }
      System.out.println("]");
   }

   
   static void imprimirPesosOcultasRede(RedeNeural rede){
      System.out.println("\t--Ocultas--");
      for(int i = 0; i < rede.ocultas.length; i++){
         System.out.println("Oculta " + i + " ");
         for(int j = 0; j < rede.ocultas[i].neuronios.length; j++){
            System.out.println("n" + j);
            for(int k = 0; k < rede.ocultas[i].neuronios[j].pesos.length; k++){
               System.out.println("p" + k + "[" +rede.ocultas[i].neuronios[j].pesos[k] + "]");
            }
            System.out.println();
         }
      }
   }
}
