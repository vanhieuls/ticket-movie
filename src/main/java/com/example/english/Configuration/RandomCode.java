package com.example.english.Configuration;

import java.security.SecureRandom;

public class RandomCode {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RNG = new SecureRandom();

    /**
     * Sinh chuỗi random, có thể chèn dấu '-' theo nhóm.
     * @param length độ dài ký tự chữ/số (không tính dấu '-'), > 0
     * @param group  kích thước nhóm để chèn '-', <=0 nghĩa là KHÔNG chèn
     */
    public static String generate(int length, int group) {
        if (length <= 0) throw new IllegalArgumentException("length must be > 0");
        if (group < 0) throw new IllegalArgumentException("group must be >= 0");

        // Số dấu '-' cần chèn: cứ mỗi 'group' ký tự (trừ vị trí cuối)
        int separators = (group > 0) ? ((length - 1) / group) : 0;
        StringBuilder sb = new StringBuilder(length + separators);

        for (int i = 0; i < length; i++) {
            int idx = RNG.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(idx));

            // Chèn '-' sau mỗi nhóm, trừ khi đã là ký tự cuối
            if (group > 0 && (i + 1) < length && ((i + 1) % group == 0)) {
                sb.append('-');
            }
        }
        return sb.toString();
    }

    public static String generate(int length) {
        // group = 0 => không chèn dấu '-', KHÔNG gây chia cho 0
        return generate(length, 0);
    }
}