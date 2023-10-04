package rna.estrutura;

import java.util.ArrayList;

import rna.ativacoes.FuncaoAtivacao;
import rna.avaliacao.Avaliador;
import rna.avaliacao.perda.ErroMedioQuadrado;
import rna.avaliacao.perda.Perda;
import rna.inicializadores.Aleatorio;
import rna.inicializadores.Inicializador;
import rna.otimizadores.Otimizador;
import rna.otimizadores.SGD;

import rna.treinamento.Treinador;

//TODO
//Implementar formas melhores de treinar com uma grande quantidade de dados (mais necessário);
//novos otimizadores;
//Mais opções de métricas e funções de perda;
//Poder configurar funções de ativação antes de compilar o modelo;
//Poder flexibilizar o tipo da camada para usar numa rede convolucional

/**
 * Modelo de Rede Neural {@code Multilayer Perceptron} criado do zero. Possui um conjunto de camadas 
 * densas em que cada uma delas por sua vez possui um conjunto de neurônios artificiais.
 * <p>
 *    O modelo pode ser usado tanto para problemas de {@code regressão e classificação}, contando com 
 *    algoritmos de treino e otimizadores variados para ajudar na convergência e desempenho da rede para 
 *    problemas diversos.
 * </p>
 * <p>
 *    Possui opções de configuração para funções de ativações de camadas individuais, valor de alcance 
 *    máximo e mínimo na aleatorização dos pesos iniciais, inicializadores de pesos e otimizadores que 
 *    serão usados durante o treino. 
 * </p>
 * <p>
 *    Após configurar as propriedades da rede, o modelo precisará ser {@code compilado} para efetivamente 
 *    poder ser utilizado.
 * </p>
 * <p>
 *    As predições do modelo são feitas usando o método de {@code calcularSaida()} onde é especificada
 *    uma única amostra de dados ou uma seqûencia de amostras onde é retornado o resultado de predição
 *    da rede.
 * </p>
 * <p>
 *    Opções de avaliação e desempenho do modelo podem ser acessadas através do {@code avaliador} da
 *    Rede Neural, que contém implementação de funções de perda e métricas para o modelo.
 * </p>
 * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, 
 * Campus Tucuruí. Maio/2023.
 */
public class RedeNeural implements Cloneable{

   /**
    * Região crítica
    * <p>
    *    Conjunto de camadas densas (ou fully connected) da Rede Neural.
    * </p>
    */
   private Camada[] camadas;

   /**
    * Array contendo a arquitetura de cada camada dentro da Rede Neural.
    * <p>
    *    Cada elemento da arquitetura representa a quantidade de neurônios 
    *    presente na camada correspondente.
    * </p>
    * <p>
    *    A "camada de entrada" não é considerada camada, pois não é alocada na
    *    rede, ela serve apenas de parâmetro para o tamanho de entrada da primeira
    *    camada densa da Rede Neural.
    * </p>
    */
   private int[] arquitetura;

   /**
    * Constante auxiliar que ajuda no controle do bias dentro da rede.
    */
   private boolean bias = true;

   /**
    * Valor máximo e mínimo na hora de aleatorizar os pesos da rede neural, para
    * alguns inicializadores.
    */
   private double alcancePeso = 0.5;

   /**
    * Ponto inicial para os geradores aleatórios
    */
   private long seedInicial = 0;
    
   /**
    * Auxiliar no controle da compilação da Rede Neural, ajuda a evitar uso 
    * indevido caso a rede não tenha suas variáveis e dependências inicializadas 
    * previamente.
    */
   private boolean compilado = false;

   /**
    * Função de perda usada durante o processo de treinamento.
    */
   private Perda perdaAtual;

   /**
    * Otimizador que será utilizado durante o processo de aprendizagem da
    * da Rede Neural.
    */
   private Otimizador otimizadorAtual;

   /**
    * Nome específico da instância da Rede Neural.
    */
   private String nome = getClass().getSimpleName();

   /**
    * Gerenciador de treino da Rede Neural. contém implementações dos 
    * algoritmos de treino.
    */
   private Treinador treinador = new Treinador();

   /**
    * Responsável pelo retorno de desempenho da Rede Neural.
    * Contém implementações de métodos tanto para cálculo de perdas
    * quanto de métricas.
    * <p>
    *    Cada instância de rede neural possui seu próprio avaliador.
    * </p>
    */
   public Avaliador avaliador = new Avaliador(this);

   /**
    * <p>
    *    Cria uma instância de rede neural artificial. A arquitetura da rede será baseada de acordo 
    *    com cada posição do array, cada valor contido nele representará a quantidade de neurônios da 
    *    camada correspondente.
    * </p> 
    * <p>
    *   Nenhum dos elementos de arquitetura deve ser menor do que 1.
    * </p>
    * <p>
    *    Exemplo de uso:
    * </p>
    * <pre>
    * int[] arq = {
    *    1, //tamanho de entrada da rede
    *    2, //neurônios da primeira camada
    *    3  //neurônios da segunda camada
    * };
    * </pre>
    * <p>
    *    É obrigatório que a arquitetura tenha no mínimo dois elementos, um para a entrada e outro
    *    para a saída da Rede Neural.
    * </p>
    * <p>
    *    Após instanciar o modelo, é necessário compilar por meio da função {@code compilar()};
    * </p>
    * <p>
    *    Certifique-se de configurar as propriedades da rede por meio das funções de configuração fornecidas 
    *    para obter os melhores resultados na aplicação específica. Caso não seja usada nenhuma das funções de 
    *    configuração, a rede será compilada com os valores padrão.
    * </p>
    * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, 
    * Campus Tucuruí. Maio/2023.
    * @param arquitetura modelo de arquitetura específico da rede.
    * @throws IllegalArgumentException se o array de arquitetura for nulo.
    * @throws IllegalArgumentException se o array de arquitetura não possuir, pelo menos, dois elementos.
    * @throws IllegalArgumentException se os valores fornecidos forem menores que um.
    */
   public RedeNeural(int[] arquitetura){
      if(arquitetura == null){
         throw new IllegalArgumentException("A arquitetura fornecida não deve ser nula.");
      }
      if(arquitetura.length < 2){
         throw new IllegalArgumentException(
            "A arquitetura fornecida deve conter no mínimo dois elementos (entrada e saída), tamanho recebido = " + arquitetura.length
         );
      }
      for(int i = 0; i < arquitetura.length; i++){
         if(arquitetura[i] < 1){
            throw new IllegalArgumentException(
               "Os valores de arquitetura fornecidos não devem ser menores do que um."
            );
         }
      }

      this.arquitetura = arquitetura;
   }

