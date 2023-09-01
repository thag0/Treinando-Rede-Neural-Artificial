package utilitarios.ged;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;


/**
 * Classe criada para centralizar um tipo de dado 
 * específico para uso dentro do Ged.
 * <p>
 *    O objeto de dados possui um conjunto de pequenas ferramentas para manipulação 
 *    e visualização de conteudo. Algumas operações mais elaboradas podem ser executadas
 *    usando o Gerenciador de Dados {@code Ged}.
 * </p>
 * @see https://github.com/thag0/Treinando-Rede-Neural-Artificial/tree/main/utilitarios/ged
 * @author Thiago Barroso, acadêmico de Engenharia da Computação pela Universidade Federal do Pará, Campus Tucuruí. Maio/2023.
 */
public class Dados{

   /**
    * <p>
    *    Região crítica.
    * </p>
    * Estrutura que armazena o conteúdo de dados lidos
    */
   private ArrayList<String[]> conteudo;

   /**
    * Quantidade de alterações feitas no conjunto de dados.
    */
   private int qAlteracoes = 0;

   /**
    * Nome personalizável.
    */
   private String nome = this.getClass().getSimpleName();


   /**
    * Inicializa um objeto do tipo Dados com seu conteúdo vazio.
    * <p>
    *    {@code Dados} é um objeto criado para armazenar informações de conjuntos de 
    *    dados, possui algumas funcionalidades mais básicas e é uma dependência para 
    *    algumas funcionalidades presentes dentro do {@code Ged}.
    * </p>
    */
   public Dados(){
      this.conteudo = new ArrayList<>();
   }


   /**
    * Inicializa um objeto do tipo Dados de acordo com o conteúdo especificado.
    * <p>
    *    {@code Dados} é um objeto criado para armazenar informações de conjuntos de 
    *    dados, possui algumas funcionalidades mais básicas e é uma dependência para 
    *    algumas funcionalidades presentes dentro do {@code Ged}.
    * </p>
    */
   public Dados(int[][] conteudo){
      this.atribuir(conteudo);
   }


   /**
    * Inicializa um objeto do tipo Dados de acordo com o conteúdo especificado.
    * <p>
    *    {@code Dados} é um objeto criado para armazenar informações de conjuntos de 
    *    dados, possui algumas funcionalidades mais básicas e é uma dependência para 
    *    algumas funcionalidades presentes dentro do {@code Ged}.
    * </p>
    */
   public Dados(float[][] conteudo){
      this.atribuir(conteudo);
   }


   /**
    * Inicializa um objeto do tipo Dados de acordo com o conteúdo especificado.
    * <p>
    *    {@code Dados} é um objeto criado para armazenar informações de conjuntos de 
    *    dados, possui algumas funcionalidades mais básicas e é uma dependência para 
    *    algumas funcionalidades presentes dentro do {@code Ged}.
    * </p>
    */
   public Dados(double[][] conteudo){
      this.atribuir(conteudo);
   }


   /**
    * Inicializa um objeto do tipo Dados de acordo com o conteúdo especificado.
    * <p>
    *    {@code Dados} é um objeto criado para armazenar informações de conjuntos de 
    *    dados, possui algumas funcionalidades mais básicas e é uma dependência para 
    *    algumas funcionalidades presentes dentro do {@code Ged}.
    * </p>
    */
   public Dados(String[][] conteudo){
      this.atribuir(conteudo);
   }


   /**
    * Inicializa um objeto do tipo Dados de acordo com o conteúdo especificado.
    * <p>
    *    {@code Dados} é um objeto criado para armazenar informações de conjuntos de 
    *    dados, possui algumas funcionalidades mais básicas e é uma dependência para 
    *    algumas funcionalidades presentes dentro do {@code Ged}.
    * </p>
    */
   public Dados(ArrayList<String[]> conteudo){
      this.atribuir(conteudo);
   }


   /**
    * Configura um novo nome personalizado para o conjunto de dados.
    * @param nome novo nome do conjunto de dados.
    * @throws IllegalArgumentException se novo nome for nulo.
    * @throws IllegalArgumentException se novo estiver vazio ou em branco.
    */
   public void editarNome(String nome){
      if(nome == null){
         throw new IllegalArgumentException("O novo nome não pode ser nulo.");
      }
      if(nome.isBlank() || nome.isEmpty()){
         throw new IllegalArgumentException("O novo nome não pode estar vazio ou em branco.");
      }
      this.nome = nome;
      this.qAlteracoes++;
   }


