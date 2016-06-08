package com.youngo.mobile.controller.auth;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 

 /**
  * 
 * ClassName: Mail 
 * @Description:
 * @author  liutao
 * @date 2015-9-30
  */
public class Mail {  
    private Session session = null;  
    private Properties properties = System.getProperties();  
    private Authenticator authenticator = null;  
    private HashMap<String, String> mailAttachment = new HashMap<String, String>();  
      
    private static String sendSmtp="";
    private static String sendEmail="";
    private static String sendPwd="";
   
    public Mail()
    {
        super();
    }

    public static String getSendSmtp()
    {
        return sendSmtp;
    }

    public static void setSendSmtp(String sendSmtp)
    {
        Mail.sendSmtp = sendSmtp;
    }

    public static String getSendEmail()
    {
        return sendEmail;
    }

    public static void setSendEmail(String sendEmail)
    {
        Mail.sendEmail = sendEmail;
    }

    public static String getSendPwd()
    {
        return sendPwd;
    }

    public static void setSendPwd(String sendPwd)
    {
        Mail.sendPwd = sendPwd;
    }

    /** 
     * No SMTP auth and no SSL 
     */  
    public Mail(String smtpHost) {  
         this(smtpHost, false, null, null, false, null);  
    }  
  
    /** 
     * SMTP auth with SSL 
     */  
    public Mail(String smtpHost, final String username, final String password,  
            String sslPort) {  
        this(smtpHost, true, username, password, true, sslPort);  
    }  
  
    /** 
     * SMTP auth without SSL 
     */  
    public Mail(String smtpHost, final String username, final String password) {  
        this(smtpHost, true, username, password, false, null);  
    }  
  
    /** 
     * All in one setting 
     */  
    public Mail(String smtpHost, boolean needAuth, final String username,  
            final String password, boolean isSSL, String sslPort) {  
        setSMTPHost(smtpHost);  
        if (isSSL) {  
            enableSSL(sslPort);  
        }  
        if (needAuth) {  
            enableAuth(username, password);  
        }  
        session = getSession(needAuth);  
    }  
  
    /** 
     * Add attachment to email. 
     */  
    public void addAttachment(String filePath) {  
        if (isStringEmpty(filePath)) {  
            throw new RuntimeException("[Error] Attachment filepath is empty!");  
        }  
        mailAttachment.put(filePath, new File(filePath).getName());  
    }  
    public void addAttachment(String filePath,String fileName) {  
        if (isStringEmpty(filePath)) {  
            throw new RuntimeException("[Error] Attachment filepath is empty!");  
        }  
        mailAttachment.put(filePath,fileName);  
    }  
  
    /** 
     * Send email from specified from-address to specified to-addresses / 
     * cc-addresses with given subject and content. 
     * <p> 
     * If already call addAttachment, the method will try to include them into 
     * email body 
     */  
    public boolean send(String fromAddress, List<String> toEmailAddresses,  
            List<String> ccEmailAddresses, String subject, String content,Date d,String remark) {  
        MimeMessage message = new MimeMessage(session);  
        MimeMultipart multipart = new MimeMultipart();  
        

        try {  
            message.setSubject(subject);  
            if(d!=null){
            message.setSentDate(d);
            }
            
            message.setRecipients(Message.RecipientType.TO,  
                    emailToInternetAddressArray(toEmailAddresses));  
            message.setRecipients(Message.RecipientType.CC,  
                    emailToInternetAddressArray(ccEmailAddresses));  
            message.setFrom(new InternetAddress(fromAddress,"洋光教育"));
           // message.addFrom(InternetAddress.parse(fromAddress));  
            message.setSentDate(new Date());  
            BodyPart mainBody = new MimeBodyPart();  
            
            //BodyPart mdp=new MimeBodyPart();//新建一个存放信件内容的BodyPart对象
            mainBody.setContent(content,"text/html;charset=UTF-8");//给BodyPart对象设置内容和格式/编码方式
            //Multipart mm=new MimeMultipart();//新建一个MimeMultipart对象用来存放BodyPart对
            //象(事实上可以存放多个)
            //mm.addBodyPart(mdp);//将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)
           // message.setContent(mm);
            
            //mainBody.setContent(content, "text/plain;charset=UTF-8");  
            multipart.addBodyPart(mainBody);  
            for (Entry<String, String> e : mailAttachment.entrySet()) {  
                BodyPart bodyPart = new MimeBodyPart();  
                bodyPart.setDataHandler(new DataHandler(new FileDataSource(e  
                        .getKey())));  
                bodyPart.setFileName(e.getValue());  
                bodyPart.setHeader("Content-ID", e.getValue());  
                multipart.addBodyPart(bodyPart);  
            }  
            message.setContent(multipart);  
            message.saveChanges();  
            Transport.send(message, message.getAllRecipients());  
            //Mail.addCommEmail(userID, toEmailAddresses.get(0), content, 1, remark);
            System.out.println("Send Mail success!");  
            return true;
  
        } catch (Exception e) {
            //Mail.addCommEmail(userID, toEmailAddresses.get(0), content, 2, remark);
            e.printStackTrace();  
            return false;
        }  
    }  
  
    private void setSMTPHost(String smtpHost) {  
        if (smtpHost == null) {  
            throw new RuntimeException("[Error] SMTP Host is empty!");  
        }  
        properties.setProperty("mail.smtp.host", smtpHost);  
        //properties.put("mail.smtp.localhost", "192.168.4.78");
    }  
  
    private Session getSession(boolean needAuth) {  
        mailAttachment.clear();  
        return needAuth ? session = Session.getInstance(properties,  
                authenticator) : Session.getInstance(properties);  
    }  
  
    private void enableAuth(final String username, final String password) {  
        if (username == null || password == null) {  
            throw new RuntimeException("[Error] Username or password is empty!");  
        }  
        properties.put("mail.smtp.auth", "true");  
        authenticator = new Authenticator() {  
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication(username, password);  
            }  
        };  
    }  
  
    private void enableSSL(String sslPort) {  
        if (isStringEmpty(sslPort)) {  
            throw new RuntimeException("[Error] SSL port is empty!");  
        }  
        properties.setProperty("mail.smtp.socketFactory.class",  
                "javax.net.ssl.SSLSocketFactory");  
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");  
        properties.setProperty("mail.smtp.port", sslPort);  
        properties.setProperty("mail.smtp.socketFactory.port", sslPort);  
    }  
  
    private boolean isStringEmpty(String s) {  
        return s == null || s.length() == 0;  
    }  
  
    private InternetAddress[] emailToInternetAddressArray(List<String> email)  
            throws AddressException {  
        if (email == null || 0 == email.size()) {  
            return new InternetAddress[0];  
        }  
        InternetAddress[] addresses = new InternetAddress[email.size()];  
        for (int i = 0; i < email.size(); i++) {  
            addresses[i] = new InternetAddress(email.get(i));  
        }  
        return addresses;  
    } 
  
    public static void main(String[] args) {  
        
        String content1=new Date().toString();
        Mail m=new Mail("smtp.exmail.qq.com","official-website@e-youngo.com","ygjy2015","465");
        List<String> to1 = new ArrayList<String>();  
        //List<String> to = new ArrayList<String>();  
        List<String> cc = new ArrayList<String>(); 
        //to1.add("xy6338_cn@sina.com");
        to1.add("443938891@qq.com");
        
        m.send("official-website@e-youngo.com",to1 , cc, "测试邮箱", content1,new Date(),"");

        
        
        
    } 
  
}
