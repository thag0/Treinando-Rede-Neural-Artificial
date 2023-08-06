package rna.otimizadores;

import java.util.ArrayList;

import rna.Camada;
import rna.Neuronio;
import rna.RedeNeural;

/**
 * Otimizador usando gradiente descendente estocástico com momentum.
 * Atualiza os pesos usando o gradiente e momentum para ajudar a otimizar o aprendizado.
 */
public class SGD implements Otimizador{
   boolean nesterov = false;//TODO implementar nesterov


   public SGD(boolean nesterov){
      this.nesterov = nesterov;
   }


   public SGD(){}
   

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
    * Atualiza os pesos da rede neural usando o algoritmo SGD com momentum.
    * @param rede rede neural que será treinada.
    * @param entrada dados de entrada do treino.
    * @param saida dados de valores de saída correspondente aos valores de entrada.
    */
   public void atualizar(RedeNeural rede, double[] entrada, double[] saida){
      //calcular saída para aplicar o erro
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
               if(nesterov){
                  double momentumAnterior = neuronio.momentum[k];
                  neuronio.pesos[k] += rede.obterTaxaMomentum() * momentumAnterior;
                  neuronio.gradiente = rede.obterTaxaAprendizagem() * neuronio.erro * camadaAnterior.neuronios[k].saida;
                  neuronio.momentum[k] = rede.obterTaxaMomentum() * momentumAnterior + neuronio.gradiente;
                  neuronio.pesos[k] -= rede.obterTaxaAprendizagem() * neuronio.momentum[k];
                  
               }else{
                  neuronio.gradiente = rede.obterTaxaAprendizagem() * neuronio.erro * camadaAnterior.neuronios[k].saida;
                  neuronio.momentum[k] = (rede.obterTaxaMomentum() * neuronio.momentum[k]) + neuronio.gradiente;
                  neuronio.pesos[k] += neuronio.momentum[k];
               }
            }
         }
      } 
   }
   
}
