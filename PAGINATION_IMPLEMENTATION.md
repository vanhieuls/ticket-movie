# Tài liệu triển khai Phân trang (Pagination)

## Tóm tắt
Đã thêm tính năng phân trang cho 4 phương thức sau, đồng thời **GIỮ NGUYÊN** các phương thức trả về List cũ:

1. `getAllTicketPrices()` - Lấy tất cả giá vé
2. `getAllSpecialDays()` - Lấy tất cả ngày đặc biệt  
3. `getInvoiceSummaryList()` - Lấy danh sách tóm tắt hóa đơn (Admin)
4. `getInvoiceList()` - Lấy danh sách hóa đơn (User)

## Chi tiết thay đổi

### 1. getAllTicketPrices (Ticket Price)

#### Service Interface
- **File**: `TicketPriceService.java`
- **Thêm method mới**: `Page<TicketPriceResponse> getAllTicketPrices(Pageable pageable)`
- **Giữ nguyên**: `List<TicketPriceResponse> getAllTicketPrice()`

#### Service Implementation  
- **File**: `TicketPriceServiceImpl.java`
- **Implementation**:
```java
@Override
public Page<TicketPriceResponse> getAllTicketPrices(Pageable pageable) {
    Page<TicketPrice> ticketPricePage = ticketPriceRepository.findAll(pageable);
    return ticketPricePage.map(ticketPriceMapper::toTicketPriceResponse);
}
```

#### Controller
- **File**: `TicketPriceAdminController.java`
- **Endpoint cũ**: `GET /admin/ticket-price/ticket-prices` - Trả về List
- **Endpoint mới**: `GET /admin/ticket-price/ticket-prices/page` - Trả về Page
- **Parameters**:
  - `page` (default: 0) - Số trang
  - `size` (default: 10) - Số lượng item mỗi trang
  - `sortBy` (default: "id") - Trường để sắp xếp
  - `sortDirection` (default: "ASC") - Hướng sắp xếp (ASC/DESC)

---

### 2. getAllSpecialDays (Special Day)

#### Service Interface
- **File**: `SpecialDayService.java`
- **Thêm method mới**: `Page<SpecialDay> getAllSpecialDays(Pageable pageable)`
- **Giữ nguyên**: `List<SpecialDay> getAllSpecialDays()`

#### Service Implementation
- **File**: `SpecialDayServiceImpl.java`
- **Implementation**:
```java
@Override
public Page<SpecialDay> getAllSpecialDays(Pageable pageable) {
    return specialDayRepository.findAll(pageable);
}
```

#### Controller
- **File**: `SpecialDayAdminController.java`
- **Endpoint cũ**: `GET /admin/special-day/special-days` - Trả về List
- **Endpoint mới**: `GET /admin/special-day/special-days/page` - Trả về Page
- **Parameters**:
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: "id")
  - `sortDirection` (default: "ASC")

---

### 3. getInvoiceSummaryList (Invoice Summary - Admin)

#### Service Interface
- **File**: `InvoiceService.java`
- **Thêm method mới**: `Page<InvoiceSummary> getInvoiceSummaryList(Pageable pageable)`
- **Giữ nguyên**: `List<InvoiceSummary> getInvoiceSummaryList()`

#### Service Implementation
- **File**: `InvoiceServiceImpl.java`
- **Implementation**:
```java
@Override
public Page<InvoiceSummary> getInvoiceSummaryList(Pageable pageable) {
    Page<Invoice> invoiceEntityPage = invoiceRepository.findAll(pageable);
    return invoiceEntityPage.map(invoiceEntity -> {
        Ticket ticketEntity = getTicket(invoiceEntity.getId());
        String showtime = ticketEntity.getShowTime().getStartTime().toString() + "-" +
                ticketEntity.getShowTime().getEndTime().toString() + " " +
                ticketEntity.getShowTime().getShowDate().toString();
        String screenRoom = ticketEntity.getShowTime().getScreenRoom().getName() + " - " +
                ticketEntity.getShowTime().getScreenRoom().getCinema().getName();
        return InvoiceSummary.builder()
                .id(invoiceEntity.getId())
                .code(invoiceEntity.getBookingCode())
                .movieName(ticketEntity.getShowTime().getMovie().getName())
                .showTime(showtime)
                .screenRoom(screenRoom)
                .totalMoney(invoiceEntity.getTotalAmount())
                .createDate(invoiceEntity.getCreatedDate().toString())
                .build();
    });
}
```

