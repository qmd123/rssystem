package cn.edu.henu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.henu.bean.Business;
import cn.edu.henu.service.IBusinessService;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qing_Y
 */
@Controller
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private IBusinessService businessSer;
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
    public Map<String, Object> businessLoginAjax(@RequestBody String body, HttpSession session) {
        Map<String, Object> info = new HashMap<>();
        body = body.replace("\"", "");
        System.out.println(body);
        String[] params = body.split("&");
        String username = params[0].split("=")[1];
        String password = params[1].split("=")[1];
        String verifyCode = params[2].split("=")[1];

        if (verifyCode == null || verifyCode.length() < CHECK_CODE_LEN) {
            info.put("login_msg", "验证码错误");
            info.put("flag", -1);
        } else {
            String checkCode = (String) session.getAttribute("checkCode");
            if (verifyCode.equalsIgnoreCase(checkCode)) {
                // 销毁验证码防止重复提交请求
                session.removeAttribute("checkCode");
                Business loginBus = businessSer.login(username, password);
                System.out.println("登录校验完成..." + loginBus);
                if (loginBus != null) {
                    session.setAttribute("busLoginInfo", loginBus);
                    System.out.println(loginBus);
                    System.out.println("查询成功..." + loginBus);
                    info.put("login_msg", "成功");
                    info.put("flag", 1);
                    return info;
                }
                info.put("login_msg", "账号或密码错误");
                info.put("flag", -1);
            } else {
                info.put("login_msg", "验证码错误");
                System.out.println("验证码错误");
                info.put("flag", -1);
            }
        }
        return info;
    }

    /**
     * 用于商家注册校验
     * 校验码：
     * >0：成功
     * 0：数据库中已经存在这个餐厅下的该窗口号
     * -1：插入数据失败
     * -2：传入数据为null
     *
     * @param business
     * @param session
     * @return
     */
    @RequestMapping("/register")
    public String businessRegister(@Validated Business business, BindingResult bindingResult, HttpSession session) {
        // 表单校验
        Map<String, Object> errorsMap = new HashMap<String, Object>();
        boolean hasErrors = bindingResult.hasErrors();
        if (hasErrors) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            session.setAttribute("errorInfo", errorsMap);
            System.out.println("有检验错误...");
            // 存放注册错误对象
            session.setAttribute("errBusiness", business);
            return "redirect:/register/business";
        }

        Integer id = businessSer.save(business);
        // 注册成功 跳转到登陆界面
        if (id > 0) {
            System.out.println("注册完成...");
            // 存放商家注册成功后生成的id
            session.setAttribute("busId", id);
            return "redirect:/login/business";
        } else { // 注册失败    跳转到注册界面
            System.out.println("注册失败...");
            // 存放注册校验码
            session.setAttribute("busRegisterCode", id);
            // 存放注册错误对象
            session.setAttribute("errBusiness", business);
            return "redirect:/register/business";
        }
    }

    @RequestMapping("/update")
    public String updateBusiness(Business business, HttpSession session) {
        Business loginInfo = (Business) session.getAttribute("busLoginInfo");
        Integer username = loginInfo.getUsername();
        business.setUsername(username);
        boolean update = businessSer.update(business);
        if (!update) {
            session.setAttribute("updateInfo", "更新失败");
        } else {
            Business one = businessSer.getOneByKey(username);
            session.setAttribute("busLoginInfo", one);
        }

        return "redirect:/business/home";
    }

    @RequestMapping("/home")
    public String toBusinessHome(HttpSession session, Model model) {
        Business busLoginInfo = (Business) session.getAttribute("busLoginInfo");
        if (busLoginInfo == null) {
            return "redirect:/login/business";
        }
        String updateInfo = (String) session.getAttribute("updateInfo");
        if (updateInfo != null) {
            session.removeAttribute("updateInfo");
            model.addAttribute("updateInfo", updateInfo);
        }
        return "business/businesshome";
    }

    @RequestMapping("/logout")
    public String businessLogout(HttpSession session) {
        session.removeAttribute("busLoginInfo");
        session.invalidate();
        return "redirect:/login/business";
    }

}
