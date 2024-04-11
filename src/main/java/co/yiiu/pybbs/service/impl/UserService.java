package co.yiiu.pybbs.service.impl;

import co.yiiu.pybbs.mapper.UserMapper;
import co.yiiu.pybbs.model.User;
import co.yiiu.pybbs.service.*;
import co.yiiu.pybbs.util.MyPage;
import co.yiiu.pybbs.util.SpringContextUtil;
import co.yiiu.pybbs.util.StringUtil;
import co.yiiu.pybbs.util.bcrypt.BCryptPasswordEncoder;
import co.yiiu.pybbs.util.identicon.Identicon;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Service
@Transactional
public class UserService implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    @Lazy
    private ICollectService collectService;
    @Resource
    @Lazy
    private ITopicService topicService;
    @Resource
    @Lazy
    private ICommentService commentService;
    @Resource
    private Identicon identicon;
    @Resource
    private INotificationService notificationService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ICodeService codeService;

    // aaaaaaaaa，aaaaaaaaaaaaa
    @Override
    public User selectByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    // aaaatoken，aatokenaa
    // aaauuidaaatokenaaaaaaa
    // aaaaaa : )
    private String generateToken() {
        String token = UUID.randomUUID().toString();
        User user = this.selectByToken(token);
        if (user != null) {
            return this.generateToken();
        }
        return token;
    }

    /**
     * aaaaaa
     *
     * @param username
     * @param password
     * @param avatar
     * @param email
     * @param bio
     * @param website
     * @param needActiveEmail aaaaaaaaaa
     *                        aaaaaaaaa
     *                        Githubaaaaa，aaaaaaaa，aaaaa，aaaaaaaa，aaaaaaa，aaaaaaaaaaaaaaaaaaaa
     * @return
     */
    @Override
    public User addUser(String username, String password, String avatar, String email, String bio, String website,
                        boolean needActiveEmail) {
        String token = this.generateToken();
        User user = new User();
        user.setUsername(username);
        if (!StringUtils.isEmpty(password)) user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setToken(token);
        user.setInTime(new Date());
        if (avatar == null) avatar = identicon.generator(username);
        user.setAvatar(avatar);
        user.setEmail(email);
        user.setBio(bio);
        user.setWebsite(website);
        user.setActive(systemConfigService.selectAllConfig().get("user_need_active").equals("0"));
        userMapper.insert(user);
        if (needActiveEmail) {
            // aaaaaa
            new Thread(() -> {
                String title = "aaaa%s，aaaaaaaaaa";
                String content = "aaaaaaaa%s，aaaaaa&nbsp;&nbsp;<a href='%s/active?email=%s&code=${code}'>aaaa</a>";
                codeService.sendEmail(user.getId(), email, String.format(title, systemConfigService.selectAllConfig().get(
                        "base_url").toString()), String.format(content,
                        systemConfigService.selectAllConfig().get("name").toString(), systemConfigService.selectAllConfig().get(
                                "base_url").toString(), email));
            }).start();
        }
        // aaaa，aaaaaaaaaaaa，aaaanull
        return this.selectById(user.getId());
    }

    // aaaaaaa，aaaaaaa
    private String generateUsername() {
        String username = StringUtil.randomString(6);
        if (this.selectByUsername(username) != null) {
            return this.generateUsername();
        }
        return username;
    }

    // aaaaaaa/aaaaaa
    @Override
    public User addUserWithMobile(String mobile) {
        // aaaaaaaaaaaaaa
        User user = selectByMobile(mobile);
        if (user == null) {
            String token = this.generateToken();
            String username = generateUsername();
            user = new User();
            user.setUsername(username);
            user.setToken(token);
            user.setInTime(new Date());
            user.setAvatar(identicon.generator(username));
            user.setEmail(null);
            user.setBio(null);
            user.setWebsite(null);
            user.setActive(true);
            userMapper.insert(user);
            // aaaa，aaaaaaaaaaaa，aaaanull
            return this.selectById(user.getId());
        }
        return user;
    }

    // aaaatokenaaaa
    @Override
    public User selectByToken(String token) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getToken, token);
        return userMapper.selectOne(wrapper);
    }

    // aaaamobileaaaa
    @Override
    public User selectByMobile(String mobile) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getMobile, mobile);
        return userMapper.selectOne(wrapper);
    }

    // aaaaemailaaaa
    @Override
    public User selectByEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getEmail, email);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User selectByIdWithoutCache(Integer id) {
        return userMapper.selectById(id);
    }

    // aaaaaaa
    @Override
    public List<User> selectTop(Integer limit) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("score").last("limit " + limit);
        return userMapper.selectList(wrapper);
    }

    // aaaaaa
    @Override
    public void update(User user) {
        userMapper.updateById(user);
        
        // aasessionaaaa
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("_user", user);

        // aaaaaaspringaa，aaaaaopaaaa
        SpringContextUtil.getBean(UserService.class).delRedisUser(user);
    }

    // ------------------------------- admin ------------------------------------------

    @Override
    public IPage<User> selectAll(Integer pageNo, String username) {
        MyPage<User> page = new MyPage<>(pageNo, Integer.parseInt((String) systemConfigService.selectAllConfig().get("page_size")));
        page.setDesc("in_time");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            wrapper.lambda().eq(User::getUsername, username);
        }
        return userMapper.selectPage(page, wrapper);
    }

    public User selectByIdNoCatch(Integer id) {
        return userMapper.selectById(id);
    }

    // aaaaaaaaaa
    @Override
    public int countToday() {
        return userMapper.countToday();
    }

    // aaaa
    @Override
    public void deleteUser(Integer id) {
        // aaaaaaa
        notificationService.deleteByUserId(id);
        // aaaaaaa
        collectService.deleteByUserId(id);
        // aaaaaaaa
        commentService.deleteByUserId(id);
        // aaaaaaaa
        topicService.deleteByUserId(id);
        // aaaaaaaacodeaa
        codeService.deleteByUserId(id);
        // aaredisaaaa
        User user = this.selectById(id);
        SpringContextUtil.getBean(UserService.class).delRedisUser(user);
        // aaaaaa
        userMapper.deleteById(id);
    }

    // aaredisaa
    @Override
    public void delRedisUser(User user) {

    }
}
