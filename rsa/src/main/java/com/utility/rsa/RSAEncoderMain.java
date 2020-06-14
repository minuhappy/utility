package com.utility.rsa;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.crypto.Cipher;

public class RSAEncoderMain {
    private final int READ_SIZE = 500;
    private final String RSA = "RSA";
    private final String LICENSE_SPEC_PATH = "spec";
    private final String LICENSE_PATH = "license";
    private final String LICENSE_FILE_NAME = "LICENSE";

    private final String PUBLIC_KEY_MODULUS = "PublicKeyModulus";
    private final String PUBLIC_KEY_EXPONENT = "PublicKeyExponent";
    private final String PRIVATE_KEY_MODULUS = "PrivateKeyModulus";
    private final String PRIVATE_KEY_EXPONENT = "PrivateKeyExponent";

    private String licenseSpecPath;
    private String publicKeyModulus;
    private String publicKeyExponent;
    private String privateKeyModulus;
    private String privateKeyExponent;
    private String decryptedText;

    public static void main(String[] args) throws Exception {
        RSAEncoderMain rsaEncoder = new RSAEncoderMain();
        // 최초 실행 시, 라이센스 스펙 생성
//        rsaEncoder.generateLicenseSpec();

        String encryptedValue = rsaEncoder.encrypt("test");
        System.out.println(encryptedValue);

        String decryptedValue = rsaEncoder.decrypt();
        System.out.println(decryptedValue);
    }

    /**
     * Public, Private에 대한 License Spec 생성
     */
    public void generateLicenseSpec() throws Exception {
        KeyPair keyPair = this.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic(); // 공개키
        PrivateKey privateKey = keyPair.getPrivate(); // 개인키

        /**
         * Public Spec
         */
        RSAPublicKeySpec publicKeySpec = this.getRSAPublicKeySpec(publicKey);
        String publicKeyModulus = publicKeySpec.getModulus().toString(16);
        String publicKeyExponent = publicKeySpec.getPublicExponent().toString(16);

        /**
         * Private Spec
         */
        RSAPrivateKeySpec privateKeySpec = this.getRSAPrivateKeySpec(privateKey);
        String privateKeyModulus = privateKeySpec.getModulus().toString(16);
        String privateKeyExponent = privateKeySpec.getPrivateExponent().toString(16);

        /**
         * File 생성
         */
        String root = this.getResourcePath(LICENSE_SPEC_PATH);
        this.createFile(root, PUBLIC_KEY_MODULUS, publicKeyModulus);
        this.createFile(root, PUBLIC_KEY_EXPONENT, publicKeyExponent);
        this.createFile(root, PRIVATE_KEY_MODULUS, privateKeyModulus);
        this.createFile(root, PRIVATE_KEY_EXPONENT, privateKeyExponent);
    }

    /**
     * Public Key로 암호화한 후 결과로 출력된 byte 배열을 Base64로 인코딩하여 String으로 변환하여 리턴함
     *
     * @param text
     * @return Base64로 인코딩된 암호화 문자열
     */
    public String encrypt(String text) throws Exception {
        String publicKeyModulus = this.getPublicKeyModulus();
        String publicKeyExponent = this.getPublicKeyExponent();

        BigInteger modulus = new BigInteger(publicKeyModulus, 16);
        BigInteger exponent = new BigInteger(publicKeyExponent, 16);

        RSAPublicKeySpec pubks = new RSAPublicKeySpec(modulus, exponent);

        return this.encrypt(text, KeyFactory.getInstance(RSA).generatePublic(pubks));
    }

    private String encrypt(String text, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int length = text.length();
        int start = 0;
        int end = 0;

        StringBuilder encryptText = new StringBuilder();

        do {
            end = start + READ_SIZE;
            if (end > length) {
                end = length;
            }

            String value = text.substring(start, end);
            encryptText.append(new String(Base64.getEncoder().encode(cipher.doFinal(value.getBytes()))));

            start = end;
        } while (end < length);

        // File 생성
        StringBuilder filePath = new StringBuilder();
        filePath.append(this.getResourcePath(""));
        filePath.append(LICENSE_PATH);

        this.createFile(filePath.toString(), LICENSE_FILE_NAME, encryptText.toString());

        return encryptText.toString();
    }

    /**
     * decode 시킨 후 RSA 비밀키(Private Key)를 이용하여 암호화된 텍스트를 원문으로 복호화
     *
     * @return
     */
    public String decrypt() throws Exception {
        return this.decrypt(false);
    }

