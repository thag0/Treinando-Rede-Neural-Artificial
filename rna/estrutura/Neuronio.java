package rna.estrutura;

import rna.inicializadores.Aleatorio;
import rna.inicializadores.AleatorioPositivo;
import rna.inicializadores.He;
import rna.inicializadores.Inicializador;
import rna.inicializadores.LeCun;
import rna.inicializadores.Xavier;

/**
 * Representa um neurônio individual dentro da estrutura da Rede Neural.
 * <p>
 *    Cada neurônio possui um conjunto de pesos para suas conexões com os 
 *    neurônios da camada anterior, além de valores de entrada, saída e 
 *    alguns parâmetros adicionais para facilitar o manuseio de durante 
 *    o treinamento.
 * </p>
 * O neurônio oferece métodos de inicialização de pesos e cálculo do somatório
 * de seus pesos multiplicados pelas entradas. Métodos de funções de ativação
 * e treino do neurônio se encontram em outros componentes da Rede Neural.
 * <p>
 *    Exemplificação de organização da estrutura no neurônio:
 * </p>
 * <pre>
 * neuronio = [
 *    e0, p0,
 *    e1, p1,
 *    e2, p2,
 *    1 , pb
 * ]
 * </pre>
 */
public class Neuronio implements Cloneable{

   /**
    * Dados de entrada que serão processados pelo neurônio.
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
    * Resultado do produto entre os dados de entrada com os pesos do neurônio com 
    * a adição do bias (se houver).
    */
   public double somatorio;

   /**
    * Resultado da função de ativação aplicada ao somatório do neurônio.
    */
   public double saida;

   /**
    * Resultado da derivada da função de ativação aplicada ao somatório do neurônio.
    */
   public double derivada;

   /**
    * Auxiliar utilizado durante o treinamento do neurônio na etapa de backpropagation.
    */
   public double gradiente;

   /**
    * Vetor de gradientes do neurônio utilizado pelos otimizadores para 
    * ajustar os pesos da Rede Neural durante o treinamento.
    * <p>
    *    Cada elemento do vetor de gradientes {@code g[i]} corresponde ao 
    *    gradiente da conexão entre a entrada {@code en[i]} e o peso 
    *    correspondente {@code p[i]}.
    * </p>
    */
   public double[] gradientes;

   /**
    * Auxiliar usado durante o treinamento em lotes. Soma
    * os gradientes acumulados.
    */
   public double[] gradientesAcumulados;

   /**
    * Constante auxiliar que ajuda no controle do bias atuando como
    * entrada e pesos adicionais para o neurônio.
    */
   private boolean bias = true;

   /**
    * Instancia um neurônio artificial.
    * <p>
    *    Os valores iniciais de pesos são dados como 0.
    * </p>
    * @param entrada capacidade dos dados de entrada do neurônio.
    * @param bias aplicar víes ao neurônio.
    */
   public Neuronio(int entrada, boolean bias){
      this.bias = bias;
      int conexoes = entrada + ((bias) ? 1 : 0);

      this.pesos = new double[conexoes];
      this.entradas = new double[conexoes];
      this.gradientes = new double[conexoes];
      this.gradientesAcumulados = new double[conexoes];

      this.saida = 0;
      this.gradiente = 0;

      //entrada do bias
      if(bias){
         this.entradas[this.entradas.length-1] = 1;
      }
   }

   /**
    * Instancia um neurônio artificial.
    * <p>
    *    Os valores iniciais de pesos são dados como 0 e o valor do 
    *    bias é usado como verdadeiro por padrão.
    * </p>
    * @param entrada capacidade dos dados de entrada do neurônio.
    */
   public Neuronio(int entrada){
      this(entrada, true);
   }
    
   /**
    * Inicialza os pesos do neurônio baseado no otimizador fornecido.
    * @param inicializador inicializador usado na geração dos valores iniciais de pesos.
    * @param alcance alcance inicial de aleatorização para alguns inicializadores.
    * @param tamSaida tamanho da saída da camada em que o neurônio está inserido.
    * @throws IllegalArgumentException se o inicializdor for nulo ou não suportado.
    */
   public void inicializarPesos(Inicializador inicializador, double alcance, int tamSaida){
      if(inicializador == null){
         throw new IllegalArgumentException("O inicializador não pode ser nulo.");
      }

      if(inicializador instanceof Aleatorio || inicializador instanceof AleatorioPositivo){
         inicializador.inicializar(this.pesos, alcance);
      
      }else if(inicializador instanceof He || inicializador instanceof LeCun){
         inicializador.inicializar(this.pesos, this.pesos.length);

      }else if(inicializador instanceof Xavier){
         inicializador.inicializar(this.pesos, this.pesos.length, tamSaida);
      
      }else{
         throw new IllegalArgumentException(
            "Inicializador (" + inicializador.getClass().getSimpleName() + 
            ") não suportado"
         );
      }

      //tentar usar um valor pequeno pra ter menos influência no começo
      if(this.bias){
         // this.pesos[this.pesos.length-1] = 0.5;
         this.pesos[this.pesos.length-1] = inicializador.gerarDouble() / 2;
      }
   }