   /**
    * Retorna o item correspondente pela linha e coluna fornecidos.
    * @param lin linha para busca.
    * @param col coluna para busca.
    * @return valor contido com base na linha e coluna.
    * @throws IllegalArgumentException se o conteúdo estiver vazio.
    * @throws IllegalArgumentException se os índices fornecidos estiverem fora de alcance.
    */
   public String obterItem(int lin, int col){
      if(this.conteudo.isEmpty()){
         throw new IllegalArgumentException("O conteúdo está vazio.");
      }
      if(lin < 0 || lin >= this.conteudo.size()){
         throw new IllegalArgumentException("Índice de busca de linha inválido");
      }
      if(col < 0 || col >= this.conteudo.get(lin).length){
         throw new IllegalArgumentException("Índice de busca de coluna inválido");
      }

      return this.conteudo.get(lin)[col];
   }


   /**
    * Edita o valor contido no espaço indicado pela linha e coluna.
    * @param lin linha para edição.
    * @param col coluna para edição.
    * @param valor novo valor.
    * @throws IllegalArgumentException se os índices fornecidos forem inválidos.
    */
   public void editarItem(int lin, int col, String valor){
      if(lin < 0 || lin >= this.conteudo.size()){
         throw new IllegalArgumentException("Índice de linha fornecido é inválido.");
      }
      if(col < 0 || col >= this.conteudo.get(lin).length){
         throw new IllegalArgumentException("Índice de coluna fornecido é inválido.");
      }

      String[] linha = this.conteudo.get(lin);
      linha[col] = valor;
      this.conteudo.set(lin, linha);
      this.qAlteracoes++;
   }


   /**
    * Edita o valor em todas as linhas de acordo com a coluna especificada.
    * @param col coluna para edição.
    * @param busca valor alvo.
    * @param valor novo valor.
    * @throws IllegalArgumentException se o conteúdo dos dados não for simétrico.
    * @throws IllegalArgumentException se o índice de coluna for inválido.
    */
   public void editarItem(int col, String busca, String valor){
      if(!this.simetrico()){
         throw new IllegalArgumentException("O conteúdo dos dados deve ser simétrico.");
      }
      if(col < 0 || col >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("O índice de coluna fornecido é inválido.");
      }

      for(String[] linha : this.conteudo){
         if(linha[col].contains(busca)){
            linha[col] = valor;
         }
      }

      this.qAlteracoes++;
   }


