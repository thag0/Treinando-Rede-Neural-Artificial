package rna;

import java.io.Serializable;
import java.util.Random;

/**
 * Representa um neurônio individual dentro da estrutura da rede neural.
 * Cada neurônio possui um conjunto de pesos para suas conexões com os neurônios da camada anterior,
 * além de valores de entrada, saída e alguns parâmetros adicionais para facilitar o uso dos otimizadores.
 */
public class Neuronio implements Serializable{
   public double[] entradas;//saídas da camada anterior
   public double[] pesos;
   public double[] momentum;//ajudar na convergência
   public double[] acumuladorGradiente;//adagrad
   public double[] acumuladorSegundaOrdem;//adam / rmsprop
   public double somatorio;//entradas * pesos
   public double saida;//resultado da ativação
   public double erro;//backpropagation
   public double[] gradiente;//backpropagation
   public double[] gradienteAcumulado;//treino lote

   private Random random = new Random();


   /**
    * Instancia um neurônio individual da rede, com pesos aleatórios para cada ligação.
    * <p>
    *    É necessário configurar o otimizador previamente, opções atualmente disponíveis:
    * </p>
    * <ul>
    *    <li>1 - Aleatória com valor dos pesos definido por {@code alcancePeso}.</li>
    *    <li>2 - He.</li>
    *    <li>3 - LeCun.</li>
    * </ul>
    * @param ligacoes quantidade de ligações, deve estar relacionada com a quatidade de neurônios da camada anterior.
    * @param alcancePeso valor de alcance em que o peso aleatório será gerado, deve ser um valor positivo e diferente de zero.
    * @param otimizador otimizador configurado.
    * @throws IllegalArgumentException se o valor de alcance dos pesos for menor ou igual a zero.
    * @throws IllegalArgumentException se o otimizador fornecido for inválido.
    */
   public Neuronio(int ligacoes, double alcancePeso, int otimizador){
      if (alcancePeso <= 0) throw new IllegalArgumentException("O valor de alcance do peso deve ser positivo e diferente de zero.");
   
      this.entradas = new double[ligacoes];
      this.momentum = new double[ligacoes];
      this.acumuladorGradiente = new double[ligacoes];
      this.acumuladorSegundaOrdem = new double[ligacoes];
      this.gradiente = new double[ligacoes];
      this.gradienteAcumulado = new double[ligacoes];

      this.pesos = new double[ligacoes];
      switch(otimizador){
         case 0: inicializacaoAleatoria(alcancePeso); break;
         case 1: inicializacaoHe(ligacoes); break;
         case 2: inicializacaoLeCun(ligacoes); break;

         default:
            throw new IllegalArgumentException("Otimizador fornecido para otimização dos pesos é inválido.");
      }
   
      this.saida = 1; // considerar que pode ter bias aplicado ao modelo
      this.erro = 0;
   }


   /**
    * Boa no geral
    * @param alcancePeso
    */
   private void inicializacaoAleatoria(double alcancePeso){
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextDouble(-alcancePeso, alcancePeso);
      }
   }


   /**
    * Boa com a relu.
    * @param alcancePeso
    */
   private void inicializacaoHe(int entradas){
      double desvioPadrao = Math.sqrt(2.0 / entradas);
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextGaussian() * desvioPadrao;
      }
   }


   /**
    * Boa com leakyRelu
    * @param entradas
    */
   private void inicializacaoLeCun(int entradas){
      double desvioPadrao = Math.sqrt(1.0 / entradas);
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextGaussian() * desvioPadrao;
      }
   }
  
}
