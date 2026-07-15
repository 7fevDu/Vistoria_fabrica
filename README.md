# Vistoria Fábrica

Formulário digital de vistoria de fábrica, com geração automática de relatório em PDF.

## O problema

Antes desse projeto, a vistoria diária era feita em papel:

- Todo dia era necessário imprimir uma folha de vistoria antes de ir até a fábrica.
- O ambiente da fábrica é sujo, e isso sujava (e às vezes danificava) o papel durante a vistoria.
- Por ser um processo manual, era comum esquecer de anotar algum item ou setor.
- Cada folha ficava avulsa, sem centralização, dificultando consultar vistorias antigas.
- Havia gasto recorrente com impressão para um processo que se repetia todos os dias.

## A solução

Substituí o papel por um formulário web que estrutura a vistoria e cuida do resto automaticamente:

- **Checklist estruturado**: os setores da fábrica já vêm fixos no formulário (Batch off & Embalagem, Banbury's, Negro de Fumo/Cargas Brancas e Plastificante, Carrossel, Backup), cada um com status (OK/INCIDENTE) e campo de observação — elimina o esquecimento de itens.
- **Sem papel**: a vistoria é preenchida direto no celular/tablet/computador, então a sujeira da fábrica deixou de ser um problema.
- **Relatório em PDF automático**: ao enviar o formulário, o sistema gera um PDF padronizado (com logo, tabela de status colorida e observações), eliminando a necessidade de digitar ou organizar manualmente o relatório.
- **Armazenamento centralizado**: o PDF é salvo automaticamente em uma pasta de rede compartilhada (`vistoria.pdf.pasta-destino`), então nada fica perdido em papéis avulsos e o histórico fica sempre acessível.
- **Assinatura digital do supervisor**: o formulário tem um campo de assinatura (desenhada na tela), que é embutida no PDF final como validação — dispensando a assinatura física em papel.

Resultado: menos tempo gasto no processo (sem impressão, sem levar papel sujo de volta, sem digitar relatório depois) e economia de material (impressão diária).

## Como funciona

1. Usuário acessa `/vistoria` e vê o formulário com os setores já listados.
2. Preenche o responsável, marca o status de cada setor e descreve observações quando houver incidente.
3. Coleta a assinatura do supervisor pelo próprio formulário (desenho na tela).
4. Ao enviar, o sistema:
   - Gera o PDF do relatório (com a assinatura incluída, se coletada).
   - Salva o PDF na pasta de rede configurada.
   - Retorna o PDF para download.

## Tecnologias

- Java 21 + Spring Boot 3.4
- Thymeleaf (formulário)
- OpenPDF (geração do relatório em PDF)
- HTML5 Canvas (captura da assinatura)

## Estrutura principal

```
src/main/java/com/eduardo/formulario
├── controller/VistoriaController.java   # rotas do formulário e envio
├── model/Vistoria.java                  # dados da vistoria (responsável, itens, assinatura)
├── model/ItemVistoria.java              # setor, status e observação
└── pdf/VistoriaPdfGenerator.java        # geração do relatório em PDF

src/main/resources/templates/vistoria.html  # formulário + captura de assinatura
src/main/resources/application.properties   # configuração da pasta de rede
```

## Configuração

No `application.properties`, defina a pasta onde os PDFs serão salvos:

```properties
vistoria.pdf.pasta-destino=\\\\servidor\\caminho\\da\\pasta
```

## Como executar

```bash
./mvnw spring-boot:run
```

Depois, acesse `http://localhost:8080/vistoria`.

### Acessando pelo celular

A aplicação escuta em todas as interfaces de rede (`server.address=0.0.0.0`), então dá para abrir o formulário direto do celular/tablet, desde que esteja na **mesma rede Wi-Fi** do computador que está rodando o servidor.

1. Descubra o IP local do computador que está rodando a aplicação:
   - Windows: `ipconfig` (veja o campo `Endereço IPv4`, algo como `192.168.x.x`)
   - Linux/Mac: `ifconfig` ou `ip addr`
2. No celular, abra o navegador e acesse `http://<IP-DO-COMPUTADOR>:8080/vistoria` (ex.: `http://192.168.1.93:8080/vistoria`).
3. Se não conectar, verifique se o Firewall do Windows está bloqueando a porta 8080 (pode ser necessário liberar entrada TCP na porta 8080 para a rede privada).
