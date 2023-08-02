package rna;

import java.util.ArrayList;

/**
 * Em teste ainda.
 */
public class Otimizadores{

   /**
    * Otimizador padrão usando gradiente descendente para atualizar diretamente os pesos
    * @param rede rede neural que será treinada.
    * @param entrada dados de entrada do treino.
    * @param saida dados de valores de saída correspondente aos valores de entrada.
    * @throws IllegalArgumentException se a quantidade de dados de entrada for diferente da quantidade de neurônios da camada de entrada, excluindo o bias.
    * @throws IllegalArgumentException se a quantidade de dados de saída for diferente da quantidade de neurônios da camada de saída.
    */
   static void backpropagation(RedeNeural rede, double[] entrada, double[] saida){
      if(entrada.length != (rede.entrada.neuronios.length - ((rede.entrada.temBias) ? 1 : 0) )){
         throw new IllegalArgumentException("O tamanho dos dados de entrada não corresponde ao tamanho dos neurônios de entrada da rede, com exceção dos bias");
      }
      if(saida.length != rede.saida.neuronios.length){
         throw new IllegalArgumentException("O tamanho dos dados de saída não corresponde ao tamanho dos neurônios de saída da rede");
      }

      //calcular saída para aplicar o erro
      rede.calcularSaida(entrada);

      //transformar a rede num vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.entrada);
      for(Camada camada : rede.ocultas) redec.add(camada);
      redec.add(rede.saida);

      //erro da saída
      for(int i = 0; i < rede.saida.neuronios.length; i++){
         Neuronio neuronio = rede.saida.neuronios[i];
         neuronio.erro = ((saida[i] - neuronio.saida) * rede.saida.funcaoAtivacaoDx(neuronio.somatorio));
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


   /**
    * Otimizador usando gradiente descendente estocástico com momentum.
    * @param rede rede neural que será treinada.
    * @param entrada dados de entrada do treino.
    * @param saida dados de valores de saída correspondente aos valores de entrada.
    * @throws IllegalArgumentException se a quantidade de dados de entrada for diferente da quantidade de neurônios da camada de entrada, excluindo o bias.
    * @throws IllegalArgumentException se a quantidade de dados de saída for diferente da quantidade de neurônios da camada de saída.
    */
   static void sgdMomentum(RedeNeural rede, double[] entrada, double[] saida){
      if(entrada.length != (rede.entrada.neuronios.length - ((rede.entrada.temBias) ? 1 : 0) )){
         throw new IllegalArgumentException("O tamanho dos dados de entrada não corresponde ao tamanho dos neurônios de entrada da rede, com exceção dos bias");
      }
      if(saida.length != rede.saida.neuronios.length){
         throw new IllegalArgumentException("O tamanho dos dados de saída não corresponde ao tamanho dos neurônios de saída da rede");
      }

      //calcular saída para aplicar o erro
      rede.calcularSaida(entrada);

      //transformar a rede num vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.entrada);
      for(Camada camada : rede.ocultas) redec.add(camada);
      redec.add(rede.saida);

      //erro da saída
      for(int i = 0; i < rede.saida.neuronios.length; i++){
         Neuronio neuronio = rede.saida.neuronios[i];
         neuronio.erro = ((saida[i] - neuronio.saida) * rede.saida.funcaoAtivacaoDx(neuronio.somatorio));
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

      for(int i = 1; i < redec.size(); i++){//percorrer rede 
         
         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i-1);
         for(int j = 0; j < camadaAtual.neuronios.length; j++){//percorrer neurônios da camada atual
            
            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente = rede.obterTaxaAprendizagem() * neuronio.erro * camadaAnterior.neuronios[k].saida; 
               neuronio.momentum[k] = (rede.obterTaxaMomentum() * neuronio.momentum[k]) + neuronio.gradiente;
               neuronio.pesos[k] += neuronio.momentum[k];
            }
         }
      } 
   }
   

   /**
    * Treina uma rede neural utilizando o algoritmo Adagrad para atualização dos pesos.
    * @param rede rede neural que será treinada.
    * @param entrada dados de entrada do treino.
    * @param saida dados de valores de saída correspondente aos valores de entrada.
    * @throws IllegalArgumentException se a quantidade de dados de entrada for diferente da quantidade de neurônios da camada de entrada, excluindo o bias.
    * @throws IllegalArgumentException se a quantidade de dados de saída for diferente da quantidade de neurônios da camada de saída.
    */
   static void adagrad(RedeNeural rede, double[] entrada, double[] saida){
      if(entrada.length != (rede.entrada.neuronios.length - ((rede.entrada.temBias) ? 1 : 0) )){
         throw new IllegalArgumentException("O tamanho dos dados de entrada não corresponde ao tamanho dos neurônios de entrada da rede, com exceção dos bias");
      }
      if(saida.length != rede.saida.neuronios.length){
         throw new IllegalArgumentException("O tamanho dos dados de saída não corresponde ao tamanho dos neurônios de saída da rede");
      }
  
      //calcular saída para aplicar o erro
      rede.calcularSaida(entrada);
  
      //transformar a rede em um vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.entrada);
      for (Camada camada : rede.ocultas) redec.add(camada);
      redec.add(rede.saida);
  
      //erro da saída
      for(int i = 0; i < rede.saida.neuronios.length; i++){
         Neuronio neuronio = rede.saida.neuronios[i];
         neuronio.erro = ((saida[i] - neuronio.saida) * rede.saida.funcaoAtivacaoDx(neuronio.somatorio));
      }
  
      double somaErros = 0.0;
      // Começar da última oculta
      for(int i = redec.size() - 2; i >= 1; i--){//percorrer camadas ocultas de trás pra frente
  
         Camada camadaAtual = redec.get(i);
         int qNeuronioAtual = camadaAtual.neuronios.length;
         if (redec.get(i).temBias) qNeuronioAtual -= 1;
         for(int j = 0; j < qNeuronioAtual; j++){//percorrer neurônios da camada atual

            Neuronio neuronio = camadaAtual.neuronios[j];
            somaErros = 0.0;
            for (Neuronio neuronioProximo : redec.get(i + 1).neuronios) {//percorrer neurônios da camada seguinte
               somaErros += neuronioProximo.pesos[j] * neuronioProximo.erro;
            }
            neuronio.erro = somaErros * camadaAtual.funcaoAtivacaoDx(neuronio.somatorio);
         }
      }
  
      double epsilon = 1e-8;//evitar divisão por zero
      //atualizar pesos
      for(int i = 1; i < redec.size(); i++){//percorrer rede
  
         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i - 1);
         for(int j = 0; j < camadaAtual.neuronios.length; j++){//percorrer neurônios da camada atual
  
            Neuronio neuronio = camadaAtual.neuronios[j];
            for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
               neuronio.gradiente = neuronio.erro * camadaAnterior.neuronios[k].saida;
               neuronio.acumuladorGradiente[k] += neuronio.gradiente * neuronio.gradiente;
               neuronio.pesos[k] += (rede.obterTaxaAprendizagem() / Math.sqrt(neuronio.acumuladorGradiente[k] + epsilon)) * neuronio.gradiente;
            }
         }
      }
   }


   /**
    * Otimização dos pesos da rede neural utilizando o algoritmo Adam.
    * @param rede rede neural que será treinada.
    * @param entrada dados de entrada do treino.
    * @param saida dados de valores de saída correspondente aos valores de entrada.
    * @throws IllegalArgumentException se a quantidade de dados de entrada for diferente da quantidade de neurônios da camada de entrada, excluindo o bias.
    * @throws IllegalArgumentException se a quantidade de dados de saída for diferente da quantidade de neurônios da camada de saída.
    */
   static void adam(RedeNeural rede, double[] entrada, double[] saida){
      if(entrada.length != (rede.entrada.neuronios.length - ((rede.entrada.temBias) ? 1 : 0))){
         throw new IllegalArgumentException("O tamanho dos dados de entrada não corresponde ao tamanho dos neurônios de entrada da rede, com exceção dos bias");
      }
      if(saida.length != rede.saida.neuronios.length){
         throw new IllegalArgumentException("O tamanho dos dados de saída não corresponde ao tamanho dos neurônios de saída da rede");
      }

      //calcular saída para aplicar o erro
      rede.calcularSaida(entrada);

      //transformar a rede em um vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.entrada);
      for (Camada camada : rede.ocultas) redec.add(camada);
      redec.add(rede.saida);

      //erro da saída
      for(int i = 0; i < rede.saida.neuronios.length; i++){
         Neuronio neuronio = rede.saida.neuronios[i];
         neuronio.erro = ((saida[i] - neuronio.saida) * rede.saida.funcaoAtivacaoDx(neuronio.somatorio));
      }

      double somaErros = 0.0;
      //começar da última oculta
      for(int i = redec.size() - 2; i >= 1; i--){//percorrer camadas ocultas de trás pra frente

         Camada camadaAtual = redec.get(i);
         int qNeuronioAtual = camadaAtual.neuronios.length;
         if(redec.get(i).temBias) qNeuronioAtual -= 1;
         for(int j = 0; j < qNeuronioAtual; j++){//percorrer neurônios da camada atual

            Neuronio neuronio = camadaAtual.neuronios[j];
            somaErros = 0.0;
            for(Neuronio neuronioProximo : redec.get(i + 1).neuronios){//percorrer neurônios da camada seguinte
               somaErros += neuronioProximo.pesos[j] * neuronioProximo.erro;
            }
            neuronio.erro = somaErros * camadaAtual.funcaoAtivacaoDx(neuronio.somatorio);
         }
      }

      //acumuladores do momento e da segunda ordem
      double beta1 = 0.9; //fator de decaimento do momento
      double beta2 = 0.999; //fator de decaimento da segunda ordem
      double epsilon = 1e-8; //evitar divisão por zero
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
