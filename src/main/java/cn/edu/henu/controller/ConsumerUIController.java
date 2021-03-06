package cn.edu.henu.controller;

import cn.edu.henu.bean.*;
import cn.edu.henu.service.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于消费者页面跳转的Controller
 *
 * @author Qing_Y
 * @date 2020-12-03 21:10
 */
@Controller
@RequestMapping("")
public class ConsumerUIController {

    @Autowired
    private IProductService productSer;
    @Autowired
    private IOrderService orderSer;
    @Autowired
    private IShopService shopSer;
    @Autowired
    private IAdminService adminSer;
    @Autowired
    private ICommentService commentSer;

    final Integer ROW = 8;

    @RequestMapping("/home")
    public String toConsumerHome(Product product, @RequestParam(value = "p", defaultValue = "1") Integer p, HttpSession session) {
        String name = product.getProductName();
        Float price = product.getProductPrice();
        Float grade = product.getProductGrade();
        boolean flag = false;

        if (name != null && !"".equals(name)) {
            flag = true;
        }
        if (price != null && price != 0f) {
            flag = true;
        }
        if (grade != null && grade != 0f) {
            flag = true;
        }

        // 分页,一页八个
        PageHelper.startPage(p, ROW);
        // 创建PageBean对象
        PageBean<Product> pageBean;
        if (!flag) {
            session.removeAttribute("conds");
            pageBean = productSer.getAllProducts(null, ROW);
        } else {
            Map<String, Object> conds = new HashMap<>();
            conds.put("name", name);
            conds.put("price", price);
            conds.put("grade", grade);
            session.setAttribute("conds", conds);
            if ("".equals(name)) {
                product.setProductName(null);
            }
            product.setProductName("%" + name + "%");
            pageBean = productSer.getAllProducts(product, ROW);
        }
        pageBean.setCurrentPage(p);
        session.setAttribute("pb", pageBean);
        return "consumer/consumerhome";
    }

    @RequestMapping("/details")
    public String toDetails(Integer pid, HttpSession session, Model model) {
        String add_info = (String) session.getAttribute("add_info");
        if (add_info != null) {
            session.removeAttribute("add_info");
            model.addAttribute("add_info", add_info);
        }
        PageBean<Product> pageBean = ((PageBean<Product>) session.getAttribute("pb"));
        boolean flag = false;
        if (pageBean != null) {
            List<Product> products = pageBean.getList();
            for (Product product : products) {
                if (product.getId().equals(pid)) {
                    model.addAttribute("product", product);
                    flag = true;
                    break;
                }
            }
        }

        if (!flag) {
            Product product = productSer.selectSimpleById(pid);
            model.addAttribute("product", product);
        }

        List<Comment> comments = commentSer.getAllByPid(pid);
        model.addAttribute("comments", comments);
        return "consumer/details";
    }

    @RequestMapping("/shopCart")
    public String toShopCart(HttpSession session, HttpServletRequest request) {
        Consumer consumer = (Consumer) session.getAttribute("conLoginInfo");
        if (consumer == null) {
            String url = request.getRequestURL().toString();
            session.setAttribute("history", url);
            session.setAttribute("login_info", "请先登录");
            return "redirect:/login/consumer";
        }

        String errorInfo = (String) session.getAttribute("errorInfo");
        if (errorInfo != null) {
            request.setAttribute("errorInfo", errorInfo);
        }

        String username = consumer.getUsername();
        List<Shop> shops = shopSer.selectByCid(username);
        Map<Integer, List<Shop>> map = new HashMap<>();
        for (Shop shop : shops) {
            System.out.println(shop);
            Integer bid = shop.getBid();
            List<Shop> list;
            if (map.containsKey(bid)) {
                list = map.get(bid);
            } else {
                list = new ArrayList<>();
            }
            list.add(shop);
            map.put(bid, list);
        }
        session.setAttribute("shops", shops);
        session.setAttribute("mapShop", map);
        return "consumer/shopCart";
    }

    @RequestMapping("/pastOrder")
    public String toPastOrder(@RequestParam(value = "p", defaultValue = "1") Integer p, HttpSession session, HttpServletRequest request) {
        Consumer consumer = (Consumer) session.getAttribute("conLoginInfo");
        if (consumer == null) {
            String url = request.getRequestURL().toString();
            session.setAttribute("history", url);
            session.setAttribute("login_info", "请先登录");
            return "redirect:/login/consumer";
        }
        String updateInfo = (String) session.getAttribute("updateInfo");
        if (updateInfo != null) {
            request.setAttribute("updateInfo", updateInfo);
            session.removeAttribute("updateInfo");
        }
        // 分页
        PageHelper.startPage(p, ROW);
        // 创建PageBean对象
        PageBean<Order> pageBean;
        pageBean = adminSer.findAllOrderByPage(null, ROW);
        pageBean.setCurrentPage(p);
        session.setAttribute("conOrders", pageBean);
        return "consumer/pastOrder";
    }

    @RequestMapping("/pastOrder/detail")
    public String toOrderDetail(Integer oid, Model model, HttpServletRequest request) {
        String referer = request.getHeader("REFERER");
        System.out.println(referer);
        if (referer == null || !referer.contains("/pastOrder")) {
            return "redirect:/pastOrder";
        }
        Order order = orderSer.selectByPrimaryKey(oid);
        List<OrderDetail> details = orderSer.selectByOid(oid);
        if (details != null) {
            for (OrderDetail detail : details) {
                Integer pid = detail.getPid();
                Product product = productSer.selectById(pid);
                detail.getProduct().setPhotosrc(product.getPhotosrc());
                detail.getProduct().setProductName(product.getProductName());
                detail.getProduct().setBusiness(product.getBusiness());
                detail.getProduct().setProductPrice(product.getProductPrice());
                detail.getProduct().setBid(product.getBid());
            }
        }
        model.addAttribute("details", details);
        model.addAttribute("order", order);
        return "consumer/orderDetail";
    }

    @RequestMapping("/assessment")
    public String toAssessment(HttpSession session, HttpServletRequest request) {
        Consumer consumer = (Consumer) session.getAttribute("conLoginInfo");
        if (consumer == null) {
            String url = request.getRequestURL().toString();
            session.setAttribute("history", url);
            session.setAttribute("login_info", "请先登录");
            return "redirect:/login/consumer";
        }

        List<Comment> comments = commentSer.getAllByCid(consumer.getUsername());
        session.setAttribute("comments", comments);

        return "consumer/assessment";
    }

    @RequestMapping("/discount")
    public String toDiscount(HttpSession session, HttpServletRequest request) {
        Consumer consumer = (Consumer) session.getAttribute("conLoginInfo");
        if (consumer == null) {
            String url = request.getRequestURL().toString();
            session.setAttribute("history", url);
            session.setAttribute("login_info", "请先登录");
            return "redirect:/login/consumer";
        }
        return "consumer/discount";
    }

    @RequestMapping("/PInfo")
    public String toPInfo(HttpSession session, HttpServletRequest request) {
        Consumer consumer = (Consumer) session.getAttribute("conLoginInfo");
        if (consumer == null) {
            String url = request.getRequestURL().toString();
            session.setAttribute("history", url);
            session.setAttribute("login_info", "请先登录");
            return "redirect:/login/consumer";
        }

        String updateInfo = (String) session.getAttribute("updateInfo");
        if (updateInfo != null) {
            request.setAttribute("updateInfo", updateInfo);
            session.removeAttribute("updateInfo");
        }
        return "consumer/PInfo";
    }

}