   /**
    * Substitui todo o conteúdo atual de Dados pela nova lista.
    * @param lista lista com os novos dados.
    */
   public void atribuir(ArrayList<String[]> lista){
      if(lista != null){
         this.conteudo = lista;
         this.qAlteracoes++;
      }
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * <p>
    *    Os dados contidos na matriz devem ser simétricos, o que quer dizer 
    *    que eles devem possuir a mesma quantidade de colunas para todas as 
    *    linhas presentes.
    * </p>
    * @param matriz matriz com os dados.
    * @throws IllegalArgumentException se a matriz for vazia.
    * @throws IllegalArgumentException se a matriz não for simétrica.
    */
   public void atribuir(int[][] matriz){
      int linhas = matriz.length;
      if(linhas == 0){
         throw new IllegalArgumentException("A matriz fornecida está vazia.");
      }

      int colunas = matriz[0].length;
      for(int i = 1; i < linhas; i++){
         if(matriz[i].length != colunas){
            throw new IllegalArgumentException("A matriz deve conter o mesmo número de colunas para todas as linhas.");
         }
      }

      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Integer.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
      this.qAlteracoes++;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * <p>
    *    Os dados contidos na matriz devem ser simétricos, o que quer dizer 
    *    que eles devem possuir a mesma quantidade de colunas para todas as 
    *    linhas presentes.
    * </p>
    * @param matriz matriz com os dados.
    * @throws IllegalArgumentException se a matriz for vazia.
    * @throws IllegalArgumentException se a matriz não for simétrica.
    */
   public void atribuir(float[][] matriz){
      int linhas = matriz.length;
      if(linhas == 0){
         throw new IllegalArgumentException("A matriz fornecida está vazia.");
      }

      int colunas = matriz[0].length;
      for(int i = 1; i < linhas; i++){
         if(matriz[i].length != colunas){
            throw new IllegalArgumentException("A matriz deve conter o mesmo número de colunas para todas as linhas.");
         }
      }

      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Float.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
      this.qAlteracoes++;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * <p>
    *    Os dados contidos na matriz devem ser simétricos, o que quer dizer 
    *    que eles devem possuir a mesma quantidade de colunas para todas as 
    *    linhas presentes.
    * </p>
    * @param matriz matriz com os dados.
    * @throws IllegalArgumentException se a matriz for vazia.
    * @throws IllegalArgumentException se a matriz não for simétrica.
    */
   public void atribuir(double[][] matriz){
      int linhas = matriz.length;
      if(linhas == 0){
         throw new IllegalArgumentException("A matriz fornecida está vazia.");
      }

      int colunas = matriz[0].length;
      for(int i = 1; i < linhas; i++){
         if(matriz[i].length != colunas){
            throw new IllegalArgumentException("A matriz deve conter o mesmo número de colunas para todas as linhas.");
         }
      }

      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = Double.toString(matriz[i][j]);
         }
         lista.add(linha);
      }

      this.conteudo = lista;
      this.qAlteracoes++;
   }


   /**
    * Atribui os valores contidos na matriz fonercida ao 
    * conteúdo de Dados.
    * <p>
    *    Os dados contidos na matriz devem ser simétricos, o que quer dizer 
    *    que eles devem possuir a mesma quantidade de colunas para todas as 
    *    linhas presentes.
    * </p>
    * @param matriz matriz com os dados.
    * @throws IllegalArgumentException se a matriz for vazia.
    * @throws IllegalArgumentException se a matriz não for simétrica.
    */
   public void atribuir(String[][] matriz){
      int linhas = matriz.length;
      if(linhas == 0){
         throw new IllegalArgumentException("A matriz fornecida está vazia.");
      }

      int colunas = matriz[0].length;
      for(int i = 1; i < linhas; i++){
         if(matriz[i].length != colunas){
            throw new IllegalArgumentException("A matriz deve conter o mesmo número de colunas para todas as linhas.");
         }
      }

      ArrayList<String[]> lista = new ArrayList<>();

      for (int i = 0; i < linhas; i++) {
         String[] linha = new String[colunas];
         for (int j = 0; j < colunas; j++) {
            linha[j] = matriz[i][j];
         }
         lista.add(linha);
      }

      this.conteudo = lista;
      this.qAlteracoes++;
   }


   /**
    * Retorna todo o conteúdo presente nos dados.
    * @return estrutura {@code ArrayList<String[]>} que armazena o conteúdo dos dados.
    */
   public ArrayList<String[]> conteudo(){
      return this.conteudo;
   }


   /**
    * Retorna todo o conteúdo da coluna de acordo com o índice fornecido.
    * @param idCol índice da coluna desejada.
    * @return coluna completa.
    * @throws IllegalArgumentException se o conteúdo dos dados não for simétrico. 
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public String[] coluna(int idCol){
      if(!this.simetrico()){
         throw new IllegalArgumentException("O conteúdo dos dados deve ser simétrico.");
      }
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice da coluna fornecido é inválido.");
      }

      return this.conteudo.get(idCol);
   }


   /**
    * Aumenta em uma unidade a quantidade de alterações
    * feitas dentro do conjunto de dados.
    */
   public void incrementarAlteracao(){
      this.qAlteracoes++;
   }


   /**
    * Verifica se o conteúdo do conjunto de dados está vazio.
    * <p>
    *    Ele é considerado vazio se não conter nenhuma linha ou 
    *    se todas as linhas estiverem vazias.
    * </p>
    * @return true se o conjunto de dados estiver vazio, false caso contrário.
    */
   public boolean vazio(){
      if(this.conteudo.isEmpty()) return true;
      
      for(int i = 0; i < this.conteudo.size(); i++){
         if(this.conteudo.get(i).length > 0) return false;
      }

      return true;
   }


   /**
    * Calula a média do conteúdo que pode ser transformado para valor numérico 
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return média dos elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double media(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      double soma = 0;
      int contador = 0;//contador de ocasiôes transformadas

      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         
         try{
            double valorTransformado = Double.parseDouble(valor);
            soma += valorTransformado;
            contador++;

         }catch(Exception e){

         }
      }

      if(contador == 0) return 0;

      return soma / contador;
   }


   /**
    * Calcula a mediana do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor da mediana dos elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double mediana(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      ArrayList<Double> valoresNumericos = new ArrayList<>();

      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            double valorTransformado = Double.parseDouble(valor);
            valoresNumericos.add(valorTransformado);
         
         }catch(NumberFormatException e){
         
         }
      }  

      if(valoresNumericos.isEmpty()) return 0;

      Collections.sort(valoresNumericos);
      int meio = valoresNumericos.size() / 2;

      if(valoresNumericos.size() % 2 == 0){
         return (valoresNumericos.get(meio-1) + valoresNumericos.get(meio))/2;
      
      }else{
         return valoresNumericos.get(meio);
      }
   }


   /**
    * Calcula a moda do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor da moda dos elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double moda(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      HashMap<String, Integer> frequenciaElementos = new HashMap<>();

      //frequência de cada valor na coluna
      for(String[] linha : this.conteudo){
         
         String valor = linha[idCol].trim();
         if(!valor.isEmpty()){
            try{
               Double.parseDouble(valor);
               if(frequenciaElementos.containsKey(valor)){//elemento existente
                  frequenciaElementos.put(valor, frequenciaElementos.get(valor) + 1);
                  
               }else{//elemento novo
                  frequenciaElementos.put(valor, 1);
               }

            }catch(Exception e){

            }
         }
      }

      if (frequenciaElementos.isEmpty()) return 0;

      String moda = "";
      int maxFrequencia = 0;

      // Encontra o valor com maior frequência
      for(String valor : frequenciaElementos.keySet()){
         
         int frequencia = frequenciaElementos.get(valor);
         if(frequencia > maxFrequencia){
            maxFrequencia = frequencia;
            moda = valor;
         }
      }

      if(moda.isEmpty()) return 0;

      try{
         return Double.parseDouble(moda);
      
      }catch(NumberFormatException e){
         return 0;
      }
   }


   /**
    * Calcula o desvio padrão dos valores numéricos presentes na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return desvio padrão dos valores numéricos na coluna.
    * @throws IllegalArgumentException Se o índice fornecido for inválido.
    */
   public double desvioPadrao(int idCol){
      if(!this.simetrico()){
         throw new IllegalArgumentException("O conteúdo dos dados deve ser simétrico.");
      }
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      ArrayList<Double> valoresNumericos = new ArrayList<>();

      //valores numéricos da coluna
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            double valorTransformado = Double.parseDouble(valor);
            valoresNumericos.add(valorTransformado);
         }catch(NumberFormatException e){
            //ignorar
         }
      }

