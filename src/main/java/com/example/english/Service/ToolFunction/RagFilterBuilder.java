package com.example.english.Service.ToolFunction;

import lombok.NoArgsConstructor;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@NoArgsConstructor
public class RagFilterBuilder {
    // Danh sách tất cả các type hợp lệ
    private static final String ALL_TYPES_FILTER =
            "type == 'movie' || type == 'cinema' || type == 'screen' || type == 'screen_type'";

    /** Bỏ dấu tiếng Việt để match từ khóa đơn giản */
    public static String asciiLower(String s) {
        if (s == null) return "";
        // Sử dụng regex để chỉ giữ lại chữ cái và khoảng trắng sau khi bỏ dấu
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z\\s]", " "); // Thay thế ký tự không phải chữ cái/khoảng trắng bằng khoảng trắng
        return n.toLowerCase(Locale.ROOT);
    }

    /** Suy luận FILTER_EXPRESSION từ câu hỏi tiếng Việt của user */
    public static String buildFilterExpression(String userMsg) {
        final String q = asciiLower(userMsg);
        List<String> parts = new ArrayList<>();
        List<String> typeParts = new ArrayList<>();

        // --- 1. Suy luận Type (Phạm vi) ---

        // Điều kiện ưu tiên cao: Nếu có yêu cầu đặt vé/tìm lịch chiếu, cần cả movie và cinema
        if (containsAny(q, "suat chieu", "gio chieu", "showtime", "dat ve")) {
            typeParts.add("type == 'movie' || type == 'cinema'");
        }

        // Điều kiện đơn giản: Phim
        if (containsAny(q, "phim", "dien vien", "dao dien", "the loai")) {
            if (!typeParts.isEmpty()) typeParts.clear();
            typeParts.add("type == 'movie'");
        }

        // Điều kiện đơn giản: Rạp
        if (containsAny(q, "rap", "dia chi", "gia ve")) {
            if (!typeParts.isEmpty()) typeParts.clear();
            typeParts.add("type == 'cinema'");
        }

        // Điều kiện đơn giản: Phòng chiếu
        if (containsAny(q, "phong chieu", "so ghe", "man hinh")) {
            if (!typeParts.isEmpty()) typeParts.clear();
            typeParts.add("type == 'screen'");
        }

        // Điều kiện đơn giản: Loại phòng chiếu
        if (containsAny(q, "loai phong", "dinh dang phong", "3d", "imax")) {
            if (!typeParts.isEmpty()) typeParts.clear();
            typeParts.add("type == 'screen_type'");
        }

        // Thêm điều kiện type (nếu có)
        if (!typeParts.isEmpty()) {
            // Lấy điều kiện type mạnh nhất (phần tử cuối cùng)
            parts.add(typeParts.get(typeParts.size() - 1));
        } else {
            // Nếu không có từ khóa về loại, mặc định tìm kiếm trên tất cả các loại
            parts.add(ALL_TYPES_FILTER);
        }

        // --- 2. Các điều kiện Metadata phụ ---

        // --- Country ---
        String countryCode = detectCountryCode(q);
        if (countryCode != null) {
            parts.add("country == '" + countryCode + "'");
        }

        // --- Status ---
        Boolean status = detectStatus(q);
        if (status != null) {
            parts.add("status == " + (status ? "true" : "false"));
        }

        if (parts.size() == 1 && parts.get(0).equals(ALL_TYPES_FILTER)) {
            return ALL_TYPES_FILTER;
        }

        // Kết hợp tất cả các phần tử (đã bao gồm type) bằng AND
        return parts.stream().map(part -> "(" + part + ")").collect(Collectors.joining(" && "));
    }

    private static boolean containsAny(String text, String... keys) {
        for (String k : keys) {
            if (text.contains(k)) return true;
        }
        return false;
    }

    private static String detectCountryCode(String lowerAscii) {
        if (containsAny(lowerAscii, "viet nam", "vn")) return "VN";
        if (containsAny(lowerAscii, "my", "hoa ky", "usa", "us")) return "US";
        if (containsAny(lowerAscii, "han quoc", "korea", "kr")) return "KR";
        if (containsAny(lowerAscii, "nhat ban", "japan", "jp")) return "JP";
        if (containsAny(lowerAscii, "trung quoc", "china", "cn")) return "CN";
        return null;
    }

    private static Boolean detectStatus(String lowerAscii) {
        if (containsAny(lowerAscii, "dang hoat dong", "dang dung", "dang mo")) return true;
        if (containsAny(lowerAscii, "tam ngung", "tam dung", "ngung", "dong cua")) return false;
        return null;
    }
}
