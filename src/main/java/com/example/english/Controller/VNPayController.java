package com.example.english.Controller;

import com.example.english.Dto.PaymentDto;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.CheckOutResponse;
import com.example.english.Service.Implement.VNPayService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayController {
    private static final Logger log = LoggerFactory.getLogger(VNPayController.class);
    VNPayService vnpayService;
    @PostMapping("/payment")
    public ApiResponse<CheckOutResponse> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        String paymentUrl = vnpayService.createPayment(paymentDto);
        return ApiResponse.<CheckOutResponse>builder()
                .result(CheckOutResponse.builder()
                        .status("success")
                        .paymentUrl(paymentUrl)
                        .build())
                .build();
    }


    @GetMapping("/IPN")
    public ApiResponse<String> returnPayment(@RequestParam("vnp_ResponseCode") String responseCode,
                                             @RequestParam("vnp_TxnRef") String vnpTxnRef) {
        log.info("VNPay return received: vnp_ResponseCode={}, vnp_TxnRef={}", responseCode, vnpTxnRef);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Payment return processed successfully")
                .result(vnpayService.handlePaymentReturn(responseCode, vnpTxnRef))
                .build();
    }

}
