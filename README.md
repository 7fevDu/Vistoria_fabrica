# Vistoria Fábrica

Formulário digital de vistoria de fábrica, com geração automática de relatório em PDF.

<p align="center">
  <img src="https://github.com/user-attachments/assets/80ba1349-fe23-42c6-9dc9-ff9169983e64" width="45%" alt="Formulário de vistoria" />
  <img src="https://github.com/user-attachments/assets/9cdfb204-fe83-4bac-b207-1e8404995940" width="45%" alt="Formulário de vistoria - continuação" />
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/997c45cd-1711-4f93-936f-8722781c7a37" width="45%" alt="Assinatura digital do supervisor" />
  <img src="https://github.com/user-attachments/assets/49234963-f237-4b05-9b61-0fae6760d82c" width="45%" alt="Relatório em PDF gerado" />
</p>

## O problema

Antes desse projeto, a vistoria diária era feita em papel:

- Todo dia era necessário imprimir uma folha de vistoria antes de ir até a fábrica.
- O ambiente da fábrica é sujo, e isso sujava (e às vezes danificava) o papel durante a vistoria.
- Havia gasto recorrente com impressão e papel para um processo que se repetia todos os dias.

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

src/main/java/com/eduardo/formulario
├── controller/VistoriaController.java # rotas do formulário e envio
├── model/Vistoria.java # dados da vistoria (responsável, itens, assinatura)
├── model/ItemVistoria.java # setor, status e observação
└── pdf/VistoriaPdfGenerator.java # geração do relatório em PDF

src/main/resources/templates/vistoria.html # formulário + captura de assinatura
src/main/resources/application.properties # configuração da pasta de rede



## Configuração

No `application.properties`, defina a pasta onde os PDFs serão salvos:

```properties
vistoria.pdf.pasta-destino=\\\\servidor\\caminho\\da\\pasta


Como executar

./mvnw spring-boot:run

Depois, acesse http://localhost:8080/vistoria.


Só consegui confirmar visualmente a 1ª imagem (é a tela do formulário) — as outras três eu não consegui abrir no preview (o carregamento travou), então ajuste os `alt` das imagens 2 a 4 se a legenda que coloquei ("continuação", "assinatura", "PDF gerado") não bater com o conteúdo real.

