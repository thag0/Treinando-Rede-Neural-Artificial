import rna.RedeNeural;

public class Auxiliares{
   static void imprimirSaidasRede(RedeNeural rede){
      System.out.println("Entrada:");
      for(int i = 0; i < rede.qtdNeuroniosEntrada; i++){
         System.out.print("[n" + i + " " + rede.entrada.neuronios[i].saida + "]");
      }
      

      System.out.println("\n\nOcultas:");
      for(int i = 0; i < rede.qtdCamadasOcultas; i++){
         System.out.print("O" + i + " ");
         for (int j = 0; j < rede.ocultas[i].neuronios.length; j++) {
            System.out.print("[n" + j + " " + rede.ocultas[i].neuronios[j].saida + "]");
         }
         System.out.println();
      }

      System.out.println("\nSaida:");
      for(int i = 0; i < rede.qtdNeuroniosSaida; i++){
         System.out.print("[n" + i + " " + rede.saida.neuronios[i].saida + "]");
      }
      System.out.println();
   }


   static void imprimirarApenasSaidasRede(RedeNeural rede){
      System.out.println("\nSaida:");
      for(int i = 0; i < rede.qtdNeuroniosSaida; i++){
         System.out.println("[n" + i + " " + rede.saida.neuronios[i].saida + "]");
      }
      System.out.println();
   }


   static void imprimirPesosRede(RedeNeural rede){
      System.out.println("\t--Entrada--");
      for(int i = 0; i < rede.qtdNeuroniosEntrada; i++){
         System.out.println("n" + i);
         for(int j = 0; j < rede.entrada.neuronios[i].qtdLigacoes; j++){
            System.out.println("p" + j + " [" + rede.entrada.neuronios[i].pesos[j] + "]");
         }
         System.out.println();
      }

      System.out.println("\t--Ocultas--");
      for(int i = 0; i < rede.qtdCamadasOcultas; i++){
         System.out.println("Oculta " + i + " ");
         for(int j = 0; j < rede.qtdNeuroniosOcultas; j++){
            System.out.println("n" + j);
            for(int k = 0; k < rede.ocultas[i].neuronios[j].pesos.length; k++){
               System.out.println("p" + k + "[" +rede.ocultas[i].neuronios[j].pesos[k] + "]");
            }
            System.out.println();
         }
      }
   }

   
   static void imprimirPesosOcultasRede(RedeNeural rede){
      System.out.println("\t--Ocultas--");
      for(int i = 0; i < rede.qtdCamadasOcultas; i++){
         System.out.println("Oculta " + i + " ");
         for(int j = 0; j < rede.qtdNeuroniosOcultas; j++){
            System.out.println("n" + j);
            for(int k = 0; k < rede.ocultas[i].neuronios[j].pesos.length; k++){
               System.out.println("p" + k + "[" +rede.ocultas[i].neuronios[j].pesos[k] + "]");
            }
            System.out.println();
         }
      }
   }
}
