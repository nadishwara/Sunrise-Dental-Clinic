package util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

    private static final Properties configProps = new Properties();

    static {
        try (InputStream input = EmailSender.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                configProps.load(input);
            } else {
                System.err.println("Error: config.properties file not found in classpath.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean sendAppointmentEmail(String recipientEmail, String patientName, String dentist, String treatment, String date, String time) {
        final String senderEmail = configProps.getProperty("mail.sender");
        final String appPassword = configProps.getProperty("mail.password");

        if (senderEmail == null || appPassword == null) {
            System.err.println("Error: Missing email credentials in config.properties.");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "Sunrise Dental Clinic"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Appointment Confirmation Details - Sunrise Dental Clinic");

            MimeMultipart multipart = new MimeMultipart("related");

            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = "<html>"
                    + "<head>"
                    + "<style>"
                    + "  body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f7f6; margin: 0; padding: 20px; }"
                    + "  .container { max-width: 550px; background: #ffffff; margin: 0 auto; padding: 30px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); border-top: 6px solid #007bfd; }"
                    + "  .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eeeeee; }"
                    + "  .logo { max-width: 140px; height: auto; font-size: 20px; font-weight: bold; color: #007bfd; }"
                    + "  .title { color: #2c3e50; margin-top: 15px; font-size: 22px; font-weight: bold; }"
                    + "  .subtitle { color: #27ae60; font-weight: 600; font-size: 16px; margin-top: 5px; }"
                    + "  .content { margin-top: 25px; color: #444444; line-height: 1.6; font-size: 15px; }"
                    + "  .card { background: #f8f9fa; border-radius: 8px; padding: 20px; margin: 20px 0; border-left: 4px solid #007bfd; }"
                    + "  .table { width: 100%; border-collapse: collapse; }"
                    + "  .table td { padding: 10px 0; border-bottom: 1px solid #eef0f2; font-size: 14px; }"
                    + "  .label { font-weight: bold; color: #555555; width: 35%; }"
                    + "  .value { color: #2c3e50; font-weight: 600; }"
                    + "  .notice { background-color: #eef7ff; color: #0056b3; padding: 12px; border-radius: 6px; font-size: 13px; text-align: center; margin-top: 20px; }"
                    + "  .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #888888; border-top: 1px solid #eeeeee; padding-top: 15px; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "  <div class='header'>"
                    + "    <img src='cid:logoImage' class='logo' alt='Sunrise Dental Logo' />"
                    + "    <div class='title'>Sunrise Dental Clinic</div>"
                    + "    <div class='subtitle'>Appointment Confirmed</div>"
                    + "  </div>"
                    + "  <div class='content'>"
                    + "    <p>Dear <strong>" + patientName + "</strong>,</p>"
                    + "    <p>Your appointment has been successfully scheduled. Below are your details:</p>"
                    + "    <div class='card'>"
                    + "      <table class='table'>"
                    + "        <tr><td class='label'>Doctor:</td><td class='value'>" + dentist + "</td></tr>"
                    + "        <tr><td class='label'>Treatment:</td><td class='value'>" + treatment + "</td></tr>"
                    + "        <tr><td class='label'>Date:</td><td class='value'>" + date + "</td></tr>"
                    + "        <tr><td class='label'>Time:</td><td class='value'>" + time + "</td></tr>"
                    + "      </table>"
                    + "    </div>"
                    + "    <div class='notice'>Please arrive at least 10 minutes prior to your scheduled time.</div>"
                    + "  </div>"
                    + "  <div class='footer'>"
                    + "    <p>Sunrise Dental Clinic, Negombo | Contact: +94 31 222 3344</p>"
                    + "    <p>If you need to reschedule or cancel, please notify us in advance.</p>"
                    + "  </div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            String logoPath = "E:\\final_year\\Advance_programming\\project\\mavenproject1\\src\\main\\resources\\images\\logo.png";
            File logoFile = new File(logoPath);

            if (logoFile.exists()) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.attachFile(logoFile);
                imagePart.setContentID("<logoImage>");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                multipart.addBodyPart(imagePart);
            }

            message.setContent(multipart);
            Transport.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}