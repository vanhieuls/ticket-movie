# Quy trình thực hiện phân quyền RBAC (Role-Based Access Control)

Tài liệu này hướng dẫn chi tiết quy trình triển khai phân quyền cho hệ thống dựa trên 3 vai trò: **ADMIN**, **STAFF**, **USER**.

## Nguyên tắc phân quyền (Hierarchy)
- **ADMIN**: Có tất cả quyền của USER và STAFF.
- **STAFF**: Có quyền của USER.
- **USER**: Quyền hạn cơ bản của người dùng.

## Bước 1: Tạo Enum Role

Tạo file `src/main/java/com/example/english/Enum/Role.java` để định nghĩa các vai trò.

```java
package com.example.english.Enum;

public enum Role {
    ADMIN,
    STAFF,
    USER
}
```

## Bước 2: Cập nhật Entity User

Cập nhật class `User` tại `src/main/java/com/example/english/Entity/User.java` để lưu trữ thông tin vai trò và cấp quyền cho Spring Security.

1.  **Thêm trường `role`**:
    ```java
    import com.example.english.Enum.Role; // Import Enum vừa tạo

    // ... bên trong class User
    @Enumerated(EnumType.STRING)
    Role role;
    ```

2.  **Cập nhật phương thức `getAuthorities`**:
    Phương thức này ánh xạ `Role` của user thành `GrantedAuthority` mà Spring Security hiểu được.
    ```java
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    ```

*Lưu ý: Khi đăng ký mới (Sign Up), hãy đảm bảo gán role mặc định là `Role.USER`.*

## Bước 3: Cấu hình Security (SecurityConfig)

Cập nhật `securityFilterChain` trong `src/main/java/com/example/english/Security/SecurityConfig.java` để phân quyền dựa trên đường dẫn (URL endpoints).

Dựa vào cấu trúc thư mục Controller, ta có các quy tắc sau:

*   `Controller/Admin/**` $\rightarrow$ Chỉ **ADMIN** (URL bắt đầu bằng `/admin/**`)
*   `Controller/Staff/**` $\rightarrow$ **ADMIN**, **STAFF** (URL bắt đầu bằng `/staff/**`)
*   `Controller/User/**` $\rightarrow$ **ADMIN**, **STAFF**, **USER** (URL bắt đầu bằng `/users/**`)
*   Các Controller gốc (`InvoiceController`, v.v.) cần xác định cụ thể (ví dụ tạo hóa đơn cần quyền USER trở lên).

dùng @PreAuthorize để kiểm soát quyền truy cập chi tiết hơn nếu cần thiết,
và không cho phép user này có thể truy cập id của user kia để đảm bảo tính bảo mật.

```java

## Bước 4: Kiểm tra và Bổ sung logic nghiệp vụ

1.  **Đăng ký (Sign Up)**: Trong `AuthenticationService.signUp`, đảm bảo gán `user.setRole(Role.USER);` khi tạo user mới.
2.  **Tạo Staff/Admin**: Cần tạo API riêng hoặc insert database thủ công cho các tài khoản ADMIN đầu tiên. API tạo Staff chỉ nên được gọi bởi Admin.

## Tóm tắt quyền truy cập

| Endpoint Pattern | Vai trò được phép | Giải thích |
| :--- | :--- | :--- |
| `/admin/**` | ADMIN | Chức năng quản trị viên |
| `/staff/**` | ADMIN, STAFF | Chức năng nhân viên (Admin kế thừa) |
| `/users/**` | ADMIN, STAFF, USER | Chức năng cá nhân người dùng (Staff/Admin cũng là user) |
| `/invoice/**` | ADMIN, STAFF, USER | Chức năng đặt vé/thanh toán |
| `/movie/**` (GET) | PUBLIC | Xem danh sách phim |
| `/auth/**` | PUBLIC | Đăng nhập/Đăng ký |

