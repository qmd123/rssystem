package cn.edu.henu.controller;

import cn.edu.henu.bean.Consumer;
import cn.edu.henu.service.IConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qing_Y
 * @date 2020-11-22 18:38
 */
@Controller
@RequestMapping("/consumer")
public class ConsumerController {

    /**
     * 注册成功
     */
    final Integer REGISTER_SUCCESS = 1;

    @Autowired
    private IConsumerService consumerSer;
    final Integer CHECK_CODE_LEN = 4;

    /**
     * 异步登录校验
     * Map集合    存放的登录验证和登录提示
     * flag：-1失败   1：成功
     * bus_login_msg:显示错误登录信息
     *
     * @param body    存放请求体信息：username、password、verifyCode
     * @param session 用来获取验证码
     * @return map集合
     */
    @ResponseBody
    @RequestMapping("/login")
    public Map<String, Object> consumerLoginAjax(@RequestBody String body, HttpSession session) {
        Map<String, Object> info = new HashMap<>();
        body = body.replace("\"", "");
        String[] params = body.split("&");
        String username = params[0].split("=")[1];
        String password = params[1].split("=")[1];
        String verifyCode = params[2].split("=")[1];

        if (verifyCode == null || verifyCode.length() < CHECK_CODE_LEN) {
            info.put("login_msg", "验证码错误");
        } else {
            String checkCode = (String) session.getAttribute("checkCode");
            if (verifyCode.equalsIgnoreCase(checkCode)) {
                // 销毁验证码防止重复提交请求
                session.removeAttribute("checkCode");
                Consumer loginCon = consumerSer.login(username, password);
                System.out.println("登录校验完成..." + loginCon);
                if (loginCon != null) {
                    session.setAttribute("conLoginInfo", loginCon);
                    System.out.println("查询成功...");
                    info.put("login_msg", "成功");
                    info.put("flag", 1);
                    return info;
                }
                info.put("login_msg", "账号或密码错误");
            } else {
                info.put("login_msg", "验证码错误");
                System.out.println("验证码错误");
            }
        }
        info.put("flag", -1);
        return info;
    }

    /**
     * 注册校验码：
     * 1：成功
     * 0：username重复
     * -1：插入数据失败
     * -2：消费者信息转存丢失变为null
     *
     * @param consumer      注册对象
     * @param bindingResult 错误信息返回
     * @param session       session
     * @return 跳转
     */
    @RequestMapping("/register")
    public String consumerRegister(@Validated Consumer consumer, BindingResult bindingResult, HttpSession session) {
        // 表单校验
        Map<String, Object> errorsMap = new HashMap<>();
        boolean hasErrors = bindingResult.hasErrors();
        if (hasErrors) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            session.setAttribute("errorInfo", errorsMap);
            System.out.println("有检验错误...");
            // 存放注册错误对象
            session.setAttribute("errConsumer", consumer);
            return "redirect:/register/consumer";
        }

        Integer registerCode = consumerSer.save(consumer);
        if (REGISTER_SUCCESS.equals(registerCode)) {
            System.out.println("注册完成...");
            // 跳转到消费者登陆界面
            return "redirect:/first";
        } else {
            System.out.println("注册失败...");
            // 存入注册失败校验码
            session.setAttribute("conRegisterCode", registerCode);
            // 存入注册错误的消费者对象
            session.setAttribute("errConsumer", consumer);
            return "redirect:/register/consumer";
        }
    }

    @RequestMapping("/updatenick")
    public String updateNickname(String nickname, HttpSession session) {
        Consumer loginInfo = (Consumer) session.getAttribute("conLoginInfo");
        // 存在用户登录信息
        if (loginInfo != null) {
            loginInfo.setNickname(nickname);
            int i = consumerSer.update(loginInfo);

            if (i < 1) {
                session.setAttribute("updateInfo", "修改失败");
                return "redirect:/PInfo";
            }else {
                session.setAttribute("updateInfo", "修改成功");
            }
            // 每次更新完后更新session中的consumer对象
            session.setAttribute("conLoginInfo", loginInfo);
            // 返回用户中心
            return "redirect:/PInfo";
        }
        return "redirect:/login/consumer";
    }

    @RequestMapping("/updatepass")
    public String updateNickname(@RequestParam("password") String password, @RequestParam(value = "newPassword") String newPass, HttpSession session) {
        Consumer loginInfo = (Consumer) session.getAttribute("conLoginInfo");
        // 存在用户登录信息
        if (loginInfo != null) {
            // 密码匹配就更改位新密码
            if (loginInfo.getPassword().equals(password)) {
                loginInfo.setPassword(newPass);
            } else {
                session.setAttribute("updateInfo", "初始密码错误");
                return "redirect:/PInfo";
            }
            // 更新
            int i = consumerSer.update(loginInfo);
            if (i < 1) {
                session.setAttribute("updateInfo", "修改失败");
                return "redirect:/PInfo";
            } else {
                session.setAttribute("updateInfo", "修改成功");
            }
            // 每次更新完后更新session中的consumer对象
            session.setAttribute("conLoginInfo", loginInfo);
            // 返回用户中心
            return "redirect:/PInfo";

        }
        return "redirect:/login/consumer";
    }


    @RequestMapping("/home")
    public String toConsumerHome(HttpSession session) {
        Consumer conLoginInfo = (Consumer) session.getAttribute("conLoginInfo");
        if (conLoginInfo == null) {
            return "redirect:/login/consumer";
        }
        return "consumer/consumerhome";
    }

    @RequestMapping("/logout")
    public String consumerLogout(HttpSession session) {
        session.removeAttribute("conLoginInfo");
        session.invalidate();
        return "redirect:/login/consumer";
    }

}
