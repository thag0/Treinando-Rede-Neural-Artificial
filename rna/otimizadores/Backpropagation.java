package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;
import rna.RedeNeural;


/**
 * Classe que implementa o algoritmo de Backpropagation para otimização de redes neurais.
 * Atualiza diretamente os pesos da rede com base no gradiente.
 */
public class Backpropagation implements Otimizador{


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
    * Atualiza os pesos da rede neural usando o algoritmo de Backpropagation
    * @param rede rede neural que será treinada.
    * @param entrada dados de entrada do treino.
    * @param saida dados de valores de saída correspondente aos valores de entrada.
    */
   public void atualizar(RedeNeural rede, double[] entrada, double[] saida){
      //calcular saída pra aplicar o erro
      rede.calcularSaida(entrada);

      //transformar a rede num vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.entrada);
      for(Camada camada : rede.ocultas) redec.add(camada);
      redec.add(rede.saida);

      calcularErro(redec, entrada, saida);

      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i-1);
         for(int j = 0; j < camadaAtual.neuronios.length; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente = rede.obterTaxaAprendizagem() * neuronio.erro * camadaAnterior.neuronios[k].saida;
               neuronio.pesos[k] += neuronio.gradiente;
            }
         }
      } 
   }
   
}
