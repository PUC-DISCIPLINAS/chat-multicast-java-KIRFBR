### AUTOR
Keterson Flôres

### RESUMO GERAL
São 3 classes que constituem a proposta: Servidor.java, Cliente.java e Sala.java;

Foi utilizada interface gráfica na interação com o usuário tanto no Servidor e principalmente no cliente.

Além do material disponibilizado pelo professor, foi usado também como referência o material em:
https://www.devmedia.com.br/como-criar-um-chat-multithread-com-socket-em-java/33639

### DETALHES ESPECÍFICOS (DO SISTEMA)
Para a manipulação de salas, foi-se utilizado ArrayList de objetos Sala.
Cada objeto Sala possui um id e ArrayList's de membros e nomes. 

### COMO USAR
1 - Clonar o repositório;

2 - Compilar as classes Servidor.java e Cliente.java caso o Java não seja o 1.8; Caso seja, este passo deve ser desconsiderado;

3 - No terminal dentro da pasta bin, executar primeiro o Servidor (java Servidor).
    Será aberta uma janela pedindo a porta. Recomenda-se usar a sugerida (12345);
    
4 - Em seguida, em outros terminais (também na pasta bin), executar tantas vezes quanto for desejado a classe Cliente (java Cliente).
    Será aberta uma janela pedindo o IP do servidor, a Porta do servidor e o Nome do usuário. Recomenda-se usar o IP e Porta sujeridos;
    
5 - E por fim para cada cliente será aberta uma interface para interação. 

Obs.: 
1 - Não existe a ação CRIAR SALA. a responsabilidade de criar está no botão Entrar na sala. 
    Quando se tenta entrar numa sala que não existe, tal sala é criada;
2 - Na interface de Chat dos cliente possuem dois campos de texto, Mensagem e Id da sala. 
3 - O campo Id da sala é extremamente importante, pois ele deve ter um valor informado antes para todas as ações disponibilizada na interface.
    A saber: Enviar Mensagem, Entrar na sala, Sair da sala, Listar Membros Sala e Listar Salas;

### PROTOCOLO DE COMUNICAÇÃO

ENTRAR NA SALA (Caso a sala não exista, ela é criada com o id passado)
envio: Id da sala;
recebimento: Mensagem informando que sala foi criada (quando não existe) ou que foi adicionado;

LISTAR SALAS
envio: nenhum parâmetro; 
recebimento: Lista de salas existentes

LISTAR MEMBROS DA SALA
envio: Id da sala;
recebimento: Membros atuais da sala;

SAIR DA SALA
envio: Id da sala;
recebimento: Mensagem informando o resultado: sucesso ou falha

ENVIAR MENSAGEM
envio: Id da sala e Mensagem;
recebimento: a própria mensagem enviada, no chat;
