package rna.estrutura;

import java.util.ArrayList;

import rna.ativacoes.FuncaoAtivacao;
import rna.avaliacao.Avaliador;
import rna.otimizadores.AMSGrad;
import rna.otimizadores.AdaGrad;
import rna.otimizadores.Adam;
import rna.otimizadores.Adamax;
import rna.otimizadores.GradientDescent;
import rna.otimizadores.Nadam;
import rna.otimizadores.Otimizador;
import rna.otimizadores.RMSProp;
import rna.otimizadores.SGD;

import rna.treinamento.Treinador;

//TODO
//Implementar formas melhores de treinar com uma grande quantidade de dados (mais necessário);
//novos otimizadores;
//Mais opções de métricas e funções de perda;
//Poder configurar funções de ativação antes de compilar o modelo;

/**
 * Modelo de Rede Neural Multilayer Perceptron criado do zero. Possui um conjunto de camadas 
 * e cada camada possui um conjunto de neurônios artificiais.
 * <p>
 *    O modelo pode ser usado para problemas de regressão e classificação, contando com algoritmos de treino 
 *    atualmente baseados no backpropagation com adição da ideia de momentum na atualização dos pesos.
 * </p>
 * Possui opções de configuração para funções de ativações de camadas individuais, valor de alcance máximo e 
 * mínimo na aleatorização dos pesos iniciais e otimizadores que serão usados durante o treino. 
 * <p>
 *    Após configurar as propriedades da rede, o modelo precisará ser compilado para efetivamente 
 *    poder ser utilizado.
 * </p>
 * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, 
 * Campus Tucuruí. Maio/2023.
 */
public class RedeNeural implements Cloneable{

   /**
    * Camada de entrada da Rede Neural.
    */
   private Camada entrada;

   /**
    * Array contendo as camadas ocultas da Rede Neural.
    */
   private Camada[] ocultas;

   /**
    * Camada de saída da Rede Neural.
    */
   private Camada saida;

   /**
    * Array contendo a arquitetura de cada camada dentro da Rede Neural.
    * Cada elemento da arquitetura representa a quantidade de neurônios 
    * presente na camada correspondente.
    */
   private int[] arquitetura;

   /**
    * Constante auxiliar que ajuda no controle do bias atuando como neurônio 
    * adicional para cálculos.
    */
   private int BIAS = 1;

   /**
    * Valor máximo e mínimo na hora de aleatorizar os pesos da rede neural.
    */
   private double alcancePeso = 1.0;

   /**
    * Otimizador usado para a inicialização dos primeiros pesos dos neurônios 
    * da Rede Neural.
    */
   private int inicializadorPeso = 1;

