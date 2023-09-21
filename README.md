# Treinando Rede Neural Artificial

![Untitled](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/15ec65b4-e28b-4f2e-bb85-ac2b1791a35a)
*vídeo acelerado*

Tentando implementar um algortimo de treino e ferramentas para masuear os dados.

Aos poucos criando um pequeno ambiente de trabalho em volta disso

# Resultados

Até o momento já consegui usar a rede em testes menores, como pegar alguns conjuntos de dados para testar o desempenho dela e tive bons resultados com precisões
entre 80% e 95%. Depois de ter adicionado novas ferramentas para evitar o overfitting da rede, a precisão dela não caiu tanto assim. 

Já consegui testar a rede em um cenário que ela deve ampliar imagens e também consegui bons resultados como é possível ver a seguir:

![imagemApliadaPelaRede](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/8610fd77-b739-4a5d-a976-085eddaf8a15)

*imagem original comparada com imagem criada pela rede neural.*

A rede consegue lidar com dados gerados por uma imagem pequena, desde que eles estejam normalizados para isso. Normalizei eles fazendo a rede trabalhar em intervalos entre
0 e 1 para x e y, onde quanto mais próximo de 1 o valor for, mais próximo ele vai estar do limite de altura e largura da imagem, então a rede trabalha com "porcentagens de coordenadas".

# Imagens com cor

Uma evolução agora foi converter os dados de imagem na escala rgb ao invés de só um valor de brilho como era na escala de cinza, não foi necessário criar funcionalidades novas para a rede, apenas adaptar novos neurônios para ela trabalhar.

![imagemArcoIrisAmpliada](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/66028643-dba6-4f46-a711-e561eb0c7515)

*imagem original comparada com imagem criada pela rede neural, agora com cores.*

# Implementando momentum para acelerar o aprendizado

A ideia de adicionar o momentum para dar uma "velocidade" na convergência da rede deu um bom resultado.

![graficos_desempenho_rede](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/db85a625-b164-47d4-ba61-049578338c8a)

*Em ambos os casos foi utilizado o sgd (stochastic gradient descent) e cada rede possuia o valor de taxa de aprendizagem igual a 0.01*

# Implementando novos algoritmos de otimização

Estou tentando expandir as opções de configuração da rede neural e com isso trouxe novos otimizadores para ajudar no aprendizado dela, como exemplo 
testei o otimizador Adam para uma rede que faz interpolação dos dados de uma imagem, com finalidade de redimensionar o conteúdo para diferentes resoluções.

![adam](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/500b9faf-3848-4f3c-9124-531917cc1bd5)

*Resultado de uma execução usando otimizador Adam, é possível observar como ele consegue ser bem mais estável comparado com o SGD*

# Algumas dificuldades

Pelo que to vendo na experiência de uso, o treino da rede neural fica muito lento quando uso conjunto de dados muito grandes. Testei ela no mesmo cenário de ampliar imagens mas com imagens maiores que as do mnist (28x28), além de que com imagens coloridas o resultado ficou pior ainda. 
Acredito que o maior problema seja o grande volume de dados que as imagens geram (as vezes mais de 100,000 amostras) e mesmo usando o SGD com momentum para treinar a rede, ela ainda tem muita dificuldade de convergir para resultados bons.