   /**
    * <p>
    *    Cria uma instância de rede neural artificial. A arquitetura da rede será baseada 
    *    de acordo com os valores de número de neurônios fornecidos.
    * </p>
    * <p>
    *   Os valores de todos os parâmetros pedidos <strong>NÃO devem</strong> ser menores que 1.
    * </p>
    * <p>
    *    Após instanciar o modelo, é necessário compilar por meio da função {@code compilar()}
    * </p>
    * <p>
    *    Certifique-se de configurar as propriedades da rede por meio das funções de configuração fornecidas 
    *    para obter os melhores resultados na aplicação específica. Caso não seja usada nenhuma das funções de 
    *    configuração, a rede será compilada com os valores padrão.
    * </p>
    * @param nEntrada número de neurônios da camada de entrada.
    * @param nOcultas número de neurônios das camadas ocultas.
    * @param nSaida número de neurônios da camada de saída.
    * @param qOcultas quantidade de camadas ocultas.
    * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, 
    * Campus Tucuruí. Maio/2023.
    * @throws IllegalArgumentException se algum dos valores fornecidos for menor que 1.
    */
   public RedeNeural(int nEntrada, int nOcultas, int nSaida, int qOcultas){
      if(nEntrada < 1 || nOcultas < 1 || nSaida < 1 || qOcultas < 1){
         throw new IllegalArgumentException("Os valores fornecidos devem ser maiores ou iguais a 1.");
      }

      this.arquitetura = new int[1 + qOcultas + 1];

      this.arquitetura[0] = nEntrada;
      for(int i = 1; i < this.arquitetura.length-1; i++){
         this.arquitetura[i] = nOcultas;
      }
      this.arquitetura[this.arquitetura.length-1] = nSaida;
   }

   /**
    * <p>
    *    Altera o nome da rede neural.
    * </p>
    * O nome é apenas estético e não influencia na performance ou na 
    * usabilidade da rede neural.
    * <p>
    *    O nome padrão é o mesmo nome da classe (RedeNeural).
    * </p>
    * @param nome novo nome da rede.
    * @throws IllegalArgumentException se o novo nome for uulo ou inválido.
    */
   public void configurarNome(String nome){
      if(nome == null){
         throw new IllegalArgumentException("O novo nome da rede neural não pode ser nulo.");
      }
      if(nome.isBlank() || nome.isEmpty()){
         throw new IllegalArgumentException("O novo nome da rede neural não pode estar vazio.");
      }

      this.nome = nome;
   }

   /**
    * Define o valor máximo e mínimo na hora de aleatorizar os pesos iniciais da 
    * rede para a compilação, os novos valores não podem ser menores ou iguais a zero.
    * <p>
    *    É necessário informar o alcance <strong>antes</strong> de compilar a rede.
    * </p>
    * <p>
    *    {@code O valor padrão de alcance é 0.5}
    * </p>
    * @param alcance novo valor máximo e mínimo.
    * @throws IllegalArgumentException se o novo valor for menor ou igual a zero.
    */
   public void configurarAlcancePesos(double alcance){
      if(alcance <= 0){
         throw new IllegalArgumentException(
            "O novo valor de alcance dos pesos (" + alcance + 
            ") deve ser maior que zero."
         );
      }
      
      this.alcancePeso = alcance;
   }

   /**
    * Define se a rede neural usará um viés em seus neurônios.
    * <p>
    *    O viés é um atributo adicional em cada neurônio que sempre emite um valor de 
    *    saída constante. A presença de viés permite que a rede neural aprenda relações 
    *    mais complexas, melhorando a capacidade de modelagem.
    * </p>
    * <p>
    *    O bias deve ser configurado antes da compilação para ser aplicado.
    * </p>
    * <p>
    *    {@code O valor padrão para uso do bias é true}
    * </p>
    * @param usarBias novo valor para o uso do bias.
    */
   public void configurarBias(boolean usarBias){
      this.bias = usarBias;
   }

   /**
    * Configura a nova seed inicial para os geradores de números
    * aleatórios utilizados durante o processo de inicialização de pesos
    * e treinamento da Rede Neural.
    * <p>
    *    Configurações personalizadas de seed premitem fazer testes com diferentes
    *    parâmetros da Rede Neural, buscando encontrar um melhor ajuste para o modelo.
    * </p>
    * <p>
    *    A configuração de seed deve ser feita antes da compilação do modelo.
    * </p>
    * @param seed nova seed.
    */
   public void configurarSeed(long seed){
      this.seedInicial = seed;
   }

