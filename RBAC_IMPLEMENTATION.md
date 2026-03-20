Quy trình Triển khai Phân quyền RBAC (Nâng cao)
Tài liệu này hướng dẫn triển khai hệ thống phân quyền dựa trên vai trò (Role-Based Access Control) sử dụng Spring Security, JWT Claims và Entity Roles.

1. Nguyên tắc thiết kế (Architecture)
   Database-Driven: Vai trò được lưu trong bảng roles thay vì fix cứng trong mã nguồn.

JWT-Based: Vai trò được đính kèm vào nội dung Token (Claims) để giảm tải truy vấn Database.

Method Security: Sử dụng Annotation để kiểm soát quyền truy cập đến từng dòng dữ liệu (IDOR protection).

Bước 1: Khởi tạo Entity Role
Thay vì dùng Enum đơn giản, ta dùng Entity để sau này dễ dàng thêm các "Permission" (quyền hạn chi tiết) vào từng Role.

Java
@Entity
@Table(name = "roles")
public class Role {
@Id
private String name; // ADMIN, STAFF, USER
private String description;
}
Bước 2: Cập nhật Entity User (Many-to-Many)
Một người dùng có thể sở hữu nhiều vai trò (ví dụ: vừa là Staff, vừa là User).

Java
@Entity
public class User implements UserDetails {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}
Bước 3: Tích hợp Role vào JWT (Token Provider)
Khi tạo Token, ta đưa danh sách vai trò vào Claims để phía Client hoặc các Service khác có thể đọc được ngay mà không cần gọi vào DB.

Java
public String generateToken(User user) {
// Chuyển danh sách Role thành String (ví dụ: "ROLE_ADMIN ROLE_USER")
String scope = user.getAuthorities().stream()
.map(GrantedAuthority::getAuthority)
.collect(Collectors.joining(" "));

    return Jwts.builder()
            .setSubject(user.getUsername())
            .claim("scope", scope) // Key "scope" hoặc "roles" tùy bạn đặt
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
}
Bước 4: Kiểm soát quyền truy cập với @PreAuthorize
Thay vì cấu hình tập trung tại SecurityConfig, ta sẽ phân quyền trực tiếp trên các Method của Controller. Cách này giúp bạn kiểm soát được cả Vai trò (Role) và Dữ liệu (Data Ownership).

1. Kích hoạt Method Security
   Tại file SecurityConfig.java, bạn cần thêm Annotation để Spring Security bắt đầu quét các Annotation phân quyền.

Java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Quan trọng: Kích hoạt @PreAuthorize, @PostAuthorize...
public class SecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        // Định nghĩa: ADMIN có quyền của STAFF, STAFF có quyền của USER
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Cho phép công khai
                .anyRequest().authenticated()           // Tất cả còn lại phải Login
            )
            // ... các cấu hình JWT Filter khác
        return http.build();
    }
}
2. Sử dụng tại các Controller
   a. AdminController (Chỉ dành cho ADMIN)
   Bạn có thể đặt Annotation ở cấp Class để áp dụng cho tất cả các phương thức bên trong.

Java
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
@DeleteMapping("/users/{id}")
public String deleteUser(@PathVariable Long id) {
return "User deleted by Admin";
}
}
b. StaffController (STAFF và ADMIN)
Nhờ vào RoleHierarchy đã cấu hình ở trên, bạn chỉ cần ghi hasRole('STAFF'), Admin sẽ tự động được vào.

Java
@RestController
@RequestMapping("/staff")
public class StaffController {

    @PostMapping("/tickets/verify")
    @PreAuthorize("hasRole('STAFF')")
    public String verifyTicket() {
        return "Ticket verified";
    }
}
c. UserController (Bảo mật theo ID - IDOR Protection)
Đây là phần quan trọng nhất: Đảm bảo người dùng chỉ xem được thông tin của chính mình, trừ khi đó là ADMIN.

Java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{userId}")
    // Logic: Có quyền ADMIN HOẶC (Có quyền USER và ID trong Token trùng với ID trong Path)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
    public UserResponse getUserProfile(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{userId}/password")
    // Chỉ chính chủ mới được đổi pass, Admin cũng không nên can thiệp trực tiếp ở đây
    @PreAuthorize("#userId == authentication.principal.id")
    public String changePassword(@PathVariable Long userId) {
        return "Password updated";
    }
}
Bước 5: Bảo mật dữ liệu cá nhân (IDOR Protection)
Sử dụng @PreAuthorize tại Controller để đảm bảo người dùng chỉ có quyền chỉnh sửa/xem dữ liệu của chính họ.

Java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
> Lưu ý: Để so sánh được #id == authentication.principal.id, bạn cần đảm bảo class thực thi UserDetails của bạn có chứa trường id