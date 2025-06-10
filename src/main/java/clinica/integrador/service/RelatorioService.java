package clinica.integrador.service;



import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import clinica.integrador.entities.Consulta;
import clinica.integrador.entities.Medico;
import clinica.integrador.repository.ConsultaRepository;
import clinica.integrador.repository.MedicoRepository;



@Service
public class RelatorioService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // Gera relatório PDF para um médico
    public byte[] gerarPdfRelatorioPorMedico(Long medicoId) throws DocumentException {
        Optional<Medico> medicoOpt = medicoRepository.findById(medicoId);
        String nomeMedico = medicoOpt.map(Medico::getNome).orElse("Médico não encontrado");
        String especialidadeMedico = medicoOpt.map(Medico::getEspecialidade).orElse("Especialidade não informada");

        List<Consulta> consultas = consultaRepository.findByMedico_Id(medicoId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Relatório de Consultas do Médico: " + nomeMedico));
        document.add(new Paragraph("Especialidade: " + especialidadeMedico));
        document.add(new Paragraph("Total de Consultas: " + consultas.size()));
        document.add(new Paragraph(" "));

        for (Consulta consulta : consultas) {
            String data = (consulta.getDataHora() != null) ? consulta.getDataHora().toString() : "Data não informada";
            String paciente = (consulta.getPaciente() != null && consulta.getPaciente().getNome() != null)
                    ? consulta.getPaciente().getNome() : "Paciente não informado";
            String linha = "Data: " + data + ", Paciente: " + paciente;
            document.add(new Paragraph(linha));
        }

        document.close();
        return out.toByteArray();
    }
}