   /**
    * Calcula o resultado do somatório da multiplicação entre os elementos do
    * array de entradas pelo array de pesos. O resultado será usado como entrada 
    * para a função de ativação.
    * <p>
    *    Os dados devem ser carregados para a entrada previamente.
    * </p>
    * <p>
    *    O algoritmo padrão do feedforward se baseia em fazer o somatório dos 
    *    produtos entre a entrada com o peso respectivo e ao final adicionar o bias. 
    *    Nesse modelo de arquitetura o bias é um parâmetro adicional nas entradas dos
    *    neurônios e possui valor de saída sempre igual a 1, então é possível generalizar
    *    num único loop.
    * </p>
    * @param entrada array com os dados de entrada para alimentar o neurônio.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente
    * da capacidade de entrada do neurônio.
    */
   public void calcularSaida(double[] entrada){
      if(this.tamanhoEntrada() != entrada.length){
         throw new IllegalArgumentException(
            "Incompatibilidade de tamanho entre os dados de entrada (" + entrada.length +
            ") e a entrada do neurônio (" + this.tamanhoEntrada() + ")."
         );
      }

      //esse método de cópia ta sendo mais eficiente
      //do que usar o system.arraycopy
      for(int i = 0; i < this.tamanhoEntrada(); i++){
         this.entradas[i] = entrada[i];
      }

      this.somatorio = 0;
      for(int i = 0; i < this.entradas.length; i++){
         this.somatorio += this.entradas[i] * this.pesos[i];
      }
   }

   /**
    * Retorna a quantidade de conexões presentes (incluindo a do bias).
    * @return quantidade de conexões presentes totais.
    */
   public int numPesos(){
      return this.pesos.length;
   }

   /**
    * Indica o tamanho de entrada do neurônio.
    * @return capacidade de entrada do neurônio.
    */
   public int tamanhoEntrada(){
      return this.entradas.length - ((this.bias) ? 1 : 0);
   }

   /**
    * Configura os pesos do neurônio de acordo com o novo valor
    * contido no array de pesos fornecido.
    * @param pesos novos valores de pesos para o neurônio.
    * @throws IllegalArgumentException se o tamanho do vetor de pesos fornecido for
    * diferente do tamanho de pesos suportado pelo neurônio.
    */
   public void configurarPesos(double[] pesos){
      if(pesos.length != this.tamanhoEntrada()){
         throw new IllegalArgumentException(
            "A quantidade de pesos fornecida (" + pesos.length + 
            ") é diferente da quantidade de pesos do neurônio (" + this.tamanhoEntrada() + ")"
         );
      }

      System.arraycopy(pesos, 0, this.pesos, 0, pesos.length);
   }

   /**
    * Configura o novo valor de bias para o neurônio.
    * @param bias novo valore de bias (viés) para o neurônio.
    * @throws IllegalArgumentException se o neurônio não foi configurado para ter
    * suporte ao bias.
    */
   public void configurarBias(double bias){
      if(!this.bias){
         throw new IllegalArgumentException(
            "O neurônio não foi configurado para suportar um bias (viés)."
         );
      }

      this.pesos[this.pesos.length-1] = bias;
   }

   /**
    * Retorna informações dos pesos do neurônio.
    * @return buffer formatado contendo as informações.
    */
   public String info(){
      String buffer = "";
      String espacamento = "    ";

      buffer += "Informações " + this.getClass().getSimpleName() + " = [\n";
      buffer += espacamento + "Bias: " + this.bias + "\n";

      buffer += espacamento + "Quantidade de pesos: " + this.numPesos() + "\n\n";
      for(int i = 0; i < this.pesos.length; i++){
         if(this.bias && i == this.pesos.length-1){
            buffer += espacamento + "pb: " + this.pesos[i] + "\n";

         }else{
            buffer += espacamento + "p" + i + ": " + this.pesos[i] + "\n";
         }
      }

      buffer += "]\n";

      return buffer;
   }

   /**
    * Clona a instância do neurônio, criando um novo objeto com as 
    * mesmas características mas em outro espaço de memória.
    * @return clone do neurônio.
    */
   @Override
   public Neuronio clone(){
      try{
         Neuronio clone = (Neuronio) super.clone();
         clone.bias = this.bias;

         //considerar a entrada do bias
         clone.entradas = new double[this.entradas.length];
         clone.pesos = new double[this.pesos.length];
         for(int i = 0; i < this.pesos.length; i++){
            clone.pesos[i] = this.pesos[i];
            clone.entradas[i] = this.entradas[i];
         }

         return clone;
      }catch(Exception e){
         throw new RuntimeException(e);
      }
   }
}
