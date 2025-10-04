

package com.room_rent.Room_Rent_Application.service;
import com.room_rent.Room_Rent_Application.enums.room.booking.BookingStatus;
import com.room_rent.Room_Rent_Application.model.booking.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendHtmlEmail(String to, String subject, String resetUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariable("resetUrl", resetUrl); // pass the URL to Thymeleaf

        String html = templateEngine.process("reset-password", context);


        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }

    //email to send otp
    public void sendOtpEmail(String to, String subject, int otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Pass the OTP to the Thymeleaf context
        Context context = new Context();
        context.setVariable("otp", otp);  // Correctly set the OTP

        String html = templateEngine.process("otp", context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);  // Set content as HTML


        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
    //notification to owner
    public void sendRoomBookingRequestEmail(String to, String subject, Booking booking) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Pass booking data to template
        Context context = new Context();
        context.setVariable("roomTitle", booking.getRoom().getTitle());
        context.setVariable("checkInDate", booking.getCreatedDate());
       // context.setVariable("checkOutDate", booking.getCheckOutDate());
        context.setVariable("seekerName", booking.getUser().getName());
        context.setVariable("seekerEmail", booking.getUser().getEmail());

        String html = templateEngine.process("booking_notification_to_room_owner", context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }







    //sent to seeker for the confirmation when it get confirmed

    public void sendRoomBookingConfirmationEmail(String to, String subject, BookingStatus status) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Pass booking data to Thymeleaf template
        Context context = new Context();
        context.setVariable("status", status);
//        context.setVariable("roomTitle", booking.getRoom().getTitle());
//        context.setVariable("checkInDate", booking.getCheckInDate());
//        context.setVariable("checkOutDate", booking.getCheckOutDate());
  //      context.setVariable("userName", booking.getUser().getFullName());

        String html = templateEngine.process("booking_status_confirmation_to_seeker", context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true); // HTML email

        mailSender.send(message);
    }

}
