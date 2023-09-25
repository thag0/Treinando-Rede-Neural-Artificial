package render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import rna.estrutura.Camada;
import rna.estrutura.Neuronio;
import rna.estrutura.RedeNeural;

public class PainelRede extends JPanel{
   final int largura = 500;
   final int altura = 400;
   Graphics2D g2;
   
   public RedeNeural rede;

   //coordenadas de origem dos neuronios
   ArrayList<ArrayList<Coordenada>> coordenadas = new ArrayList<>();
   int contadorCoordenadas = 0;
   int contadorCamadas = 0;

   //auxilinar na inicialização
   Coordenada c = new Coordenada(0, 0, 0);
   //auxiliar no desenho das conexões
   Coordenada c1;
   Coordenada c2;

   //desenho
   int contador = 0;
   int contador2 = 0;
   int x0 = 0;//posição x base de desenho da rede
   int y0 = 0;//posição y base de desenho da rede
   int x = 0;
   int y = 0;
   int larguraDesenho = 26;
   int alturaDesenho = larguraDesenho;
   int espacoVerticalEntreNeuronio = 7;
   int espacoHorizontalEntreCamadas = (int)(larguraDesenho * 2.3);
   String texto = "";

   //cores
   int r = 150;
   int g = 180;
   int b = 210;
   Color corNeuronioAtivo = new Color(r, g, b);
   Color corNeuronioInativo = new Color((int)(r * 0.4), (int)(g * 0.4), (int)(b * 0.4));
   Color corBordaNeuronio = Color.BLACK;

   Color corConexaoAtiva = new Color((int)(r * 0.7), (int)(g * 0.7), (int)(b * 0.7));
   Color corConexaoInativa = new Color((int)(r * 0.25), (int)(g * 0.25), (int)(b * 0.25));

   Color corTexto = new Color(r, g, b);

   //auxiliares
   BasicStroke linhaDesenho = new BasicStroke(1.8f);


   public PainelRede(){
      setBackground(new Color(25, 25, 25));
      setPreferredSize(new Dimension(largura, altura));
      setFocusable(true);
      setDoubleBuffered(true);
      setEnabled(true);
      setVisible(true);

      int arq[] = {1, 1, 1};
      this.rede = new RedeNeural(arq);
      this.rede.compilar();
   }


   public void desenhar(RedeNeural rede){
      this.rede = rede;
      repaint();
   }


   public void configurarRede(RedeNeural rede){
      this.rede = rede;
   }

   
   @Override
   protected void paintComponent(Graphics g){
      super.paintComponent(g);
      g2 = (Graphics2D) g;
   
      g2.setFont(getFont().deriveFont(14.f));
      contadorCoordenadas = 0;
      contadorCamadas = 0;
      
      // int larguraTotalDesenho = rede.obterQuantidadeCamadas() * ((larguraDesenho + espacoHorizontalEntreCamadas) + 1);
      // int xInicial = (largura - larguraTotalDesenho) / 2;

      x = 0;
      for (Camada camada : rede.obterCamadas()){
         desenharCamada(camada);
         x += espacoHorizontalEntreCamadas;
         contadorCamadas++;
      }

      desenhaConexoes(g2);

      // g2.setColor(Color.white);
      // g2.drawLine(this.largura/2, 0, this.largura/2, this.altura);

      g2.dispose();
   }   
    
   public void desenharCamada(Camada camada){
      // Calcula a posição y inicial para que os neurônios estejam centralizados na altura da janela
      int alturaJanela = getHeight(); // Obtém a altura da janela
      int alturaCamada = camada.quantidadeNeuronios() * (alturaDesenho + espacoVerticalEntreNeuronio);
      int yInicial = (alturaJanela - alturaCamada) / 2;
   
      // Define o valor inicial de y
      int y = yInicial;
   
      // Itera pelos neurônios da camada
      for(Neuronio neuronio : camada.neuronios()){
         desenharNeuronio(g2, x, y, neuronio.saida);
         y += alturaDesenho + espacoVerticalEntreNeuronio;
      }
   }
   
   private void desenhaConexoes(Graphics2D g2){
      g2.setStroke(linhaDesenho);

      // Itera pelas camadas (exceto a última)
      for (int i = 0; i < coordenadas.size() - 1; i++) {
         ArrayList<Coordenada> camadaAtual = coordenadas.get(i);
         ArrayList<Coordenada> proximaCamada = coordenadas.get(i+1);

         // Itera pelos neurônios da camada atual
         for(Coordenada c1 : camadaAtual){

            // Itera pelos neurônios da próxima camada
            for(Coordenada c2 : proximaCamada){
               if(c1.valor > 0 && c2.valor > 0) g2.setColor(corConexaoAtiva);
               else g2.setColor(corConexaoInativa);

               int x1 = c1.x + (larguraDesenho);
               int x2 = c2.x + (larguraDesenho * (3/4));
               int y1 = c1.y + (alturaDesenho/2);
               int y2 = c2.y + (alturaDesenho/2);

               // Desenha uma linha de c1 para c2
               g2.drawLine(x1, y1, x2, y2);
            }
         }
      }
   }
  

   private void desenharNeuronio(Graphics2D g2, int x, int y, double valorSaida){
      g2.setColor(corBordaNeuronio);
      g2.fillOval(x, y, larguraDesenho, alturaDesenho);

      int borda = 3;

      if(valorSaida > 0) g2.setColor(corNeuronioAtivo);
      else g2.setColor(corNeuronioInativo);

      Coordenada coordenada = new Coordenada(x, y, valorSaida);

      if(contadorCamadas < coordenadas.size()){
         coordenadas.get(contadorCamadas).add(coordenada);
      
      }else{
         ArrayList<Coordenada> novaLista = new ArrayList<>();
         novaLista.add(coordenada);
         coordenadas.add(novaLista);
      }

      g2.fillOval(x+borda, y+borda, larguraDesenho-(borda*2), alturaDesenho-(borda*2));  
   }
}