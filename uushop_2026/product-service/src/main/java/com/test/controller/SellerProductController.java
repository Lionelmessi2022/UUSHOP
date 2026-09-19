package com.test.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.com.test.vo.ResultVO;
import com.test.entity.ProductCategory;
import com.test.entity.ProductInfo;
import com.test.exception.ShopException;
import com.test.form.UpdateProductForm;
import com.test.handler.CustomCellWriteHandler;
import com.test.result.ResponseEnum;
import com.test.service.ProductCategoryService;
import com.test.service.ProductInfoService;
import com.test.util.ResultVOUtil;
import com.test.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author test
 * @since 2026-08-13
 */
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductInfoService productInfoService;
    // 查询所有商品类别
    @GetMapping("/findAllProductCategory")
    public ResultVO findAllProductCategory() {
        List<ProductCategory> list = this.productCategoryService.list();
        List<SellerProductCategoryVO> result = new ArrayList<>();
        for (ProductCategory productCategory : list) {
            SellerProductCategoryVO vo = new SellerProductCategoryVO();
            vo.setName(productCategory.getCategoryName());
            vo.setType(productCategory.getCategoryType());
            result.add(vo);
        }
        Map map = new HashMap();
        map.put("content", result);


        return ResultVOUtil.success(map);
    }

    //添加商品
    @PostMapping("/add")
    public ResultVO add(@RequestBody ProductInfo productInfo) {
        Integer categoryType = productInfo.getCategoryType();
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_type", categoryType);
        ProductCategory productCategory = this.productCategoryService.getOne(queryWrapper);
        if (productCategory == null) return ResultVOUtil.fail(ResponseEnum.PRODUCT_CATEGORY_NOT_EXIST.getMsg());
        boolean save = this.productInfoService.save(productInfo);
        if (save) return ResultVOUtil.success(null);
        return ResultVOUtil.fail(ResponseEnum.PRODUCT_ADD_ERROR.getMsg());

    }

    //查询商品
    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        Page<ProductInfo> pageModel = new Page<>(page, size);
        Page<ProductInfo> result = this.productInfoService.page(pageModel,null);
        PageVO pageVo = new PageVO();
        pageVo.setSize(result.getSize());
        pageVo.setTotal(result.getTotal());
        List<ProductInfo> records = result.getRecords();
        List<SellerProductVO> list = new ArrayList<>();
        for (ProductInfo record : records) {
            SellerProductVO vo = new SellerProductVO();
            QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_type", record.getCategoryType());
            ProductCategory productCategory = this.productCategoryService.getOne(queryWrapper);
            vo.setCategoryName(productCategory.getCategoryName());
            Integer productStatus = record.getProductStatus();
            if (productStatus == 1){
                vo.setStatus(true);
            } else {
                vo.setStatus(false);
            }
            BeanUtils.copyProperties(record, vo);
            list.add(vo);
        }
        pageVo.setContent(list);
        return ResultVOUtil.success(pageVo);
    }

    //商品模糊查询
    @GetMapping("/like/{page}/{size}/{keyWord}")
    public ResultVO like(
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size,
            @PathVariable("keyWord") String keyWord
    ) {
        Page<ProductInfo> pageModel = new Page<>(page, size);
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("product_name", keyWord);
        Page<ProductInfo> result = this.productInfoService.page(pageModel, queryWrapper);
        PageVO pageVo = new PageVO();
        pageVo.setSize(result.getSize());
        pageVo.setTotal(result.getTotal());
        List<ProductInfo> records = result.getRecords();
        List<SellerProductVO> list = new ArrayList<>();
        for (ProductInfo record : records) {
            SellerProductVO vo = new SellerProductVO();
            QueryWrapper<ProductCategory> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("category_type", record.getCategoryType());
            ProductCategory productCategory = this.productCategoryService.getOne(queryWrapper1  );
            vo.setCategoryName(productCategory.getCategoryName());
            Integer productStatus = record.getProductStatus();
            if (productStatus == 1){
                vo.setStatus(true);
            } else {
                vo.setStatus(false);
            }
            BeanUtils.copyProperties(record, vo);
            list.add(vo);
        }
        pageVo.setContent(list);
        return ResultVOUtil.success(pageVo);

    }

    //通过分类查询商品
    @GetMapping("/findByCategory/{categoryType}/{page}/{size}")
    public ResultVO findByCategory(
            @PathVariable("categoryType") Integer categoryType,
            @PathVariable("page") Integer page,
            @PathVariable("size") Integer size
    ) {
        Page<ProductInfo> pageModel = new Page<>(page, size);
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_type", categoryType);
        Page<ProductInfo> result = this.productInfoService.page(pageModel, queryWrapper);
        PageVO pageVo = new PageVO();
        pageVo.setSize(result.getSize());
        pageVo.setTotal(result.getTotal());
        List<ProductInfo> records = result.getRecords();
        List<SellerProductVO> list = new ArrayList<>();
        for (ProductInfo record : records) {
            SellerProductVO vo = new SellerProductVO();
            QueryWrapper<ProductCategory> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("category_type", record.getCategoryType());
            ProductCategory productCategory = this.productCategoryService.getOne(queryWrapper1  );
            vo.setCategoryName(productCategory.getCategoryName());
            Integer productStatus = record.getProductStatus();
            if (productStatus == 1){
                vo.setStatus(true);
            } else {
                vo.setStatus(false);
            }
            BeanUtils.copyProperties(record, vo);
            list.add(vo);
        }
        pageVo.setContent(list);
        return ResultVOUtil.success(pageVo);
    }

    //通过id查询商品
    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable("id") Integer id) {

        ProductInfo productInfo = this.productInfoService.getById(id);
        if (productInfo == null) throw new ShopException(ResponseEnum.PRODUCT_NULL.getMsg());
        SellerProductFindVo vo = new SellerProductFindVo();
        vo.setStock(productInfo.getProductStock());
        if (productInfo.getProductStatus() == 1){
            vo.setStatus(true);
        } else {
            vo.setStatus(false);
        }
        vo.setName(productInfo.getProductName());
        vo.setPrice(productInfo.getProductPrice());
        vo.setDescription(productInfo.getProductDescription());
        vo.setId(productInfo.getProductId());
        vo.setIcon(productInfo.getProductIcon());
        Map map = new HashMap();
        map.put("categoryType", productInfo.getCategoryType());
        vo.setCategory(map);
        return ResultVOUtil.success(vo);
    }

    //通过ID删除商品
    @DeleteMapping("/delete/{id}")
    public ResultVO delete(@PathVariable("id") Integer id) {
        ProductInfo productInfo = this.productInfoService.getById(id);
        if (productInfo == null) throw new ShopException(ResponseEnum.PRODUCT_NULL.getMsg());
        boolean removeById = this.productInfoService.removeById(id);
        if (removeById  ) return ResultVOUtil.success(null);
        return ResultVOUtil.fail(ResponseEnum.PRODUCT_DELETE_ERROR.getMsg());
    }

    //修改商品状态
    @PutMapping("/updateStatus/{id}/{status}")
    public ResultVO updateStatus(@PathVariable("id") Integer id, @PathVariable("status") Boolean status) {
        ProductInfo productInfo = this.productInfoService.getById(id);
        if (productInfo == null) throw new ShopException(ResponseEnum.PRODUCT_NULL.getMsg());
        if (status && productInfo.getProductStatus() == 1){
            throw new ShopException (ResponseEnum.PRODUCT_STATUS_TRUE_CHANGE_ERROR  .getMsg());
        }
        if (!status && productInfo.getProductStatus() == 0){
            throw new ShopException (ResponseEnum.PRODUCT_STATUS_FALSE_CHANGE_ERROR  .getMsg());
        }
        if (status) {
            productInfo.setProductStatus(1);
        } else {
            productInfo.setProductStatus(0);
        }
        boolean updateById = this.productInfoService.updateById(productInfo);
        if (updateById) return ResultVOUtil.success(true);
        return ResultVOUtil.fail(ResponseEnum.PRODUCT_STATUS_ERROR.getMsg());

    }

    //修改商品
    @PutMapping ("/update")
    public ResultVO update(@RequestBody UpdateProductForm form) {
        Integer id = form.getId();
        ProductInfo productInfo = this.productInfoService.getById(id);
        if (productInfo == null) throw new ShopException(ResponseEnum.PRODUCT_NULL.getMsg());
        if (form.isStatus()) {
            productInfo.setProductStatus(1);
        } else {
            productInfo.setProductStatus(0);
        }
        productInfo.setProductName(form.getName());
        productInfo.setProductPrice(form.getPrice());
        productInfo.setProductStock(form.getStock());
        productInfo.setProductDescription(form.getDescription());
        productInfo.setProductIcon(form.getIcon());
        productInfo.setCategoryType(form.getCategory().getCategoryType());
        boolean updateById = this.productInfoService.updateById(productInfo);
        if (updateById) return   ResultVOUtil.success(true);
        return ResultVOUtil.fail(ResponseEnum.PRODUCT_UPDATE_ERROR.getMsg());

    }

    //导出Excel
    @GetMapping("/export")
    public void export (HttpServletResponse response){
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("商品信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<ProductInfo> productInfoList = this.productInfoService.list(null);


        List<ExcelProductVO> list = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            ExcelProductVO vo = new ExcelProductVO();
            BeanUtils.copyProperties(productInfo,vo );
            if(productInfo.getProductStatus() == 1) {
                vo.setProductStatus("上架");
            } else {
                vo.setProductStatus("下架");
            }
            QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_type", productInfo.getCategoryType());
            ProductCategory productCategory = this.productCategoryService.getOne(queryWrapper);
            vo.setCategoryName(productCategory.getCategoryName());
            list.add(vo);
        }

        EasyExcel.write(response.getOutputStream(), ExcelProductVO.class)
                .registerWriteHandler(new CustomCellWriteHandler())
                .sheet("商品列表")
                .doWrite(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //导入Excel
    @PostMapping("/import")
    public ResultVO importData(@RequestParam("file") MultipartFile file){
        List<ProductInfo> productInfoList = null;
        List<ProductInfo> List = new ArrayList<>();
        try {

            productInfoList = this.productInfoService.excleToProductInfoList(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把Excel中的数据转换为数据库中的格式
        if(productInfoList==null){
            return ResultVOUtil.fail("导入Excel失败！");
        }
        boolean result = this.productInfoService.saveBatch(productInfoList);
        if(result)return ResultVOUtil.success(null);
        return ResultVOUtil.fail("导入Excel失败！");
    }
}

