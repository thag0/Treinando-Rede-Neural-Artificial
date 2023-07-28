# Treinando Rede Neural Artificial

![gifTreinamentoTempoReal](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/6f06f83b-e4cd-46d5-b323-5d4e263da568)
*vídeo acelerado em 10x*

Tentando implementar um algortimo de treino e ferramentas para masuear os dados.

Aos poucos criando um pequeno ambiente de trabalho em volta disso

# Resultados

Até o momento já consegui usar a rede em testes menores, como pegar alguns conjuntos de dados para testar o desempenho dela e tive bons resultados com precições
entre 80% e 95%

Consegui testar a rede em um cenário que ela deve ampliar imagens e também consegui bons resultados como é possível ver a seguir:

![imagemApliadaPelaRede](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/8610fd77-b739-4a5d-a976-085eddaf8a15)

*imagem original comparada com imagem criada pela rede neural.*

A rede consegue lidar com dados gerados por uma imagem pequena, desde que eles estejam normalizados para isso. Normalizei eles fazendo a rede trabalhar em intervalos entre
0 e 1 para x e y, onde quanto mais próximo de 1 o valor for, mais próximo ele vai estar do limite de altura e largura da imagem, então a rede trabalha com "porcentagens de coordenadas".

# Imagens com cor

Uma evolução agora foi converter os dados de imagem na escala rgb ao invés de só um valor de brilho como era na escala de cinza, não foi necessário criar funcionalidades novas para a rede, apenas adaptar novos neurônios para ela trabalhar.

![imagemArcoIrisAmpliada](https://github.com/thag0/Treinando-Rede-Neural-Artificial/assets/91092364/66028643-dba6-4f46-a711-e561eb0c7515)

*imagem original comparada com imagem criada pela rede neural, agora com cores.*