   /**
    * Configura a função de ativação da camada correspondente. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação.
    * <p>
    *    Segue a lista das funções disponíveis:
    * </p>
    * <ul>
    *    <li> 1 - ReLU. </li>
    *    <li> 2 - Sigmoid. </li>
    *    <li> 3 - Tangente Hiperbólica. </li>
    *    <li> 4 - Leaky ReLU. </li>
    *    <li> 5 - ELU .</li>
    *    <li> 6 - Swish. </li>
    *    <li> 7 - GELU. </li>
    *    <li> 8 - Linear. </li>
    *    <li> 9 - Seno. </li>
    *    <li> 10 - Argmax. </li>
    *    <li> 11 - Softmax. </li>
    *    <li> 12 - Softplus. </li>
    * </ul>
    * <p>
    *    {@code A função de ativação padrão é a ReLU para todas as camadas}
    * </p>
    * @param camada camada que será configurada.
    * @param ativacao valor relativo a lista de ativações disponíveis.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public void configurarAtivacao(Camada camada, int ativacao){
      this.verificarCompilacao();
      if(camada == null){
         throw new IllegalArgumentException("A camada fornecida é nula.");
      } 
      
      camada.configurarAtivacao(ativacao);
   }

   /**
    * Configura a função de ativação de todas as camadas da rede. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação.
    * <p>
    *    Segue a lista das funções disponíveis:
    * </p>
    * <ul>
    *    <li> 1 - ReLU. </li>
    *    <li> 2 - Sigmoid. </li>
    *    <li> 3 - Tangente Hiperbólica. </li>
    *    <li> 4 - Leaky ReLU. </li>
    *    <li> 5 - ELU .</li>
    *    <li> 6 - Swish. </li>
    *    <li> 7 - GELU. </li>
    *    <li> 8 - Linear. </li>
    *    <li> 9 - Seno. </li>
    *    <li> 10 - Argmax. </li>
    *    <li> 11 - Softmax. </li>
    *    <li> 12 - Softplus. </li>
    * </ul>
    * <p>
    *    {@code A função de ativação padrão é a ReLU para todas as camadas}
    * </p>
    * @param ativacao valor relativo a lista de ativações disponíveis.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public void configurarAtivacao(int ativacao){
      this.verificarCompilacao();
      
      for(Camada camada : this.camadas){
         camada.configurarAtivacao(ativacao);
      }
   }

   /**
    * Configura a função de ativação de todas as camadas da rede. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação.
    * <p>
    *    Segue a lista das funções disponíveis:
    * </p>
    * <ul>
    *    <li> ReLU. </li>
    *    <li> Sigmoid. </li>
    *    <li> Tangente Hiperbólica. </li>
    *    <li> Leaky ReLU. </li>
    *    <li> ELU .</li>
    *    <li> Swish. </li>
    *    <li> GELU. </li>
    *    <li> Linear. </li>
    *    <li> Seno. </li>
    *    <li> Argmax. </li>
    *    <li> Softmax. </li>
    *    <li> Softplus. </li>
    * </ul>
    * <p>
    *    {@code A função de ativação padrão é a ReLU para todas as camadas}
    * </p>
    * @param camada camada que será configurada.
    * @param ativacao nova função de ativação.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a camada for nula.
    * @throws IllegalArgumentException se a função de ativação fornecida for nula.
    */
   public void configurarAtivacao(Camada camada, FuncaoAtivacao ativacao){
      this.verificarCompilacao();

      if(camada == null){
         throw new IllegalArgumentException("A camada fornecida não pode ser nula.");
      }
      if(ativacao == null){
         throw new IllegalArgumentException("A nova função de ativação não pode ser nula.");
      }

      camada.configurarAtivacao(ativacao);
   }

   /**
    * Configura a função de ativação de todas as camadas da rede. É preciso
    * compilar o modelo previamente para poder configurar suas funções de ativação.
    * <p>
    *    Segue a lista das funções disponíveis:
    * </p>
    * <ul>
    *    <li> ReLU. </li>
    *    <li> Sigmoid. </li>
    *    <li> Tangente Hiperbólica. </li>
    *    <li> Leaky ReLU. </li>
    *    <li> ELU .</li>
    *    <li> Swish. </li>
    *    <li> GELU. </li>
    *    <li> Linear. </li>
    *    <li> Seno. </li>
    *    <li> Argmax. </li>
    *    <li> Softmax. </li>
    *    <li> Softplus. </li>
    * </ul>
    * <p>
    *    {@code A função de ativação padrão é a ReLU para todas as camadas}
    * </p>
    * @param ativacao nova função de ativação.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a função de ativação fornecida for nula.
    */
   public void configurarAtivacao(FuncaoAtivacao ativacao){
      verificarCompilacao();

      if(ativacao == null){
         throw new IllegalArgumentException("A nova função de ativação não pode ser nula.");
      }

      for(Camada camada : this.camadas){
         camada.configurarAtivacao(ativacao);
      }
   }

   /**
    * Configura a função de perda que será utilizada durante o processo
    * de treinamento da Rede Neural.
    * @param perda nova função de perda.
    */
   public void configurarPerda(Perda perda){
      if(perda == null){
         throw new IllegalArgumentException("A função de perda não pode ser nula.");
      }

      this.perdaAtual = perda;
   }

   /**
    * Configura o novo otimizdor da Rede Neural com base numa nova instância de otimizador.
    * <p>
    *    Configurando o otimizador passando diretamente uma nova instância permite configurar
    *    os hiperparâmetros do otimizador fora dos valores padrão, o que pode ajudar a
    *    melhorar o desempenho de aprendizado da Rede Neural em cenário específicos.
    * </p>
    * Otimizadores disponíveis.
    * <ol>
    *    <li>
    *       <strong> GradientDescent </strong>: Método clássico de retropropagação de erro e 
    *       ajuste de pesos para treinamento de Redes Neurais.
    *    </li>
    *    <li>
    *       <strong> SGD (Gradiente Descendente Estocástico) </strong>: Atualiza os pesos 
    *       usando o conjunto de treino embaralhado a cada época, com adicional de momentum
    *       e correção de nesterov para a atualização.
    *    </li>
    *    <li>
    *       <strong> AdaGrad </strong>: Um otimizador que adapta a taxa de aprendizado para 
    *       cada parâmetro da rede com base em iterações anteriores.
    *    </li>
    *    <li>
    *       <strong> RMSProp </strong>: Um otimizador que utiliza a média móvel dos quadrados 
    *       dos gradientes acumulados para ajustar a taxa de aprendizado.
    *    </li>
    *    <li>
    *       <strong> Adam </strong>: Um otimizador que combina o AdaGrad e o Momentum para 
    *       convergência rápida e estável.
    *    </li>
    *    <li>
    *       <strong> Nadam </strong>: Possui as mesmas vantagens de se utilizar o adam, com 
    *       o adicional do acelerador de Nesterov na atualização dos pesos.
    *    </li>
    *    <li>
    *       <strong> AMSGrad </strong>: Um otimizador que mantém um histórico dos valores
    *       dos gradientes acumulados para evitar a degradação da taxa de aprendizado,
    *       proporcionando uma convergência mais estável.
    *    </li>
    *    <li>
    *       <strong> Adamax </strong>: Um otimizador que é uma variação do Adam e
    *       mantém o máximo absoluto dos valores dos gradientes acumulados em vez de usar
    *       a média móvel dos quadrados dos gradientes.
    *    </li>
    *    <li>
    *       <strong> Lion </strong>: Esse é particularmente novo e não conheço muito bem. 
    *    </li>
    *    <li>
    *       <strong> Adadelta </strong>: Também é novo pra mim e ainda to testando melhor. 
    *    </li>
    * </ol>
    * <p>
    *    {@code O otimizador padrão é o SGD}
    * </p>
    * @param otimizador novo otimizador.
    * @throws IllegalArgumentException se o novo otimizador for nulo.
    */
   public void configurarOtimizador(Otimizador otimizador){
      if(otimizador == null){
         throw new IllegalArgumentException("O novo otimizador não pode ser nulo.");
      }

      this.otimizadorAtual = otimizador;
   }