#### Controller
- **File**: `InvoiceAdminController.java`
- **Endpoint cũ**: `GET /admin/invoices/invoices` - Trả về List
- **Endpoint mới**: `GET /admin/invoices/invoices/page` - Trả về Page
- **Parameters**:
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: "id")
  - `sortDirection` (default: "DESC")

---

### 4. getInvoiceList (Invoice List - User)

#### Service Interface
- **File**: `InvoiceService.java`
- **Thêm method mới**: `Page<InvoiceResponse> getInvoiceList(Pageable pageable)`
- **Giữ nguyên**: `List<InvoiceResponse> getInvoiceList()`

#### Repository
- **File**: `InvoiceRepository.java`
- **Thêm method**: `Page<Invoice> findByUser_Id(Long userId, Pageable pageable)`

#### Service Implementation
- **File**: `InvoiceServiceImpl.java`
- **Implementation**:
```java
@Override
public Page<InvoiceResponse> getInvoiceList(Pageable pageable) {
    User user = getUser();
    Page<Invoice> invoiceEntityPage = invoiceRepository.findByUser_Id(user.getId(), pageable);
    return invoiceEntityPage.map(invoiceEntity -> {
        Ticket ticketEntity = getTicket(invoiceEntity.getId());
        int totalTicket = ticketRepository.countByInvoice_Id(invoiceEntity.getId());
        return InvoiceResponse.builder()
                .invoiceId(invoiceEntity.getId())
                .totalMoney(invoiceEntity.getTotalAmount())
                .movieName(ticketEntity.getShowTime().getMovie().getName())
                .totalTicket(totalTicket)
                .showDate(ticketEntity.getShowTime().getShowDate())
                .startTime(ticketEntity.getShowTime().getStartTime())
                .build();
    });
}
```

#### Controller
- **File**: `InvoiceController.java`
- **Endpoint cũ**: `GET /invoice/list` - Trả về List
- **Endpoint mới**: `GET /invoice/list/page` - Trả về Page
- **Parameters**:
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: "id")
  - `sortDirection` (default: "DESC")

---

## Cách sử dụng

### Ví dụ 1: Lấy danh sách giá vé có phân trang
```
GET /admin/ticket-price/ticket-prices/page?page=0&size=20&sortBy=price&sortDirection=DESC
```

### Ví dụ 2: Lấy danh sách ngày đặc biệt có phân trang
```
GET /admin/special-day/special-days/page?page=1&size=15
```

### Ví dụ 3: Lấy danh sách hóa đơn của user có phân trang
```
GET /invoice/list/page?page=0&size=10&sortBy=createdDate&sortDirection=DESC
```

### Ví dụ 4: Lấy danh sách tóm tắt hóa đơn (Admin) có phân trang
```
GET /admin/invoices/invoices/page?page=0&size=20&sortBy=createdDate&sortDirection=DESC
```

---

## Response Format

Khi gọi API có phân trang, response sẽ có dạng:

```json
{
  "code": 200,
  "message": "Get ... with pagination successfully",
  "result": {
    "content": [...],      // Dữ liệu của trang hiện tại
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 5,       // Tổng số trang
    "totalElements": 50,   // Tổng số phần tử
    "last": false,         // Có phải trang cuối không
    "first": true,         // Có phải trang đầu không
    "size": 10,
    "number": 0,
    "numberOfElements": 10,
    "empty": false
  }
}
```

---

## Lưu ý

1. **Các phương thức cũ trả về List vẫn được giữ nguyên** để đảm bảo tương thích ngược
2. Tất cả các phương thức phân trang đều hỗ trợ tham số sắp xếp linh hoạt
3. Giá trị mặc định cho sortDirection ở Invoice là DESC (mới nhất trước), còn lại là ASC
4. Có thể sắp xếp theo bất kỳ trường nào của entity (id, createdDate, price, v.v.)

