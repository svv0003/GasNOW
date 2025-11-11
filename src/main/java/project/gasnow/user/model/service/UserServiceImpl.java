package project.gasnow.user.model.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import project.gasnow.user.model.dto.User;
import project.gasnow.user.model.dto.UserPoint;
import project.gasnow.user.model.dto.UserPointHistory;
import project.gasnow.user.model.mapper.UserMapper;
import project.gasnow.user.model.mapper.UserPointMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserPointMapper userPointMapper;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine; // auth/signup.html 에 있는 HTML 코드를 Java로 변환
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * 회원가입 메서드
     * @param user 클라이언트가 view에서 form에 작성한 정보를 User 객체로 받아옴
     */
    @Transactional
    @Override
    public int register(User user) {
        // 아이디 유효성 검사
        if(user.getUserId() == null) {
            return 0;
        }
        if(user.getUserId().length() < 6) {
            return 0;
        }
        // 아이디 중복 체크
        if(!checkUserIdDuplicate(user.getUserId())) {
            return 0;
        }

        // 이름 유효성 검사
        if(user.getUserName() == null) {
            return 0;
        }

        // 비밀번호 유효성 검사
        if(user.getUserPassword() == null) {
            return 0;
        }
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,16}$";
        if(!user.getUserPassword().matches(pattern)) {
            return 0;
        }

        // 연락처 유효성 검사
        if(user.getUserPhone() == null) {
            return 0;
        }
        // 연락처 중복 체크
        if(!checkPhoneDuplicate(user.getUserPhone())) {
            return 0;
        }

        // 비밀번호 암호화
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));

        // 회원 등록 - 최종 DB에 저장
        userMapper.insertNewUser(user);

        // 포인트 초기화
        userPointMapper.insertNewUserPoint(user.getUserId());

        // 포인트 이력 추가
        UserPointHistory userPointHistory = new UserPointHistory();
        userPointHistory.setUserId(user.getUserId());
        userPointHistory.setPointChange(300);
        userPointHistory.setPointType("EARN");
        userPointHistory.setDescription("회원가입");
        userPointMapper.insertPointHistory(userPointHistory);
        return 1;
    }

    /**
     * 아이디 중복 체크 메서드
     * @param userId 클라이언트가 view에서 작성한 아이디
     * @return DB에 존재하면 false (사용 불가능한 아이디) <br>
     *      존재하지 않으면 true (사용 가능한 아이디)
     */
    @Override
    public boolean checkUserIdDuplicate(String userId) {
        // DB에 아이디가 존재하는 경우 (사용 불가능한 아이디)
        if(userMapper.checkUserIdExist(userId) > 0){
            return false;
        }
        return true;
    }

    /**
     * 연락처 중복 체크 메서드
     * @param userPhone 클라이언트가 view에서 작성한 아이디
     * @return DB에 존재하면 false (사용 불가능한 아이디) <br>
     * 존재하지 않으면 true (사용 가능한 아이디)
     */
    @Override
    public boolean checkPhoneDuplicate(String userPhone) {
        // DB에 연락처가 존재하는 경우 (사용 불가능한 연락처)
        if(userMapper.checkUserPhoneExist(userPhone) > 0){
            return false;
        }
        return true;
    }

    /**
     * 이메일 인증코드 전송 메서드
     * @param htmlName 이메일 전송 화면이 구현된 html 파일명, Controller에서 넘겨받음
     * @param userEmail 세션에 저장된 클라이언트의 이메일 정보
     * @return null 또는 인증코드
     */
    @Override
    public String sendEmail(String htmlName, String userEmail) {
        String authKey = createAuthKey(); // 인증키 생성

        try {
            String title = "[GASNOW] 회원가입 인증번호 입니다."; // 메일 제목

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(userEmail);    // 수신자
            helper.setSubject(title);   // 메일 제목
            helper.setText(loadHtml(authKey, htmlName), true);

            javaMailSender.send(mimeMessage);
            log.info("메일 전송 완료: {}", userEmail);
            return authKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 인증키 생성 메서드
     * @return 인증키
     */
    public String createAuthKey() {
        String key = "";

        for(int i=0; i<7; i++) {
            int num = (int)(Math.random() * 10); // 0 ~ 9
            key += num;
        }

        return key;
    }

    /**
     * HTML 파일을 읽어와 String 으로 변환
     * @param authKey 인증키
     * @param htmlName 이메일 전송 화면이 구현된 html 파일명
     * @return html을 String으로 변환한 결과
     */
    public String loadHtml(String authKey, String htmlName) {
        Context context = new Context();
        context.setVariable("authKey", authKey);  // thymeleaf가 적용된 html에서 사용할 값 추가

        return templateEngine.process("pages/" + htmlName, context);
    }

    /**
     * 로그인 메서드
     * @param userId 클라이언트가 view에서 작성한 아이디
     * @param userPassword 클라이언트가 view에서 작성한 비밀번호
     * @return 회원가입한 유저 객체
     */
    @Override
    public User login(HttpSession session, String userId, String userPassword) {
        User user = userMapper.getUserById(userId);
        
        // 아이디가 DB에 존재하지 않는 경우
        if(user == null){
            return null;
        }
        
        // 비밀번호가 일치하지 않는 경우
        if(!bCryptPasswordEncoder.matches(userPassword, user.getUserPassword())){
            return null;
        }

        user.setUserPassword(null);
        addLoginPoint(userId);
        return user;
    }

    /**
     * 로그인 시 포인트 적립 메서드
     * @param userId 세션에 저장된 userId
     * @return 적립된 포인트
     */
    @Override
    public int addLoginPoint(String userId) {
        UserPointHistory userPointHistory = userPointMapper.getPointHistoryById(userId);
        UserPoint userPoint = userPointMapper.getUserPointById(userId);

        // String 형태의 createdAt -> 날짜 형식으로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdAt = LocalDateTime.parse(userPointHistory.getCreatedAt(), formatter);

        // 마지막 로그인 일자가 오늘이 아니라면 포인트 적립
        if(!createdAt.toLocalDate().isEqual(LocalDate.now())) {
            // 새로운 point_history 행 추가
            userPointHistory.setUserId(userId);
            userPointHistory.setPointChange(30);
            userPointHistory.setPointType("EARN");
            userPointHistory.setDescription("출첵");
            userPointMapper.insertPointHistory(userPointHistory);
            // 총 적립 포인트 변경
            int currentPoint = userPointMapper.getCurrentPoint(userId);
            userPointMapper.updatePoint(currentPoint + 30);  // 현재 포인트 변경
            userPointMapper.updateTotalEarned(userPoint.getTotalEarned() + 30);  // 총 적립 포인트(total_earned) 변경

            log.info("출석 포인트 적립 - userId: {}, 적립: 30", userId);

            return 30;
        }
        return 0;
    }
}