   /**
    * Define se durante o processo de treinamento, a rede vai salvar dados relacionados a 
    * função de custo/perda de cada época.
    * <p>
    *    Calcular o custo é uma operação que pode ser computacionalmente cara dependendo do 
    *    tamanho da rede e do conjunto de dados, então deve ser bem avaliado querer habilitar 
    *    ou não esse recurso.
    * </p>
    * <p>
    *    {@code O valor padrão é false}
    * </p>
    * @param calcular se verdadeiro, a rede armazenará o histórico de custo de cada época.
    */
   public void configurarHistoricoCusto(boolean calcular){
      this.treinador.configurarHistoricoCusto(calcular);
   }

   /**
    * Compila o modelo de Rede Neural inicializando as camadas, neurônios e pesos respectivos, 
    * baseado nos valores fornecidos.
    * <p>
    *    Caso nenhuma configuração inicial seja feita, a rede será inicializada com os argumentos padrão. 
    * </p>
    * Após a compilação o modelo está pronto para ser usado, mas deverá ser treinado.
    * <p>
    *    Para treinar o modelo deve-se fazer uso da função função {@code treinar()} informando os 
    *    dados necessários para a rede.
    * </p>
    * <p>
    *    Para usar as predições da rede basta usar a função {@code calcularSaida()} informando os
    *    dados necessários. Após a predição pode-se obter o resultado da rede por meio da função 
    *    {@code obterSaidas()};
    * </p>
    * Os valores de função de perda, otimizador e inicializador serão definidos como os padrões 
    * {@code ErroMedioQuadrado (MSE)}, {@code SGD} e {@code Aleatorio}. 
    * <p>
    *    Valores de perda e otimizador configurados previamente são mantidos.
    * </p>
    */
   public void compilar(){
      //usando valores de configuração prévia, se forem criados.
      if(this.perdaAtual == null && this.otimizadorAtual == null){
         this.compilar(new ErroMedioQuadrado(), new SGD(), new Aleatorio());
         
      }else if(this.perdaAtual == null){
         this.compilar(new ErroMedioQuadrado(), this.otimizadorAtual, new Aleatorio());
         
      }else{
         this.compilar(this.perdaAtual, this.otimizadorAtual, new Aleatorio());
      }
   }

   /**
    * Compila o modelo de Rede Neural inicializando as camadas, neurônios e pesos respectivos, 
    * baseado nos valores fornecidos.
    * <p>
    *    Caso nenhuma configuração inicial seja feita, a rede será inicializada com os argumentos padrão. 
    * </p>
    * Após a compilação o modelo está pronto para ser usado, mas deverá ser treinado.
    * <p>
    *    Para treinar o modelo deve-se fazer uso da função função {@code treinar()} informando os 
    *    dados necessários para a rede.
    * </p>
    * <p>
    *    Para usar as predições da rede basta usar a função {@code calcularSaida()} informando os
    *    dados necessários. Após a predição pode-se obter o resultado da rede por meio da função 
    *    {@code obterSaidas()};
    * </p>
    * O valor do otimizador será definido como {@code SGD} o valor do inicializador 
    * será definido como o padrão {@code Aleatorio}.
    * <p>
    *    Valor de otimizador configurado previamente é mantido.
    * </p>
    * @param perda função de perda da Rede Neural usada durante o treinamento.
    * @throws IllegalArgumentException se a função de perda for nula.
    */
   public void compilar(Perda perda){
      if(perda == null){
         throw new IllegalArgumentException("A função de perda não pode ser nula.");
      }

      //usando valores de configuração prévia, se forem criados.
      if(this.otimizadorAtual == null){
         this.compilar(perda, new SGD(), new Aleatorio());

      }else{
         this.compilar(perda, this.otimizadorAtual, new Aleatorio());
      }
   }

   /**
    * Compila o modelo de Rede Neural inicializando as camadas, neurônios e pesos respectivos, 
    * baseado nos valores fornecidos.
    * <p>
    *    Caso nenhuma configuração inicial seja feita, a rede será inicializada com os argumentos padrão. 
    * </p>
    * Após a compilação o modelo está pronto para ser usado, mas deverá ser treinado.
    * <p>
    *    Para treinar o modelo deve-se fazer uso da função função {@code treinar()} informando os 
    *    dados necessários para a rede.
    * </p>
    * <p>
    *    Para usar as predições da rede basta usar a função {@code calcularSaida()} informando os
    *    dados necessários. Após a predição pode-se obter o resultado da rede por meio da função 
    *    {@code obterSaidas()};
    * </p>
    * A função de perda será definida como {@code ErroMedioQuadrado (MSE)} O valor do inicializador 
    * será definido como o padrão {@code Aleatorio}.
    * <p>
    *    Valor de função de perda configurada previamente é mantido.
    * </p>
    * @param otimizador otimizador que será usado durante o treino da Rede Neural.
    * @throws IllegalArgumentException se o otimizador for nulo.
    */
   public void compilar(Otimizador otimizador){
      if(otimizador == null){
         throw new IllegalArgumentException("O otimizador fornecido não pode ser nulo.");
      }

      //usando valores de configuração prévia, se forem criados.
      if(this.perdaAtual == null){
         this.compilar(new ErroMedioQuadrado(), otimizador, new Aleatorio());

      }else{
         this.compilar(this.perdaAtual, otimizador, new Aleatorio());
      }
   }

