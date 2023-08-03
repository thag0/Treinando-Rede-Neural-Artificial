package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;
import rna.RedeNeural;


/**
 * Implementação do algoritmo de otimização Adam.
 * O algoritmo ajusta os pesos da rede neural usando o gradiente descendente com momento
 * e a estimativa adaptativa de momentos de primeira e segunda ordem.
 */
public class Adam implements Otimizador{

   //acumuladores do momento e da segunda ordem
   private double beta1 = 0.9; //fator de decaimento do momento
   private double beta2 = 0.999; //fator de decaimento da segunda ordem
   private double epsilon = 1e-8; //evitar divisão por zero


   public void calcularErro(ArrayList<Camada> redec, double[] entrada, double[] saida){
      //erro da saída
      Camada saidaRede = redec.get(redec.size()-1);
      for(int i = 0; i < saidaRede.neuronios.length; i++){
         Neuronio neuronio = saidaRede.neuronios[i];
         neuronio.erro = ((saida[i] - neuronio.saida) * saidaRede.funcaoAtivacaoDx(neuronio.somatorio));
      }

      double somaErros = 0.0;
      //começar da ultima oculta
      for(int i = redec.size()-2; i >= 1; i--){// percorrer camadas ocultas de trás pra frente
         
         Camada camadaAtual = redec.get(i);
         int qNeuronioAtual = camadaAtual.neuronios.length;
         if(redec.get(i).temBias) qNeuronioAtual -= 1;
         for (int j = 0; j < qNeuronioAtual; j++){//percorrer neurônios da camada atual
         
            Neuronio neuronio = camadaAtual.neuronios[j];
            somaErros = 0.0;
            for(Neuronio neuronioProximo : redec.get(i+1).neuronios){//percorrer neurônios da camada seguinte
               somaErros += neuronioProximo.pesos[j] * neuronioProximo.erro;
            }
            neuronio.erro = somaErros * camadaAtual.funcaoAtivacaoDx(neuronio.somatorio);
         }
      }
   }


   /**
    * @param rede rede neural que será treinada.
    * @param entrada dados de entrada do treino.
    * @param saida dados de valores de saída correspondente aos valores de entrada.
    */
   public void atualizar(RedeNeural rede, double[] entrada, double[] saida){
      //calcular saída para aplicar o erro
      rede.calcularSaida(entrada);

      //transformar a rede em um vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.entrada);
      for (Camada camada : rede.ocultas) redec.add(camada);
      redec.add(rede.saida);

      calcularErro(redec, entrada, saida);

      double t = 1; //contador de iterações
      for(int i = 1; i < redec.size(); i++){//percorrer rede

         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i - 1);
         for(int j = 0; j < camadaAtual.neuronios.length; j++){//percorrer neurônios da camada atual

            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente = neuronio.erro * camadaAnterior.neuronios[k].saida;

               //atualização do momentum
               neuronio.momentum[k] = (beta1 * neuronio.momentum[k]) + ((1 - beta1) * neuronio.gradiente);
               // Atualização do acumulador da segunda ordem
               neuronio.acumuladorSegundaOrdem[k] = (beta2 * neuronio.acumuladorSegundaOrdem[k]) + ((1 - beta2) * neuronio.gradiente * neuronio.gradiente);
               //bias corrigido pelo momento
               double momentumCorrigido = neuronio.momentum[k] / (1 - Math.pow(beta1, t));
               //bias corrigido pela segunda ordem
               double segundaOrdemCorrigida = neuronio.acumuladorSegundaOrdem[k] / (1 - Math.pow(beta2, t));
               //atualização dos pesos usando o Adam
               neuronio.pesos[k] += rede.obterTaxaAprendizagem() * momentumCorrigido / (Math.sqrt(segundaOrdemCorrigida) + epsilon);
            }
         }
      }
   }
}