    public String decrypt(boolean isRefresh) throws Exception {
        if (decryptedText != null && !isRefresh)
            return decryptedText;

        String privateKeyModulus = this.getPrivateKeyModulus();
        String privateKeyExponent = this.getPrivateKeyExponent();

        BigInteger modulus = new BigInteger(privateKeyModulus, 16);
        BigInteger exponent = new BigInteger(privateKeyExponent, 16);

        String dirPath = this.getResourcePath(LICENSE_PATH);
        RSAPrivateKeySpec priks = new RSAPrivateKeySpec(modulus, exponent);

        String encryptedText = Files.readAllLines(this.getFullPath(dirPath, LICENSE_FILE_NAME))
            .stream().collect(Collectors.joining());

        return this.decrypt(encryptedText, KeyFactory.getInstance(RSA).generatePrivate(priks));
    }

    private String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = Base64.getDecoder().decode(encryptedText.getBytes());
        decryptedText = new String(cipher.doFinal(bytes));
        return decryptedText;
    }

    private KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * RSA 공개키로부터 RSAPublicKeySpec 객체를 생성함
     *
     * @param publicKey 공개키
     * @return RSAPublicKeySpec spec
     */

    private RSAPublicKeySpec getRSAPublicKeySpec(PublicKey publicKey) throws Exception {
        return KeyFactory.getInstance(RSA).getKeySpec(publicKey, RSAPublicKeySpec.class);
    }

    /**
     * RSA 비밀키로부터 RSAPrivateKeySpec 객체를 생성함
     *
     * @param privateKey 비밀키
     * @return RSAPrivateKeySpec
     */
    private RSAPrivateKeySpec getRSAPrivateKeySpec(PrivateKey privateKey) throws Exception {
        return KeyFactory.getInstance(RSA).getKeySpec(privateKey, RSAPrivateKeySpec.class);
    }

    private String getResourcePath(String resource) {
        return "rsa/" + resource;
//        StringBuilder path = new StringBuilder();
//        path.append(getClass().getClassLoader().getResource(resource).getFile());
//        return path.toString().replace("bin/", "src/main/resources/");
    }

    private String getLicenseSpecPath() {
        if (licenseSpecPath == null) {
            licenseSpecPath = this.getResourcePath(LICENSE_SPEC_PATH);
        }
        return licenseSpecPath;
    }

    private String getPublicKeyModulus() throws Exception {
        if (publicKeyModulus == null) {
            publicKeyModulus = this.readFileContent(this.getLicenseSpecPath(), PUBLIC_KEY_MODULUS);
        }
        return publicKeyModulus;
    }

    private String getPublicKeyExponent() throws Exception {
        if (publicKeyExponent == null) {
            publicKeyExponent = this.readFileContent(this.getLicenseSpecPath(), PUBLIC_KEY_EXPONENT);
        }
        return publicKeyExponent;
    }

    private String getPrivateKeyModulus() throws Exception {
        if (privateKeyModulus == null) {
            privateKeyModulus = this.readFileContent(this.getLicenseSpecPath(), PRIVATE_KEY_MODULUS);
        }
        return privateKeyModulus;
    }

    private String getPrivateKeyExponent() throws Exception {
        if (privateKeyExponent == null) {
            privateKeyExponent = this.readFileContent(this.getLicenseSpecPath(), PRIVATE_KEY_EXPONENT);
        }
        return privateKeyExponent;
    }

    /**
     * Root Path와 File명을 이용하여, Full path를 생성
     *
     * @param root
     * @param fileName
     * @return
     */
    private Path getFullPath(String root, String fileName) {
        String fullPath = new StringJoiner("/")
            .add(root)
            .add(fileName)
            .toString();

        return Paths.get(fullPath);
    }

    /**
     * 파일 생성
     *
     * @param dirPath
     * @param fileName
     * @param value
     * @throws Exception
     */
    private void createFile(String dirPath, String fileName, String value) throws Exception {
        File directory = new File(dirPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream output = new FileOutputStream(dirPath + "/" + fileName)) {
            output.write(value.getBytes());
        }
    }

    /**
     * 파일 내용 읽기
     *
     * @param root
     * @param fileName
     * @return
     * @throws Exception
     */
    private String readFileContent(String root, String fileName) throws Exception {
        Path path = this.getFullPath(root, fileName);
        return Files.readAllLines(path).stream().collect(Collectors.joining());
    }
}