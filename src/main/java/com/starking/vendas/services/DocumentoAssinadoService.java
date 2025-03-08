package com.starking.vendas.services;

import com.starking.vendas.model.DocumentoAssinado;
import com.starking.vendas.model.Usuario;
import com.starking.vendas.repositories.DocumentoAssinadoRepository;
import com.starking.vendas.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentoAssinadoService {

    private final DocumentoAssinadoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DigitalSignatureService digitalSignatureService;

    public DocumentoAssinado assinarDocumento(Long usuarioId, MultipartFile file) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        // Assinar o documento
        String keyStorePath = "caminho/para/seu/certificado.pfx";
        String keyStorePassword = "sua-senha";
        String keyPassword = "sua-senha";

        byte[] documentoAssinado = digitalSignatureService.signPdf(file.getBytes(), keyStorePath, keyStorePassword, keyPassword);

        // Persistir no banco de dados
        DocumentoAssinado documento = new DocumentoAssinado();
        documento.setConteudo(documentoAssinado);
        documento.setNomeArquivo(file.getOriginalFilename());
        documento.setUsuario(usuario.get());

        return documentoRepository.save(documento);
    }

    public List<DocumentoAssinado> listarDocumentos(Long usuarioId) {
        return documentoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<DocumentoAssinado> obterDocumento(Long id) {
        return documentoRepository.findById(id);
    }
}