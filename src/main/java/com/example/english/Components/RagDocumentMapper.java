package com.example.english.Components;

import com.example.english.Entity.Cinema;
import com.example.english.Entity.Movie;
import com.example.english.Entity.ScreenRoom;
import com.example.english.Entity.ScreenRoomType;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RagDocumentMapper {

    public Document fromMovie(Movie m){
        String content = """
      Phim: %s
      Thể loại: %s
      Quốc gia: %s
      Độ tuổi: %d+
      Thời lượng: %d phút
      Đạo diễn: %s
      Diễn viên: %s
      Khởi chiếu: %s
      Kết thúc: %s
      Ảnh poster: %s
      Tóm tắt:
      %s
      """.formatted(
                n(m.getName()), n(m.getCategory()), n(m.getCountry()),
                m.getAgeLimit(), m.getDuration(),
                n(m.getDirector()), n(m.getActors()),
                n(m.getReleaseDate()), n(m.getEndDate()),
                n(m.getPosterUrl()),
                stripHtml(n(m.getDescription()))
        );

        Map<String,Object> meta = new HashMap<>();
        meta.put("type", "movie");
        meta.put("id", "movie/" + m.getId());
        meta.put("movieId", m.getId());
        meta.put("ageLimit", m.getAgeLimit());
        meta.put("country", m.getCountry());
        meta.put("releaseDate", m.getReleaseDate() != null ? m.getReleaseDate().toString() : null);
        meta.put("endDate", m.getEndDate() != null ? m.getEndDate().toString() : null);
        meta.put("posterUrl", m.getPosterUrl() != null ? m.getPosterUrl().toString() : null);
        meta.put("status", m.isStatus());
        meta.put("source", "db:movie");
        return new Document(content, meta);
    }
    public Document fromCinema(Cinema c) {
        String content = """
      Rạp: %s
      Loại rạp: %s
      Địa chỉ: %s
      Trạng thái: %s
      Số phòng chiếu: %d
      """.formatted(
                n(c.getName()), n(c.getCinemaType().getName()), n(c.getAddress()),
                Boolean.TRUE.equals(c.getStatus()) ? "đang hoạt động" : "tạm ngưng",
                c.getScreenRooms()!=null ? c.getScreenRooms().size() : 0
        );

        Map<String,Object> meta = new HashMap<>();
        meta.put("type", "cinema");
        meta.put("id", "cinema/" + c.getId());
        meta.put("cinemaId", c.getId());
        meta.put("status", c.getStatus());
        meta.put("source", "db:cinema");
        return new Document(content, meta);
    }

    public Document fromScreenRoom(ScreenRoom s) {
        String content = """
      Phòng chiếu: %s
      Thuộc rạp: %s
      Loại phòng: %s
      Số ghế: %d
      Trạng thái: %s
      """.formatted(
                n(s.getName()),
                s.getCinema()!=null ? n(s.getCinema().getName()) : "",
                s.getScreenRoomType()!=null ? n(s.getScreenRoomType().getName()) : "",
                s.getSeats()!=null ? s.getSeats().size() : 0,
                s.getStatus()? "đang hoạt động":"tạm ngưng"
        );

        Map<String,Object> meta = new HashMap<>();
        meta.put("type", "screen");
        meta.put("id", "screen/" + s.getId());
        meta.put("screenId", s.getId());
        meta.put("cinemaId", s.getCinema()!=null ? s.getCinema().getId() : null);
        meta.put("screenRoomTypeId", s.getScreenRoomType()!=null ? s.getScreenRoomType().getId() : null);
        meta.put("status", s.getStatus());
        meta.put("source", "db:screen_room");
        return new Document(content, meta);
    }

    public Document fromScreenRoomType(ScreenRoomType srt) {
        String content = """
      Loại phòng chiếu: %s
      Trạng thái: %s
      """.formatted(
                n(srt.getName()),
                srt.isStatus()? "đang dùng":"ngưng"
        );

        Map<String,Object> meta = new HashMap<>();
        meta.put("type", "screen_type");
        meta.put("id", "screen_type/" + srt.getId());
        meta.put("screenRoomTypeId", srt.getId());
        meta.put("priceFactor", srt.getPriceFactor());
        meta.put("status", srt.isStatus());
        meta.put("source", "db:screen_room_type");
        return new Document(content, meta);
    }


    private static String n(Object o){ return o==null? "" : o.toString(); }
    private static String stripHtml(String s) {
        if (s == null) return "";
        s = s.replaceAll("(?i)<br\\s*/?>", "\n");   // giữ xuống dòng
        s = s.replaceAll("<[^>]+>", "");           // bỏ thẻ
        s = s.replace("&nbsp;", " ").replace("&amp;", "&");
        return s.trim();
    }
}