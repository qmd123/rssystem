package cn.edu.henu.service.impl;

import cn.edu.henu.bean.PageBean;
import cn.edu.henu.bean.Product;
import cn.edu.henu.dao.ProductMapper;
import cn.edu.henu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @author Qing_Y
 * @date 2020-11-26 21:31
 */
@Service("productSer")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    void initPageBean(PageBean<Product> pageBean, Product product, Integer row) {
        pageBean.setRows(row);
        int total = getTotal(product);
        pageBean.setTotalCount(total);
        int rows = pageBean.getRows();
        int a = total % rows;
        int b = total / rows;
        pageBean.setTotalPage(a == 0 ? b : b + 1);
    }

    @Override
    public PageBean<Product> getAllProducts(Product product, Integer row) {
        try {
            List<Product> products;
            if (product != null) {
                products = productMapper.selectByCondition(product);
            } else {
                products = productMapper.selectAll();
            }
            PageBean<Product> pageBean = new PageBean<>();
            initPageBean(pageBean, product, row);
            pageBean.setList(products);
            return pageBean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getAllByBid(Integer bid) {
        try {
            return productMapper.getAllByBid(bid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> getAllByBidAndName(Integer bid, String name) {
        try {
            return productMapper.getAllByBidAndName(bid, name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getTotal(Product product) {
        try {
            return productMapper.selectTotal(product);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int add(Product product) {
        try {
            return productMapper.insert(product);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int updateById(Product product) {
        if (product.getId() == null) {
            return -1;
        }
        if ("".equals(product.getProductName())) {
            product.setProductName(null);
        }
        if ("".equals(product.getProductIntr())) {
            product.setProductIntr(null);
        }
        if ("".equals(product.getPhotosrc())) {
            product.setPhotosrc(null);
        }
        try {
            return productMapper.updateByPrimaryKey(product);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteById(Integer id, String path) throws Exception {
        // 先删除数据库中的对应商品
        int i = productMapper.deleteByPrimaryKey(id);
        if (i < 1) {
            throw new Exception();
        }
        // 删除服务器上的对应图片
        File file = new File(path);
        boolean delete = file.delete();
        if (!delete) {
            throw new Exception();
        }
        return 1;
    }

    @Override
    public Product selectSimpleById(Integer id) {
        try {
            return productMapper.selectSimpleById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product selectById(Integer id) {
        try {
            return productMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