      //média dos valores
      double media = 0;
      int contador = 0;
      for(double valor : valoresNumericos){
         media += valor;
         contador++;
      }
      if(contador > 0) media /= contador;
      else return 0;//não houver valores numéricos.

      //somatório dos quadrados das diferenças entre os valores e a média.
      double somaDiferencasQuadrado = 0;
      for(double valor : valoresNumericos){
         double diferenca = valor - media;
         somaDiferencasQuadrado += diferenca * diferenca;
      }

      //desvio padrão.
      double desvioPadrao = Math.sqrt(somaDiferencasQuadrado / contador);
      return desvioPadrao;
   }


   /**
    * Calcula o maior valor do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor máximo entre os elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double maximo(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      ArrayList<Double> valoresNumericos = new ArrayList<>();
  
      //transformando o que dá em valor
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            double num = Double.parseDouble(valor);
            valoresNumericos.add(num);
         
         }catch(NumberFormatException e){
            
         }
      }
  
      if(valoresNumericos.isEmpty()) return 0;
      double valorMaximo = valoresNumericos.get(0);

      //procurando valor maior
      for(int i = 1; i < valoresNumericos.size(); i++){
         if(valoresNumericos.get(i) > valorMaximo){
            valorMaximo = valoresNumericos.get(i);
         }
      }
  
      return valorMaximo;
   }


   /**
    * Calcula o menor valor do conteúdo que pode ser transformado para valor numérico
    * presente na coluna especificada.
    * @param idCol índice da coluna desejada.
    * @return valor mínimo entre os elementos numéricos presentes na coluna, 0 caso não seja possível converter nenhum valor.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public double minimo(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      ArrayList<Double> valoresNumericos = new ArrayList<>();
  
      //transformando o que dá em valor
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            double num = Double.parseDouble(valor);
            valoresNumericos.add(num);
         
         }catch(NumberFormatException e){
            
         }
      }
  
      if(valoresNumericos.isEmpty()) return 0;
      double valorMaximo = valoresNumericos.get(0);

      //procurando valor menor
      for(int i = 1; i < valoresNumericos.size(); i++){
         if(valoresNumericos.get(i) < valorMaximo){
            valorMaximo = valoresNumericos.get(i);
         }
      }
  
      return valorMaximo;
   }


   /**
    * Normaliza os valores numéricos contido na coluna fornecida.
    * <p>
    *    Caso a coluna possua algum valor que não possa ser convertido o 
    *    processo é cancelado.
    * </p>
    * Exemplo:
    * <pre>
    * d = [
    *    1, 5 
    *    2, a
    *    3, 7
    *    4, 8
    *    5, 9
    * ]
    *
    * d.normalizar(0);
    *
    * d = [
    *    0.00, 5 
    *    0.25, a
    *    0.50, 7
    *    0.75, 8
    *    1.00, 9
    * ]
    * </pre>
    * @param idCol índice da coluna desejada.
    * @throws IllegalArgumentException se o conteúdo dos dados estiver vazio.
    * @throws IllegalArgumentException se o conteúdo não for simétrico.
    */
   public void normalizar(int idCol){
      if(this.vazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }
      if(!this.simetrico()){
         throw new IllegalArgumentException("Os dados devem ser simétricos para normalização.");
      }

      if(this.contemNaoNumericos(idCol)) return;
          
      double min = minimo(idCol);
      double max = maximo(idCol);

      for(String[] linha : this.conteudo){
         double valor = Double.parseDouble(linha[idCol]);
         double valorNormalizado = (valor - min) / (max - min);
         linha[idCol] = Double.toString(valorNormalizado);
      }
      this.qAlteracoes++;
   }


   /**
    * Capitaliza todo o conteúdo alfabético contido na coluna fornecida.
    * @param idCol índice da coluna desejada.
    * @throws IllegalArgumentException se o conteúdo dos dados estiver vazio.
    * @throws IllegalArgumentException se os dados não forem simétricos.
    * @throws IllegalArgumentException e o índice da coluna for inválido.
    */
   public void capitalizar(int idCol){
      if(this.vazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }
      if(!this.simetrico()){
         throw new IllegalArgumentException("Os dados devem ser simétricos para normalização.");
      }
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido da coluna é inválido.");
      }

      for(String[] linha : this.conteudo){
         linha[idCol] = cap(linha[idCol]);
      }
      this.qAlteracoes++;
   }


   /**
    * Captaliza a palavra.
    * @param palavra palavra desejada.
    * @return nova palavra captalizada com base na forncedida.
    */
   private String cap(String palavra){
      if(palavra == null || palavra.isEmpty()) return palavra;

      char[] caracteres = palavra.toCharArray();

      //primeiro caractere é maiúsculo e os demais são minúsculos.
      caracteres[0] = Character.toUpperCase(caracteres[0]);
      for(int i = 1; i < caracteres.length; i++){
         caracteres[i] = Character.toLowerCase(caracteres[i]);
      }

      return new String(caracteres);
   }


   /**
    * Substitui pelo novo valor todo o conteúdo encontrado na linha de acordo com a busca.
    * <p>
    *    Exemplo:
    * </p>
    * <pre>
    * d = [
    *    a.b, c.d
    *    e.f, g.h  
    * ]
    * 
    * substituir(0, ".", "");
    * 
    * d = [
    *    ab, c.d
    *    ef, g.h  
    * ]
    * </pre>
    * @param idCol índice da coluna desejada.
    * @param busca valor desejado para substituição.
    * @param valor novo valor que ficará no lugar do valor buscado.
    * @throws IllegalArgumentException se o conteúdo dos dados estiver vazio.
    * @throws IllegalArgumentException se o conteúdo dos dados não forem simétricos.
    * @throws IllegalArgumentException se o índice da coluna for inválido.
    */
   public void substituir(int idCol, String busca, String valor){
      if(this.vazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }
      if(!this.simetrico()){
         throw new IllegalArgumentException("O conteúdo dos dados deve ser simétrico.");
      }
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("O índice fornecido é inválido.");
      }

      for(String[] linha : this.conteudo){
         if(linha[idCol].contains(busca)){
            linha[idCol] = linha[idCol].replace(busca, valor);
         }
      }
      this.qAlteracoes++;
   }


   /**
    * Ordena o conteúdo contido nos dados de acordo com a coluna desejada.
    * <p>
    *    A ordenação consequentemente irá mudar a ordem de organização
    *    dos outros elementos.
    * </p>
    * Exemplo:
    * <pre>
    * d = [
    *    30
    *    40
    *    10
    *    50
    *    20
    * ]
    * 
    * d.ordenar(0, true).
    * 
    * d = [
    *    10
    *    20
    *    30
    *    40
    *    50
    * ]
    * </pre>
    * @param idCol índice da coluna desejada.
    * @param crescente true caso a ordenação deva ser crescente, false caso contrário.
    * @throws IllegalArgumentException se o conteúdo dos dados estiver vazio.
    * @throws IllegalArgumentException se o conteúdo dos dados não forem simétricos.
    * @throws IllegalArgumentException se o índice da coluna for inválido.
    * @throws IllegalArgumentException se a coluna conter valores que não possam ser convertidos para números.
    */
   public void ordenar(int idCol, boolean crescente){
      if(this.vazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }
      if(!this.simetrico()){
         throw new IllegalArgumentException("O conteúdo dos dados deve ser simétrico.");
      }
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("O índice da coluna fornecido é inválido.");
      }
      if(this.contemNaoNumericos(idCol)){
         throw new IllegalArgumentException("A coluna contém valores não numéricos.");
      }

      //isso é novo pra mim
      Collections.sort(this.conteudo, (linha1, linha2) -> {
         String valor1 = linha1[idCol];
         String valor2 = linha2[idCol];
         return crescente ? valor1.compareTo(valor2) : valor2.compareTo(valor1);
      });

      this.qAlteracoes++;
   }


   /**
    * Ordena o conteúdo contido nos dados de acordo com a coluna desejada.
    * <p>
    *    A ordenação consequentemente irá mudar a ordem de organização
    *    dos outros elementos.
    * </p>
    * Exemplo:
    * <pre>
    * d = [
    *    d
    *    b
    *    a
    *    e
    *    c
    * ]
    * 
    * d.ordenarAlfabetico(0, true).
    * 
    * d = [
    *    a
    *    b
    *    c
    *    d
    *    e
    * ]
    * </pre>
    * @param idCol índice da coluna desejada.
    * @param crescente true caso a ordenação deva ser crescente, false caso contrário.
    * @throws IllegalArgumentException se o conteúdo dos dados estiver vazio.
    * @throws IllegalArgumentException se o conteúdo dos dados não forem simétricos.
    * @throws IllegalArgumentException se o índice da coluna for inválido.
    */
   public void ordenarAlfabetico(int idCol, boolean crescente){
      if(this.vazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }
      if(!this.simetrico()){
         throw new IllegalArgumentException("O conteúdo dos dados deve ser simétrico.");
      }
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("O índice da coluna fornecido é inválido.");
      }

      Comparator<String[]> comp = (linha1, linha2) -> {
         String valor1 = linha1[idCol];
         String valor2 = linha2[idCol];

         Locale regiao = new Locale("pt", "BR");//configurando língua e região
         Collator collator = Collator.getInstance(regiao);
         collator.setStrength(Collator.TERTIARY);

         return crescente ? collator.compare(valor1, valor2) : collator.compare(valor2, valor1);
      };

      Collections.sort(this.conteudo, comp);
      this.qAlteracoes++;
   }


   /**
    * Retorna um array contendo as linhas e colunas do conteúdo dos dados.
    * <p>
    *    {@code shape[0] = linhas}
    * </p>
    * <p>
    *    {@code shape[1] = colunas}
    * </p>
    * @return estrutura contendo o formato da lista, considerando que ela é simétrica.
    * @throws IllegalArgumentException se o conteúdo estiver vazio.
    * @throws IllegalArgumentException se o conteúdo não for simétrico.
    */
   public int[] shape(){
      if(this.vazio()){
         throw new IllegalArgumentException("O conteúdo dos dados está vazio.");
      }

      if(!this.simetrico()){
         throw new IllegalArgumentException("O conteúdo dos dados não é simétrico.");
      }

      int[] shape = {
         this.conteudo.size(), 
         this.conteudo.get(0).length
      };
      return shape;
   }


   /**
    * Retorna um buffer contendo as informações do conteúdo dos dados, onde:
    * <p>
    *    {@code shape = [linhas, colunas]}
    * </p>
    * @return buffer contendo o formato da lista, considerando que ela é simétrica.
    */
   public String shapeInfo(){
      if(this.vazio()){
         return "[ (Vazio) ]";
      }

      int[] shape = {
         this.conteudo.size(), 
         this.conteudo.get(0).length
      };

      return "[" + shape[0] + ", " + shape[1] + "]";
   }


   /**
    * Método de impressão básico, via console, do conteúdo
    * contido em formato de tabela.
    * <p>
    *    Caso os dados sejam simétricos, também é exibido o formato do conteúdo.
    * </p>
    */
   public void imprimir(){
      String espacamento = "   ";

      if(this.vazio()){
         System.out.println(this.nome + " = [");
         System.out.println(espacamento + "(Vazio)");
         
      }else{
         if(this.simetrico()){
            int[] shape = this.shape();
            System.out.println(this.nome + " (" + shape[0] + ", " + shape[1] + ") = [");

         }else{
            System.out.println(this.nome + " = [");
         }

         for(String linha[] : this.conteudo){
            for(int i = 0; i < linha.length; i++){
               System.out.print(espacamento + linha[i] + "\t");
            }
            System.out.println();
         }
      }
      
      System.out.println("]\n");
   }


   /**
    * Exibe algumas informações sobre o conjunto de dados no geral.
    * <p>
    *    As informações incluem:
    * </p>
    * <ul>
    *    <li>Formato dos dados;</li>
    *    <li>Existência de valores não numéricos;</li>
    *    <li>Quantidade de valores ausentes;</li>
    * </ul>
    * @return buffer formatado contendo as informações.
    */
   public String info(){
      String espacamento = "    ";
      String formatacao = "\t\t";

      String buffer = "Informações do conjunto de dados = [\n";

      if(this.nome == this.getClass().getSimpleName()){
         buffer += espacamento + "Nome padrão\n";
      
      }else buffer += espacamento + "Nome:" + formatacao + "\"" +this.nome + "\"\n";

      if(this.vazio()){
         buffer += espacamento + "Conteúdo vazio.\n]"; 
         return buffer;
      
      }else{
         if(this.simetrico()){
            int[] shape = this.shape();
            buffer += espacamento + "Linhas: " + formatacao + shape[0] + "\n";
            buffer += espacamento + "Colunas: " + formatacao + shape[1] + "\n";
         
         }else{
            buffer += espacamento + "Tamanho inconsistente \n";
         }
      }
      buffer += espacamento + "Alterações:" + formatacao + this.qAlteracoes + "\n";
      buffer += "\n";


      boolean apenasNumericos = true;
      for(int i = 0; i < this.conteudo.get(0).length; i++){
         if(contemNaoNumericos(i)){
            apenasNumericos = false;
            break;
         }
      }
      buffer += espacamento + "Numéricos:" + formatacao + ((apenasNumericos) ? "sim" : "não") + "\n";

      boolean temValoresAusentes = false;
      for(int i = 0; i < this.conteudo.get(0).length; i++){
         int valoresAusentes = ausentes(i);
         if (valoresAusentes > 0) {
            buffer += espacamento + "Ausentes coluna " + i + ": \t" + valoresAusentes + "\n";
            temValoresAusentes = true;
         }
      }
  
      if(!temValoresAusentes){
         buffer += espacamento + "Valores Ausentes: \tnão\n";
      }
      
      buffer += "]\n";

      return buffer;
   }


   /**
    * Exibe uma visão geral das informações da coluna especificada.
    * <p>
    *    As informações incluem:
    * </p>
    * <ul>
    *    <li>Média;</li>
    *    <li>Mediana;</li>
    *    <li>Valor máximo;</li>
    *    <li>Valor mínimo;</li>
    *    <li>Moda;</li>
    *    <li>Desvio Padrão;</li>
    *    <li>Coluna composta apenas por valores numéricos;</li>
    *    <li>Coluna contém valores ausentes ou em branco;</li>
    * </ul>
    * @param idCol índice da coluna desejada.
    * @return buffer formatado contendo informações da coluna.
    * @throws IllegalArgumentException se o índice fornecido for inválido.
    */
   public String info(int idCol){
      if(idCol < 0 || idCol > this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }

      String espacamento = "   ";

      String buffer = "Coluna " + idCol + ": [\n";
      buffer += espacamento + "Média: \t\t" + media(idCol) + "\n";
      buffer += espacamento + "Mediana: \t\t" + mediana(idCol) + "\n";
      buffer += espacamento + "Máximo: \t\t" + maximo(idCol) + "\n";
      buffer += espacamento + "Mínimo: \t\t" + minimo(idCol) + "\n";
      buffer += espacamento + "Moda: \t\t" + moda(idCol) + "\n";
      buffer += espacamento + "Desv Padrão: \t" + desvioPadrao(idCol) + "\n";
      buffer += espacamento + "Numéricos: \t\t" + (!contemNaoNumericos(idCol) ? "sim" : "não") + "\n";
      buffer += espacamento + "Ausentes: \t\t" + ausentes(idCol) + "\n";
      buffer += "]\n";

      return buffer;
   }


   /**
    * Retorna o nome personalizado do conjunto de dados.
    * @return nome personalizado do conjunto de dados.
    */
   public String nome(){
      return this.nome;
   }


   /**
    * Verifica se a coluna indicada possui algum valor que não possa
    * ser convertido para um valor numérico.
    * @param idCol índice da coluna desejada.
    * @return verdadeiro caso a coluna possua valores que não possam ser convertidos, falso caso contrário.
    * @throws IllegalArgumentException se o índice for inválido.
    */
   public boolean contemNaoNumericos(int idCol){
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }
  
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         try{
            Double.parseDouble(valor);
         
         }catch(NumberFormatException e){
            return true;
         }
      }
  
      return false;
   }


   /**
    * Verifica a quantidade de valores ausentes presentes na coluna especificada.
    * <p>
    *    São considerados dados ausentes quaisquer valores que sejam {@code vazio},
    *    {@code em branco} ou {@code com "?"}
    * </p>
    * @param idCol índice da coluna desejada.
    * @return quantidade de valores considerados ausentes.
    * @throws IllegalArgumentException se o índice for inválido.
    */
   public int ausentes(int idCol){
      if(idCol < 0 || idCol >= this.conteudo.get(0).length){
         throw new IllegalArgumentException("Índice fornecido inválido.");
      }
  
      int contador = 0;
      for(String[] linha : this.conteudo){
         String valor = linha[idCol];
         if(valor.equals("?") || valor.trim().isEmpty()){
            contador++;
         }
      }
  
      return contador;
   }
 

   /**
    * Verifica se o conteúdo dos dados é simetrico. A simetria leva em conta 
    * se todas as colunas têm o mesmo tamanho.
    * <p>
    *    A simetria também leva em conta se o conteúdo dos dados possui elementos, 
    *    caso o tamanho seja zero será considerada como não simétrica.
    * <p>
    * <p>
    *    Dados nulos não classificados.
    * <p>
    * Exemplo:
    * <pre>
    * a =  [
    *    1, 2, 3
    *    4, 5, 6, 7
    *    8, 9
    * ]
    * 
    * a.simetrico() == false
    *
    * b =  [
    *    1, 2, 3
    *    4, 5, 6
    *    7, 8, 9
    * ]
    * 
    * b.simetrico() == true
    * </pre>
    * @param dados conjunto de dados.
    * @return true caso os dados sejam simétricos, false caso contrário.
    * @throws IllegalArgumentException se o conteúdo dos dados for nulo.
    */
   public boolean simetrico(){
      if(this.conteudo == null){
        throw new IllegalArgumentException("O conteúdo dos dados é nulo."); 
      } 
      
      //lista sem dados é considerada como não simétrica
      if(this.conteudo.size() == 0) return false;

      int colunas = this.conteudo.get(0).length;// tamanho base
      for(String[] linha : this.conteudo){
         if(linha.length != colunas) return false;
      }

      return true;
   }


   /**
    * Clona o conteúdo em uma nova estrutura, devolvendo um novo objeto
    * de {@code Dados} com o mesmo conteúdo.
    * @return novo objeto do tipo {@code Dados} com a cópia do conteúdo.
    */
   public Dados clonar(){
      ArrayList<String[]> cloneConteudo = new ArrayList<>();
      for(String[] linha : this.conteudo){
         cloneConteudo.add(linha);
      }
      Dados cloneDados = new Dados(cloneConteudo);

      return cloneDados;
   }
}
