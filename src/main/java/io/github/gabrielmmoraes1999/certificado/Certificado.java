package io.github.gabrielmmoraes1999.certificado;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@SuppressWarnings("WeakerAccess")
public class Certificado {

    private static final String TLSV_1_2 = "TLSv1.2";

    private String nome;
    private LocalDate vencimento;
    private LocalDateTime dataHoraVencimento;
    private Long diasRestantes;
    private String arquivo;
    private byte[] arquivoBytes;
    private String senha;
    private String cnpjCpf;
    private TipoCertificadoEnum tipoCertificado;
    private boolean valido;
    private String sslProtocol;
    private BigInteger numeroSerie;
    private Provider provider;
    private boolean isModoMultithreading;
    private String issuer;
    private String subject;
    private X509Certificate certificate;

    public Certificado() {
        this.setSslProtocol(TLSV_1_2);
        this.setModoMultithreading(false);
    }

    public String extractCommonName(String dn, boolean subject) {
        String commonName;

        if (subject) {
            commonName = certificate.getSubjectDN().getName();
        } else {
            commonName = certificate.getIssuerDN().getName();
        }

        Pattern pattern = Pattern.compile("(\\w+)=\\s*\"?([^,]+)\"?");
        Matcher matcher = pattern.matcher(commonName);

        while (matcher.find()) {
            if (dn.equals(matcher.group(1))) {
                return matcher.group(2);
            }
        }

        return null;
    }
}
