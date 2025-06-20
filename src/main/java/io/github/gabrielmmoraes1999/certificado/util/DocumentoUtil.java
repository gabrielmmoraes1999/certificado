package io.github.gabrielmmoraes1999.certificado.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentoUtil {
    DocumentoUtil(){}

    private static final String CPF_INDICATOR = "\u0001";
    private static final String CPF_TERMINATOR = "\u0017";
    private static final Pattern PATTERN_CPF = Pattern.compile("(?<!\\d)\\d{11}(?!\\d)");
    private static final int CPF_LENGTH = 11;
    private static final int CPF_OFFSET = 15;
    private static final Pattern PATTERN_CNPJ = Pattern.compile("\\d{14}");
    private static final String CNPJ_INDICATOR = "\u0006\u0005`L\u0001\u0003\u0003";
    private static final int CNPJ_OFFSET = 6;
    private static final int CNPJ_LENGTH = 25;

    private static final byte[] NOME_PESSOA_FISICA_INDICATOR = new byte[]{6, 5, 96, 76, 1 ,3, 2};
    private static final byte[] INSCRICAO_PESSOA_FISICA_INDICATOR = new byte[]{6, 5, 96, 76, 1 ,3, 4};
    private static final int NOME_PESSOA_FISICA_OFFSET = 11;
    private static final int INSCRICAO_PESSOA_FISICA_OFFSET = 19;


    public static Optional<String> getDocumentoFromCertificado(byte[] extensionValue) {
        String valor = new String(extensionValue);
        processaNomePF(extensionValue);

        return Optional.ofNullable(
                processaCNPJ(valor)
                        .orElse(processaCPF(valor)
                                .orElse(null)));
    }

    public static Optional<String> processaCPFPJ(byte[] extensionValue) {
        int index = findSequence(extensionValue, INSCRICAO_PESSOA_FISICA_INDICATOR);

        if (index != -1) {
            return validarDocumento(
                    new String(getBytes(
                            extensionValue,
                            index + INSCRICAO_PESSOA_FISICA_OFFSET,
                            index + INSCRICAO_PESSOA_FISICA_OFFSET + CPF_LENGTH
                    ))
            );
        }

        return Optional.empty();
    }

    public static Optional<String> processaNomePF(byte[] extensionValue) {
        int index = findSequence(extensionValue, NOME_PESSOA_FISICA_INDICATOR);
        int size = extensionValue[index + NOME_PESSOA_FISICA_OFFSET - 1];

        if (index != -1) {
            return Optional.of(new String(getBytes(
                    extensionValue,
                    index + NOME_PESSOA_FISICA_OFFSET,
                    index + NOME_PESSOA_FISICA_OFFSET + size
            )));
        }
        return Optional.empty();
    }

    private static Optional<String> processaCPF(String extensionValue) {
        int cpfStartIndex = extensionValue.indexOf(CPF_INDICATOR);
        if (cpfStartIndex != -1) {
            cpfStartIndex += CPF_OFFSET;
            int cpfEndIndex = extensionValue.indexOf(CPF_TERMINATOR, cpfStartIndex);
            if (cpfEndIndex != -1) {
                String cpf = extrairNumeros(extensionValue.substring(cpfStartIndex, cpfStartIndex + CPF_LENGTH));
                return validarDocumento(cpf);
            }
        }
        return Optional.empty();
    }

    private static Optional<String> processaCNPJ(String extensionValue) {
        int cnpjIndex = extensionValue.indexOf(CNPJ_INDICATOR);
        if (cnpjIndex != -1) {
            String cnpj = extrairNumeros(extensionValue.substring(cnpjIndex + CNPJ_OFFSET, cnpjIndex + CNPJ_LENGTH));
            return validarDocumento(cnpj);
        }
        return Optional.empty();
    }

    private static String extrairNumeros(String valor) {
        return valor.replaceAll("[^\\d]", "");
    }

    private static Optional<String> validarDocumento(String documento) {
        Matcher matcherCNPJ = PATTERN_CNPJ.matcher(documento);
        if (matcherCNPJ.find()) {
            return Optional.of(matcherCNPJ.group());
        }

        Matcher matcherCPF = PATTERN_CPF.matcher(documento);
        if (matcherCPF.find()) {
            return Optional.of(matcherCPF.group());
        }

        return Optional.empty();
    }

    public static int findSequence(byte[] array, byte[] sequence) {
        if (array.length < sequence.length) {
            return -1;
        }

        for (int i = 0; i <= array.length - sequence.length; i++) {
            boolean match = true;

            for (int j = 0; j < sequence.length; j++) {
                if (array[i + j] != sequence[j]) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return i;
            }
        }

        return -1;
    }

    public static byte[] getBytes(byte[] array, int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex >= array.length || beginIndex > endIndex) {
            throw new IllegalArgumentException("Índices inválidos");
        }

        byte[] result = new byte[endIndex - beginIndex];
        System.arraycopy(array, beginIndex, result, 0, result.length);
        return result;
    }
}
