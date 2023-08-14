package rna.treinamento;

import java.util.ArrayList;
import java.util.Random;

import rna.Camada;
import rna.Neuronio;

/**
 * Operadores auxiliares para o treino da rede neural;
 */
class AuxiliaresTreino{
      Random random = new Random();

   public AuxiliaresTreino(){

   }


   /**
    * Método exclusivo para separar a forma de calcular os erros da camada de saída.
    * Dando suporte não apenas para problemas de regressão.
    * <p>
    *    Isso ainda ta em teste para problemas de classificação, para regressão funciona normalmente.
    * </p>
    * @param redec Rede Neural em formato de lista de camadas.
    * @param saidas array com as saídas esperadas
    */
   public void calcularErroSaida(ArrayList<Camada> redec, double[] saidas){
      Camada saida = redec.get(redec.size()-1);
      
      if(saida.argmax){//classificação
         int indiceMaior = indiceMaiorValor(saidas);
         for(int i = 0; i < saida.obterQuantidadeNeuronios(); i++){
            Neuronio neuronio = saida.neuronios[i];
            if(i == indiceMaior) neuronio.erro = 1 - neuronio.saida;
            else neuronio.erro = 0 - neuronio.saida;
         }

      }else if(saida.softmax){//classificação
         for(int i = 0; i < saida.neuronios.length; i++){
            Neuronio neuronio = saida.neuronios[i];
            neuronio.erro = (saidas[i] - neuronio.saida);
         }
      
      }else{//regressão
         for(int i = 0; i < saida.neuronios.length; i++){
            Neuronio neuronio = saida.neuronios[i];
            neuronio.erro = ((saidas[i] - neuronio.saida) * saida.funcaoAtivacaoDx(neuronio.somatorio));
         }
      }
   }


   /**
    * Método exclusivo para separar a forma de calcular os erros das camadas ocultas
    * da rede neural.
    * @param redec Rede Neural em formato de lista de camadas.
    */
   public void calcularErroOcultas(ArrayList<Camada> redec){
      //começar da ultima oculta
      for(int i = redec.size()-2; i >= 1; i--){// percorrer camadas ocultas de trás pra frente
         
         Camada camadaAtual = redec.get(i);
         int qNeuronioAtual = camadaAtual.obterQuantidadeNeuronios();
         if(camadaAtual.temBias) qNeuronioAtual -= 1;
         for (int j = 0; j < qNeuronioAtual; j++){//percorrer neurônios da camada atual
         
            Neuronio neuronio = camadaAtual.neuronios[j];
            double somaErros = 0.0;
            for(Neuronio neuronioProximo : redec.get(i+1).neuronios){//percorrer neurônios da camada seguinte
               somaErros += neuronioProximo.pesos[j] * neuronioProximo.erro;
            }
            neuronio.erro = somaErros * camadaAtual.funcaoAtivacaoDx(neuronio.somatorio);
         }
      }
   }


   /**
    * Embaralha os dados da matriz usando o algoritmo Fisher-Yates.
    * @param entradas matriz com os dados de entrada.
    * @param saidas matriz com os dados de saída.
    */
   public void embaralharDados(double[][] entradas, double[][] saidas){
      int linhas = entradas.length;
  
      //evitar muitas inicializações
      double tempDados[];
      double tempSaidas[];
      int i, indiceAleatorio;

      for(i = linhas - 1; i > 0; i--){
         indiceAleatorio = random.nextInt(i + 1);
  
         tempDados = entradas[i];
         entradas[i] = entradas[indiceAleatorio];
         entradas[indiceAleatorio] = tempDados;

         tempSaidas = saidas[i];
         saidas[i] = saidas[indiceAleatorio];
         saidas[indiceAleatorio] = tempSaidas;
      }
   }

   /**
    * Dedicado para treino em lote e multithread em implementações futuras.
    * @param dados conjunto de dados completo.
    * @param inicio índice de inicio do lote.
    * @param fim índice final do lote.
    * @return lote contendo os dados de acordo com os índices fornecidos.
    */
   public double[][] obterSubMatriz(double[][] dados, int inicio, int fim){
      if(inicio < 0 || fim > dados.length || inicio >= fim){
         throw new IllegalArgumentException("Índices de início ou fim inválidos.");
      }

      int linhas = fim - inicio;
      int colunas = dados[0].length;
      double[][] subMatriz = new double[linhas][colunas];

      for(int i = 0; i < linhas; i++){
         System.arraycopy(dados[inicio + i], 0, subMatriz[i], 0, colunas);
      }

      return subMatriz;
   }


   /**
    * Encontra o índice com o maior valor contido no array fornecido
    * @param dados array contendo os dados
    * @return índice com o maior valor contido nos dados.
    */
   public int indiceMaiorValor(double[] dados){
      int indiceMaiorValor = 0;
      double maiorValor = dados[0];
  
      for(int i = 1; i < dados.length; i++){
         if (dados[i] > maiorValor) {
            maiorValor = dados[i];
            indiceMaiorValor = i;
         }
      }
  
      return indiceMaiorValor;
   }


   /**
    * Zera todos os gradientes para o cálculo do gradiente em lote.
    * @param redec
    */
   public void zerarGradientesAcumulados(ArrayList<Camada> redec){
         for(int i = 1; i < redec.size(); i++){ 
            
            Camada camadaAtual = redec.get(i);
            int nNeuronios = camadaAtual.obterQuantidadeNeuronios();
            nNeuronios -= (camadaAtual.temBias) ? 1 : 0;
            for(int j = 0; j < nNeuronios; j++){//percorrer neurônios da camada atual
               
               Neuronio neuronio = camadaAtual.neuronios[j];
               for(int k = 0; k < neuronio.pesos.length; k++){//percorrer pesos do neurônio atual
                  neuronio.gradienteAcumulado[k] = 0;
               }
            }
         }
   }
}
