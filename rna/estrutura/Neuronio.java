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
 *    alguns parâmetros adicionais para facilitar o uso dos otimizadores.
 * </p>
 * O neurônio oferece métodos de inicialização de pesos e cálculo do somatório
 * de seus pesos multiplicados pelas entradas. Métodos de funções de ativação
 * e treino do neurônio se encontram em outros componentes da Rede Neural.
 */
public class Neuronio{

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
    * Resultado da multiplicação entre os pesos e entradas com adição do bias (se houver).
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
    * Auxiliar utilizado durante o cálculo dos erros dos neurônios
    * na etapa do Backpropagation.
    */
   public double erro;

   /**
    * Vetor de gradientes do neurônio utilizado pelos otimizadores para 
    * ajustar os pesos da Rede Neural durante o treinamento.
    * <p>
    *    Cada elemento do vetor de gradientes {@code g[i]} corresponde ao 
    *    gradiente da conexão entre a entrada {@code en[i]} e o peso 
    *    correspondente {@code p[i]}.
    * </p>
    * <p>
    *    O gradiente de cada conexão do neurônio é dado por:
    * </p>
    * <pre>
    *    g[i] = -e * en[i]
    * </pre>
    * onde:
    * <p>
    *    g - vetor de gradientes do neurônio.
    * </p>
    * <p>
    * <p>
    *    e - erro do neurônio. 
    * </p>
    * <p>
    *    en - vetor de entradas do neurônio. 
    * </p>
    */
   public double[] gradiente;

   /**
    * Auxiliar usado durante o treinamento em lotes. Soma
    * os gradientes acumulados.
    */
   public double[] gradienteAcumulado;

   /**
    * Instancia um neurônio individual da rede.
    * <p>
    *    Os valores iniciais de pesos são dados como 0.
    * </p>
    * @param conexoes quantidade de conexões, deve estar relacionada com a quatidade de 
    * neurônios da camada anterior (incluindo bias, caso tenha).
    */
   public Neuronio(int conexoes){
      this.pesos = new double[conexoes];
      this.entradas = new double[conexoes];
      this.gradiente = new double[conexoes];
      this.gradienteAcumulado = new double[conexoes];
   
      //considerar que pode ter bias aplicado ao modelo
      //a saída do bias é sempre 1.
      this.saida = 1;
      this.erro = 0;
   }
    
   /**
    * Inicialza os pesos do neurônio baseado no otimizador fornecido.
     * @param inicializador inicializador usado na geração dos valores iniciais de pesos.
     * @param alcanceInicial alcance inicial de aleatorização para alguns inicializadores.
     * @param tamSaida tamanho da saída da camada em que o neurônio está inserido.
     * @throws IllegalArgumentException se o inicializdor for nulo ou não suportado.
    */
   public void inicializarPesos(Inicializador inicializador, double alcanceInicial, int tamSaida){
      if(inicializador == null){
         throw new IllegalArgumentException("O inicializador não pode ser nulo.");
      }

      if(inicializador instanceof Aleatorio || inicializador instanceof AleatorioPositivo){
         inicializador.inicializar(this.pesos, alcanceInicial);
      
      }else if(inicializador instanceof He || inicializador instanceof LeCun){
         inicializador.inicializar(this.pesos, this.pesos.length);

      }else if(inicializador instanceof Xavier){
         inicializador.inicializar(this.pesos, this.pesos.length, tamSaida);
      
      }else{
         throw new IllegalArgumentException("Inicializador não suportado");
      }
   }

   /**
    * Calcula o resultado do somatório da multiplicação entre os elementos do
    * array de entradas pelo array de pesos. O resultado será usado como entrada 
    * para a função de ativação.
    * <p>
    *    O algoritmo padrão do feedforward se baseia em fazer o somatório dos 
    *    produtos entre a entrada com o peso respectivo e ao final adicionar o bias. 
    *    Como o neurônio do bias sempre tem saída igual a 1, dá pra generalizar num
    *    loop só.
    * </p>
    */
   public void somatorio(){
      this.somatorio = 0;
      for(int i = 0; i < this.entradas.length; i++){
         this.somatorio += this.entradas[i] * this.pesos[i];
      }
   }

   /**
    * Calcula o gradiente de cada peso do neurônio usando a expressão:
    * <pre>
    *    g[i] = -e * en[i]
    * </pre>
    * onde:
    * <p>
    *    g - vetor de gradientes do neurônio.
    * </p>
    * <p>
    *    e - erro do neurônio. 
    * </p>
    * <p>
    *    en - vetor de entradas do neurônio. 
    * </p>
    * <strong>Observação</strong>: em alguns lugares o erro se calcula como 
    * {@code y-p} (onde {@code y} é o dado real e {@code p} é o dado previsto), 
    * mas também encontrei outros lugares onde o erro é calculado como {@code p-y},
    * para padronizar decidi que o erro é calculado como {@code y-p} e multiplicar o valor
    * do gradiente por -1, assim faz mais sentido o termo "gradiente descendente" onde é 
    * usado o oposto do gradiente para minimizar a perda da rede.
    * <p>
    *    Além de que essa abordagem de {@code p-y} não estava funcionando para calcular
    *    o erro dos neurônio quando a camada de saída de rede usava a função argmax então
    *    acabei sendo um pouco forçado em deixar dessa forma.
    * </p>
    */
   public void calcularGradiente(){
      int numPesos = this.pesos.length;
      for(int i = 0; i < numPesos; i++){
         this.gradiente[i] = -this.erro * this.entradas[i];
      }
   }

   /**
    * Retorna a quantidade de conexões presentes (incluindo a do bias).
    * @return quantidade de conexões presentes totais.
    */
   public int numConexoes(){
      return this.pesos.length;
   }

   /**
    * Retorna informações dos pesos do neurônio.
    * @return buffer formatado contendo as informações.
    */
   public String info(){
      String buffer = "";
      String espacamento = "    ";

      buffer += "Informações " + this.getClass().getSimpleName() + " = [\n";

      buffer += espacamento + "Quantidade de pesos: " + this.numConexoes() + "\n\n";
      for(int i = 0; i < this.pesos.length; i++){
         buffer += espacamento + "p" + i + ": " + this.pesos[i] + "\n";
      }

      buffer += "]\n";

      return buffer;
   } 
}