   /**
    * Auxiliar no controle da compilação da Rede Neural, ajuda a evitar uso 
    * indevido caso a rede não tenha suas variáveis inicializadas previamente.
    */
   private boolean compilado = false;

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
    * Objeto responsável pelo retorno de desempenho da Rede Neural.
    * Contém implementações de métodos tanto para cálculo de perdas
    * quanto para cálculo de métricas.
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
    *    A camada de entrada deverá ser especificada pelo indice 0, a camada de saída será representada 
    *    pelo último valor do array e as camadas ocultas serão representadas pelos valores intermediários.
    * </p>
    * <p>
    *   Os valores de todos os parâmetros pedidos <strong>NÃO devem</strong> ser menores que 1.
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
    * @throws IllegalArgumentException se o array de arquitetura não possuir, pelo menos, três elementos.
    * @throws IllegalArgumentException se os valores fornecidos forem menores que um.
    */
   public RedeNeural(int[] arquitetura){
      if(arquitetura.length < 3) throw new IllegalArgumentException("A arquitetura da rede não pode conter menos de três elementos");
      
      for(int i = 0; i < arquitetura.length; i++){
         if(arquitetura[i] < 1) throw new IllegalArgumentException("Os valores fornecidos devem ser maiores ou iguais a um.");
      }

      int quantidadeOcultas = arquitetura.length-2;//evitar problemas
      this.arquitetura = new int[1 + quantidadeOcultas + 1];

      this.arquitetura[0] = arquitetura[0];
      for(int i = 0; i < quantidadeOcultas; i++) this.arquitetura[i+1] = arquitetura[i+1];
      this.arquitetura[this.arquitetura.length-1] = arquitetura[arquitetura.length-1];
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
    * @throws IllegalArgumentException se o novo nome for inválido.
    */
   public void configurarNome(String nome){
      if(nome == null) throw new IllegalArgumentException("O novo nome da rede neural não pode ser nulo.");
      if(nome.isBlank() || nome.isEmpty()){
         throw new IllegalArgumentException("O novo nome da rede neural não pode estar vazio.");
      }

      this.nome = nome;
   }

   /**
    * Define o valor máximo e mínimo na hora de aleatorizar os pesos da rede 
    * para a compilação, os novos valores não podem ser menores ou iguais a zero.
    * <p>
    *    {@code O valor padrão de alcance é 1}
    * </p>
    * @param alcance novo valor máximo e mínimo.
    * @throws IllegalArgumentException se o novo valor for menor ou igual a zero.
    */
   public void configurarAlcancePesos(double alcance){
      if(alcance <= 0){
         throw new IllegalArgumentException("Os novos valores de alcance dos pesos não podem ser menores ou iguais a zero.");
      }
      this.alcancePeso = alcance;
   }

   /**
    * Configura a inicialização dos pesos da Rede Neural. A forma de inicialização 
    * pode afetar o tempo de convergência da rede durante o treinamento.
    * <p>
    *    Inicializadores disponíveis:
    * </p>
    * <ul>
    *    <li>
    *       1 - Inicialização aleatória no intervalo {@code [-alcancePeso, alcancePeso]}.
    *    </li>
    *    <li>
    *       2 - Inicialização aleatória positiva no intervalo {@code [0, alcancePeso]}.
    *    </li>
    *    <li>
    *       3 - Inicialização He: Adequada para funções de ativação ReLU e suas derivadas.
    *    </li>
    *    <li>
    *       4 - Inicialização LeCun: Recomendada para funções de ativação como a Tangente Hiperbólica (tanh).
    *    </li>
    *    <li>
    *       5 - Inicialização Xavier/Glorot: Ideal para funções de ativação comuns, como a Sigmoid ou tanh.
    *    </li>
    * </ul>
    * <p>
    *    {@code O valor padrão de inicializador é 1}
    * </p>
    * @param inicializador novo valor de inicializador de pesos da rede.
    * @throws IllegalArgumentException se o novo valor de inicializdor for menor que um.
    * @throws IllegalArgumentException se o novo valor de inicializador for inválido, no 
    * momento da compilação.
    */
   public void configurarInicializador(int inicializador){
      if(inicializador < 1){
         throw new IllegalArgumentException("O novo valor de inicializador não pode ser menor que um.");
      } 

      this.inicializadorPeso = inicializador;
   }

   /**
    * Define se a rede neural usará um neurônio adicional como bias nas camadas da rede.
    * O bias não é adicionado na camada de saída.
    * <p>
    *    {@code O valor padrão para uso do bias é true}
    * </p>
    * @param usarBias novo valor para o uso do bias.
    */
   public void configurarBias(boolean usarBias){
      this.BIAS = (usarBias) ? 1 : 0;
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
   public void configurarFuncaoAtivacao(Camada camada, int ativacao){
      this.modeloCompilado();
      if(camada.equals(this.entrada)){
         throw new IllegalArgumentException("Não é possível, nem necessário, configurar função de ativação para a camada de entrada.");
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
   public void configurarFuncaoAtivacao(int ativacao){
      this.modeloCompilado();
      
      for(Camada camada : this.ocultas){
         camada.configurarAtivacao(ativacao);
      } 
      this.saida.configurarAtivacao(ativacao);
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
    * @throws IllegalArgumentException se a camada for a camada de entrada da rede.
    * @throws IllegalArgumentException se a função de ativação fornecida for nula.
    */
   public void configurarFuncaoAtivacao(Camada camada, FuncaoAtivacao ativacao){
      this.modeloCompilado();

      if(camada == null){
         throw new IllegalArgumentException("A camada fornecida não pode ser nula.");
      }
      if(camada.equals(this.entrada)){
         throw new IllegalArgumentException("Não é possível, nem necessário, configurar função de ativação para a camada de entrada.");
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
   public void configurarFuncaoAtivacao(FuncaoAtivacao ativacao){
      modeloCompilado();

      if(ativacao == null){
         throw new IllegalArgumentException("A nova função de ativação não pode ser nula.");
      }

      for(Camada camada : this.ocultas){
         camada.configurarAtivacao(ativacao);
      }
      this.saida.configurarAtivacao(ativacao);
   }

   /**
    * Configura o otimizador usado durante o treino da rede neural. Cada otimizador possui 
    * sua própia maneira de atualizar os pesos da rede e cada um deles pode ser apropriado 
    * em uma determinada tarefa.
    * <p>
    *    Os otimizadores disponíveis são:
    * </p>
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
    * </ol>
    * <p>
    *    {@code O otimizador padrão é o SGD}
    * </p>
    * Ao utilziar essa abordagem de configuração de otimizador, os novos otimizadores congigurados 
    * serão inicializados usando seus valores padrão para os hiperparâmetros.
    * @param otimizador valor do novo otimizador.
    * @throws IllegalArgumentException se o valor fornecido do otimizador estiver fora da lista 
    * dos otimizadores disponíveis.
    */
   public void configurarOtimizador(int otimizador){
      modeloCompilado();

      switch(otimizador){
         case 1 -> this.otimizadorAtual = new GradientDescent();
         case 2 -> this.otimizadorAtual = new SGD();
         case 3 -> this.otimizadorAtual = new AdaGrad();
         case 4 -> this.otimizadorAtual = new RMSProp();
         case 5 -> this.otimizadorAtual = new Adam();
         case 6 -> this.otimizadorAtual = new Nadam();
         case 7 -> this.otimizadorAtual = new AMSGrad();
         case 0 -> this.otimizadorAtual = new Adamax();
         default-> throw new IllegalArgumentException("Valor fornecido do otimizador é inválido.");
      }

      this.otimizadorAtual.inicializar(this.obterQuantidadePesos());
   }

   /**
    * Configura o novo otimizdor da Rede Neural com base numa nova instância de otimizador.
    * <p>
    *    Configurando o otimizador passando diretamente uma nova instância permite configurar
    *    os hiperparâmetros do otimizador fora dos valores padrão, o que pode ajudar a
    *    melhorar o desempenho de aprendizado da Rede Neural em cenário específicos.
    * </p>
    * <p>
    *    {@code O otimizador padrão é o SGD}
    * </p>
    * @param otimizador novo otimizador
    * @throws IllegalArgumentException se o novo otimizador for nulo.
    */
   public void configurarOtimizador(Otimizador otimizador){
      modeloCompilado();

      if(otimizador == null){
         throw new IllegalArgumentException("O novo otimizador não pode ser nulo.");
      }

      this.otimizadorAtual = otimizador;
      this.otimizadorAtual.inicializar(this.obterQuantidadePesos());
   }

   /**
    * Define se durante o processo de treinamento, a rede vai salvar dados relacionados a 
    * função de custo de cada época.
    * <p>
    *    Calcular o custo é uma operação que pode ser computacionalmente cara, então deve ser
    *    bem avaliado querer ativar ou não esse recurso.
    * </p>
    * <p>
    *    {@code O valor padrão é false}
    * </p>
    * @param historicoCusto se verdadeiro, a rede armazenará o histórico de custo de cada época.
    */
   public void configurarHistoricoCusto(boolean historicoCusto){
      this.treinador.configurarHistoricoCusto(historicoCusto);
   }

   /**
    * Compila o modelo de rede inicializando as camadas, neurônios e pesos respectivos, 
    * baseado nos valores fornecidos. Antes da compilação é possível
    * informar alguns valores ajustáveis na inicialização da rede, como:
    * <ul>
    *    <li>
    *       Valor máximo e mínimo para os pesos gerados aleatoriamente.
    *    </li>
    *    <li>
    *       Funções de ativação para as camadas ocultas e para a camada de saída.
    *    </li>
    *    <li>
    *       Neurônios adicionais nas camadas atuando como bias.</li>
    *    <li>
    *       Taxa de aprendizagem.
    *    </li>
    *    <li>
    *       Taxa de momentum.
    *    </li>
    *    <li>
    *       Otimizador que será usado durante o treinamento da Rede Neural.
    *    </li>
    *    <li>
    *       Habilitar histórico de custos durante o treino.
    *    </li>
    *    <li>
    *       Inicializador de pesos.
    *    </li>
    * </ul>
    * <p>
    *    Caso nenhuma configuração seja feita, a rede será inicializada com os valores padrão. 
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
    */
   public void compilar(){
      //adicionando bias como neuronio adicional nas camadas
      //não adicionar na camada de saída
      for(int i = 0; i < arquitetura.length-1; i++){
         arquitetura[i] += BIAS;
      }

      //passar as informações do bias paras as camadas
      boolean temBias = (this.BIAS == 1) ? true : false;

      //identificar cada camada dentro da rede.
      int idCamada = 0;
      
      //inicializar camada de entrada
      this.entrada = new Camada(temBias);
      this.entrada.configurarId(idCamada);
      idCamada++;
      this.entrada.inicializar(arquitetura[0], 1, alcancePeso, inicializadorPeso);

      //inicializar camadas ocultas
      int quantidadeOcultas = this.arquitetura.length-2;
      this.ocultas = new Camada[quantidadeOcultas];
      for(int i = 0; i < this.ocultas.length; i++){// percorrer ocultas
         Camada novaOculta = new Camada(temBias);

         if(i == 0) novaOculta.inicializar(arquitetura[i+1], arquitetura[0], alcancePeso, inicializadorPeso);
         else novaOculta.inicializar(arquitetura[i+1], arquitetura[i], alcancePeso, inicializadorPeso);

         this.ocultas[i] = novaOculta;
         this.ocultas[i].configurarId(idCamada);
         idCamada++;
      }

      //inicializar camada de saída
      this.saida = new Camada(false);
      this.saida.configurarId(idCamada);
      this.saida.inicializar(arquitetura[arquitetura.length-1], arquitetura[arquitetura.length-2], alcancePeso, inicializadorPeso);

      //inicializar otimizador
      if(this.otimizadorAtual == null){
         this.otimizadorAtual = new SGD();
         this.otimizadorAtual.inicializar(this.obterQuantidadePesos());
      }

      compilado = true;//modelo pode ser usado
   }

   /**
    * Verifica se o modelo já foi compilado para evitar problemas de uso indevido, 
    * bem como componentes nulos.
    * @throws IllegalArgumentException se o modelo não foi compilado.
    */
   private void modeloCompilado(){
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
    *       Dados de entrada possuem a mesma quantidade de exemplos que a quantidade de 
    *       neurônios da camada de entrada da rede, exluindo bias.
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
      int nEntrada = this.entrada.quantidadeNeuronios() - (this.entrada.temBias() ? 1 : 0);
      int nSaida = this.obterCamadaSaida().quantidadeNeuronios();

      if(entrada.length != saida.length){
         throw new IllegalArgumentException("A quantidade de linhas de dados e saídas são diferentes.");
      }
      if(nEntrada != entrada[0].length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de entrada e os neurônios de entrada da rede.");
      }
      if(nSaida != saida[0].length){
         throw new IllegalArgumentException("Incompatibilidade entre os dados de saída e os neurônios de saída da rede.");
      }      
   }

   /**
    * <p>
    *    Propaga os dados de entrada pela rede neural pelo método de feedforward.
    * </p>
    * Os dados são alimentados para as entradas dos neurônios e é calculado o produto junto com os pesos.
    * No final é aplicado a função de ativação da camada no neurônio e o resultado fica armazenado na saída dele.
    * @param entradas dados usados para alimentar a camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos 
    * neurônios de entrada, excluindo o bias.
    */
   public void calcularSaida(double[] entradas){
      this.modeloCompilado();
      
      int nEntrada = this.entrada.quantidadeNeuronios() - (this.entrada.temBias() ? 1 : 0);
      if(entradas.length != nEntrada){
         throw new IllegalArgumentException("As dimensões dos dados de entrada com os neurônios de entrada da rede não são iguais.");
      }

      //carregar dados na camada de entrada
      for(int i = 0; i < nEntrada; i++){
         this.entrada.neuronio(i).saida = entradas[i];
      }
      
      //ativar neurônios das ocultas
      for(int i = 0; i < this.ocultas.length; i++){
         if(i == 0) this.ocultas[i].ativarNeuronios(this.entrada);
         else this.ocultas[i].ativarNeuronios(this.ocultas[i-1]);
      }

      //ativar neurônios da saída
      this.saida.ativarNeuronios(this.ocultas[this.ocultas.length-1]);
   }

   /**
    * Propaga os dados de entrada pela rede neural pelo método de feedforward através do conjunto 
    * de dados fornecido.
    * <p>
    *    Os dados são alimentados para as entradas dos neurônios e é calculado o produto junto 
    *    com os pesos. No final é aplicado a função de ativação da camada no neurônio e o resultado 
    *    fica armazenado na saída dele.
    * </p>
    * @param entradas dados usados para alimentar a camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se a quantidade de amostras em cada linha dos dados for diferente.
    * @throws IllegalArgumentException se o tamanho dos dados de entrada for diferente do tamanho dos 
    * neurônios de entrada, excluindo o bias.
    * @return matriz contendo os resultados das predições da rede.
    */
   public double[][] calcularSaida(double[][] entradas){
      this.modeloCompilado();

      int cols = entradas[0].length;
      for(int i = 1; i < entradas.length; i++){
         if(entradas[i].length != cols){
            throw new IllegalArgumentException("As dimensões dos dados de entrada são diferentes.");
         }
      }

      int nEntrada = this.entrada.quantidadeNeuronios() - (this.entrada.temBias() ? 1 : 0);
      if(entradas[0].length != nEntrada){
         throw new IllegalArgumentException("As dimensões dos dados de entrada com os neurônios de entrada da rede não são iguais.");
      }

      //dimensões dos dados
      int nAmostras = entradas.length;
      int nSaidas = this.saida.quantidadeNeuronios();

      double[][] resultados = new double[nAmostras][nSaidas];
      double[] entradaRede = new double[entradas[0].length];
      double[] saidaRede = new double[nSaidas];

      for(int i = 0; i < nAmostras; i++){//iterar pelos dados de entrada
         System.arraycopy(entradas[i], 0, entradaRede, 0, entradas[i].length);
         
         this.calcularSaida(entradaRede);
         saidaRede = this.obterSaidas();

         System.arraycopy(saidaRede, 0, resultados[i], 0, saidaRede.length);
      }

      return resultados;
   }

   /**
    * Treina a rede de acordo com as configurações predefinidas.
    * <p>
    *    Certifique-se de configurar adequadamente o modelo para obter os 
    *    melhores resultados.
    * </p>
    * @param entradas dados de entrada do treino (features).
    * @param saidas dados de saída correspondente a entrada (class).
    * @param epochs quantidade de épocas.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinar(double[][] entradas, double[][] saidas, int epochs){
      this.modeloCompilado();
      consistenciaDados(entradas, saidas);

      if(epochs < 1) throw new IllegalArgumentException("O valor de epochs deve ser maior que zero.");

      boolean embaralhar;
      if(otimizadorAtual.getClass().equals(rna.otimizadores.GradientDescent.class)) embaralhar = false;
      else embaralhar = true;
      
      //enviar clones pra não embaralhar os dados originais
      treinador.treino(this, this.otimizadorAtual, entradas.clone(), saidas.clone(), epochs, embaralhar);
   }

   /**
    * Treina a rede de acordo com as configurações predefinidas.
    * <p>
    *    O modo de treinamento em lote ainda ta em teste e costuma demorar pra convergir
    *    se forem usados os mesmo parâmetros do treino convencional.
    * </p>
    * Certifique-se de configurar adequadamente o modelo para obter os 
    * melhores resultados.
    * @param entradas dados de entrada do treino (features).
    * @param saidas dados de saída correspondente a entrada (class).
    * @param epochs quantidade de épocas.
    * @param tamanhoLote tamanho que o lote vai assumir durante o treino.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    */
   public void treinar(double[][] entradas, double[][] saidas, int epochs, int tamanhoLote){
      this.modeloCompilado();
      consistenciaDados(entradas, saidas);

      if(epochs < 1){
         throw new IllegalArgumentException("O valor de epochs não pode ser menor que um");
      }
      if(tamanhoLote <= 0 || tamanhoLote > entradas.length){
         throw new IllegalArgumentException("O valor de tamanho do lote é inválido.");
      }

      boolean embaralhar;
      if(otimizadorAtual.getClass().equals(rna.otimizadores.GradientDescent.class)) embaralhar = false;
      else embaralhar = true;

      //enviar clones pra não embaralhar os dados originais
      treinador.treino(this, this.otimizadorAtual, entradas.clone(), saidas.clone(), epochs, embaralhar, tamanhoLote);
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
    * @param epochs número de épocas do treinamento
    * @param custoMinimo valor de custo desejável, o treino será finalizado caso o valor de custo mínimo 
    * seja atingido. Caso o custo mínimo seja zero, o treino irá continuar até o final das épocas fornecidas
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se houver alguma inconsistência dos dados de entrada e saída para a operação.
    * @throws IllegalArgumentException se o valor de perturbação for igual a zero.
    * @throws IllegalArgumentException se o valor de épocas for menor que um.
    * @throws IllegalArgumentException se o valor de custo mínimo for menor que zero.
    */
   public void diferencaFinita(double[][] entradas, double[][] saidas, double eps, int epochs, double custoMinimo){
      this.modeloCompilado();
      consistenciaDados(entradas, saidas);

      
      if(eps == 0){
         throw new IllegalArgumentException("O valor de perturbação não pode ser igual a zero.");
      }
      if(epochs < 1){
         throw new IllegalArgumentException("O valor de epochs não pode ser menor que um.");
      }
      if(custoMinimo < 0){
         throw new IllegalArgumentException("O valor de custo mínimo não pode ser negativo.");
      }
      
      RedeNeural redeG = this.clone();//copia da rede para guardar os valores de "gradiente"
      
      //transformar as redes em arrays para facilitar
      Camada[] camadasR = new Camada[this.arquitetura.length];
      Camada[] camadasG = new Camada[this.arquitetura.length];
      
      //copiando as camadas das redes para os arrays
      camadasR[0] = this.entrada;
      camadasG[0] = redeG.obterCamadaEntrada();
      for(int i = 0; i < this.ocultas.length; i++){
         camadasR[i+1] = this.ocultas[i];
         camadasG[i+1] = redeG.obterCamadaOculta(i);
      }
      camadasR[camadasR.length-1] = this.saida;
      camadasG[camadasG.length-1] = redeG.obterCamadaSaida();
      
      double taxaAprendizagem = 0.01;

      for(int epocas = 0; epocas < epochs; epocas++){
         
         double custo = avaliador.erroMedioQuadrado(entradas, saidas);
         if(custo < custoMinimo) break;

         double valorAnterior = 0;
         for(int i = 0; i < camadasR.length; i++){//percorrer camadas da rede
            for(int j = 0; j < camadasR[i].quantidadeNeuronios(); j++){//percorrer neuronios da camada
               for(int k = 0; k < camadasR[i].neuronio(j).pesos.length; k++){//percorrer pesos do neuronio
                  valorAnterior = camadasR[i].neuronio(j).pesos[k];
                  camadasR[i].neuronio(j).pesos[k] += eps;
                  camadasG[i].neuronio(j).pesos[k] = ((avaliador.erroMedioQuadrado(entradas, saidas) - custo)/eps);//"derivada" da função de custo
                  camadasR[i].neuronio(j).pesos[k] = valorAnterior;
               }
            }
         }

         //atualizar pesos
         for(int i = 0; i < camadasR.length; i++){
            for(int j = 0; j < camadasR[i].quantidadeNeuronios(); j++){
               for(int k = 0; k < camadasR[i].neuronio(j).pesos.length; k++){
                  camadasR[i].neuronio(j).pesos[k] -= taxaAprendizagem * camadasG[i].neuronio(j).pesos[k];
               }
            }
         }
      }

   }

   /**
    * Retorna a instâcia de otimizador está sendo usado para o treino da Rede Neural.
    * @return otimizador atual da rede.
    */
   public Otimizador obterOtimizador(){
      return this.otimizadorAtual;
   }

   /**
    * Retorna a {@code camada de entrada} da Rede Neural.
    * @return camada de entrada.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public Camada obterCamadaEntrada(){
      this.modeloCompilado();
      return this.entrada;
   }

   /**
    * Informa a quantidade de camadas ocultas presentes na Rede Neural.
    * @return quantiade de camadas ocultas da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int obterQuantidadeOcultas(){
      this.modeloCompilado();
      return this.ocultas.length;
   }

   /**
    * Retorna a {@code camada oculta} da Rede Neural correspondente 
    * ao índice fornecido.
    * @param indice índice da busca.
    * @return camada oculta baseada na busca.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @throws IllegalArgumentException se o índice estiver fora do alcance do tamanho 
    * das camadas ocultas.
    */
   public Camada obterCamadaOculta(int indice){
      this.modeloCompilado();
      if((indice < 0) || (indice > this.ocultas.length-1)){
         throw new IllegalArgumentException("O índice fornecido está fora do alcance das camadas disponíveis");
      }
   
      return this.ocultas[indice];
   }

   /**
    * Retorna a {@code camada de saída} da Rede Neural.
    * @return camada de saída.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public Camada obterCamadaSaida(){
      this.modeloCompilado();
      return this.saida;
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
      this.modeloCompilado();

      double saida[] = new double[this.saida.quantidadeNeuronios()];
      for(int i = 0; i < saida.length; i++){
         saida[i] = this.saida.neuronio(i).saida;
      }

      return saida;
   }

   /**
    * Cria um array que representa a estrutura da Rede Neural. Nele cada elemento 
    * indica uma camada da rede e cada valor contido nesse elementos indica a 
    * quantidade de neurônios daquela camada correspondente.
    * <p>
    *    Os valores podem sofrer alteração caso a rede possua o bias adicionado 
    *    na hora da compilação, incluindo um neurônio a mais em cada elementos do array.
    * </p>
    * @return array com a arquitetura da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public int[] obterArquitetura(){
      this.modeloCompilado();
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
         throw new IllegalArgumentException("Deve ser habilitado o cálculo do histórico de custos para obter os resultados.");
      }
      return this.treinador.obterHistorico();
   }

   /**
    * Retorna a quantiade de pesos total da rede, incluindo conexões com bias.
    * @return quantiade de pesos total da rede.
    */
   public int obterQuantidadePesos(){
      int numConexoes = 0;
      for(Camada camada : this.ocultas){
         numConexoes += camada.numConexoes();
      }
      numConexoes += this.saida.numConexoes();

      return numConexoes;
   }

   public boolean temBias(){
      return (this.BIAS == 1) ? true : false;
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
    *       Função de ativação de todas as camadas ocultas.
    *    </li>
    *    <li>
    *       Função de ativação da camada de saída.
    *    </li>
    *    <li>
    *       Arquitetura da rede.
    *    </li>
    * </ul>
    * @return buffer formatado contendo as informações.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    */
   public String info(){
      this.modeloCompilado();

      String buffer = "";
      String espacamento = "    ";
      System.out.println("\nInformações " + this.nome + " = [");

      //otimizador
      buffer += espacamento + "Otimizador: " + this.otimizadorAtual.getClass().getSimpleName() + "\n";
      buffer += this.otimizadorAtual.info();

      //bias
      buffer += "\n" + espacamento + "Bias = ";
      buffer += (this.BIAS == 1) ? "true" : "false";
      buffer += "\n\n";

      for(int i = 0; i < this.ocultas.length; i++){
         buffer += espacamento + "Ativação oculta " + i + " : " + this.ocultas[i].obterAtivacao().getClass().getSimpleName() + "\n";
      }
      buffer += espacamento + "Ativação saída : " + this.saida.obterAtivacao().getClass().getSimpleName() + "\n";

      //arquitetura
      buffer += "\n" + espacamento + "arquitetura = [" + this.arquitetura[0];
      for(int i = 0; i < this.ocultas.length; i++) buffer += ", " + this.arquitetura[i+1];
      buffer += ", " + this.arquitetura[this.arquitetura.length-1] + "]";

      buffer += "\n]\n";

      return buffer;
   }
 
   /**
    * Clona a instância da rede.
    * @throws IllegalArgumentException se o modelo não foi compilado previamente.
    * @return Clone da rede
    */
   @Override
   public RedeNeural clone(){
      this.modeloCompilado();

      try{
         RedeNeural clone = (RedeNeural) super.clone();

         //dados importantes
         clone.BIAS = this.BIAS;
         clone.arquitetura = this.arquitetura;

         //entrada
         clone.entrada = cloneCamada(this.entrada);

         //ocultas
         int quantidadeOcultas = this.arquitetura.length-2;
         clone.ocultas = new Camada[quantidadeOcultas];
         for(int i = 0; i < quantidadeOcultas; i++){
            clone.ocultas[i] = cloneCamada(this.ocultas[i]);
         }

         //saída
         clone.saida = cloneCamada(this.saida);

         return clone;
      }catch(CloneNotSupportedException e){
         throw new RuntimeException(e);
      }
   }

   /**
    * Clona uma instância de camada da rede neural.
    * @param camada camada original
    * @return clone da camada fornecida.
    */
   private Camada cloneCamada(Camada camada){
      Camada clone = new Camada(camada.temBias());
      clone.neuronios = new Neuronio[camada.quantidadeNeuronios()];
      clone.ativacao = camada.ativacao;

      for (int i = 0; i < camada.quantidadeNeuronios(); i++) {
         clone.neuronios[i] = cloneNeuronio(camada.neuronio(i));
      }

      return clone;
   }

   /**
    * Clona uma instância de neurônio da rede neural.
    * @param neuronio neurônio original.
    * @return clone do neurônio fornecido.
    */
   private Neuronio cloneNeuronio(Neuronio neuronio){
      Neuronio clone = new Neuronio(neuronio.pesos.length, this.alcancePeso, this.inicializadorPeso);

      //método nativo mais eficiente na cópia de vetores
      System.arraycopy(neuronio.entradas, 0, clone.entradas, 0, clone.entradas.length);
      System.arraycopy(neuronio.pesos, 0, clone.pesos, 0, clone.pesos.length);
      System.arraycopy(neuronio.gradiente, 0, clone.gradiente, 0, clone.gradiente.length);
      System.arraycopy(neuronio.gradienteAcumulado, 0, clone.gradienteAcumulado, 0, clone.gradienteAcumulado.length); 

      return clone;
   }

   @Override
   public String toString(){
      this.modeloCompilado();

      String buffer = "";
      String espacamento = "   ";
      String espacamentoDuplo = espacamento + espacamento;
      String espacamentoTriplo = espacamento + espacamento + espacamento;
      
      buffer += "\nArquitetura " + nome + " = [\n";

      //ocultas
      for(int i = 0; i < this.ocultas.length; i++){

         buffer += espacamento + "Camada oculta " + i + " = [\n";
         for(int j = 0; j < this.ocultas[i].quantidadeNeuronios()-BIAS; j++){
            
            buffer += espacamentoDuplo + "n" + j + " = [\n";
            for(int k = 0; k < this.ocultas[i].neuronio(j).pesos.length; k++){
               if(k == this.ocultas[i].neuronio(j).pesos.length-1 && (this.BIAS == 1)){
                  buffer += espacamentoTriplo + "pb" + " = " + this.ocultas[i].neuronio(j).pesos[k] + "\n";
               
               }else{
                  buffer += espacamentoTriplo + "p" + k + " = " + this.ocultas[i].neuronio(j).pesos[k] + "\n";
               }
            }
            buffer += espacamentoDuplo + "]\n";

         }
         buffer += espacamento + "]\n\n";

      }

      //saida
      buffer += espacamento + "Camada saída = [\n";
      for(int i = 0; i < this.saida.quantidadeNeuronios(); i++){

         buffer += espacamentoDuplo + "n" + i + " = [\n";
         for(int j = 0; j < this.saida.neuronio(i).pesos.length; j++){
            if(j == this.saida.neuronio(i).pesos.length-1 && (this.BIAS == 1)){
               buffer += espacamentoTriplo + "pb" + " = " + this.saida.neuronio(i).pesos[j] + "\n";
            
            }else buffer += espacamentoTriplo + "p" + j + " = " + this.saida.neuronio(i).pesos[j] + "\n";
         }
         buffer += espacamentoDuplo + "]\n";

      }
      buffer += espacamento + "]\n";

      buffer += "]\n";

      return buffer;
   }
}
