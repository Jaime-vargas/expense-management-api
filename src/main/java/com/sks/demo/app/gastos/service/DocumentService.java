package com.sks.demo.app.gastos.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sks.demo.app.gastos.Mapper.Mapper;
import com.sks.demo.app.gastos.dto.ExpenseDTO;
import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.Attachment;
import com.sks.demo.app.gastos.model.Expense;
import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.model.UserEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class DocumentService {

    private final SpringTemplateEngine templateEngine;
    private final ProjectService projectService;
    private final ExpenseService expenseService;
    private final AttachmentService attachmentService;
    private final Mapper mapper;

    public DocumentService(SpringTemplateEngine templateEngine, ProjectService projectService, ExpenseService expenseService, AttachmentService attachmentService, Mapper mapper) {
        this.templateEngine = templateEngine;
        this.projectService = projectService;
        this.expenseService = expenseService;
        this.attachmentService = attachmentService;
        this.mapper = mapper;
    }

    public byte[] generatePdf(Long idProject)  {
        Project project = projectService.findById(idProject);
        UserEntity userEntity = project.getUser();
        String actualDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "MX")));
        BigDecimal totalBudget = project.getBudget();
        List<ExpenseDTO> expenses = expenseService.getExpensesByProjectId(idProject).stream().map(mapper::entityToDto).toList();

        BigDecimal totalExpenses = expenses.stream()
                .map(ExpenseDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add
        );
        BigDecimal remainingBudget = totalBudget.subtract(totalExpenses);

        Context context = new Context();
        context.setVariable("project", project);
        context.setVariable("user", userEntity);
        context.setVariable("totalBudget", totalBudget);
        context.setVariable("actualDate", actualDate);
        context.setVariable("expenses", expenses);
        context.setVariable("totalExpenses", totalExpenses);
        context.setVariable("remainingBudget", remainingBudget);

        String html = templateEngine.process("ProjectTemplate", context);

        //Convert html to PDF with OpenHTMLToPDF
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            // baseUrl: permite resolver recursos relativos (css/img) que estén en classpath:/templates/

            String baseUrl = Objects.requireNonNull(this.getClass().getResource("/templates/")).toString();
            builder.withHtmlContent(html, baseUrl);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        }catch (Exception e){
            throw  new HandleException(500, "error", "error generando pdf " + e.getMessage());
        }
    }

    public List<File> getAttachmentsAsFiles(Long idProject) {
        projectService.findById(idProject);// Implementation to retrieve attachments as files
        List<Expense> expenses = expenseService.getExpensesByProjectId(idProject);

        List<Attachment> allAttachments = expenses.stream()
                .flatMap(expense -> attachmentService.getAttachments(expense.getId()).stream())
                .toList();

        return allAttachments.stream().map(attachment -> {
            return new File(attachment.getStorePath());
        }).toList(); // Placeholder return
    }
}
