package rna.estrutura;

import java.io.Serializable;
import java.util.Random;

/**
 * Representa um neurônio individual dentro da estrutura da Rede Neural.
 * <p>
 *    Cada neurônio possui um conjunto de pesos para suas conexões com os 
 *    neurônios da camada anterior, além de valores de entrada, saída e 
 *    alguns parâmetros adicionais para facilitar o uso dos otimizadores.
 * </p>
 * O neurônio oferece métodos de inicialização de pesos e cálculo do somatório
 * de seus pesos multiplicados pelas entradas. Métodos de funções de ativação
 * e treino do neurônio se encontram em outros componentes da Rede Neural.
 */
public class Neuronio implements Serializable{

   /**
    * Array que representa às saídas dos neurônios da camada anterior.
    */
   public double[] entradas;

   /**
    * <p>
    *    Região crítica
    * </p>
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
    * Auxiliar no treino da Rede Neural usando otimizadores RMSprop e Adam.
    */
   public double[] momentum2ordem;
   
   /**
    * Auxiliar no treino usando otimizador AdaGrad.
    */
   public double[] acumuladorGradiente;


   /**
    * Resultado do somatório das entradas multiplicadas pelos pesos do neurônio.
    */
   public double somatorio;

   /**
    * Resultado da função de ativação aplicada ao somatório do neurônio.
    */
   public double saida;

   /**
    * Auxiliar no treino, deve conter o resultado da função de 
    * ativação derivada.
    */
   public double derivada;

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
    *    <li>
    *       1 - Inicialização aleatória com valor inicial dos pesos definido por 
    *       {@code alcancePeso}, onde o valor de aleatorização dos pesos é definido 
    *       por {@code -alcancePeso : alcancePeso}.
    *    </li>
    *    <li>
    *       2 - Inicialização aleatória positiva com valor dos pesos definido por {@code alcancePeso}.
    *       Sua diferença para a aleatória comum é que na aleatória positiva o valor de 
    *       aleatorização é definido por {@code 0 : alcancePeso}.
    *    </li>
    *    <li>
    *       3 - He - Boa com ReLU e suas derivadas.
    *    </li>
    *    <li>
    *       4 - LeCun.
    *    </li>
    * </ul>
    * @param conexoes quantidade de conexões, deve estar relacionada com a quatidade de 
    * neurônios da camada anterior (incluindo bias, caso tenha).
    * @param alcancePeso valor de alcance em que o peso aleatório será gerado, deve ser 
    * um valor positivo e diferente de zero.
    * @param inicializador algoritmo inicializador dos pesos.
    * @throws IllegalArgumentException se o valor de alcance dos pesos for menor ou igual 
    * a zero.
    * @throws IllegalArgumentException se o otimizador fornecido for inválido.
    */
   public Neuronio(int conexoes, double alcancePeso, int inicializador){
      if (alcancePeso <= 0){
         throw new IllegalArgumentException("O valor de alcance do peso deve ser positivo e diferente de zero.");
      }

      this.pesos = new double[conexoes];
      switch(inicializador){
         case 1 -> aleatoria(alcancePeso);
         case 2 -> aleatoriaPositiva(alcancePeso);
         case 3 -> he(conexoes);
         case 4 -> leCun(conexoes);
         default -> throw new IllegalArgumentException("Otimizador fornecido para otimização dos pesos é inválido.");
      }

      this.entradas = new double[conexoes];
      this.momentum = new double[conexoes];
      this.momentum2ordem = new double[conexoes];
      this.gradiente = new double[conexoes];
      this.acumuladorGradiente = new double[conexoes];
      this.gradienteAcumulado = new double[conexoes];

      //só por segurança
      for(int i = 0; i < this.pesos.length; i++){
         this.entradas[i] = 0;
         this.momentum[i] = 0;
         this.momentum2ordem[i] = 0;
         this.gradiente[i] = 0;
         this.acumuladorGradiente[i] = 0;
         this.gradienteAcumulado[i] = 0;
      }
   
      this.saida = 1;//considerar que pode ter bias aplicado ao modelo
      this.erro = 0;
   }

   /**
    * Calcula o resultado do somatório da multiplicação entre os elementos do
    * array de entradas pelo array de pesos. O resultado será usado como entrada 
    * para a função de ativação.
    */
   public void somatorio(){
      this.somatorio = 0;
      for(int i = 0; i < this.entradas.length; i++){
         this.somatorio += this.entradas[i] * this.pesos[i];
      }
   }

   /**
    * Boa no geral.
    * @param alcance valor máximo e mínimo na hora de aleatorizar os pesos.
    */
   private void aleatoria(double alcance){
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextDouble(-alcance, alcance);
      }
   }

   /**
    * Boa no geral.
    * @param alcance valor máximo e mínimo na hora de aleatorizar os pesos.
    */
   private void aleatoriaPositiva(double alcance){
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextDouble(0, alcance);
      }
   }

   /**
    *
    * @param entradas quantidade de entrada de cada neurônio da camada.
    */
   private void he(int entradas){
      double desvioPadrao = Math.sqrt(2.0 / entradas);
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextGaussian() * desvioPadrao;
      }
   }

   /**
    *
    * @param entradas quantidade de entrada de cada neurônio da camada.
    */
   private void leCun(int entradas){
      double desvioPadrao = Math.sqrt(1.0 / (entradas + entradas));
      for(int i = 0; i < pesos.length; i++){
         this.pesos[i] = random.nextGaussian() * desvioPadrao;
      }
   }
  

   /**
    * Retorna informações dos pesos do neurônio.
    * @return buffer formatado contendo as informações.
    */
   public String info(){
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
