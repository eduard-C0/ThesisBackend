package com.example.backend.service.user;


import com.example.backend.domain.User;
import com.example.backend.repository.IUserRepository;
import com.example.backend.service.mapper.UserMapperImplementation;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImplementation implements IUserService{

    private UserMapperImplementation userMapper;
    private final IUserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    private final PasswordEncoder encoder;

    @Override
    public UserDto saveUser(UserDto userDto, String siteUrl) throws UnsupportedEncodingException, MessagingException{
        User userEntity = userMapper.toEntity(userDto);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        String randomCode = RandomString.make(64);
        userEntity.setVerificationCode(randomCode);
        userEntity.setEnabled(false);
        if(userRepository.findByEmail(userDto.getEmail()) == null) {
            sendVerificationEmail(userEntity, siteUrl);
            return userMapper.toService(userRepository.save(userEntity));
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto findUser(UserDto user) {
        User foundUser = userRepository.findByEmail(user.getEmail());

        if(foundUser == null)
            return null;
        else if (!encoder.matches(user.getPassword(), foundUser.getPassword())) {
            foundUser.setPassword(null);
        }
        return userMapper.toService(foundUser);

    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "musicstreamingapp2000@gmail.com";
        String senderName = "MusicStreamingApp";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "MusicStreamingApp.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }
    @Transactional
    public String verify (String verificationCode){
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.getEnabled()) {
            return "<h3>Your email is already confirmed!</h3>";
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
            return "<h3>Your email is confirmed. Thank you for using our service!</h3>";
        }
    }

}