   /**
    * Compila o modelo de Rede Neural inicializando as camadas, neurônios e pesos respectivos, 
    * baseado nos valores fornecidos.
    * <p>
    *    Caso nenhuma configuração inicial seja feita, a rede será inicializada com os argumentos padrão. 
    * </p>
    * Após a compilação o modelo está pronto para ser usado, mas deverá ser treinado.
    * <p>
    *    Para treinar o modelo deve-se fazer uso da função função {@code treinar()} informando os 
    *    dados necessários para a rede.
    * </p>
    * <p>
    *    Para usar as predições da rede basta usar a função {@code calcularSaida()} informando os
    *    dados necessários. Após a predição pode-se obter o resultado da rede por meio da função 
    *    {@code obterSaidas()};
    * </p>
    * A função de perda usada será a {@code ErroMedioQuadrado (MSE)}.
    * <p>
    *    Valor de função de perda configurada previamente é mantido.
    * </p>
    * @param otimizador otimizador que será usando para o treino da Rede Neural.
    * @param inicializador inicializador de pesos dos neurônios da Rede Neural.
    * @throws IllegalArgumentException se o otimizador ou inicializador forem nulos.
    */
   public void compilar(Otimizador otimizador, Inicializador inicializador){
      if(otimizador == null){
         throw new IllegalArgumentException("O otimizador fornecido não pode ser nulo.");
      }
      if(inicializador == null){
         throw new IllegalArgumentException("O inicializador fornecido não pode ser nulo.");
      }

      //usando valores de configuração prévia, se forem criados.
      if(this.perdaAtual == null){
         this.compilar(new ErroMedioQuadrado(), otimizador, inicializador);

      }else{
         this.compilar(this.perdaAtual, otimizador, inicializador);
      }   
   }

   /**
    * Compila o modelo de Rede Neural inicializando as camadas, neurônios e pesos respectivos, 
    * baseado nos valores fornecidos.
    * <p>
    *    Caso nenhuma configuração inicial seja feita, a rede será inicializada com os argumentos padrão. 
    * </p>
    * Após a compilação o modelo está pronto para ser usado, mas deverá ser treinado.
    * <p>
    *    Para treinar o modelo deve-se fazer uso da função função {@code treinar()} informando os 
    *    dados necessários para a rede.
    * </p>
    * <p>
    *    Para usar as predições da rede basta usar a função {@code calcularSaida()} informando os
    *    dados necessários. Após a predição pode-se obter o resultado da rede por meio da função 
    *    {@code obterSaidas()};
    * </p>
    * @param perda função de perda da Rede Neural usada durante o treinamento.
    * @param otimizador otimizador que será usando para o treino da Rede Neural.
    * @param inicializador inicializador de pesos dos neurônios da Rede Neural.
    * @throws IllegalArgumentException se a perda, otimizador ou inicializador forem nulos.
    */
   public void compilar(Perda perda, Otimizador otimizador, Inicializador inicializador){
      if(inicializador == null){
         throw new IllegalArgumentException("O inicializador não pode ser nulo.");
      }
      if(perda == null){
         throw new IllegalArgumentException("A função de perda não pode ser nula.");
      }
      if(otimizador == null){
         throw new IllegalArgumentException("O otimizador não pode ser nulo.");
      }

      if(this.seedInicial != 0){
         inicializador.configurarSeed(seedInicial);
         this.treinador.configurarSeed(seedInicial);
      }

      //inicializar camadas
      this.camadas = new Camada[this.arquitetura.length-1];
      for(int i = 0; i < this.camadas.length; i++){
         this.camadas[i] = new Camada(this.arquitetura[i+1], this.bias);
         this.camadas[i].inicializar(this.arquitetura[i], this.alcancePeso, inicializador);
         this.camadas[i].configurarId(i);
      }

      this.perdaAtual = perda;

      this.otimizadorAtual = otimizador;
      this.otimizadorAtual.inicializar(this.obterQuantidadeParametros());

      this.compilado = true;//modelo pode ser usado
   }

   /**
    * Verifica se o modelo já foi compilado para evitar problemas de uso indevido, 
    * bem como componentes nulos.
    * @throws IllegalArgumentException se o modelo não foi compilado.
    */
   private void verificarCompilacao(){
      if(!this.compilado){
         throw new IllegalArgumentException("O modelo ainda não foi compilado");
      }
   }

   /**
    * Verifica se os dados são apropriados para serem usados dentro da rede neural, incluindo:
    * <ul>
    *    <li>
    *       Mesma quantidade de amostras nos dados de entrada e saída.
    *    </li>
    *    <li>
    *       Dados de entrada possuem a mesma quantidade de exemplos que a capacidade da camada 
    *       de entrada da rede.
    *    </li>
    *    <li>
    *       Dados de saída possuem a mesma quantidade de exemplos que a quantidade de 
    *       neurônios da camada de saída da rede.
    *    </li>
    * </ul>
    * Caso os dados fornecidos atendam a essas condições, o fluxo de execução segue normalmente.
    * @param entrada conjunto de dados de entrada.
    * @param saida conjunto de dados de saída.
    */
   private void consistenciaDados(double[][] entrada, double[][] saida){
      int tamEntrada = this.obterTamanhoEntrada();
      int tamSaida = this.obterTamanhoSaida();

      if(entrada.length != saida.length){
         throw new IllegalArgumentException(
            "Quantidade de linhas de dados de entrada (" + entrada.length +
            ") e saída (" + saida.length + 
            ") devem ser iguais."
         );
      }
      if(tamEntrada != entrada[0].length){
         throw new IllegalArgumentException(
            "Dimensões dos dados de entrada (" + entrada[0].length +
            ") e capacidade de entrada da rede (" + tamEntrada + 
            ") incompatíveis."
         );
      }
      if(tamSaida != saida[0].length){
         throw new IllegalArgumentException(
            "Dados de saída (" + saida[0].length +
            ") e neurônios de saída da rede (" + tamSaida + 
            ") incompatíveis."
         );
      }
   }

