package rna;

import java.util.ArrayList;

/**
 * Em teste ainda.
 */
public class Otimizadores{

   public static void backpropagation(RedeNeural rede, double[] entrada, double[] saida){
      rede.modeloValido();

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


   public static void backpropagationMomentum(RedeNeural rede, double[] entrada, double[] saida){
      rede.modeloValido();

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
   

   public static void adagrad(RedeNeural rede, double[] entrada, double[] saida){
      rede.modeloValido();
  
      if(entrada.length != (rede.entrada.neuronios.length - ((rede.entrada.temBias) ? 1 : 0) )){
         throw new IllegalArgumentException("O tamanho dos dados de entrada não corresponde ao tamanho dos neurônios de entrada da rede, com exceção dos bias");
      }
      if(saida.length != rede.saida.neuronios.length){
         throw new IllegalArgumentException("O tamanho dos dados de saída não corresponde ao tamanho dos neurônios de saída da rede");
      }
  
      // Calcular saída para aplicar o erro
      rede.calcularSaida(entrada);
  
      // Transformar a rede em um vetor de camadas pra facilitar minha vida
      ArrayList<Camada> redec = new ArrayList<>();
      redec.add(rede.entrada);
      for (Camada camada : rede.ocultas) redec.add(camada);
      redec.add(rede.saida);
  
      // Erro da saída
      for (int i = 0; i < rede.saida.neuronios.length; i++) {
         Neuronio neuronio = rede.saida.neuronios[i];
         neuronio.erro = ((saida[i] - neuronio.saida) * rede.saida.funcaoAtivacaoDx(neuronio.somatorio));
      }
  
      double somaErros = 0.0;
      // Começar da última oculta
      for(int i = redec.size() - 2; i >= 1; i--){// Percorrer camadas ocultas de trás pra frente
  
         Camada camadaAtual = redec.get(i);
         int qNeuronioAtual = camadaAtual.neuronios.length;
         if (redec.get(i).temBias) qNeuronioAtual -= 1;
         for (int j = 0; j < qNeuronioAtual; j++) {// Percorrer neurônios da camada atual

            Neuronio neuronio = camadaAtual.neuronios[j];
            somaErros = 0.0;
            for (Neuronio neuronioProximo : redec.get(i + 1).neuronios) {// Percorrer neurônios da camada seguinte
               somaErros += neuronioProximo.pesos[j] * neuronioProximo.erro;
            }
            neuronio.erro = somaErros * camadaAtual.funcaoAtivacaoDx(neuronio.somatorio);
         }
      }
  
      //atualizar pesos
      for(int i = 1; i < redec.size(); i++){//percorrer rede
  
         Camada camadaAtual = redec.get(i);
         Camada camadaAnterior = redec.get(i - 1);
         for(int j = 0; j < camadaAtual.neuronios.length; j++){//percorrer neurônios da camada atual
  
            Neuronio neuronio = camadaAtual.neuronios[j];
            for (int k = 0; k < neuronio.pesos.length; k++) {//percorrer pesos do neurônio atual
               neuronio.gradiente = neuronio.erro * camadaAnterior.neuronios[k].saida;
               neuronio.acumuladorGradiente[k] += neuronio.gradiente * neuronio.gradiente;
               neuronio.pesos[k] += (rede.obterTaxaAprendizagem() / Math.sqrt(neuronio.acumuladorGradiente[k] + 1e-8)) * neuronio.gradiente;
            }
         }
      }
   }
}
