package io.gabrielmmoraes1999;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.security.Provider;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Certificado() {
        this.setSslProtocol(TLSV_1_2);
        this.setModoMultithreading(false);
    }

    @Override
    public String toString() {
        return "Certificado{" +
                "nome='" + nome + '\'' +
                ", dataHoraVencimento=" + dataHoraVencimento +
                ", cnpjCpf='" + cnpjCpf + '\'' +
                ", tipoCertificado=" + tipoCertificado +
                '}';
    }
}