   /**
    * Alimenta os dados pela rede neural usando o método de feedforward através do conjunto
    * de dados fornecido. 
    * <p>
    *    Os dados são alimentados para as entradas dos neurônios e é calculado o produto junto 
    *    com os pesos. No final é aplicado a função de ativação da camada no neurônio e o resultado 
    *    fica armazenado na saída dele.
    * </p>
    * @param entradas dados usados para alimentar a camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente da capacidade
    * de entrada da rede.
    */
   public void calcularSaida(double[] entradas){
      this.verificarCompilacao();
      
      int tamEntrada = this.obterTamanhoEntrada();
      if(entradas.length != tamEntrada){
         throw new IllegalArgumentException(
            "Dimensões dos dados de entrada (" + entradas.length +
            ") e capacidade de entrada da rede (" + tamEntrada + 
            ") incompatíveis."
         );
      }

      this.camadas[0].ativarNeuronios(entradas);
      //ativar neurônios das camadas seguintes
      for(int i = 1; i < this.camadas.length; i++){
         this.camadas[i].ativarNeuronios(this.camadas[i-1].obterSaida());
      }
   }

   /**
    * Alimenta os dados pela rede neural usando o método de feedforward através do conjunto
    * de dados fornecido. 
    * <p>
    *    Os dados são alimentados para as entradas dos neurônios e é calculado o produto junto 
    *    com os pesos. No final é aplicado a função de ativação da camada no neurônio e o resultado 
    *    fica armazenado na saída dele.
    * </p>
    * @param entradas dados usados para alimentar a camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a quantidade de amostras em cada linha dos dados for diferente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente da capacidade
    * de entrada da rede.
    * @return matriz contendo os resultados das predições da rede.
    */
   public double[][] calcularSaida(double[][] entradas){
      this.verificarCompilacao();

      int cols = entradas[0].length;
      for(int i = 1; i < entradas.length; i++){
         if(entradas[i].length != cols){
            throw new IllegalArgumentException(
               "As dimensões dos dados de entrada possuem tamanhos diferentes."
            );
         }
      }

      int nEntrada = this.obterTamanhoEntrada();
      if(entradas[0].length != nEntrada){
         throw new IllegalArgumentException(
            "Dimensões dos dados de entrada (" + entradas.length +
            ") e capacidade de entrada da rede (" + nEntrada + 
            ") incompatíveis."
         );
      }

      //dimensões dos dados
      int nAmostras = entradas.length;
      int tamEntrada = this.obterTamanhoEntrada();
      int tamSaida = this.obterTamanhoSaida();

      double[][] resultados = new double[nAmostras][tamSaida];
      double[] entradaRede = new double[tamEntrada];
      double[] saidaRede = new double[tamSaida];

      for(int i = 0; i < nAmostras; i++){
         System.arraycopy(entradas[i], 0, entradaRede, 0, entradas[i].length);

         this.calcularSaida(entradaRede);
         System.arraycopy(this.obterSaidas(), 0, saidaRede, 0, saidaRede.length);
         System.arraycopy(saidaRede, 0, resultados[i], 0, saidaRede.length);
      }

      return resultados;
   }

