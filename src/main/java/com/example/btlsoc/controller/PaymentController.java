package com.example.btlsoc.controller;

import com.example.btlsoc.model.*;
import com.example.btlsoc.repository.UserRepository;
import com.example.btlsoc.security.CustomUserDetailsService;
import com.example.btlsoc.service.OrderService;
import com.example.btlsoc.service.PaymentService;
import com.example.btlsoc.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class PaymentController {

    @Value("${VNPAY_TMN_CODE}")
    private String VNPAY_TMN_CODE;
    @Value("${VNPAY_RETURN_URL}")
    private String VNPAY_RETURN_URL;

    @Value("${VNPAY_HASH_SECRET_KEY}")
    private String VNPAY_HASH_SECRET_KEY;
    @Value("${VNPAY_PAYMENT_URL}")
    private String VNPAY_PAYMENT_URL;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PlanService planService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestAttribute("test") String test){
        return ResponseEntity.status(HttpStatus.OK).body("OK " + test);
    }

    @GetMapping("/payment_return")
    public ResponseEntity<?> paymentReturn(HttpServletRequest request) throws UnsupportedEncodingException, ParseException {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }

        String signValue = hashAllFields(fields);

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                Payment payment = new Payment();
                payment.setPaymentAmount(Integer.parseInt(request.getParameter("vnp_Amount")));
                payment.setBankCode(request.getParameter("vnp_BankCode"));
                payment.setBankTranNo(request.getParameter("vnp_BankTranNo"));
                payment.setOrderInfo(request.getParameter("vnp_OrderInfo"));
                String payDate = request.getParameter("vnp_PayDate");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                payment.setPayDate(format.parse(payDate));
                payment.setPayTranNo(request.getParameter("vnp_TransactionNo"));
                payment.setPayTranStatus(request.getParameter("vnp_TransactionStatus"));
                payment.setIp(getIpAddress(request));
                Order order = orderService.findByIdString(request.getParameter("vnp_TxnRef"));
                order.setStatusOrder(StatusOrder.success);
                orderService.save(order);
                payment.setOrder(order);
                paymentService.save(payment);
                User user = (User) customUserDetailsService.loadUserById(order.getUser().getId());
                if(order.getPlan().getId() == 1){
                    user.setAccountType(AccountType.SILVER);
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("Vao 1");
                    user.setVipExpirationTime(now.plusMinutes(2));
                } else if(order.getPlan().getId() == 2){
                    user.setAccountType(AccountType.GOLD);
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("Vao 2");
                    user.setVipExpirationTime(now.plusMinutes(3));
                } else if(order.getPlan().getId() == 3){
                    user.setAccountType(AccountType.DIAMOND);
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println("Vao 3");
                    user.setVipExpirationTime(now.plusMinutes(4));
                }
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body("Thanh toán thành công " + user.getUsername() + " " + order.getPlan().getId() + " " + order.getPlan().getPlanName());
            } else {
                Order order = orderService.findByIdString(request.getParameter("vnp_TxnRef"));
                order.setStatusOrder(StatusOrder.failed);
                orderService.save(order);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Giao dịch không thành công vui lòng thử lại");
            }

        } else {
            Order order = orderService.findByIdString(request.getParameter("vnp_TxnRef"));
            order.setStatusOrder(StatusOrder.failed);
            orderService.save(order);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chữ ký không hợp lệ");
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> payment(@RequestBody Map<String, Object> order, Principal principal, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Plan plan = planService.findById((Integer) order.get("plan"));
        String orderDesc = (String) order.get("orderDesc");
        String bankCode = (String) order.get("bankCode");
        String locate = (String) order.get("locate");
        String ipAddr = getIpAddress(request);
        int amount = plan.getPrice()*100;

        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = cld.getTime();
        String orderId = formatter.format(date)+user.getId();

        Map vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VNPAY_TMN_CODE);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", String.valueOf(orderId));
        vnp_Params.put("vnp_OrderInfo", orderDesc);

        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VNPAY_RETURN_URL);
        vnp_Params.put("vnp_IpAddr", ipAddr);


        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);

        //Build data to hash and querystring
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
        String vnp_SecureHash = hmacSHA512(VNPAY_HASH_SECRET_KEY, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPAY_PAYMENT_URL + "?" + queryUrl;
        System.out.println(paymentUrl);

        try {
            orderService.save(new Order(orderId, amount, date, orderDesc, StatusOrder.pending,plan, user));
            return ResponseEntity.status(HttpStatus.OK).body(paymentUrl);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi vui lòng thử lại");
    }

    public static String getIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null) {
            String[] ips = xForwardedFor.split(",");
            return ips[0].trim();
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String hmacSHA512(final String key, final String data) {
        System.out.println("hmacSHA512");
        System.out.println(key);
        System.out.println(data);
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }


    public String hashAllFields(Map fields) throws UnsupportedEncodingException {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return hmacSHA512(VNPAY_HASH_SECRET_KEY, sb.toString());
    }
}
