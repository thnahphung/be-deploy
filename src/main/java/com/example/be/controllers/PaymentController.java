package com.example.be.controllers;

import com.example.be.dto.PaymentReqDTO;
import com.example.be.dto.PaymentResDTO;
import com.example.be.models.*;
import com.example.be.services.*;
import com.example.be.utils.PaymentConfig;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private SeatService seatService;

    @Autowired
    private BookingHistoryService bookingHistoryService;
    @Autowired
    private ShowTimeService showTimeService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestParam long amount) throws UnsupportedEncodingException {
        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "1");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", "172.16.2.173");
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);


//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+8"));
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Singapore"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.HOUR, 2);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setMessage("Successful");
        paymentResDTO.setStatus("200");
        paymentResDTO.setUrl(paymentUrl);
        return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
    }

    @PostMapping("/payment_success")
    public ResponseEntity<ResponseObject> paymentSuccess(@RequestBody PaymentReqDTO paymentReqDTO) throws MessagingException {
        User user = userService.get(paymentReqDTO.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "User not found", ""));
        }

        List<Seat> seats = seatService.getListSeats(paymentReqDTO.getSeatIds());
        for (Seat seat : seats) {
            seat.setStatus(Seat.BOOKED);
        }
        seatService.updateList(seats);

        ShowTime showTime = showTimeService.getById(paymentReqDTO.getShowTimeId());

        BookingHistory bookingHistory = new BookingHistory();
        bookingHistory.setTime(LocalDateTime.now());
        bookingHistory.setDiscount(paymentReqDTO.getDiscount());
        bookingHistory.setTotal(seats.size() * showTime.getPrice());
        bookingHistory.setStatus(BookingHistory.SUCCESS);
        bookingHistory.setUser(user);

        bookingHistoryService.add(bookingHistory);
        List<Ticket> tickets = ticketService.addListTickets(seats, showTime, bookingHistory);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("name", user.getName());
        templateModel.put("email", user.getEmail());
        templateModel.put("phone", user.getPhone());
        templateModel.put("imageMovie", showTime.getMovie().getImage());
        templateModel.put("movieName", showTime.getMovie().getNameVn());
        templateModel.put("screenName", showTime.getScreenShowTime().getScreen().getName());
        templateModel.put("price", showTime.getPrice());
        templateModel.put("amountTicket", tickets.size());
        templateModel.put("idBooking", bookingHistory.getId());
        templateModel.put("startTime", showTime.getStartTime().format(formatter));
        templateModel.put("endTime", showTime.getEndTime().format(formatter));
        templateModel.put("subtotal", bookingHistory.getTotal());
        templateModel.put("discount", bookingHistory.getDiscount());
        templateModel.put("total", bookingHistory.getTotal() - bookingHistory.getDiscount());

        emailService.sendHtmlEmailPaymentSuccess(user.getEmail(), "Booking Success", templateModel);
        return ResponseEntity.ok(new ResponseObject("ok", "Success", tickets));
    }

}