   /**
    * Treina a Rede Neural de acordo com as configurações predefinidas.
    * <p>
    *    Certifique-se de configurar adequadamente o modelo para obter os 
    *    melhores resultados.
    * </p>
    * @param entradas dados de entrada do treino (features).
    * @param saidas dados de saída correspondente a entrada (class).
    * @param epochs quantidade de épocas de treinamento.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinar(double[][] entradas, double[][] saidas, int epochs){
      this.verificarCompilacao();
      consistenciaDados(entradas, saidas);

      if(epochs < 1){
         throw new IllegalArgumentException(
            "O valor de epochs (" + epochs + ") não pode ser menor que um"
         );
      }

      treinador.treino(
         this,
         this.perdaAtual,
         this.otimizadorAtual,
         entradas,
         saidas,
         epochs
      );
   }

   /**
    * Treina a rede de acordo com as configurações predefinidas.
    * <p>
    *    O modo de treinamento em lote costuma ser mais lento para convergir
    *    e não é recomendável utilizar as mesma configurações da rede que foram
    *    usadas pelo método sequencial convencional.
    * </p>
    * <p>
    *    Em compensação ele tende a ser mais estável e possui menos ruídos durante
    *    o treinamento.
    * </p>
    * Certifique-se de configurar adequadamente o modelo para obter os 
    * melhores resultados.
    * @param entradas dados de entrada do treino (features).
    * @param saidas dados de saída correspondente a entrada (class).
    * @param epochs quantidade de épocas de treinamento.
    * @param tamLote tamanho que o lote vai assumir durante o treino.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinar(double[][] entradas, double[][] saidas, int epochs, int tamLote){
      this.verificarCompilacao();
      consistenciaDados(entradas, saidas);

      if(epochs < 1){
         throw new IllegalArgumentException(
            "O valor de epochs (" + epochs + ") não pode ser menor que um"
         );
      }
      if(tamLote <= 0 || tamLote > entradas.length){
         throw new IllegalArgumentException(
            "O valor de tamanho do lote (" + tamLote + ") é inválido."
         );
      }

      treinador.treino(
         this,
         this.perdaAtual,
         this.otimizadorAtual,
         entradas,
         saidas,
         epochs,
         tamLote
      );
   }
   
   /**
    * Método alternativo no treino da rede neural usando diferenciação finita (finite difference), 
    * que calcula a "derivada" da função de custo levando a rede ao mínimo local dela. É importante 
    * encontrar um bom balanço entre a taxa de aprendizagem da rede e o valor de perturbação usado.
    * <p>
    *    Vale ressaltar que esse método é mais lento e menos eficiente que o backpropagation, em 
    *    arquiteturas de rede maiores e que tenha uma grande volume de dados de treino ou para 
    *    problemas mais complexos ele pode demorar muito para convergir ou simplemente não funcionar 
    *    como esperado.
    * </p>
    * <p>
    *    Ainda sim não deixa de ser uma abordagem válida.
    * </p>
    * @param entradas matriz com os dados de entrada 
    * @param saidas matriz com os dados de saída
    * @param eps valor de perturbação
    * @param tA valor de taxa de aprendizagem do método, contribui para o quanto os pesos
    * serão atualizados. Valores altos podem convergir rápido mas geram instabilidade, valores pequenos
    * atrasam a convergência.
    * @param epochs número de épocas do treinamento
    * @param perdaMinima valor de perda desejável, o treino será finalizado caso o valor de perda mínima seja
    * atingido. Caso a perda mínima seja zero, o treino irá continuar até o final das épocas fornecidas.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de perturbação for igual a zero.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    * @throws IllegalArgumentException se o valor de custo mínimo for menor que zero.
    */
   public void diferencaFinita(double[][] entradas, double[][] saidas, double eps, double tA, int epochs, double perdaMinima){
      this.verificarCompilacao();
      consistenciaDados(entradas, saidas);
      
      if(eps == 0){
         throw new IllegalArgumentException(
            "O valor de perturbação (" + eps + ") não pode ser igual a zero."
         );
      }
      if(epochs < 1){
         throw new IllegalArgumentException(
            "O valor de epochs (" + epochs + ") não pode ser menor que um."
         );
      }
      if(perdaMinima < 0){
         throw new IllegalArgumentException(
            "O valor de perda mínima (" + perdaMinima + ") não pode ser negativo."
         );
      }
      
      //copia da rede para guardar os valores de "gradientes"
      RedeNeural redeG = this.clone();

      Camada[] redec = this.camadas;
      Camada[] gradc = redeG.camadas;

      for(int epocas = 0; epocas < epochs; epocas++){
         
         double custo = avaliador.erroMedioQuadrado(entradas, saidas);
         if(custo < perdaMinima) break;

         double valorAnterior = 0;
         for(int i = 0; i < redec.length; i++){
            for(int j = 0; j < redec[i].quantidadeNeuronios(); j++){
               for(int k = 0; k < redec[i].neuronios[j].pesos.length; k++){
                  valorAnterior = redec[i].neuronios[j].pesos[k];
                  redec[i].neuronios[j].pesos[k] += eps;
                  gradc[i].neuronios[j].pesos[k] = ((avaliador.erroMedioQuadrado(entradas, saidas) - custo)/eps);
                  redec[i].neuronios[j].pesos[k] = valorAnterior;
               }
            }
         }

         //atualizar pesos
         for(int i = 0; i < redec.length; i++){
            for(int j = 0; j < redec[i].quantidadeNeuronios(); j++){
               for(int k = 0; k < redec[i].neuronios[j].pesos.length; k++){
                  redec[i].neuronios[j].pesos[k] -= tA * gradc[i].neuronios[j].pesos[k];
               }
            }
         }
      }

   }

   /**
    * Retorna a função de perda configurada da Rede Neural.
    * @return função de perda atual da rede.
    */
   public Perda obterPerda(){
      return this.perdaAtual;
   }

   /**
    * Retorna o otimizador que está sendo usado para o treino da Rede Neural.
    * @return otimizador atual da rede.
    */
   public Otimizador obterOtimizador(){
      return this.otimizadorAtual;
   }

   /**
    * Retorna a {@code camada} da Rede Neural correspondente ao índice fornecido.
    * @param id índice da busca.
    * @return camada baseada na busca.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o índice estiver fora do alcance do tamanho 
    * das camadas ocultas.
    */
   public Camada obterCamada(int id){
      this.verificarCompilacao();

      if((id < 0) || (id >= this.camadas.length)){
         throw new IllegalArgumentException(
            "O índice fornecido (" + id + ") está é inválido ou fora de alcance."
         );
      }
   
      return this.camadas[id];
   }

   /**
    * Retorna a {@code camada de saída} da Rede Neural.
    * @return camada de saída, ou ultima camada densa.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public Camada obterCamadaSaida(){
      this.verificarCompilacao();

      return this.camadas[this.camadas.length-1];
   }

   /**
    * Retorna todo o conjunto de camadas densas presente na Rede Neural.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @return conjunto de camadas da rede.
    */
   public Camada[] obterCamadas(){
      this.verificarCompilacao();

      return this.camadas;
   }

   /**
    * Copia os dados da saída de cada neurônio da camada de saída da Rede Neural para 
    * um array. 
    * <p>
    *    A ordem de cópia é crescente, do primeiro neurônio da saída ao último.
    * </p>
    * @return array com os dados das saídas da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public double[] obterSaidas(){
      this.verificarCompilacao();

      return this.obterCamadaSaida().obterSaida();
   }

   /**
    * Retorna o array que representa a estrutura da Rede Neural. Nele cada elemento 
    * indica uma camada da rede e cada valor contido nesse elemento indica a 
    * quantidade de neurônios daquela camada correspondente.
    * <p>
    *    Nessa estrutura de rede, a camada de entrada não é considerada uma camada,
    *    o que significa dizer também que ela não é uma instância de camada dentro
    *    da Rede Neural.
    * </p>
    * <p>
    *    A "camada de entrada" representa o tamanho de entrada da primeira camda densa
    *    da rede, ou seja, ela é apenas um parâmetro pro tamanho de entrada da primeira
    *    camada oculta. 
    * </p>
    * @return array com a arquitetura da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int[] obterArquitetura(){
      this.verificarCompilacao();
      return this.arquitetura;
   }

   /**
    * Informa o nome configurado da Rede Neural.
    * @return nome específico da rede.
    */
   public String obterNome(){
      return this.nome;
   }

