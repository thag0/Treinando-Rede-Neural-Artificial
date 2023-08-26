package rna;

import java.io.Serializable;
import java.util.Random;

/**
 * Representa um neurônio individual dentro da estrutura da rede neural.
 * Cada neurônio possui um conjunto de pesos para suas conexões com os neurônios da camada anterior,
 * além de valores de entrada, saída e alguns parâmetros adicionais para facilitar o uso dos otimizadores.
 */
public class Neuronio implements Serializable{

   /**
    * Saídas dos neurônios camada anterior.
    */
   public double[] entradas;

   /**
    * <p>Região crítica</p>
    * Representa os pesos responsáveis por associar a conexão recebida com a 
    * intensidade que ela deve receber.
    * Essa região é fundamental para o cálculo da saída de cada neurônio e
    * para a propagação da informação através da Rede Neural.
    */
   public double[] pesos;

   /**
    * Coeficientes de momentum para cada peso do neurônio.
    * <p>
    * Os coeficientes de momentum são usados nos algoritmos de otimização
    * para controlar a influência das atualizações anteriores de peso nas
    * atualizações atuais. Eles ajudam a evitar oscilações excessivas durante
    * o treinamento da Rede Neural e podem ajudar ela a escapar de mínimos locais.
    * </p>
    */
   public double[] momentum;

   /**
    * Auxiliar no treino usando otimizador AdaGrad.
    */
   public double[] acumuladorGradiente;

   /**
    * Auxiliar no treino da Rede Neural usando otimizadores RMSprop e Adam.
    */
   public double[] acumuladorSegundaOrdem;

   /**
    * Resultado do somatório das entradas multiplicadas pelos pesos do neurônio.
    */
   public double somatorio;

   /**
    * Resultado da função de ativação aplicada ao somatório do neurônio.
    */
   public double saida;

   /**
    * Auxiliar utilizado durante o cálculo dos erros dos neurônios
    * na etapa do Backpropagation.
    */
   public double erro;

   /**
    * Auxiliar usado pelos otimizadores para o ajuste dos pesos da
    * Rede Neural durante o treinamento.
    */
   public double[] gradiente;

   /**
    * Auxiliar usado durante o treinamento em lotes. Soma
    * os gradientes acumulados.
    */
   public double[] gradienteAcumulado;

   /**
    * Auxiliar no uso da aleatorização dos pesos iniciais.
    */
   private Random random = new Random();


   /**
    * Instancia um neurônio individual da rede, com pesos aleatórios para cada ligação.
    * <p>
    *    A quantidade de entradas dos neurônios corresponde as saídas dos neurônios da 
    *    camada anterior respectivamente.
    * </p>
    * <p>
    *    É necessário configurar o otimizador previamente, opções atualmente disponíveis:
    * </p>
    * <ul>
    *    <li>1 - Aleatória com valor dos pesos definido por {@code alcancePeso}.</li>
    *    <li>2 - He.</li>
    *    <li>3 - LeCun.</li>
    * </ul>
    * @param ligacoes quantidade de ligações, deve estar relacionada com a quatidade de neurônios da camada anterior (incluindo bias, caso tenha).
    * @param alcancePeso valor de alcance em que o peso aleatório será gerado, deve ser um valor positivo e diferente de zero.
    * @param inicializador algoritmo inicializador dos pesos.
    * @throws IllegalArgumentException se o valor de alcance dos pesos for menor ou igual a zero.
    * @throws IllegalArgumentException se o otimizador fornecido for inválido.
    */
   public Neuronio(int ligacoes, double alcancePeso, int inicializador){
      if (alcancePeso <= 0) throw new IllegalArgumentException("O valor de alcance do peso deve ser positivo e diferente de zero.");
   
      this.entradas = new double[ligacoes];
      this.momentum = new double[ligacoes];
      this.acumuladorGradiente = new double[ligacoes];
      this.acumuladorSegundaOrdem = new double[ligacoes];
      this.gradiente = new double[ligacoes];
      this.gradienteAcumulado = new double[ligacoes];

      this.pesos = new double[ligacoes];
      switch(inicializador){
         case 0 -> inicializacaoAleatoria(alcancePeso);
         case 1 -> inicializacaoHe(ligacoes);
         case 2 -> inicializacaoLeCun(ligacoes);
         default -> throw new IllegalArgumentException("Otimizador fornecido para otimização dos pesos é inválido.");
      }

      //só por segurança
      for(int i = 0; i < this.pesos.length; i++){
         this.entradas[i] = 0;
         this.momentum[i] = 0;
         this.acumuladorGradiente[i] = 0;
         this.acumuladorSegundaOrdem[i] = 0;
         this.gradiente[i] = 0;
         this.gradienteAcumulado[i] = 0;
      }
   
      this.saida = 1; //considerar que pode ter bias aplicado ao modelo
      this.erro = 0;
   }


   /**
    * Calcula o resultado do somatório da multiplicação entre cada entrada 
    * e seu peso respectivo. O resultado será usado como entrada para a função
    * de ativação.
    */
   public void calcularSomatorio(){
      this.somatorio = 0;
      for(int i = 0; i < this.entradas.length; i++){
         this.somatorio += (this.entradas[i] * this.pesos[i]);
      }
   }


   /**
    * Boa no geral.
    * @param alcancePeso valor máximo e mínimo na hora de aleatorizar os pesos.
    */
   private void inicializacaoAleatoria(double alcancePeso){
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextDouble(-alcancePeso, alcancePeso);
      }
   }


   /**
    * Boa com a relu.
    * @param entradas quantidade de entrada de cada neurônio da camada.
    */
   private void inicializacaoHe(int entradas){
      double desvioPadrao = Math.sqrt(2.0 / entradas);
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextGaussian() * desvioPadrao;
      }
   }


   /**
    * Boa com leakyRelu
    * @param entradas quantidade de entrada de cada neurônio da camada.
    */
   private void inicializacaoLeCun(int entradas){
      double desvioPadrao = Math.sqrt(1.0 / entradas);
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextGaussian() * desvioPadrao;
      }
   }


   /**
    * Retorna informações dos pesos do neurônio.
    * @return buffer formatado contendo as informações.
    */
   public String obterInformacoes(){
      String buffer = "";
      String espacamento = "    ";

      buffer += "Informações " + this.getClass().getSimpleName() + " = [\n";

      buffer += espacamento + "Quantidade de pesos: " + this.pesos.length + "\n\n";
      for(int i = 0; i < this.pesos.length; i++){
         buffer += espacamento + "p" + i + ": " + this.pesos[i] + "\n";
      }

      buffer += "]\n";

      return buffer;
   }
  
}
