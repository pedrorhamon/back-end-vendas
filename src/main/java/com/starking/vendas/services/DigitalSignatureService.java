package com.starking.vendas.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.cms.*;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.springframework.stereotype.Service;

@Service
public class DigitalSignatureService {

    public byte[] signPdf(byte[] pdfBytes, String keyStorePath, String keyStorePassword, String keyPassword) throws Exception {
        // Carregar o KeyStore
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        try (InputStream keyStoreStream = new FileInputStream(keyStorePath)) {
            keystore.load(keyStoreStream, keyStorePassword.toCharArray());
        }

        Enumeration<String> aliases = keystore.aliases();
        String alias = aliases.hasMoreElements() ? aliases.nextElement() : null;
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keyPassword.toCharArray());
        Certificate[] chain = keystore.getCertificateChain(alias);
        X509Certificate certificate = (X509Certificate) chain[0];

        // Criar a assinatura digital
        PDDocument document = PDDocument.load(pdfBytes);
        PDSignature signature = new PDSignature();
        document.addSignature(signature);

        // Configurar o ContentSigner
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);

        // Gerar o SignerInfoGenerator
        SignerInfoGenerator signerInfoGenerator = new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().build())
                .build(contentSigner, certificate);

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        generator.addSignerInfoGenerator(signerInfoGenerator);

        CMSSignedData signedData = generator.generate(new CMSProcessableByteArray(pdfBytes), true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.saveIncremental(baos);
        document.close();

        return signedData.getEncoded();
    }
}