   /**
    * Disponibiliza o histórico da função de custo da rede neural durante cada época
    * de treinamento.
    * @return lista contendo o histórico de custos durante o treinamento da rede.
    * @throws IllegalArgumentException se não foi habilitado previamente o cálculo do 
    * histórico de custos.
    */
   public ArrayList<Double> obterHistoricoCusto(){
      if(!this.treinador.calcularHistorico){
         throw new IllegalArgumentException(
            "O histórico de custo da rede deve ser habilitado."
         );
      }
      return this.treinador.obterHistorico();
   }

   /**
    * Retorna a quantidade total de parâmetros da rede.
    * <p>
    *    isso inclui todos os pesos de todos os neurônios presentes 
    *    (incluindo o peso adicional do bias).
    * </p>
    * @return quantiade de parâmetros total da rede.
    */
   public int obterQuantidadeParametros(){
      int numPesos = 0;
      for(Camada camada : this.camadas){
         numPesos += camada.numConexoes();
      }
      return numPesos;
   }

   /**
    * Retorna a quantidade de camadas densas presente na Rede Neural.
    * <p>
    *    A {@code camada de entrada} não é considerada uma camada densa e é usada
    *    apenas para especificar o tamanho de entrada suportado pela rede.
    * </p>
    * @return quantidade de camadas da Rede Neural.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int obterQuantidadeCamadas(){
      this.verificarCompilacao();

      return this.camadas.length;
   }

   /**
    * Retorna a capacidade da camada de entrada da Rede Neural. Em outras palavras
    * diz quantos dados de entrada a rede suporta.
    * @return tamanho de entrada da Rede Neural.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int obterTamanhoEntrada(){
      this.verificarCompilacao();

      return this.arquitetura[0];
   }

   /**
    * Retorna a capacidade de saída da Rede Neural. Em outras palavras
    * diz quantos dados de saída a rede produz.
    * @return tamanho de saída da Rede Neural.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int obterTamanhoSaida(){
      this.verificarCompilacao();

      return this.arquitetura[this.arquitetura.length-1];
   }

   /**
    * Retorna o valor de uso do bias da Rede Neural.
    * @return valor de uso do bias da Rede Neural.
    */
   public boolean temBias(){
      return this.bias;
   }

   /**
    * Exibe algumas informações importantes sobre a Rede Neural, como:
    * <ul>
    *    <li>
    *       Otimizador atual e suas informações específicas.
    *    </li>
    *    <li>
    *       Contém bias como neurônio adicional.
    *    </li>
    *    <li>
    *       Função de ativação de todas as camadas.
    *    </li>
    *    <li>
    *       Arquitetura da rede.
    *    </li>
    * </ul>
    * @return buffer formatado contendo as informações.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public String info(){
      this.verificarCompilacao();

      String buffer = "";
      String espacamento = "    ";
      System.out.println("\nInformações " + this.nome + " = [");

      //perda
      buffer += espacamento + "Perda: " + this.perdaAtual.getClass().getSimpleName() + "\n\n";

      //otimizador
      buffer += espacamento + "Otimizador: " + this.otimizadorAtual.getClass().getSimpleName() + "\n";
      buffer += this.otimizadorAtual.info();

      //bias
      buffer += "\n" + espacamento + "Bias = " + this.bias;
      buffer += "\n\n";

      for(int i = 0; i < this.camadas.length; i++){
         buffer += espacamento + "Ativação camada " + i + ": " + this.camadas[i].obterAtivacao().getClass().getSimpleName() + "\n";
      }

      //arquitetura
      buffer += "\n" + espacamento + "arquitetura = [(" + this.arquitetura[0] + ")";
      for(int i = 1; i < this.arquitetura.length; i++){
         buffer += ", " + this.arquitetura[i];
      }
      buffer += "]";

      buffer += "\n]\n";

      return buffer;
   }
 
   /**
    * Clona a instância da rede, criando um novo objeto com as mesmas características
    * mas em outro espaço de memória.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @return Clone da Rede Neural.
    */
   @Override
   public RedeNeural clone(){
      this.verificarCompilacao();

      try{
         RedeNeural clone = (RedeNeural) super.clone();

         //dados importantes
         clone.bias = this.bias;
         clone.arquitetura = this.arquitetura;
         clone.compilado = this.compilado;

         clone.nome = "Clone de " + this.nome;

         clone.camadas = new Camada[this.camadas.length];
         for(int i = 0; i < this.camadas.length; i++){
            clone.camadas[i] = this.camadas[i].clone();
         }

         return clone;
      }catch(CloneNotSupportedException e){
         throw new RuntimeException(e);
      }
   }

   @Override
   public String toString(){
      this.verificarCompilacao();

      String buffer = "";
      String espacamento = "   ";
      String espacamentoDuplo = espacamento.repeat(2);
      String espacamentoTriplo = espacamento.repeat(3);
      
      buffer += "\nArquitetura " + nome + " = [\n";
      for(int i = 0; i < this.camadas.length; i++){

         buffer += espacamento + "Camada " + i + " = [\n";
         for(int j = 0; j < this.camadas[i].quantidadeNeuronios(); j++){

            buffer += espacamentoDuplo + "n" + j + " = [\n";
            for(int k = 0; k < this.camadas[i].neuronio(j).pesos.length; k++){
               if(k == this.camadas[i].neuronio(j).pesos.length-1 && this.bias){
                  buffer += espacamentoTriplo + "pb" + " = " + this.camadas[i].neuronio(j).pesos[k] + "\n";
               }else{
                  buffer += espacamentoTriplo + "p" + k + " = " + this.camadas[i].neuronio(j).pesos[k] + "\n";
               }
            }
            buffer += espacamentoDuplo + "]\n";

         }
         buffer += espacamento + "]\n\n";
      
      }
      buffer += "]\n";

      return buffer;
   }
}
